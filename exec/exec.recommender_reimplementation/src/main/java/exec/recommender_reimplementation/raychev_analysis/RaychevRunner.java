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

import static exec.recommender_reimplementation.raychev_analysis.HistoryExtractor.extractHistories;
import static exec.recommender_reimplementation.raychev_analysis.HistoryExtractor.getHistoryAsString;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;

import com.google.common.collect.Lists;

import exec.recommender_reimplementation.ContextReader;

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

		while (!contextList.isEmpty()) {
			Context context = contextList.poll();
			try {
				Set<ConcreteHistory> extractedHistories = extractHistories(context);
				sb.append(getHistoryAsString(extractedHistories));
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

		for (Context context : contextList) {
			try {
				String javaCode = QueryExtractor.createJavaCodeForQuery(context);
				if (!javaCode.isEmpty()) {
					FileUtils.writeStringToFile(new File(FOLDERPATH + "\\"
							+ context.getSST().getEnclosingType().getName() + ".java"), javaCode);
				}
			} catch (Exception e) {
				continue;
			}
		}
	}

	private static void queryFromCompletionEvents() {
		List<CompletionEvent> completionEventList = Lists.newLinkedList();
		try {
			completionEventList = ContextReader.GetCompletionEvents(Paths.get(FOLDERPATH.toString() + "\\Queries"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (CompletionEvent completionEvent: completionEventList) {
			try {
				String javaCode = QueryExtractor.createJavaCodeForQuery(completionEvent);
				if (!javaCode.isEmpty()) {
					FileUtils.writeStringToFile(new File(FOLDERPATH + "\\"
							+ completionEvent.getContext().getSST().getEnclosingType().getName() + ".java"), javaCode);
				}
			} catch (Exception e) {
				continue;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		sentenceBuilder();
		queryBuilder();
		queryFromCompletionEvents();
	}

}
