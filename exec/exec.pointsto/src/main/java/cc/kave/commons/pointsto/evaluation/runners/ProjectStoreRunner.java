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
package cc.kave.commons.pointsto.evaluation.runners;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.collect.Iterables;

import cc.kave.commons.pointsto.evaluation.PointsToUsageFilter;
import cc.kave.commons.pointsto.evaluation.TypeNameComparator;
import cc.kave.commons.pointsto.stores.ProjectIdentifier;
import cc.kave.commons.pointsto.stores.ProjectUsageStore;
import cc.recommenders.names.CoReNames;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.Usage;

public class ProjectStoreRunner {

	public static void main(String[] args) throws IOException {
		Path storeDir = Paths.get(args[0]);
		try (ProjectUsageStore store = new ProjectUsageStore(storeDir)) {
			List<ICoReTypeName> types = new ArrayList<>(store.getAllTypes());
			types.sort(new TypeNameComparator());

			System.out.println(types.stream().filter(t -> t.getIdentifier().contains("KaVE.")).collect(Collectors.toList()));
//			if (args.length == 1 || args[1].equals("countFilteredUsages")) {
//				countFilteredUsages(types, store);
//			} else if (args[1].equals("countRecvCallSites")) {
//				countRecvCallSites(types, store);
//			}
			// methodPropabilities(types, store);
		}

	}

	private static void countFilteredUsages(Collection<ICoReTypeName> types, ProjectUsageStore store)
			throws IOException {
		PointsToUsageFilter filter = new PointsToUsageFilter();
		int numTypes = 0;
		long numUsages = 0;

		for (ICoReTypeName type : types) {
			if (filter.test(type)) {
				++numTypes;

				numUsages += store.getNumberOfUsagesPerProject(type).values().stream().mapToLong(i -> i.longValue())
						.sum();
			}
			store.flush();
		}

		System.out.printf(Locale.US, "%s: %d types, %d usages\n", store.getName(), numTypes, numUsages);
	}

	private static void countUsages(Collection<ICoReTypeName> types, ProjectUsageStore store) throws IOException {
		int totalNumberOfProjects = store.getNumberOfProjects();

		for (ICoReTypeName type : types) {
			int numProjects = store.getProjects(type).size();
			if (numProjects < 10) {
				continue;
			}

			Map<ProjectIdentifier, Integer> usageCounts = store.getNumberOfUsagesPerProject(type);
			DescriptiveStatistics statistics = new DescriptiveStatistics();
			usageCounts.values().forEach(count -> statistics.addValue(count));
			for (int i = 0; i < totalNumberOfProjects - numProjects; ++i) {
				statistics.addValue(0);
			}

			System.out.printf(Locale.US, "%s: %.1f\n", CoReNames.vm2srcQualifiedType(type),
					statistics.getStandardDeviation());
		}
	}

	private static void countRecvCallSites(Collection<ICoReTypeName> types, ProjectUsageStore store)
			throws IOException {
		DescriptiveStatistics statistics = new DescriptiveStatistics();
		for (ICoReTypeName type : types) {
			if (store.getProjects(type).size() < 10) {
				continue;
			}

			int numDistinctRecvCallsite = store.load(type, new PointsToUsageFilter()).stream()
					.flatMap(usage -> usage.getReceiverCallsites().stream()).map(CallSite::getMethod)
					.collect(Collectors.toSet()).size();
			if (numDistinctRecvCallsite > 0) {
				statistics.addValue(numDistinctRecvCallsite);
				System.out.printf(Locale.US, "%s: %d\n", CoReNames.vm2srcQualifiedType(type), numDistinctRecvCallsite);
			}
		}
		System.out.println();
		System.out.printf(Locale.US, "mean: %.3f, stddev: %.3f, median: %.1f\n", statistics.getMean(),
				statistics.getStandardDeviation(), statistics.getPercentile(50));
	}

	private static void methodPropabilities(Collection<ICoReTypeName> types, ProjectUsageStore store)
			throws IOException {
		ICoReTypeName stringType = Iterables.find(types, t -> t.getClassName().equals("String"));

		Map<ICoReMethodName, Integer> methods = new HashMap<>();
		for (Usage usage : store.load(stringType)) {
			for (CallSite cs : usage.getReceiverCallsites()) {
				Integer currentCount = methods.getOrDefault(cs.getMethod(), 0);
				methods.put(cs.getMethod(), currentCount + 1);
			}
		}

		double total = methods.values().stream().mapToInt(i -> i).sum();
		List<Map.Entry<ICoReMethodName, Integer>> methodEntries = new ArrayList<>(methods.entrySet());
		methodEntries.sort(Comparator.<Map.Entry<ICoReMethodName, Integer>> comparingInt(e -> e.getValue()).reversed());
		double mrr = 0;
		double rank = 1.0;
		for (Map.Entry<ICoReMethodName, Integer> method : methodEntries) {
			double probability = method.getValue() / total;
			mrr += probability * (1.0 / rank);
			++rank;
			System.out.printf(Locale.US, "%s\t%.3f\n", method.getKey().getName(), probability);
		}
		System.out.println(mrr);
	}

}
