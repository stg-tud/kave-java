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
package cc.kave.commons.pointsto.evaluation.cv;

import java.util.ArrayList;
import java.util.List;

import cc.recommenders.usages.Usage;

public class UsageSetProvider extends AbstractSetProvider {

	private final List<List<Usage>> folds;

	public UsageSetProvider(List<List<Usage>> folds) {
		super(folds.size());
		this.folds = folds;
	}

	@Override
	public List<Usage> getTrainingSet(int validationFoldIndex) {
		int numFolds = folds.size();
		List<Usage> training = new ArrayList<>((numFolds - 1) * folds.get(0).size());

		for (int i = 0; i < numFolds; ++i) {
			if (i != validationFoldIndex) {
				training.addAll(folds.get(i));
			}
		}

		updateAvgTrainingFoldSize(training.size());
		return training;
	}

	@Override
	public List<Usage> getValidationSet(int validationFoldIndex) {
		List<Usage> validation = folds.get(validationFoldIndex);
		updateAvgValidationFoldSize(validation.size());
		return validation;
	}

}
