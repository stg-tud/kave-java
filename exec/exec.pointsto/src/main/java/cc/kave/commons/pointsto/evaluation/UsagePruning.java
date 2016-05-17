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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;

import com.google.inject.Inject;

import cc.kave.commons.pointsto.stores.ProjectIdentifier;
import cc.recommenders.usages.Usage;

public class UsagePruning {

	private final RandomGenerator rndGenerator;

	@Inject
	public UsagePruning(RandomGenerator rndGenerator) {
		this.rndGenerator = rndGenerator;
	}

	/**
	 * @return The number of pruned usages
	 */
	public int prune(final int maxUsages, Map<ProjectIdentifier, List<Usage>> usages) {
		final int initialNumUsages = usages.values().stream().mapToInt(Collection::size).sum();
		int numUsages = initialNumUsages;

		if (numUsages > maxUsages) {
			Random rnd = new Random(rndGenerator.nextLong());
			List<Pair<ProjectIdentifier, Double>> projectUsageCounts = new ArrayList<>(usages.size());
			for (Map.Entry<ProjectIdentifier, List<Usage>> entry : usages.entrySet()) {
				projectUsageCounts.add(Pair.create(entry.getKey(), (double) entry.getValue().size()));
				Collections.shuffle(entry.getValue(), rnd);
			}
			EnumeratedDistribution<ProjectIdentifier> distribution = new EnumeratedDistribution<>(rndGenerator,
					projectUsageCounts);

			while (numUsages > maxUsages) {
				ProjectIdentifier project = distribution.sample();
				List<Usage> projectUsages = usages.get(project);
				if (!projectUsages.isEmpty()) {
					projectUsages.remove(projectUsages.size() - 1);
					--numUsages;
				}
			}
		}

		return initialNumUsages - numUsages;
	}
}
