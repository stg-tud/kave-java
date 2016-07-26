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
import java.util.Set;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.model.events.completionevents.Context;

import com.google.common.collect.Lists;

import exec.recommender_reimplementation.ContextReader;

public class RaychevRunner {
	public static final Path FOLDERPATH = Paths.get("C:\\SST Datasets\\Small Testset");

	
	public static void sentenceBuilder() throws IOException {
		List<Context> contextList = Lists.newLinkedList();
		try {
			contextList = ContextReader.GetContexts(FOLDERPATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		StringBuilder sb = new StringBuilder();
		
		for (Context context : contextList) {
			Set<ConcreteHistory> extractedHistories = extractHistories(context);
			sb.append(getHistoryAsString(extractedHistories));
			sb.append("\n");
		}
		
		FileUtils.writeStringToFile(new File(FOLDERPATH + "\\train_all"), sb.toString());
	}
	
	public static void main(String[] args) throws IOException {
		sentenceBuilder();

	}

}
