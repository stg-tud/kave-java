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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Provider;

import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.kave.commons.model.events.completionevents.IProposal;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.pointsto.PointsToAnalysisFactory;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.PointsToQueryBuilder;
import cc.kave.commons.pointsto.evaluation.PointsToUsageFilter;
import cc.kave.commons.pointsto.evaluation.ResultExporter;
import cc.kave.commons.pointsto.evaluation.TypeNameComparator;
import cc.kave.commons.pointsto.evaluation.UsagePruning;
import cc.kave.commons.pointsto.evaluation.measures.AbstractMeasure;
import cc.kave.commons.pointsto.extraction.CoReNameConverter;
import cc.kave.commons.pointsto.extraction.PointsToUsageExtractor;
import cc.kave.commons.pointsto.stores.UsageStore;
import cc.kave.commons.utils.ssts.SSTNodeHierarchy;
import cc.recommenders.mining.calls.ICallsRecommender;
import cc.recommenders.mining.calls.pbn.PBNMiner;
import cc.recommenders.names.CoReNames;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

public class MRREvaluation extends AbstractCompletionEventEvaluation implements Closeable {

	private static final Logger LOGGER = LoggerFactory.getLogger(MRREvaluation.class);

	private final List<UsageStore> usageStores;
	private final PointsToUsageFilter usageFilter;
	private final UsagePruning pruning;

	private final Provider<PBNMiner> pbnMinerProvider;
	private final AbstractMeasure measure;

	private final Map<Triple<ICoReTypeName, String, String>, Double> results = new HashMap<>();
	private final Map<Pair<ICoReTypeName, String>, Integer> numQueries = new HashMap<>();
	private final Map<String, Integer> zeroExtractedQueries = new HashMap<>();
	private final List<Triple<String, ICoReTypeName, Integer>> prunedUsages = new ArrayList<>();
	private final Map<String, Set<ICoReTypeName>> missingStoreTypes = new HashMap<>();

	@Inject
	public MRREvaluation(List<UsageStore> usageStores, PointsToUsageFilter usageFilter, UsagePruning pruning,
			Provider<PBNMiner> pbnMinerProvider, AbstractMeasure measure) {
		this.usageStores = usageStores;
		this.usageFilter = usageFilter;
		this.pruning = pruning;
		this.pbnMinerProvider = pbnMinerProvider;
		this.measure = measure;
	}

	public void exportResults(Path dir, ResultExporter exporter) throws IOException {
		Path resultFile = dir.resolve(getClass().getSimpleName() + ".txt");
		exporter.export(resultFile, results.entrySet().stream().map(entry -> {
			ICoReTypeName type = entry.getKey().getLeft();
			return new String[] { CoReNames.vm2srcQualifiedType(type), entry.getKey().getMiddle(),
					entry.getKey().getRight(), String.format(Locale.US, "%.5f", entry.getValue()) };
		}));

		Path numQueriesFile = dir.resolve(getClass().getSimpleName() + ".nq.txt");
		exporter.export(numQueriesFile,
				numQueries.entrySet().stream()
						.map(entry -> new String[] { CoReNames.vm2srcQualifiedType(entry.getKey().getLeft()),
								entry.getKey().getRight(), Integer.toString(entry.getValue()) }));

		Path zeroExtractedQueriesFile = dir.resolve(getClass().getSimpleName() + ".zeq.txt");
		exporter.export(zeroExtractedQueriesFile, zeroExtractedQueries.entrySet().stream()
				.map(entry -> new String[] { entry.getKey(), Integer.toString(entry.getValue()) }));

		Path prunedUsagesFile = dir.resolve(getClass().getSimpleName() + ".pruned.txt");
		exporter.export(prunedUsagesFile, prunedUsages.stream().map(entry -> new String[] { entry.getLeft(),
				CoReNames.vm2srcQualifiedType(entry.getMiddle()), entry.getRight().toString() }));

		exporter.export(dir.resolve(getClass().getSimpleName() + ".mst.txt"),
				missingStoreTypes.entrySet().stream().map(entry -> {
					String typeNames = entry.getValue().stream().map(CoReNames::vm2srcQualifiedType)
							.collect(Collectors.joining(","));
					return new String[] { entry.getKey(), typeNames };
				}));
	}

	@Override
	protected void evaluate(List<ICompletionEvent> completionEvents, List<PointsToAnalysisFactory> ptFactories)
			throws IOException {
		for (PointsToAnalysisFactory ptFactory : ptFactories) {
			Map<ICoReTypeName, Map<ICompletionEvent, List<Usage>>> queries = createQueries(completionEvents, ptFactory);
			Set<ICoReTypeName> types = getQueryTypes(queries.values());
			int numQueryTypes = types.size();
			Set<ICoReTypeName> storeTypes = getStoreTypes();
			missingStoreTypes.put(ptFactory.getName(), ImmutableSet.copyOf(Sets.difference(types, storeTypes)));
			types.retainAll(storeTypes);
			log("%s: %d types, queries for %d\n", ptFactory.getName(), types.size(), numQueryTypes);

			for (ICoReTypeName type : types) {
				evaluateType(ptFactory.getName(), type, queries);
			}
		}
	}

