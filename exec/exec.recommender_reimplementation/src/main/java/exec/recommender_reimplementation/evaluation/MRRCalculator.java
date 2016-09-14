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

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;

public class MRRCalculator implements MeasureCalculator {

	private int queryCount;
	private double reciprocalRank;

	public MRRCalculator() {
		queryCount = 0;
		reciprocalRank = 0.0;
	}

	@Override
	public void addValue(ICoReMethodName expectedMethod, Set<Tuple<ICoReMethodName, Double>> proposals) {
		queryCount++;
		Iterator<Tuple<ICoReMethodName, Double>> iterator = proposals.iterator();
		int i = 1;
		while (iterator.hasNext()) {
			Tuple<ICoReMethodName, Double> proposal = iterator.next();
			if (expectedMethod.equals(proposal.getFirst())) {
				reciprocalRank += 1 / (double) i;
				return;
			}
			i++;
		}
		reciprocalRank += 0;
	}

	@Override
	public double getMean() {
		return reciprocalRank / queryCount;
	}

	@Override
	public String getName() {
		return "MRR";
	}

	@Override
	public void addValue(String expectedRaychevMethod, List<String> proposals) {
		queryCount++;
		for (int i = 0; i < proposals.size(); i++) {
			String proposal = proposals.get(i);
			if(expectedRaychevMethod.equals(proposal)) {
				reciprocalRank += 1 / (double) (i + 1);
			}
		}
		reciprocalRank += 0;
	}

}
