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

import static exec.recommender_reimplementation.util.QueryUtil.isValidCompletionEvent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.pointsto.extraction.CoReNameConverter;
import cc.kave.commons.utils.json.JsonUtils;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;
import exec.recommender_reimplementation.java_transformation.JavaTransformationVisitor;
import exec.recommender_reimplementation.util.ContextReader;
import exec.recommender_reimplementation.util.QueryUtil;

public class EvaluationExecutor {

	public static final int STATEMENT_LIMIT = 800;

	public static String LIN_QUERY_PATH = "/home/markus/Documents/SLANG/tests/src/com/example/fill";
	public static String WIN_QUERY_PATH = "C:\\SSTDatasets\\WorkingQueries\\SuperPutty\\";
	public String queryPath;

	public static String LIN_ANALYSIS_PATH = "/media/sf_C_DRIVE/SSTDataSets/UsedForQueries";
	public static String WIN_ANALYSIS_PATH = "C:\\SSTDatasets\\UsedForQueries\\";
	public String analysisPath;

	public static String LIN_RESULT_PATH = "/home/markus/Documents/EvaluationResults/";
	public static String WIN_RESULT_PATH = "C:\\SSTDatasets\\";
	public String resultPath;

	private List<QueryContext> queryList;

	private List<EvaluationRecommender> recommender;

	public EvaluationExecutor() {
		Injector injector = Guice.createInjector(new PBNMinerModule());
		
		if (isWindows()) {
			queryPath = WIN_QUERY_PATH;
			analysisPath = WIN_ANALYSIS_PATH;
			resultPath = WIN_RESULT_PATH;

			recommender = Lists.newArrayList(new HeinemannEvaluationRecommender(), //
					injector.getInstance(PBNEvaluationRecommender.class));
		} else {
			queryPath = LIN_QUERY_PATH;
			analysisPath = LIN_ANALYSIS_PATH;
			resultPath = LIN_RESULT_PATH;

			recommender = Lists.newArrayList(new HeinemannEvaluationRecommender(), new RaychevEvaluationRecommender(),
					injector.getInstance(PBNEvaluationRecommender.class));
		}

		for (EvaluationRecommender evalRecommender : recommender) {
			evalRecommender.initalizeMeasures(Sets.newHashSet(new F1Calculator(), new MRRCalculator()));
		}
	}

	public void runAnalysis() {
		List<Context> contextList = Lists.newLinkedList();
		try {
			List<Path> zipList = ContextReader.GetAllZipFiles(Paths.get(analysisPath));
			System.out.println("Now starting Analysis ...");
			int size = zipList.size();
			int zipNum = 0;
			for (Path path : zipList) {
				contextList = ContextReader.readType(path, Context.class);
				for (EvaluationRecommender evalRecommender : recommender) {
					if (evalRecommender.supportsAnalysis()) {
						evalRecommender.analysis(contextList);
					}
				}
				zipNum++;
				System.out.println("Processed Zip Number: " + zipNum + " of " + size);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void runEvaluation() throws IOException, InterruptedException {
		readQueries();
		System.out.println("Now building Models ...");
		for (EvaluationRecommender evalRecommender : recommender) {
			evalRecommender.initalizeRecommender();
		}
		System.out.println("Now starting Evaluation ...");

		int queryCount = 0;
		for (QueryContext queryContext : queryList) {
			queryCount++;
			Context context = queryContext.completionEvent.context;
			transformContext(context);
			IMethodName expectedMethodName = QueryUtil.getExpectedMethodName(queryContext.getCompletionEvent());
			ICoReMethodName expectedCoReMethodName = CoReNameConverter.convert(expectedMethodName);

			for (EvaluationRecommender evalRecommender : recommender) {
				Set<Tuple<ICoReMethodName, Double>> proposals = evalRecommender.handleQuery(queryContext);
				evalRecommender.calculateMeasures(proposals, expectedCoReMethodName);
			}
		}

		String evaluationResults = createEvaluationResults(queryCount);
		writeEvaluationResults(evaluationResults);
	}

	private void readQueries() {
		queryList = Lists.newArrayList();
		File[] folderPaths = new File(queryPath).listFiles(File::isDirectory);
		for (File file : folderPaths) {
			String pathToCompletionEvent = MessageFormat.format("{0}/{1}/{1}.json", queryPath, file.getName());
			CompletionEvent completionEvent = JsonUtils.fromJson(new File(pathToCompletionEvent), CompletionEvent.class);
			if (isValidCompletionEvent(completionEvent)) {
				QueryContext query = new QueryContext(file.getName(), completionEvent);
				queryList.add(query);
			}
		}
	}

	private String createEvaluationResults(int queryCount) {
		StringBuilder sb = new StringBuilder();
		sb.append("Evaluation Results for automatic evaluation");
		sb.append(System.lineSeparator());
		sb.append("Query Count: ").append(queryCount);
		sb.append(System.lineSeparator());
		for (EvaluationRecommender evalRecommender : recommender) {
			sb.append(evalRecommender.getEvaluationResults());
			sb.append(System.lineSeparator());
		}
		return sb.toString();
	}

	private void writeEvaluationResults(String evaluationResults) throws IOException {
		FileUtils.writeStringToFile(new File(resultPath + "EvaluationResults.txt"), evaluationResults);
	}

	private void transformContext(Context context) {
		JavaTransformationVisitor transformationVisitor = new JavaTransformationVisitor(context.getSST());
		context.setSST(transformationVisitor.transform(context.getSST(), ISST.class));
	}

	private boolean isWindows() {
		return System.getProperty("os.name").startsWith("Windows");
	}
}
