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
package exec.recommender_reimplementation.pbn;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.json.JsonUtils;
import cc.recommenders.usages.Usage;

import com.google.common.collect.Lists;

import exec.recommender_reimplementation.ContextReader;


public class RunPbnModelGeneration {
	public static final Path FOLDERPATH = Paths.get("C:\\SST Datasets\\Small Testset");


	public static void main(String[] args) {
		UsageExtractor usageExtractor = new UsageExtractor();
		List<Context> contextList = Lists.newLinkedList();
		try {
			contextList = ContextReader.GetContexts(FOLDERPATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<Usage> usageList = new ArrayList<Usage>();  
		
		for (Context context : contextList) {
			usageExtractor.extractUsageFromContext(context, usageList);
		}
		
		printUsages(usageList);
	}
	
	private static void printUsages(List<Usage> usageList) {
		StringBuilder sb = new StringBuilder();
		for (Usage usage : usageList) 
		{
			sb.append(JsonUtils.toJson(usage));
			sb.append("\n");
		}
		try {
			FileUtils.writeStringToFile(new File(FOLDERPATH + "\\pbn_usages.txt"), sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}

}
