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
package exec.recommender_reimplementation.raychev_analysis;

import static cc.kave.commons.utils.json.JsonUtils.toJson;
import static org.apache.commons.io.FileUtils.writeStringToFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import exec.recommender_reimplementation.java_printer.JavaClassPathGenerator;
import exec.recommender_reimplementation.java_printer.PhantomClassGenerator;
import exec.recommender_reimplementation.java_printer.printer.JavaPrinter;
import exec.recommender_reimplementation.raychev_analysis.QueryGenerator.QueryStrategy;
import exec.recommender_reimplementation.util.ContextReader;

public class RaychevRunner {
	public static final Path FOLDERPATH = Paths.get("C:\\SSTDatasets\\NewTestset\\dotnet");
	public static Path QUERY_FOLDER_PATH;
	public static final Path WIN_QUERY_FOLDER_PATH = Paths.get("C:\\SSTDatasets\\Events\\");
	public static final Path LIN_QUERY_FOLDER_PATH = Paths.get("//media//sf_C_DRIVE//SSTDataSets//NewQuerySet");

	@SuppressWarnings("unchecked")
	public static void sentenceBuilder() throws IOException {
		Queue<Context> contextList = Lists.newLinkedList();
		try {
			List<Path> zipList = ContextReader.GetAllZipFiles(FOLDERPATH);
			for (Path path : zipList) {
				contextList = (Queue<Context>) ContextReader.readType(path, Context.class);
				buildSentencesForContextList(contextList);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(HistoryExtractor.filteredCount);
	}

	private static void buildSentencesForContextList(Queue<Context> contextList) {
		HistoryExtractor historyExtractor = new HistoryExtractor();
		while (!contextList.isEmpty()) {
			Context context = contextList.poll();
			try (FileWriter fw = new FileWriter(FOLDERPATH + File.separator + "train_all", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {
				try {
					Set<ConcreteHistory> extractedHistories = historyExtractor.extractHistories(context);
					for (ConcreteHistory concreteHistory : extractedHistories) {
						out.print(historyExtractor.getHistoryAsString(concreteHistory));
					}
				} catch (Exception e) {
					continue;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void printContexts() throws IOException {
		List<Context> contextList = readContexts(FOLDERPATH);

		for (Context context : contextList) {
			String javaCode = new JavaPrinter().print(context);
			File file = JavaClassPathGenerator.generateClassPath(context.getSST(), FOLDERPATH.toString());
			writeStringToFile(file, javaCode);
		}
	}

	public static void queryBuilderWithRandomHoles() {
		queryBuilder(QueryStrategy.RANDOM);
	}

	public static void queryBuilderFromCompletionExpressions() {
		queryBuilder(QueryStrategy.COMPLETION);
	}

	private static void queryBuilder(QueryStrategy queryStrategy) {
		List<Context> contextList = readContexts(QUERY_FOLDER_PATH);
		int counter = 0;
		for (Context context : contextList) {
			counter = buildQueryFromContext(queryStrategy, counter, context);
		}
		System.out.println("Number of unsuccessful queries: " + counter);
		System.out.println("Number of contexts: " + contextList.size());
	}

	private static int buildQueryFromContext(QueryStrategy queryStrategy, int counter, Context context) {
		try {
			Path path = Paths.get(QUERY_FOLDER_PATH.toString(), "Query_" + context.getSST().getEnclosingType().getName());
			if (new File(path.toString()).exists()) {
				return counter;
			}
			QueryGenerator queryGenerator = new QueryGenerator(path);
			boolean successful = queryGenerator.generateQuery(context, queryStrategy);
			if (successful) {
				writeClassPaths(Sets.newHashSet(context), path);
				writeCompletionEvent(context, queryGenerator.getLatestQuery());
			}
			else {
				System.out.println("Unsuccessful Query: " + context.getSST().getEnclosingType().getName());
				counter++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return counter;
	}

	private static void writeCompletionEvent(Context context, String completionEventAsString) throws IOException {
		String queryFileName = "Query_" + context.getSST().getEnclosingType().getName();
		writeStringToFile(new File(QUERY_FOLDER_PATH + File.separator + queryFileName + File.separator + queryFileName + ".json"),
				completionEventAsString);
	}

	private static void writeCompletionEvent(Context context, String completionEventAsString, int counter) throws IOException {
		String queryFileName = "Query_" + context.getSST().getEnclosingType().getName();
		writeStringToFile(new File(QUERY_FOLDER_PATH + File.separator + queryFileName + counter + File.separator + queryFileName + ".json"),
				completionEventAsString);
	}

	private static List<Context> readContexts(Path path) {
		List<Context> contextList = new LinkedList<>();
		try {
			contextList = ContextReader.GetContexts(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return contextList;
	}

	public static void queryBuilderFromCompletionEvents() {
		List<CompletionEvent> completionEventList = Lists.newLinkedList();
		try {
			completionEventList = ContextReader.GetCompletionEvents(QUERY_FOLDER_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int unsuccesfulCounter = 0;
		int successfulCounter = 0;
		for (CompletionEvent completionEvent: completionEventList) {
			try {
				Context context = completionEvent.context;
				Path path = Paths.get(QUERY_FOLDER_PATH.toString(), "Query_" + context.getSST().getEnclosingType().getName() + successfulCounter);
				if (new File(path.toString()).exists()) {
					continue;
				}
				QueryGenerator queryGenerator = new QueryGenerator(path);
				String completionEventAsJson = toJson(completionEvent);
				boolean successful = queryGenerator.generateQuery(completionEvent);
				if (successful) {
					writeClassPaths(Sets.newHashSet(context), path);
					writeCompletionEvent(context, completionEventAsJson, successfulCounter);
					successfulCounter++;
				} else {
					System.out.println("Unsuccessful Query: " + context.getSST().getEnclosingType().getName());
					unsuccesfulCounter++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Number of unsuccessful queries: " + unsuccesfulCounter);
		System.out.println("Number of contexts: " + completionEventList.size());
	}

	private static void writeClassPaths(Set<Context> contexts, Path path) {
		JavaClassPathGenerator classPathGenerator = new JavaClassPathGenerator(path.toString());
		PhantomClassGenerator classGenerator = new PhantomClassGenerator();
		Set<ISST> convertedSSTs = classGenerator.convert(contexts);
		try {
			classPathGenerator.generate(convertedSSTs);
			copyOverJavaCSharpUtilsFiles(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void copyOverJavaCSharpUtilsFiles(Path path) throws IOException {
		FileUtils.copyDirectory(new File(QUERY_FOLDER_PATH.toString() + File.separator + "JavaToCSharpUtils"),
				new File(path.toString() + File.separator + "JavaToCSharpUtils"));
	}

	public static void main(String[] args) throws IOException {
		if (System.getProperty("os.name").startsWith("Windows")) {
			QUERY_FOLDER_PATH = WIN_QUERY_FOLDER_PATH;
		} else {
			QUERY_FOLDER_PATH = LIN_QUERY_FOLDER_PATH;
		}
		// sentenceBuilder();
		// printContexts();
		// queryBuilderFromCompletionExpressions();
		queryBuilderFromCompletionEvents();
		// queryBuilderWithRandomHoles();
	}

}
