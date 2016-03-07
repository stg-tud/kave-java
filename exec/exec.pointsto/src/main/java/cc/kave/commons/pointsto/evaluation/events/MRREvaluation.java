/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.evaluation.events;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.inject.Inject;
import com.google.inject.Provider;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.kave.commons.model.events.completionevents.IProposal;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.pointsto.PointsToAnalysisFactory;
import cc.kave.commons.pointsto.analysis.PointsToQueryBuilder;
import cc.kave.commons.pointsto.evaluation.PointsToUsageFilter;
import cc.kave.commons.pointsto.evaluation.ResultExporter;
import cc.kave.commons.pointsto.extraction.CoReNameConverter;
import cc.kave.commons.pointsto.extraction.PointsToUsageExtractor;
import cc.kave.commons.pointsto.stores.UsageStore;
import cc.kave.commons.utils.SSTNodeHierarchy;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.mining.calls.ICallsRecommender;
import cc.recommenders.mining.calls.pbn.PBNMiner;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.names.Names;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

public class MRREvaluation extends AbstractCompletionEventEvaluation implements Closeable {

	private final List<UsageStore> usageStores;
	private final PointsToUsageFilter usageFilter;

	private final Provider<PBNMiner> pbnMinerProvider;

	private final Map<Triple<ICoReTypeName, String, String>, Double> results = new HashMap<>();

	@Inject
	public MRREvaluation(List<UsageStore> usageStores, PointsToUsageFilter usageFilter,
			Provider<PBNMiner> pbnMinerProvider) {
		this.usageStores = usageStores;
		this.usageFilter = usageFilter;
		this.pbnMinerProvider = pbnMinerProvider;
	}

	public void exportResults(Path target, ResultExporter exporter) throws IOException {
		exporter.export(target, results.entrySet().stream().map(entry -> {
			ICoReTypeName type = entry.getKey().getLeft();
			return new String[] { Names.vm2srcQualifiedType(type), entry.getKey().getMiddle(),
					entry.getKey().getRight(), String.format(Locale.US, "%.3f", entry.getValue()) };
		}));
	}

	@Override
	protected void evaluate(List<ICompletionEvent> completionEvents, List<PointsToAnalysisFactory> ptFactories)
			throws IOException {
		for (PointsToAnalysisFactory ptFactory : ptFactories) {
			Map<ICoReTypeName, Map<ICompletionEvent, List<Usage>>> queries = createQueries(completionEvents, ptFactory);
			Set<ICoReTypeName> types = getQueryTypes(queries.values());
			types.retainAll(getStoreTypes());
			log("%s: %d types\n", ptFactory.getName(), types.size());

			for (ICoReTypeName type : types) {
				evaluateType(ptFactory.getName(), type, queries);
			}
		}
	}

	private void evaluateType(String validationAnalysisName, ICoReTypeName type,
			Map<ICoReTypeName, Map<ICompletionEvent, List<Usage>>> queries) throws IOException {
		for (UsageStore store : usageStores) {
			ICallsRecommender<Query> recommender = trainRecommender(store.load(type, usageFilter));
			store.flush();
			double score = calcMRR(recommender, queries.get(type));
			results.put(ImmutableTriple.of(type, store.getName(), validationAnalysisName), score);
		}
	}

	private Map<ICoReTypeName, Map<ICompletionEvent, List<Usage>>> createQueries(
			List<ICompletionEvent> completionEvents, PointsToAnalysisFactory ptFactory) {
		Map<ICoReTypeName, Map<ICompletionEvent, List<Usage>>> queries = new HashMap<>();
		CompletionExpressionCollector completionExprCollector = new CompletionExpressionCollector();
		PointsToUsageExtractor usageExtractor = new PointsToUsageExtractor();

		for (ICompletionEvent event : completionEvents) {
			Context context = event.getContext();
			PointsToQueryBuilder queryBuilder = new PointsToQueryBuilder(context);
			SSTNodeHierarchy nodeHierarchy = new SSTNodeHierarchy(context.getSST());
			List<ICompletionExpression> completionExprs = completionExprCollector.collect(context);

			for (ICompletionExpression expr : completionExprs) {
				List<Usage> exprQueries = usageExtractor.extractQueries(expr, ptFactory.create().compute(context),
						queryBuilder, nodeHierarchy);
				for (Usage query : exprQueries) {
					Map<ICompletionEvent, List<Usage>> typeBin = queries.get(query.getType());
					if (typeBin == null) {
						typeBin = new HashMap<>();
						queries.put(query.getType(), typeBin);
					}

					List<Usage> eventBin = typeBin.get(event);
					if (eventBin == null) {
						eventBin = new ArrayList<>();
						typeBin.put(event, eventBin);
					}
					eventBin.add(query);
				}
			}
		}

		return queries;
	}

	private Set<ICoReTypeName> getQueryTypes(Collection<Map<ICompletionEvent, List<Usage>>> eventUsages) {
		return eventUsages.stream().flatMap(eu -> eu.values().stream()).flatMap(u -> u.stream()).map(u -> u.getType())
				.collect(Collectors.toSet());
	}

	private Set<ICoReTypeName> getStoreTypes() {
		return usageStores.stream().flatMap(store -> store.getAllTypes().stream()).filter(t -> usageFilter.test(t))
				.collect(Collectors.toSet());
	}

	private ICallsRecommender<Query> trainRecommender(List<Usage> usages) {
		PBNMiner miner = pbnMinerProvider.get();
		return miner.createRecommender(usages);
	}

	private double calcMRR(ICallsRecommender<Query> recommender, Map<ICompletionEvent, List<Usage>> eventQueries) {
		DescriptiveStatistics reciprocalRank = new DescriptiveStatistics();
		for (Map.Entry<ICompletionEvent, List<Usage>> eventEntry : eventQueries.entrySet()) {
			ICompletionEvent event = eventEntry.getKey();
			IProposal expectedProposal = event.getLastSelectedProposal();
			ICoReMethodName expectedMethod = CoReNameConverter
					.convert((cc.kave.commons.model.names.IMethodName) expectedProposal.getName());

			for (Usage query : eventEntry.getValue()) {
				int rank = getRank(expectedMethod, new ArrayList<>(recommender.query(Query.createAsCopyFrom(query))));
				if (rank == 0) {
					reciprocalRank.addValue(0);
				} else {
					reciprocalRank.addValue(1.0 / rank);
				}
			}
		}

		return reciprocalRank.getMean();
	}

	private int getRank(ICoReMethodName expectedMethod, List<Tuple<ICoReMethodName, Double>> recommendations) {
		for (int r = 0; r < recommendations.size(); ++r) {
			ICoReMethodName method = recommendations.get(r).getFirst();
			if (expectedMethod.equals(method)) {
				return r + 1;
			}
		}
		return 0;
	}

	@Override
	public void close() throws IOException {
		for (UsageStore store : usageStores) {
			store.close();
		}
	}

}
