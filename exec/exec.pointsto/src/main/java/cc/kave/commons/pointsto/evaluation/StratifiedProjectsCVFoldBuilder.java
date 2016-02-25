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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;

import com.google.inject.Inject;

import cc.kave.commons.pointsto.evaluation.annotations.NumberOfCVFolds;
import cc.kave.commons.pointsto.stores.ProjectIdentifier;
import cc.recommenders.usages.Usage;

public class StratifiedProjectsCVFoldBuilder extends AbstractCVFoldBuilder {

	@Inject
	public StratifiedProjectsCVFoldBuilder(@NumberOfCVFolds int numFolds, RandomGenerator rndGenerator) {
		super(numFolds, rndGenerator);
	}

	@Override
	public List<List<Usage>> createFolds(Map<ProjectIdentifier, List<Usage>> projectUsages) {
		EnumeratedDistribution<ProjectIdentifier> projectDistribution;
		List<Pair<ProjectIdentifier, Double>> projectProbabilities = new ArrayList<>(projectUsages.size());

		// initialize distributions
		long totalUsages = 0;
		for (Map.Entry<ProjectIdentifier, List<Usage>> project : projectUsages.entrySet()) {
			projectProbabilities
					.add(new Pair<ProjectIdentifier, Double>(project.getKey(), (double) project.getValue().size()));
			totalUsages += project.getValue().size();
		}
		// normalization of the probability mass is done by the constructor
		projectDistribution = new EnumeratedDistribution<>(rndGenerator, projectProbabilities);

		shuffleUsages(projectUsages.values());

		List<List<Usage>> folds = new ArrayList<>(numFolds);
		int avgFoldSize = (int) (totalUsages / numFolds);
		for (int f = 0; f < numFolds - 1; ++f) {
			List<Usage> fold = new ArrayList<>(avgFoldSize);
			folds.add(fold);

			for (int i = 0; i < avgFoldSize; ++i) {
				ProjectIdentifier project;
				List<Usage> usages;
				do {
					project = projectDistribution.sample();
					usages = projectUsages.get(project);
				} while (usages.isEmpty());

				int lastIndex = usages.size() - 1;
				Usage usage = usages.remove(lastIndex);
				fold.add(usage);
			}
		}
		// add remaining usages to the last fold
		List<Usage> lastFold = new ArrayList<>(avgFoldSize);
		folds.add(lastFold);
		for (List<Usage> usages : projectUsages.values()) {
			lastFold.addAll(usages);
			usages.clear();
		}
		Collections.shuffle(lastFold, new Random(rndGenerator.nextLong()));

		return folds;
	}

}
