/**
 * Copyright 2015 Simon Reuß
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
package cc.kave.commons.pointsto;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.google.common.io.Files;

import cc.kave.commons.pointsto.analysis.FieldSensitivity;
import cc.kave.commons.pointsto.analysis.ReferenceBasedAnalysis;
import cc.kave.commons.pointsto.analysis.TypeBasedAnalysis;
import cc.kave.commons.pointsto.analysis.unification.UnificationAnalysis;
import cc.kave.commons.pointsto.evaluation.UsageEvaluation;
import cc.kave.commons.pointsto.statistics.TypeHistogramUsageStatisticsCollector;
import cc.kave.commons.pointsto.statistics.UsageStatisticsCollector;
import cc.kave.commons.pointsto.stores.ProjectUsageStore;
import cc.kave.commons.pointsto.stores.UsageStore;
import cc.recommenders.usages.Usage;

public class PointsToEvaluation {

	private static final Logger LOGGER = LoggerFactory.getLogger(PointsToEvaluation.class);

	private static final Path BASE_PATH = Paths.get("E:\\Coding\\MT");

	private static final Path SRC_PATH = BASE_PATH.resolve("Contexts");
	private static final Path CONTEXT_DEST = BASE_PATH.resolve("annotatedContexts");
	private static final Path USAGE_DEST = BASE_PATH.resolve("Usages");
	private static final Path STATISTICS_DEST = BASE_PATH.resolve("Statistics");
	private static final Path EVALUATION_RESULTS_DEST = BASE_PATH.resolve("EvaluationResults");

	public static void main(String[] args) {

		List<PointsToAnalysisFactory> factories = Arrays.asList(
				// new SimplePointsToAnalysisFactory<>(TypeBasedAnalysis.class),
				// new SimplePointsToAnalysisFactory<>(ReferenceBasedAnalysis.class),
				new AdvancedPointsToAnalysisFactory<>(UnificationAnalysis.class, FieldSensitivity.FULL));

		generateUsages(factories);
		// evaluateUsages(factories);

	}

	private static Map<PointsToAnalysisFactory, List<Usage>> generateUsages(List<PointsToAnalysisFactory> factories) {
		try {
			Function<PointsToAnalysisFactory, UsageStore> usageStoreFactory = (PointsToAnalysisFactory factory) -> {
				try {
					return new ProjectUsageStore(USAGE_DEST.resolve(factory.getName()));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			};

			PointsToUsageGenerator generator = new PointsToUsageGenerator(factories, SRC_PATH, null, usageStoreFactory,
					new TypeHistogramUsageStatisticsCollector());

			Stopwatch stopwatch = Stopwatch.createStarted();
			Map<PointsToAnalysisFactory, List<Usage>> usages = generator.getUsages();
			stopwatch.stop();
			LOGGER.info("Usage generation took {}", stopwatch.toString());

			outputStatisticsCollectors(generator.getStatisticsCollectors());

			for (Map.Entry<PointsToAnalysisFactory, List<Usage>> entry : usages.entrySet()) {
				LOGGER.info("{}: {} usages", entry.getKey().getName(), entry.getValue().size());
			}

			return usages;
		} catch (IOException e) {
			LOGGER.error("Error during usage generation", e);
		}

		return Collections.emptyMap();
	}

	private static void outputStatisticsCollectors(Map<PointsToAnalysisFactory, UsageStatisticsCollector> collectors) {
		try {
			for (Map.Entry<PointsToAnalysisFactory, UsageStatisticsCollector> entry : collectors.entrySet()) {
				Path statFile = STATISTICS_DEST.resolve(entry.getKey().getName() + ".txt");
				Files.createParentDirs(statFile.toFile());

				entry.getValue().output(statFile);
			}
		} catch (IOException e) {
			LOGGER.error("Failed to write the results of the statistics collectors to disk", e);
		}
	}

	private static void evaluateUsages(List<PointsToAnalysisFactory> factories) {
		Locale.setDefault(Locale.US);

		for (PointsToAnalysisFactory factory : factories) {
			String factoryName = factory.getName();
			Path store = USAGE_DEST.resolve(factoryName);
			Path evaluationExport = EVALUATION_RESULTS_DEST.resolve(factoryName + ".txt");
			try {
				UsageEvaluation.run(store, evaluationExport);
			} catch (IOException e) {
				LOGGER.error("Failed to evaluate usages of " + factoryName, e);
			}
		}

		UsageEvaluation.shutdown();
	}

}
