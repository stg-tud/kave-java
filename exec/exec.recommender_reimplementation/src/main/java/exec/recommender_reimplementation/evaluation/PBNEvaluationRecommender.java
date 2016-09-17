/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.recommender_reimplementation.evaluation;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.pointsto.evaluation.StatementCounterVisitor;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.mining.calls.ICallsRecommender;
import cc.recommenders.mining.calls.ProposalHelper;
import cc.recommenders.mining.calls.pbn.PBNMiner;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import exec.recommender_reimplementation.pbn.PBNQueryExtractor;
import exec.recommender_reimplementation.pbn.UsageExtractor;

public class PBNEvaluationRecommender extends EvaluationRecommender {

	private List<Usage> pbnFullUsageList;
	private PBNMiner pbnMiner;
	private PBNQueryExtractor pbnQueryExtraction;
	private StringBuilder log;
	private Map<ICoReTypeName, ICallsRecommender<Query>> typeToRecommenderMap;

	@Inject
	public PBNEvaluationRecommender(PBNMiner pbnMiner) {
		this.pbnMiner = pbnMiner;
		pbnFullUsageList = Lists.newArrayList();
	}

	@Override
	public String getName() {
		return "PBN";
	}

	@Override
	public void analysis(List<Context> contextList) {
		UsageExtractor usageExtractor = new UsageExtractor();
		for (Context context : contextList) {
			Integer statementCount = context.getSST().accept(new StatementCounterVisitor(), null);
			if (statementCount > EvaluationConstants.STATEMENT_LIMIT) {
				continue;
			}
			try {
				transformContext(context);
				usageExtractor.extractUsageFromContext(context, pbnFullUsageList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void initalizeRecommender() {
		Map<ICoReTypeName, List<Usage>> typeToUsagesMap = Maps.newHashMap();
		createTypeMap(typeToUsagesMap);
		typeToRecommenderMap = Maps.newHashMap();
		createRecommenders(typeToUsagesMap);
		pbnQueryExtraction = new PBNQueryExtractor();
	}

	protected void createTypeMap(Map<ICoReTypeName, List<Usage>> typeToUsagesMap) {
		for (Usage usage : pbnFullUsageList) {
			ICoReTypeName type = usage.getType();
			if(typeToUsagesMap.containsKey(type)) {
				typeToUsagesMap.get(type).add(usage);
			}
			else {
				typeToUsagesMap.put(type, Lists.newArrayList());
			}
		}
	}

	protected void createRecommenders(
			Map<ICoReTypeName, List<Usage>> typeToUsagesMap) {
		for (Entry<ICoReTypeName, List<Usage>> entry : typeToUsagesMap.entrySet()) {
			ICallsRecommender<Query> recommender = pbnMiner.createRecommender(entry.getValue());
			typeToRecommenderMap.put(entry.getKey(), recommender);
		}
	}

	@Override
	public Set<Tuple<ICoReMethodName, Double>> handleQuery(QueryContext query) {
		Query pbnQuery = pbnQueryExtraction.extractQueryFromCompletion(query.getCompletionEvent());
		ICoReTypeName type = pbnQuery.getType();
		Set<Tuple<ICoReMethodName, Double>> proposals = ProposalHelper.createSortedSet();
		ICallsRecommender<Query> recommender = null;
		if(typeToRecommenderMap.containsKey(type)) {
			recommender = typeToRecommenderMap.get(type);
			proposals = recommender.query(pbnQuery);
		}
		if (loggingActive) {
			addLogString(pbnQuery, query, proposals, recommender);
		}
		return proposals;
	}

	private void addLogString(Query pbnQuery, QueryContext query, Set<Tuple<ICoReMethodName, Double>> proposals, ICallsRecommender<Query> recommender) {
		log.append(query.getQueryName()).append(System.lineSeparator()).append(pbnQuery.toString()).append(System.lineSeparator())
				.append("Expected Method: ").append(System.lineSeparator())
				.append(query.getExpectedMethod().toString()).append(System.lineSeparator());
		if(recommender == null) {
			log.append("No model for this type");
		}
		else {
			log.append("Model Size: ").append(recommender.getSize());
		}
		log.append(System.lineSeparator()).append("Proposals: ").append(System.lineSeparator());
				
		for (Tuple<ICoReMethodName, Double> tuple : proposals) {
			log.append(tuple.getFirst()).append(" 	").append(tuple.getSecond()).append(System.lineSeparator());
		}
		log.append(System.lineSeparator());
	}

	@Override
	public boolean supportsAnalysis() {
		return true;
	}

	@Override
	public void setLogging(boolean value) {
		super.setLogging(value);
		if (loggingActive) {
			log = new StringBuilder();
		}
	}

	@Override
	public String returnLog() {
		return log.toString();
	}

}