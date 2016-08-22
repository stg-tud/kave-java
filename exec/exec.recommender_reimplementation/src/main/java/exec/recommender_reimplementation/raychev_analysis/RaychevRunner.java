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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import exec.recommender_reimplementation.java_printer.printer.JavaPrinter;

public class RaychevRunner {
	public static final Path FOLDERPATH = Paths.get("C:\\SST Datasets\\Testset");
	public static final Path QUERY_FOLDER_PATH = Paths.get(FOLDERPATH.toString() + "\\Queries");

	@SuppressWarnings("unchecked")
	public static void sentenceBuilder() throws IOException {
		Queue<Context> contextList = Lists.newLinkedList();
		try {
			contextList = (Queue<Context>) ContextReader.GetContexts(QUERY_FOLDER_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}

		HistoryExtractor historyExtractor = new HistoryExtractor();
		while (!contextList.isEmpty()) {
			Context context = contextList.poll();
			try (FileWriter fw = new FileWriter(QUERY_FOLDER_PATH + "\\train_all", true);
					BufferedWriter bw = new BufferedWriter(fw);
					PrintWriter out = new PrintWriter(bw)) {
				try {
					Set<ConcreteHistory> extractedHistories = historyExtractor.extractHistories(context);
					out.println(historyExtractor.getHistoryAsString(extractedHistories));
				} catch (Exception e) {
					continue;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void queryBuilder() {
		List<Context> contextList = Lists.newLinkedList();
		try {
			contextList = ContextReader.GetContexts(QUERY_FOLDER_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}

		QueryExtractor queryExtractor = new QueryExtractor();
		Set<Context> contexts = Sets.newHashSet();
		for (Context context : contextList) {
			try {
				String javaCode = new JavaPrinter().print(context);
				ISST transformedQuery = new RaychevQueryTransformer().transfromIntoQuery(context.getSST());
				if (transformedQuery == null) {
					continue;
				}
				String transformedQueryJavaCode = queryExtractor.createJavaCodeForQuery(transformedQuery);
				if (!javaCode.isEmpty()) {
					ISST sst = context.getSST();
					if (!sst.getEnclosingType().isUnknown()) {
						File file = JavaClassPathGenerator.generateClassPath(sst, QUERY_FOLDER_PATH.toString());
						writeJavaFile(javaCode, file);
						writeQueryFile(transformedQueryJavaCode, transformedQuery);
						contexts.add(context);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}
		writeClassPaths(contexts);
	}

	private static void queryFromCompletionEvents() {
		List<CompletionEvent> completionEventList = Lists.newLinkedList();
		try {
			completionEventList = ContextReader.GetCompletionEvents(QUERY_FOLDER_PATH);
		} catch (IOException e) {
			e.printStackTrace();
		}

		QueryExtractor queryExtractor = new QueryExtractor();
		Set<Context> contexts = Sets.newHashSet();
		for (CompletionEvent completionEvent: completionEventList) {
			try {
				Context context = completionEvent.getContext();
				String javaCode = new JavaPrinter().print(context);
				ISST transformedQuery = new RaychevQueryTransformer()
						.transfromIntoQuery(context.getSST());
				if (transformedQuery == null) {
					continue;
				}
				String transformedQueryJavaCode = queryExtractor.createJavaCodeForQuery(transformedQuery);
				if (!javaCode.isEmpty()) {
					ISST sst = completionEvent.getContext().getSST();
					if (!sst.getEnclosingType().isUnknown()) {
						File file = JavaClassPathGenerator.generateClassPath(sst, QUERY_FOLDER_PATH.toString());
						writeJavaFile(javaCode, file);
						writeQueryFile(transformedQueryJavaCode, transformedQuery);
						contexts.add(context);
					}
				}
			} catch (Exception e) {
				continue;
			}
		}
		writeClassPaths(contexts);
	}

	private static void writeJavaFile(String javaCode, File file) throws IOException {
		FileUtils.writeStringToFile(file, javaCode);
	}
	
	private static void writeQueryFile(String javaCode, ISST sst) throws IOException {
		FileUtils.writeStringToFile(
				new File(QUERY_FOLDER_PATH.toString() + "\\" + sst.getEnclosingType().getName() + ".java"),
				javaCode);
	}

	private static void writeClassPaths(Set<Context> contexts) {
		JavaClassPathGenerator classPathGenerator = new JavaClassPathGenerator(QUERY_FOLDER_PATH.toString()); 
		try {
			classPathGenerator.generate(contexts);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		// sentenceBuilder();
		queryBuilder();
//		queryFromCompletionEvents();
	}

}