	private void evaluateType(String validationAnalysisName, ICoReTypeName type,
			Map<ICoReTypeName, Map<ICompletionEvent, List<Usage>>> queries) throws IOException {
		log("\t%s:\n", CoReNames.vm2srcQualifiedType(type));
		for (UsageStore store : usageStores) {

			ICallsRecommender<Query> recommender = null;
			{
				List<Usage> usages = store.load(type, usageFilter);
				store.flush();
				int numPrunedUsages = pruning.prune(MAX_USAGES, usages);
				if (numPrunedUsages > 0) {
					usages = new ArrayList<>(usages);
					prunedUsages.add(ImmutableTriple.of(store.getName(), type, numPrunedUsages));
				}

				if (!usages.isEmpty()) {
					recommender = trainRecommender(usages);
				}
			}

			double score = 0;
			if (recommender == null) {
				log("\t\t%s: no usages\n", store.getName());
			} else {
				score = calcMRR(recommender, queries.get(type));
				log("\t\t%s: %.3f\n", store.getName(), score);
			}
			results.put(ImmutableTriple.of(type, store.getName(), validationAnalysisName), score);
		}
	}

	private Map<ICoReTypeName, Map<ICompletionEvent, List<Usage>>> createQueries(
			List<ICompletionEvent> completionEvents, PointsToAnalysisFactory ptFactory) {
		Map<ICoReTypeName, Map<ICompletionEvent, List<Usage>>> queries = new HashMap<>();
		CompletionExpressionCollector completionExprCollector = new CompletionExpressionCollector();
		PointsToUsageExtractor usageExtractor = new PointsToUsageExtractor();

		int numFailedEvents = 0;
		for (ICompletionEvent event : completionEvents) {
			try {
				PointsToContext context = ptFactory.create().compute(event.getContext());
				PointsToQueryBuilder queryBuilder = new PointsToQueryBuilder(context);
				SSTNodeHierarchy nodeHierarchy = new SSTNodeHierarchy(context.getSST());
				List<ICompletionExpression> completionExprs = completionExprCollector.collect(context);

				for (ICompletionExpression expr : completionExprs) {
					List<Usage> exprQueries = usageExtractor.extractQueries(expr, context, queryBuilder, nodeHierarchy);

					if (exprQueries.isEmpty()) {
						increaseZeroExtractedQueries(ptFactory.getName());
					}

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

						increaseNumberOfQueries(query.getType(), ptFactory.getName());
					}
				}
			} catch (RuntimeException ex) {
				++numFailedEvents;
				if (ex.getMessage() != null && ex.getMessage().startsWith("Invalid Signature Syntax: ")) {
					LOGGER.error("Failed to extract queries for context due to MethodName.getSignature bug: {}",
							ex.getMessage());
				} else {
					LOGGER.error("Failed to create queries from completion event", ex);
				}
			}
		}

		if (numFailedEvents > 0) {
			log("Failed to create queries from %d completion events\n", numFailedEvents);
		}

		return queries;
	}

	private void increaseNumberOfQueries(ICoReTypeName type, String analysis) {
		Pair<ICoReTypeName, String> key = ImmutablePair.of(type, analysis);
		Integer currentNumber = numQueries.getOrDefault(key, 0);
		numQueries.put(key, currentNumber + 1);
	}

	private void increaseZeroExtractedQueries(String analysis) {
		int currentCount = zeroExtractedQueries.getOrDefault(analysis, 0);
		zeroExtractedQueries.put(analysis, currentCount + 1);
	}

	private Set<ICoReTypeName> getQueryTypes(Collection<Map<ICompletionEvent, List<Usage>>> eventUsages) {
		List<ICoReTypeName> types = new ArrayList<>(eventUsages.stream().flatMap(eu -> eu.values().stream())
				.flatMap(u -> u.stream()).map(u -> u.getType()).collect(Collectors.toSet()));
		types.sort(new TypeNameComparator());
		return Sets.newLinkedHashSet(types);
	}

	private Set<ICoReTypeName> getStoreTypes() {
		Set<ICoReTypeName> storeTypes = new HashSet<>();
		for (UsageStore store : usageStores) {
			Set<ICoReTypeName> types = store.getAllTypes().stream().filter(usageFilter::test)
					.collect(Collectors.toSet());
			if (storeTypes.isEmpty()) {
				storeTypes.addAll(types);
			} else {
				storeTypes.retainAll(types);
			}
		}
		return storeTypes;
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
					.convert((cc.kave.commons.model.naming.codeelements.IMethodName) expectedProposal.getName());

			for (Usage query : eventEntry.getValue()) {
				double rr = measure.calculate(recommender, Query.createAsCopyFrom(query),
						ImmutableSet.of(expectedMethod));
				reciprocalRank.addValue(rr);
			}
		}

		return reciprocalRank.getMean();
	}

	@Override
	public void close() throws IOException {
		for (UsageStore store : usageStores) {
			store.close();
		}
	}
}