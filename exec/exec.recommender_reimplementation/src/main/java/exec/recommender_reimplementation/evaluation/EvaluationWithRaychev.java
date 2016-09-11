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

import static exec.recommender_reimplementation.util.QueryUtil.calculateReciprocalRank;
import static exec.recommender_reimplementation.util.QueryUtil.isValidCompletionEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.pointsto.evaluation.StatementCounterVisitor;
import cc.kave.commons.pointsto.extraction.CoReNameConverter;
import cc.kave.commons.utils.json.JsonUtils;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.mining.calls.ICallsRecommender;
import cc.recommenders.mining.calls.pbn.PBNMiner;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import exec.recommender_reimplementation.heinemann_analysis.Entry;
import exec.recommender_reimplementation.heinemann_analysis.HeinemannQuery;
import exec.recommender_reimplementation.heinemann_analysis.HeinemannQueryExtractor;
import exec.recommender_reimplementation.heinemann_analysis.HeinemannRecommender;
import exec.recommender_reimplementation.heinemann_analysis.IndexExtractor;
import exec.recommender_reimplementation.java_transformation.JavaTransformationVisitor;
import exec.recommender_reimplementation.pbn.PBNQueryExtractor;
import exec.recommender_reimplementation.pbn.UsageExtractor;
import exec.recommender_reimplementation.raychev_analysis.RaychevEvaluation;
import exec.recommender_reimplementation.raychev_analysis.RaychevRecommender;
import exec.recommender_reimplementation.util.ContextReader;
import exec.recommender_reimplementation.util.QueryUtil;

public class EvaluationWithRaychev {

	private static final int HEINEMANN_LOOKBACK = 4;

	private static final double HEINEMANN_MINIMUM_PROBABILITY = 0.8;

	private static final int STATEMENT_LIMIT = 500;

	// Linux Path: public static String QUERY_PATH = "/home/markus/Documents/SLANG/tests/src/com/example/fill";
	public static String QUERY_PATH = "C:\\SSTDatasets\\WorkingQueries\\SuperPutty\\";

	// Linux Path: public static String ANALYSIS_PATH = "/media/sf_C_DRIVE/SSTDataSets/SmallTestset";
	public static String ANALYSIS_PATH = "C:\\SSTDatasets\\UsedForQueries\\";

	// Linux Path: public static String RESULT_PATH = "/home/markus/Documents/EvaluationResults/";
	public static String RESULT_PATH = "C:\\SSTDatasets\\";

	private List<QueryContext> queryList;

	private PBNMiner pbnMiner;

	private List<Usage> pbnUsageList;

	private Map<ITypeName, Set<Entry>> heinemannModel;

	@Inject
	public EvaluationWithRaychev(PBNMiner pbnMiner) {
		this.pbnMiner = pbnMiner;
		pbnUsageList = new ArrayList<>();
		heinemannModel = new HashMap<>();
	}

