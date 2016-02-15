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
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
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

	public ProjectFoldedEvaluation(Path storePath, int numFolds, Predicate<Usage> usageFilter, CrossValidationFoldBuilder foldBuilder,
			CVEvaluator cvEvaluator) {
		this.storePath = storePath;
		this.numFolds = numFolds;
		this.usageFilter = usageFilter;
		this.foldBuilder = foldBuilder;
		this.cvEvaluator = cvEvaluator;
	}

	public void run() throws IOException {

		try (ProjectUsageStore usageStore = new ProjectUsageStore(storePath)) {
			Set<ITypeName> types = usageStore.getAllTypes();
			int numProjects = usageStore.getNumberOfProjects();
			log("Loaded usage store containing %d projects and %d types\n", numProjects, types.size());

			long skippedTypes = 0;
			for (ITypeName type : types) {
				if (!evaluateType(usageStore, type)) {
					++skippedTypes;
				}
				usageStore.flush();
			}

			log("Skipped %d/%d (%.2f%%) types\n", skippedTypes, types.size(),
					((double) skippedTypes) / types.size() * 100);
		}

	}

	private boolean evaluateType(ProjectUsageStore usageStore, ITypeName type) throws IOException {
		int numProjects = usageStore.getNumberOfProjects();
		Map<ProjectIdentifier, Integer> projectUsageCounts = usageStore.getNumberOfUsagesPerProject(type);
		Set<ProjectIdentifier> projects = getProjectsUsingType(projectUsageCounts);
		int numProjectsWithType = projects.size();

		if (numProjectsWithType <= 1 && HIDE_UNIQUE_TYPES) {
			return false;
		}

		log("%s:\n", Names.vm2srcQualifiedType(type));
		if (numProjectsWithType < numFolds) {
			log("\tSkipping because type is only used in %d projects\n", numProjectsWithType);
			return false;
		}
		log("\tType is used in %d/%d (%.2f%%) projects\n", numProjectsWithType, numProjects,
				((double) numProjectsWithType) / numProjects * 100);

		Map<ProjectIdentifier, List<Usage>> projectUsages = usageStore.loadUsagesPerProject(type, usageFilter);
		// re-check whether enough projects with usages are available after filtering
		projectUsages.values().removeIf(usages -> usages.isEmpty());
		numProjectsWithType = projectUsages.size();
		if (numProjectsWithType < numFolds) {
			log("\tSkipping because type is only used in %d projects after filtering\n", numProjectsWithType);
			return false;
		}
		
		List<List<Usage>> folds = foldBuilder.createFolds(projectUsages);
		double score = cvEvaluator.evaluate(folds);
		log("\tF1: %.3f\n", score);

		return true;
	}

	private Set<ProjectIdentifier> getProjectsUsingType(Map<ProjectIdentifier, Integer> projectUsageCounts) {
		Set<ProjectIdentifier> projects = new HashSet<>();
		for (Map.Entry<ProjectIdentifier, Integer> entry : projectUsageCounts.entrySet()) {
			if (entry.getValue() > 0) {
				projects.add(entry.getKey());
			}
		}

		return projects;
	}

	private static final Injector INJECTOR = Guice.createInjector(new Module());

	public static void main(String[] args) throws IOException {
		Locale.setDefault(Locale.US);
		Path storePath = Paths.get("E:\\Coding\\MT\\Usages\\TypeBasedAnalysis");//("E:\\Coding\\MT\\Usages\\UnificationAnalysis_FULL");
		int numFolds = 10;
		RandomGenerator rndGenerator = load(RandomGenerator.class);
		CVEvaluator cvEvaluator = load(CVEvaluator.class);
		new ProjectFoldedEvaluation(storePath, numFolds, new PointsToUsageFilter(), new StratifiedCVFoldBuilder(numFolds, rndGenerator),
				cvEvaluator).run();
	}

	private static <T> T load(Class<T> c) {
		return INJECTOR.getInstance(c);
	}

}
