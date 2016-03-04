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
import java.util.Map;

import com.google.inject.Inject;

import cc.kave.commons.pointsto.evaluation.annotations.NumberOfCVFolds;

public class GenericFoldBuilder {

	private final int numFolds;

	@Inject
	public GenericFoldBuilder(@NumberOfCVFolds int numFolds) {
		this.numFolds = numFolds;
	}

	public <T> List<List<T>> createFolds(List<T> sortedItems, Map<T, Double> numberOfItems) {
		List<List<T>> folds = new ArrayList<>(numFolds);
		List<Double> foldSizes = new ArrayList<>(numFolds);
		for (int i = 0; i < numFolds; ++i) {
			folds.add(new ArrayList<>());
			foldSizes.add(0.0);
		}

		while (!sortedItems.isEmpty()) {
			T item = sortedItems.remove(sortedItems.size() - 1);
			int foldIndex = getSmallestFold(folds, foldSizes);
			foldSizes.set(foldIndex, foldSizes.get(foldIndex) + numberOfItems.get(item));
			folds.get(foldIndex).add(item);
		}

		return folds;
	}

	private <T> int getSmallestFold(List<List<T>> folds, List<Double> foldSizes) {
		double minFoldSize = foldSizes.get(0);
		int smallestFoldIndex = 0;

		for (int i = 1; i < folds.size(); ++i) {
			double foldSize = foldSizes.get(i);
			if (foldSize < minFoldSize) {
				minFoldSize = foldSize;
				smallestFoldIndex = i;
			}
		}

		return smallestFoldIndex;
	}
}
