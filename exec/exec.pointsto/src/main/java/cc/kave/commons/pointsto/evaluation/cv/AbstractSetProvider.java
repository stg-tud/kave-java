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

import java.util.concurrent.atomic.DoubleAdder;

public abstract class AbstractSetProvider implements SetProvider {

	protected final int numFolds;
	private final DoubleAdder sumAvgTrainingFoldSize = new DoubleAdder();
	private final DoubleAdder sumValidationFoldSize = new DoubleAdder();

	public AbstractSetProvider(int numFolds) {
		this.numFolds = numFolds;
	}

	protected final void updateAvgTrainingFoldSize(int trainingSetSize) {
		sumAvgTrainingFoldSize.add(trainingSetSize / (numFolds - 1.0));
	}

	protected final void updateAvgValidationFoldSize(int validationFoldSize) {
		sumValidationFoldSize.add(validationFoldSize);
	}

	@Override
	public double getAbsoluteFoldSizeDeviation() {
		double avgTrainingFoldSize = sumAvgTrainingFoldSize.sum() / numFolds;
		double avgValidationFoldSize = sumValidationFoldSize.sum() / numFolds;

		return Math.abs(avgTrainingFoldSize - avgValidationFoldSize);
	}

}
