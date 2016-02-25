/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.evaluation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.random.RandomGenerator;

import com.google.inject.Inject;

import cc.kave.commons.pointsto.evaluation.annotations.NumberOfCVFolds;
import cc.recommenders.usages.Usage;

public abstract class AbstractCVFoldBuilder implements CrossValidationFoldBuilder {

	protected int numFolds;
	protected RandomGenerator rndGenerator;

	@Inject
	public AbstractCVFoldBuilder(@NumberOfCVFolds int numFolds, RandomGenerator rndGenerator) {
		this.numFolds = numFolds;
		this.rndGenerator = rndGenerator;
	}

	protected void shuffleUsages(Iterable<List<Usage>> usageLists) {
		Random rnd = new Random(rndGenerator.nextLong());
		for (List<Usage> usages : usageLists) {
			Collections.shuffle(usages, rnd);
		}
	}

	protected int calcAvgFoldSize(Collection<List<Usage>> usageLists) {
		return (int) (usageLists.stream().mapToLong(usages -> usages.size()).sum() / numFolds);
	}

}
