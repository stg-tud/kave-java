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
package exec.recommender_reimplementation.frequency_recommender;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IMethodName;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import exec.recommender_reimplementation.ContextReader;

public class RunFrequencyModelGeneration {

	public static final Path FOLDERPATH = Paths.get("C:\\SST Datasets\\Small Testset");
	
	public static void main(String[] args) {
		FrequencyModelGenerator modelgen = new FrequencyModelGenerator();
		List<Context> contextList = Lists.newLinkedList();
		try {
			contextList = ContextReader.GetContexts(FOLDERPATH);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Map<IMethodName, MethodFrequency> frequencyModel = Maps.newHashMap();
		
		for (Context context : contextList) {
			modelgen.generateMethodFrequencyModelForContext(context, frequencyModel);
		}
		
		printMap(frequencyModel);
	}
	
	private static void printMap(Map<IMethodName, MethodFrequency> map) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<IMethodName, MethodFrequency> entry : map.entrySet().stream()
				.sorted((entry1, entry2) -> Integer.compare(entry1.getValue().frequency, entry2.getValue().frequency))
				.collect(Collectors.toList())) 
		{
			sb.append(entry.getValue().methodName.getIdentifier() + "   Frequency: " + entry.getValue().frequency);
			sb.append("\n");
		}
		try {
			FileUtils.writeStringToFile(new File(FOLDERPATH + "\\frequency_model.txt"), sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}    
	}

}
