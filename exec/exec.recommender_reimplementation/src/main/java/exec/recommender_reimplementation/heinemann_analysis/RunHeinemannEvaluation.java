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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.recommenders.datastructures.Tuple;
import exec.recommender_reimplementation.util.ContextReader;

public class RunHeinemannEvaluation {

	public static final Path FOLDERPATH = Paths.get("C:\\SST Datasets\\Small Testset");

	public static void main(String[] args) {
		List<Context> contextList = new LinkedList<Context>();
		try {
			contextList = ContextReader.GetContexts(FOLDERPATH);
		} catch (IOException e) {
			System.exit(0);
		}
		
		Tuple<List<Context>, List<Context>> contextTuple = splitContexts(contextList);
		
		Map<ITypeName, Set<Entry>> model = IndexExtractor.buildModel(contextTuple.getFirst(), 4, false, false);
		HeinemannRecommender heinemannRecommender = new HeinemannRecommender(model,0.8);
		
		HeinemannEvaluation heinemannEvaluation = new HeinemannEvaluation(heinemannRecommender, 4, false, false);
		double mrrScore = heinemannEvaluation.evaluateSet(contextTuple.getSecond());
		
		System.out.println("MRR: " + mrrScore);
		System.out.print("Invocation Count: " + heinemannEvaluation.invocationCount);
	}

	private static Tuple<List<Context>, List<Context>> splitContexts(List<Context> contextList) {
		int size = contextList.size();
		int evaluationSize = size / 10;
		Random randomGen = new Random();
		List<Context> evaluationSet = new LinkedList<>();
		for (int i = 0; i < evaluationSize; i++) {
			int index = randomGen.nextInt(size);
			evaluationSet.add(contextList.get(index));
			contextList.remove(index);
			size--;
		}
		return Tuple.newTuple(contextList, evaluationSet);
	}

}
