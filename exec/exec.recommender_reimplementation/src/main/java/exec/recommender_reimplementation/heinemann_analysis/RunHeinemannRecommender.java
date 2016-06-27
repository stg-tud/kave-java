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
package exec.recommender_reimplementation.heinemann_analysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;

import com.google.common.collect.Sets;

import exec.recommender_reimplementation.ContextReader;

public class RunHeinemannRecommender {

	public static final Path FOLDERPATH = Paths.get("C:\\SST Datasets\\Small Testset");
	
	public static void main(String[] args) {
		List<Context> contextList = new LinkedList<Context>();
		try {
			contextList = ContextReader.GetContexts(FOLDERPATH);
		} catch (IOException e) {
			System.exit(0);
		}
		
		Map<ITypeName, Set<Entry>> model = IndexExtractor.buildModel(contextList, 3, false, false);
		HeinemannRecommender heinemannRecommender = new HeinemannRecommender(model,10);
		
		// read queries here...
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
				
		while(true) {
			try {	
				System.out.println("Input lookback");
				String inputLookback;
				inputLookback = reader.readLine();
				Set<String> lookback = Sets.newHashSet(inputLookback.split(" "));
				
				System.out.println("Input type name");
				String typeNameString = reader.readLine();
				ITypeName declaringType = TypeName.newTypeName(typeNameString);
				
				HeinemannQuery query = new HeinemannQuery(lookback, declaringType, 0.4);
				Set<Tuple<ICoReMethodName, Double>> results = heinemannRecommender.query(query);
				
				for (Tuple<ICoReMethodName, Double> tuple : results) {
					System.out.println();
					System.out.println(tuple.getFirst().toString() + " " + tuple.getSecond());
				}
				
			} catch (IOException e) {
				System.out.println("wrong input");
			}

		}
		
	}

}
