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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Predicate;

import org.apache.commons.math3.random.RandomGenerator;

import com.google.inject.Guice;
import com.google.inject.Injector;

import cc.kave.commons.pointsto.stores.ProjectIdentifier;
import cc.kave.commons.pointsto.stores.ProjectUsageStore;
import cc.recommenders.names.ITypeName;
import cc.recommenders.names.Names;
import cc.recommenders.usages.Usage;

public class ProjectFoldedEvaluation {

	private static final boolean HIDE_UNIQUE_TYPES = true;

	private Path storePath;
	private int numFolds;

	private Predicate<Usage> usageFilter;
	private CrossValidationFoldBuilder foldBuilder;
	private CVEvaluator cvEvaluator;

	private long skippedNumProjects;
	private long skippedUsageFilter;
	private Map<ITypeName, Double> results = new HashMap<>();

	public ProjectFoldedEvaluation(Path storePath, int numFolds, Predicate<Usage> usageFilter,
			CrossValidationFoldBuilder foldBuilder, CVEvaluator cvEvaluator) {
		this.storePath = storePath;
		this.numFolds = numFolds;
		this.usageFilter = usageFilter;
		this.foldBuilder = foldBuilder;
		this.cvEvaluator = cvEvaluator;
	}

	private void reset() {
		skippedNumProjects = 0;
		skippedUsageFilter = 0;
		results.clear();
	}

	public void run() throws IOException {
		reset();

		try (ProjectUsageStore usageStore = new ProjectUsageStore(storePath)) {
			// store types in a list and sort it alphabetically to get a consistent ordering in which types are
			// evaluated
			List<ITypeName> types = new ArrayList<>(usageStore.getAllTypes());
			types.sort(new Comparator<ITypeName>() {

				@Override
				public int compare(ITypeName o1, ITypeName o2) {
					return o1.getIdentifier().compareTo(o2.getIdentifier());
				}
			});

			int numProjects = usageStore.getNumberOfProjects();
			log("Loaded usage store containing %d projects and %d types\n", numProjects, types.size());

			for (ITypeName type : types) {
				evaluateType(usageStore, type);
				usageStore.flush();
			}

			long totalTypesSkipped = skippedNumProjects + skippedUsageFilter;
			log("Skipped %d/%d (%.2f%%) types\n", totalTypesSkipped, types.size(),
					((double) totalTypesSkipped) / types.size() * 100);
			log("\t%d types due to an insufficient number of projects\n", skippedNumProjects);
			log("\t%d types due to an insufficient number of projects after filtering\n", skippedUsageFilter);
		}

	}

	private void evaluateType(ProjectUsageStore usageStore, ITypeName type) throws IOException {
		int numProjects = usageStore.getNumberOfProjects();
		Set<ProjectIdentifier> projects = usageStore.getProjects(type);
		int numProjectsWithType = projects.size();

		if (numProjectsWithType <= 1 && HIDE_UNIQUE_TYPES) {
			++skippedNumProjects;
			return;
		}

		log("%s:\n", Names.vm2srcQualifiedType(type));
		if (numProjectsWithType < numFolds) {
			log("\tSkipping because type is only used in %d projects\n", numProjectsWithType);
			++skippedNumProjects;
			return;
		}
		log("\tType is used in %d/%d (%.2f%%) projects\n", numProjectsWithType, numProjects,
				((double) numProjectsWithType) / numProjects * 100);

		Map<ProjectIdentifier, List<Usage>> projectUsages = usageStore.loadUsagesPerProject(type, usageFilter);
		// re-check whether enough projects with usages are available after filtering
		projectUsages.values().removeIf(usages -> usages.isEmpty());
		numProjectsWithType = projectUsages.size();
		if (numProjectsWithType < numFolds) {
			log("\tSkipping because type is only used in %d projects after filtering\n", numProjectsWithType);
			skippedUsageFilter++;
			return;
		}

		long numUsages = projectUsages.values().stream().mapToLong(usages -> usages.size()).sum();
		log("\t%d usages in total\n", numUsages);

		List<List<Usage>> folds = foldBuilder.createFolds(projectUsages);
		double score = cvEvaluator.evaluate(folds);
		results.put(type, score);
		log("\tF1: %.3f\n", score);
	}

	private static final Injector INJECTOR = Guice.createInjector(new Module());

	public static void main(String[] args) throws IOException {
		Locale.setDefault(Locale.US);
		Path storePath = Paths.get("E:\\Coding\\MT\\Usages\\UnificationAnalysis_FULL");
		int numFolds = 10;
		RandomGenerator rndGenerator = load(RandomGenerator.class);
		CVEvaluator cvEvaluator = load(CVEvaluator.class);
		new ProjectFoldedEvaluation(storePath, numFolds, new PointsToUsageFilter(),
				new StratifiedCVFoldBuilder(numFolds, rndGenerator), cvEvaluator).run();

		load(ExecutorService.class).shutdown();
	}

	private static <T> T load(Class<T> c) {
		return INJECTOR.getInstance(c);
	}

}