	public void runAnalysis() {
		List<Context> contextList = Lists.newLinkedList();
		try {
			List<Path> zipList = ContextReader.GetAllZipFiles(Paths.get(ANALYSIS_PATH));
			System.out.println("Now starting Analysis ...");
			int size = zipList.size();
			int zipNum = 0;
			for (Path path : zipList) {
				contextList = ContextReader.readType(path, Context.class);
				extractUsagesForPBN(contextList);
				extractHeinemannModel(contextList);
				zipNum++;
				System.out.println("Processed Zip Number: " + zipNum + " of " + size);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void runEvaluation() throws IOException, InterruptedException {
		readQueries();
		ICallsRecommender<Query> pbnRecommender = pbnMiner.createRecommender(pbnUsageList);
		ICallsRecommender<HeinemannQuery> heinemannRecommender = new HeinemannRecommender(heinemannModel, HEINEMANN_MINIMUM_PROBABILITY);
		RaychevRecommender raychevRecommender = new RaychevRecommender("");

		double pbnRR = 0.0;
		double heinemannRR = 0.0;
		double raychevRR = 0.0;

		int queryCount = 0;

		HeinemannQueryExtractor heinemannQueryExtractor = new HeinemannQueryExtractor();
		PBNQueryExtractor pbnQueryExtraction = new PBNQueryExtractor();

		System.out.println("Now starting Evaluation ...");

		for (QueryContext queryContext : queryList) {
			Context context = queryContext.completionEvent.context;
			transformContext(context);
			IMethodName expectedMethodName = QueryUtil.getExpectedMethodName(queryContext.getCompletionEvent());
			ICoReMethodName expectedCoReMethodName = CoReNameConverter.convert(expectedMethodName);

			heinemannRR += calculateHeinemannRank(heinemannRecommender, heinemannQueryExtractor, queryContext, expectedCoReMethodName);
			pbnRR += calculatePBNRank(pbnRecommender, pbnQueryExtraction, queryContext, expectedCoReMethodName);
			// raychevRR += calculateRaychevRank(raychevRecommender,
			// queryContext, expectedMethodName);

			queryCount++;
		}

		String evaluationResults = createEvaluationResults(pbnRR, heinemannRR, raychevRR, queryCount);
		writeEvaluationResults(evaluationResults);
	}

	private String createEvaluationResults(double pbnRR, double heinemannRR, double raychevRR, int queryCount) {
		double heinemannMRR = heinemannRR / queryCount;
		double pbnMRR = pbnRR / queryCount;
		double raychevMRR = raychevRR / queryCount;
		StringBuilder sb = new StringBuilder();
		sb.append("Evaluation Results for automatic evaluation");
		sb.append(System.lineSeparator());
		sb.append("Heinemann, PBN and Raychev");
		sb.append(System.lineSeparator());
		sb.append("Query Count: ").append(queryCount);
		sb.append(System.lineSeparator());
		sb.append("Heinemann MRR: ").append(heinemannMRR);
		sb.append(System.lineSeparator());
		sb.append("PBN MRR: ").append(pbnMRR);
		sb.append(System.lineSeparator());
		sb.append("Raychev MRR: ").append(raychevMRR);
		return sb.toString();
	}

	private void writeEvaluationResults(String evaluationResults) throws IOException {
		FileUtils.writeStringToFile(new File(RESULT_PATH + "EvaluationResults.txt"), evaluationResults);
	}

	private void readQueries() {
		queryList = generateQueries(QUERY_PATH);
	}

	private List<QueryContext> generateQueries(String path) {
		File[] folderPaths = new File(path).listFiles(File::isDirectory);
		List<QueryContext> queries = new ArrayList<>();
		for (File file : folderPaths) {
			String pathToCompletionEvent = MessageFormat.format("{0}/{1}/{1}.json", QUERY_PATH, file.getName());
			CompletionEvent completionEvent = JsonUtils.fromJson(new File(pathToCompletionEvent), CompletionEvent.class);
			if (isValidCompletionEvent(completionEvent)) {
				QueryContext query = new QueryContext(file.getName(), completionEvent);
				queries.add(query);
			}
		}
		return queries;
	}

	private double calculateHeinemannRank(ICallsRecommender<HeinemannQuery> heinemannRecommender, HeinemannQueryExtractor heinemannQueryExtractor,
			QueryContext queryContext, ICoReMethodName expectedCoReMethodName) {
		HeinemannQuery heinemannQuery = heinemannQueryExtractor.extractQueryFromCompletion(queryContext.getCompletionEvent(), HEINEMANN_LOOKBACK,
				false, false);
		Set<Tuple<ICoReMethodName, Double>> proposals = heinemannRecommender.query(heinemannQuery);
		double heinemannRank = calculateReciprocalRank(expectedCoReMethodName, proposals);
		return heinemannRank;
	}

	private double calculatePBNRank(ICallsRecommender<Query> pbnRecommender, PBNQueryExtractor pbnQueryExtraction, QueryContext queryContext,
			ICoReMethodName expectedCoReMethodName) {
		Query pbnQuery = pbnQueryExtraction.extractQueryFromCompletion(queryContext.getCompletionEvent());
		Set<Tuple<ICoReMethodName, Double>> proposals = pbnRecommender.query(pbnQuery);
		double pbnRank = calculateReciprocalRank(expectedCoReMethodName, proposals);
		return pbnRank;
	}

	private double calculateRaychevRank(RaychevRecommender raychevRecommender, QueryContext queryContext, IMethodName expectedMethodName)
			throws IOException, InterruptedException {
		raychevRecommender.executeRecommender(queryContext.getQueryName(), false);
		int rank = raychevRecommender.parseOutputAndReturnRank(RaychevEvaluation.getRaychevMethodName(expectedMethodName));
		return rank == 0 ? 0.0 : 1 / (double) rank;
	}

	private void extractHeinemannModel(List<Context> contextList) {
		Map<ITypeName, Set<Entry>> model = IndexExtractor.buildModel(contextList, HEINEMANN_LOOKBACK, false, false);
		mergeModel(model);
	}

	private void mergeModel(Map<ITypeName, Set<Entry>> model) {
		for (java.util.Map.Entry<ITypeName, Set<Entry>> entry : model.entrySet()) {
			if(heinemannModel.containsKey(entry.getKey())) {
				heinemannModel.get(entry.getKey()).addAll(entry.getValue());
			} else {
				heinemannModel.put(entry.getKey(), entry.getValue());
			}
		}
	}

	private void extractUsagesForPBN(List<Context> contextList) {
		UsageExtractor usageExtractor = new UsageExtractor();
		for (Context context : contextList) {
			Integer statementCount = context.getSST().accept(new StatementCounterVisitor(), null);
			if (statementCount > STATEMENT_LIMIT) {
				continue;
			}
			try {
				transformContext(context);
				usageExtractor.extractUsageFromContext(context, pbnUsageList);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void transformContext(Context context) {
		JavaTransformationVisitor transformationVisitor = new JavaTransformationVisitor(context.getSST());
		context.setSST(transformationVisitor.transform(context.getSST(), ISST.class));
	}
}
