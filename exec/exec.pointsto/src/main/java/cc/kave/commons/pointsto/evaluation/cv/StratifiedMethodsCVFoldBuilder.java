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
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.util.Pair;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;

import cc.kave.commons.pointsto.evaluation.annotations.NumberOfCVFolds;
import cc.kave.commons.pointsto.stores.ProjectIdentifier;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.Usage;

public class StratifiedMethodsCVFoldBuilder extends AbstractCVFoldBuilder {

	@Inject
	public StratifiedMethodsCVFoldBuilder(@NumberOfCVFolds int numFolds, RandomGenerator rndGenerator) {
		super(numFolds, rndGenerator);
	}

	@Override
	public List<List<Usage>> createFolds(Map<ProjectIdentifier, List<Usage>> projectUsages) {
		shuffleUsages(projectUsages.values());
		Map<ICoReMethodName, Integer> methods = countMethods(Iterables.concat(projectUsages.values()));
		EnumeratedDistribution<ICoReMethodName> distribution = buildDistribution(methods);
		MethodRegistry registry = new MethodRegistry(Iterables.concat(projectUsages.values()), methods.size());

		List<List<Usage>> folds = new ArrayList<>(numFolds);
		int avgFoldSize = calcAvgFoldSize(projectUsages.values());
		for (int f = 0; f < numFolds - 1; ++f) {
			List<Usage> fold = new ArrayList<>(avgFoldSize);
			folds.add(fold);
			for (int i = 0; i < avgFoldSize; ++i) {
				ICoReMethodName method;
				Usage usage;

				do {
					method = distribution.sample();
					usage = registry.next(method);
				} while (usage == null);

				fold.add(usage);
			}
		}
		folds.add(registry.remaining());

		return folds;
	}

	private Map<ICoReMethodName, Integer> countMethods(Iterable<Usage> usages) {
		Map<ICoReMethodName, Integer> methods = new HashMap<>();

		for (Usage usage : usages) {
			for (CallSite callsite : usage.getReceiverCallsites()) {
				ICoReMethodName method = callsite.getMethod();
				int count = methods.getOrDefault(method, 0) + 1;
				methods.put(method, count);
			}
		}

		return methods;
	}

	private EnumeratedDistribution<ICoReMethodName> buildDistribution(Map<ICoReMethodName, Integer> methods) {
		List<Pair<ICoReMethodName, Double>> methodProbabilities = new ArrayList<>(methods.size());

		for (Map.Entry<ICoReMethodName, Integer> methodEntry : methods.entrySet()) {
			methodProbabilities.add(Pair.create(methodEntry.getKey(), (double) methodEntry.getValue()));
		}

		// probabilities are normalized by the constructor
		return new EnumeratedDistribution<>(rndGenerator, methodProbabilities);
	}

	private static class MethodRegistry {

		private Map<ICoReMethodName, List<Usage>> methodUsages; // usages that use a specific method
		private Map<Usage, Set<ICoReMethodName>> usageMethods; // methods of a specific usage

		public MethodRegistry(Iterable<Usage> usages, int numMethods) {
			methodUsages = new HashMap<>(numMethods);
			usageMethods = new IdentityHashMap<>();

			for (Usage usage : usages) {
				Set<ICoReMethodName> methods = usageMethods.get(usage);
				if (methods == null) {
					methods = new HashSet<>();
					usageMethods.put(usage, methods);
				}

				for (CallSite callsite : usage.getReceiverCallsites()) {
					ICoReMethodName method = callsite.getMethod();
					List<Usage> mu = methodUsages.get(method);
					if (mu == null) {
						mu = new ArrayList<>();
						methodUsages.put(method, mu);
					}
					mu.add(usage);
					methods.add(method);
				}
			}
		}

		public Usage next(ICoReMethodName method) {
			List<Usage> mu = methodUsages.get(method);

			if (mu == null || mu.isEmpty()) {
				return null;
			}

			int lastIndex = mu.size() - 1;
			Usage usage = mu.remove(lastIndex);
			Set<ICoReMethodName> methods = usageMethods.get(usage);
			if (methods == null) {
				// usage has been used for another method and is no longer available
				return null;
			}

			usageMethods.remove(usage);
			return usage;
		}

		public List<Usage> remaining() {
			List<Usage> usages = new ArrayList<>(usageMethods.keySet());
			methodUsages.clear();
			usageMethods.clear();
			return usages;
		}
	}

}
