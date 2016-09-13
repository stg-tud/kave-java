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
package exec.recommender_reimplementation.evaluation;

import java.util.Set;
import java.util.stream.Collectors;

import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;

public class F1Calculator implements MeasureCalculator {

	private int queryCount;
	private double f1Value;

	public F1Calculator() {
		queryCount = 0;
		f1Value = 0.0;
	}

	@Override
	public void addValue(ICoReMethodName expectedMethod, Set<Tuple<ICoReMethodName, Double>> proposals) {
		queryCount++;
		int numberOfProposals = proposals.size();
		int numberOfHits = 0;
		Set<ICoReMethodName> proposedMethods = proposals.stream().map(tuple -> tuple.getFirst()).collect(Collectors.toSet());
		if (proposedMethods.contains(expectedMethod)) {
			numberOfHits = 1;
		}
		double precision = numberOfHits != 0 ? numberOfHits / (double) numberOfProposals : 0.0;
		double recall = numberOfHits / 1.0;
		double f1 = precision != 0.0 || recall != 0.0 ? 2 * (precision * recall / (precision + recall)) : 0.0;
		f1Value += f1;
	}

	@Override
	public double getMean() {
		return f1Value / queryCount;
	}

	@Override
	public String getName() {
		return "F1";
	}

}
