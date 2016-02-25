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
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.random.RandomGenerator;

import com.google.inject.Inject;

import cc.kave.commons.pointsto.evaluation.annotations.NumberOfCVFolds;
import cc.kave.commons.pointsto.stores.ProjectIdentifier;
import cc.recommenders.usages.Usage;

public class ProjectCVFoldBuilder extends AbstractCVFoldBuilder {

	@Inject
	public ProjectCVFoldBuilder(@NumberOfCVFolds int numFolds, RandomGenerator rndGenerator) {
		super(numFolds, rndGenerator);
	}

	@Override
	public List<List<Usage>> createFolds(Map<ProjectIdentifier, List<Usage>> projectUsages) {
		shuffleUsages(projectUsages.values());

		List<ProjectIdentifier> projects = new ArrayList<>(projectUsages.keySet());
		// sort projects in ascending order according to the number of usages
		projects.sort(new Comparator<ProjectIdentifier>() {

			@Override
			public int compare(ProjectIdentifier o1, ProjectIdentifier o2) {
				return projectUsages.get(o1).size() - projectUsages.get(o2).size();
			}
		});

		List<List<Usage>> folds = createFolds(calcAvgFoldSize(projectUsages.values()));
		for (int i = projects.size() - 1; i >= 0; --i) {
			ProjectIdentifier project = projects.get(i);
			List<Usage> fold = getSmallestFold(folds);

			fold.addAll(projectUsages.remove(project));
		}

		shuffleUsages(folds);
		return folds;
	}

	private List<List<Usage>> createFolds(int avgFoldSize) {
		List<List<Usage>> folds = new ArrayList<>(numFolds);
		for (int i = 0; i < numFolds; ++i) {
			folds.add(new ArrayList<>(avgFoldSize));
		}

		return folds;
	}

	private List<Usage> getSmallestFold(List<List<Usage>> folds) {
		int minFoldSize = Integer.MAX_VALUE;
		List<Usage> smallestFold = Collections.emptyList();

		for (List<Usage> fold : folds) {
			if (fold.size() < minFoldSize) {
				minFoldSize = fold.size();
				smallestFold = fold;
			}
		}

		return smallestFold;
	}

}
