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

import static cc.kave.commons.pointsto.evaluation.Logger.log;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import cc.kave.commons.pointsto.evaluation.annotations.NumberOfCVFolds;
import cc.kave.commons.pointsto.evaluation.cv.CVEvaluator;
import cc.kave.commons.pointsto.evaluation.cv.EmptySetException;
import cc.kave.commons.pointsto.evaluation.cv.GenericFoldBuilder;
import cc.kave.commons.pointsto.evaluation.cv.ProjectTrainValidateSetProvider;
import cc.kave.commons.pointsto.stores.ProjectIdentifier;
import cc.kave.commons.pointsto.stores.ProjectUsageStore;
import cc.kave.commons.pointsto.stores.UsageStore;
import cc.recommenders.names.ITypeName;
import cc.recommenders.names.Names;
import cc.recommenders.usages.Usage;

public class ProjectTrainValidateEvaluation {

	private final int numFolds;
	private final PointsToUsageFilter usageFilter;
	private final CVEvaluator cvEvaluator;

	private final GenericFoldBuilder foldBuilder;

	private long skippedNumProjects;
	private long skippedUsageFilter;

	private Map<ITypeName, List<EvaluationResult>> results = new HashMap<>();

	@Inject
	public ProjectTrainValidateEvaluation(@NumberOfCVFolds int numFolds, PointsToUsageFilter usageFilter,
			CVEvaluator cvEvaluator) {
		this.numFolds = numFolds;
		this.usageFilter = usageFilter;
		this.cvEvaluator = cvEvaluator;

		foldBuilder = new GenericFoldBuilder(numFolds);
	}

	private void reset() {
		skippedNumProjects = 0;
		skippedUsageFilter = 0;
		results.clear();
	}

	public Map<ITypeName, List<EvaluationResult>> getResults() {
		return Collections.unmodifiableMap(results);
	}

	private void printSummary(List<String> names) {
		double[][] grid = new double[names.size()][names.size()];

		for (Map.Entry<ITypeName, List<EvaluationResult>> resultEntry : results.entrySet()) {
			for (EvaluationResult result : resultEntry.getValue()) {
				grid[names.indexOf(result.training)][names.indexOf(result.validation)] += result.score;
			}
		}

		PrintStream printer = System.out;
		double numTypes = results.size();
		for (int i = 0; i < names.size(); ++i) {
			printer.print(names.get(i));

			for (int j = 0; j < names.size(); ++j) {
				printer.print(' ');
				printer.printf(Locale.US, "%.3f", grid[i][j] / numTypes);
			}
			printer.println();
		}
	}

	public void run(Path usageStoresDir) throws IOException {
		reset();

		List<ProjectUsageStore> usageStores = getUsageStores(usageStoresDir);
		try {
			List<ITypeName> types = new ArrayList<>(
					usageStores.stream().flatMap(store -> store.getAllTypes().stream()).collect(Collectors.toSet()));
			types.sort(new TypeNameComparator());

			for (ITypeName type : types) {
				// if (!Names.vm2srcQualifiedType(type).equals("System.Collections.Generic.Stack")) {
				// continue;
				// }

				if (!usageFilter.test(type) || !Names.vm2srcQualifiedType(type).startsWith("System.")) {
					continue;
				}

				evaluateType(type, usageStores);
				for (UsageStore store : usageStores) {
					store.flush();
				}
			}

			printSummary(usageStores.stream().map(store -> store.getName()).collect(Collectors.toList()));
		} finally {
			for (UsageStore store : usageStores) {
				store.close();
			}
		}

		log("Skipped %d types due to insufficient projects\n", skippedNumProjects);
		log("Skipped %d types due to insufficient usages after filtering\n", skippedUsageFilter);
	}

	private List<ProjectUsageStore> getUsageStores(Path dir) throws IOException {
		List<ProjectUsageStore> stores = new ArrayList<>();
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(dir)) {
			for (Path storeDir : dirStream) {
				if (Files.isDirectory(storeDir)) {
					stores.add(new ProjectUsageStore(storeDir));
				}
			}
		}

