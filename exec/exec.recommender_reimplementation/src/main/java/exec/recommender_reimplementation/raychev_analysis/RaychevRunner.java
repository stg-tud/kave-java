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

import java.io.File;
import java.io.IOException;
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

public class RaychevRunner {
	public static final Path FOLDERPATH = Paths.get("C:\\SST Datasets\\Testset");

	@SuppressWarnings("unchecked")
	public static void sentenceBuilder() throws IOException {
		Queue<Context> contextList = Lists.newLinkedList();
		try {
			contextList = (Queue<Context>) ContextReader.GetContexts(FOLDERPATH);
		} catch (IOException e) {
			e.printStackTrace();
		}

		StringBuilder sb = new StringBuilder();
		HistoryExtractor historyExtractor = new HistoryExtractor();
		while (!contextList.isEmpty()) {
			Context context = contextList.poll();
			try {
				Set<ConcreteHistory> extractedHistories = historyExtractor.extractHistories(context);
				sb.append(historyExtractor.getHistoryAsString(extractedHistories));
				sb.append("\n");
			} catch (Exception e) {
				continue;
			}
		}

		FileUtils.writeStringToFile(new File(FOLDERPATH + "\\train_all"), sb.toString());
	}

	public static void queryBuilder() {
		List<Context> contextList = Lists.newLinkedList();
		try {
			contextList = ContextReader.GetContexts(Paths.get(FOLDERPATH.toString() + "\\Queries"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		QueryExtractor queryExtractor = new QueryExtractor();
		Set<ISST> ssts = Sets.newHashSet();
		for (Context context : contextList) {
			try {
				String javaCode = queryExtractor.createJavaCodeForQuery(context);
				if (!javaCode.isEmpty()) {
					writeJavaFile(javaCode, context.getSST());
					ssts.add(context.getSST());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		writeClassPaths(ssts);
	}

	private static void queryFromCompletionEvents() {
		List<CompletionEvent> completionEventList = Lists.newLinkedList();
		try {
			completionEventList = ContextReader.GetCompletionEvents(Paths.get(FOLDERPATH.toString() + "\\Queries"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		QueryExtractor queryExtractor = new QueryExtractor();
		Set<ISST> ssts = Sets.newHashSet();
		for (CompletionEvent completionEvent: completionEventList) {
			try {
				String javaCode = queryExtractor.createJavaCodeForQuery(completionEvent);
				if (!javaCode.isEmpty()) {
					ISST sst = completionEvent.getContext().getSST();
					writeJavaFile(javaCode, sst);
					ssts.add(sst);
				}
			} catch (Exception e) {
				continue;
			}
		}
		writeClassPaths(ssts);
	}

	private static void writeJavaFile(String javaCode, ISST sst) throws IOException {
		FileUtils.writeStringToFile(new File(FOLDERPATH + "\\Queries" + "\\"
				+ sst.getEnclosingType().getName() + ".java"), javaCode);
	}
	
	private static void writeClassPaths(Set<ISST> ssts) {
		JavaClassPathGenerator phantomClassGenerator = new JavaClassPathGenerator(FOLDERPATH.toString()+ "\\Queries"); 
		try {
			phantomClassGenerator.generate(ssts);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		sentenceBuilder();
		queryBuilder();
//		queryFromCompletionEvents();
	}

}
