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
import exec.recommender_reimplementation.ContextReader;
import exec.recommender_reimplementation.java_printer.JavaClassPathGenerator;
import exec.recommender_reimplementation.java_printer.PhantomClassGenerator;
import exec.recommender_reimplementation.java_printer.printer.JavaPrinter;
import exec.recommender_reimplementation.raychev_analysis.QueryGenerator.QueryStrategy;

public class RaychevRunner {
	public static final Path FOLDERPATH = Paths.get("C:\\SST Datasets\\NewTestset");
	public static final Path QUERY_FOLDER_PATH = Paths.get("C:\\SST Datasets\\NewQuerySet");

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

		buildSentencesForContextList(contextList);
	}

	private static void buildSentencesForContextList(Queue<Context> contextList) {
		HistoryExtractor historyExtractor = new HistoryExtractor();
		while (!contextList.isEmpty()) {
			Context context = contextList.poll();
			try (FileWriter fw = new FileWriter(FOLDERPATH + "\\train_all", true);
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
			try {
				Path path = Paths.get(QUERY_FOLDER_PATH.toString(), "Query_" + context.getSST().getEnclosingType().getName());
				if (new File(path.toString()).exists()) {
					continue;
				}
				QueryGenerator queryGenerator = new QueryGenerator(path);
				boolean successful = queryGenerator.generateQuery(context, queryStrategy);
				if (successful) {
					writeClassPaths(Sets.newHashSet(context), path);
				}
				else {
					System.out.println("Unsuccessful Query: " + context.getSST().getEnclosingType().getName());
					counter++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("Number of unsuccessful queries: " + counter);
		System.out.println("Number of contexts: " + contextList.size());
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

		for (CompletionEvent completionEvent: completionEventList) {
			try {
				Path path = Paths.get(QUERY_FOLDER_PATH.toString(), "Query_" + completionEvent.context.getSST().getEnclosingType().getName());
				QueryGenerator queryGenerator = new QueryGenerator(path);
				boolean successful = queryGenerator.generateQuery(completionEvent);
				if (successful) {
					writeClassPaths(Sets.newHashSet(completionEvent.getContext()), QUERY_FOLDER_PATH);
				}
			} catch (Exception e) {
				continue;
			}
		}
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
		FileUtils.copyDirectory(new File(QUERY_FOLDER_PATH.toString() + "\\JavaToCSharpUtils"), new File(path.toString() + "\\JavaToCSharpUtils"));
	}

	public static void main(String[] args) throws IOException {
		sentenceBuilder();
		// printContexts();
		// queryBuilderFromCompletionExpressions();
		// queryBuilderFromCompletionEvents();
		// queryBuilderWithRandomHoles();
	}

}