		return stores;
	}

	private void evaluateType(ITypeName type, List<ProjectUsageStore> usageStores) throws IOException {
		Set<ProjectIdentifier> projects = usageStores.stream().flatMap(store -> store.getProjects(type).stream())
				.collect(Collectors.toSet());

		if (projects.size() < numFolds) {
			++skippedNumProjects;
			return;
		}

		log("%s:\n", Names.vm2srcQualifiedType(type));

		List<List<ProjectIdentifier>> projectFolds = createProjectFolds(projects, type, usageStores);
		List<EvaluationResult> localResults = new ArrayList<>(usageStores.size() * usageStores.size());
		for (ProjectUsageStore trainingStore : usageStores) {
			Map<ProjectIdentifier, List<Usage>> trainingUsages = trainingStore.loadUsagesPerProject(type, usageFilter);

			for (ProjectUsageStore validationStore : usageStores) {
				// avoid unnecessary loading of usages
				Map<ProjectIdentifier, List<Usage>> validationUsages = (trainingStore == validationStore)
						? trainingUsages : validationStore.loadUsagesPerProject(type, usageFilter);
				ProjectTrainValidateSetProvider setProvider = new ProjectTrainValidateSetProvider(projectFolds,
						trainingUsages, validationUsages);
				double score;
				try {
					score = cvEvaluator.evaluate(setProvider);
					localResults.add(new EvaluationResult(trainingStore.getName(), validationStore.getName(), score));
				} catch (RuntimeException e) {
					if (e.getCause() instanceof EmptySetException) {
						++skippedUsageFilter;
						return;
					} else {
						throw e;
					}
				}
				log("\t%s-%s: F1=%.3f, Fold size deviation=%.1f\n", trainingStore.getName(), validationStore.getName(),
						score, setProvider.getAbsoluteFoldSizeDeviation());
			}
		}
		results.put(type, localResults);

	}

	private List<List<ProjectIdentifier>> createProjectFolds(Set<ProjectIdentifier> projects, ITypeName type,
			List<ProjectUsageStore> usageStores) throws IOException {
		Map<ProjectIdentifier, Double> numberOfUsages = new HashMap<>(projects.size());
		for (ProjectUsageStore store : usageStores) {
			Map<ProjectIdentifier, Integer> numberOfStoreUsages = store.getNumberOfUsagesPerProject(type);
			for (Map.Entry<ProjectIdentifier, Integer> entry : numberOfStoreUsages.entrySet()) {
				double currentAverage = numberOfUsages.getOrDefault(entry.getKey(), 0.0);
				numberOfUsages.put(entry.getKey(), currentAverage + (1.0 / usageStores.size()) * entry.getValue());
			}
		}

		List<ProjectIdentifier> sortedProjects = new ArrayList<>(projects);
		sortedProjects.sort(new Comparator<ProjectIdentifier>() {

			@Override
			public int compare(ProjectIdentifier o1, ProjectIdentifier o2) {
				double avg1 = numberOfUsages.get(o1);
				double avg2 = numberOfUsages.get(o2);
				return Double.compare(avg1, avg2);
			}
		});

		return foldBuilder.createFolds(sortedProjects, numberOfUsages);
	}

	private static class EvaluationResult {
		public String training;
		public String validation;
		public double score;

		public EvaluationResult(String training, String validation, double score) {
			this.training = training;
			this.validation = validation;
			this.score = score;
		}

	}

	private static final Injector INJECTOR = Guice.createInjector(new Module());

	public static void main(String[] args) throws IOException {
		Locale.setDefault(Locale.US);

		Path baseDir = Paths.get("E:\\Coding\\MT");
		Path usageStoresDir = baseDir.resolve("Usages");
		Path resultFile = baseDir.resolve("EvaluationResults").resolve("TrainValidate.txt");

		ProjectTrainValidateEvaluation evaluator = INJECTOR.getInstance(ProjectTrainValidateEvaluation.class);
		evaluator.run(usageStoresDir);

		INJECTOR.getInstance(ResultExporter.class).export(resultFile, evaluator.getResults().entrySet().stream()
				.flatMap(e -> e.getValue().stream().map(er -> ImmutablePair.of(e.getKey(), er))).map(p -> {
					return new String[] { Names.vm2srcQualifiedType(p.left), p.right.training, p.right.validation,
							String.format(Locale.US, "%.3f", p.right.score) };
				}));

		INJECTOR.getInstance(ExecutorService.class).shutdown();
	}
}
