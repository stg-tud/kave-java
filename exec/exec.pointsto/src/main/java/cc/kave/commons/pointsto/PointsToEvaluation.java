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
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Stopwatch;
import com.google.common.io.Files;

import cc.kave.commons.pointsto.analysis.AdvancedPointerAnalysisFactory;
import cc.kave.commons.pointsto.analysis.FieldSensitivity;
import cc.kave.commons.pointsto.analysis.ReferenceBasedAnalysis;
import cc.kave.commons.pointsto.analysis.SimplePointerAnalysisFactory;
import cc.kave.commons.pointsto.analysis.TypeBasedAnalysis;
import cc.kave.commons.pointsto.analysis.unification.UnificationAnalysis;
import cc.kave.commons.pointsto.extraction.TypeHistogramUsageStatisticsCollector;
import cc.kave.commons.pointsto.extraction.UsageStatisticsCollector;
import cc.recommenders.usages.Usage;

public class PointsToEvaluation {

	private static final Logger LOGGER = LoggerFactory.getLogger(PointsToEvaluation.class);

	private static final Path BASE_PATH = Paths.get("E:\\Coding\\MT");

	private static final Path SRC_PATH = BASE_PATH.resolve("Contexts");
	private static final Path CONTEXT_DEST = BASE_PATH.resolve("annotatedContexts");
	private static final Path USAGE_DEST = BASE_PATH.resolve("Usages");
	private static final Path STATISTICS_DEST = BASE_PATH.resolve("Statistics");

	public static void main(String[] args) {
		List<PointerAnalysisFactory> factories = Arrays.asList(
				// new SimplePointerAnalysisFactory<>(TypeBasedAnalysis.class),
				// new SimplePointerAnalysisFactory<>(ReferenceBasedAnalysis.class)
				new AdvancedPointerAnalysisFactory<>(UnificationAnalysis.class, FieldSensitivity.FULL)
				);
		Stopwatch stopwatch = Stopwatch.createStarted();
		new PointsToEvaluation().generateUsages(factories);
		stopwatch.stop();
		
		LOGGER.info("Usage generation took {}", stopwatch.toString());

	}

	private Map<PointerAnalysisFactory, List<Usage>> generateUsages(List<PointerAnalysisFactory> factories) {
		try {
			PointsToUsageGenerator generator = new PointsToUsageGenerator(factories, SRC_PATH, CONTEXT_DEST, USAGE_DEST,
					new TypeHistogramUsageStatisticsCollector());

			Map<PointerAnalysisFactory, List<Usage>> usages = generator.getUsages();
			outputStatisticsCollectors(generator.getStatisticsCollectors());

			return usages;
		} catch (IOException e) {
			LOGGER.error("Error during usage generation", e);
		}

		return Collections.emptyMap();
	}

	private void outputStatisticsCollectors(Map<PointerAnalysisFactory, UsageStatisticsCollector> collectors) {
		try {
			for (Map.Entry<PointerAnalysisFactory, UsageStatisticsCollector> entry : collectors.entrySet()) {
				Path statFile = STATISTICS_DEST.resolve(entry.getKey().getName() + ".txt");
				Files.createParentDirs(statFile.toFile());

				entry.getValue().output(statFile);
			}
		} catch (IOException e) {
			LOGGER.error("Failed to write the results of the statistics collectors to disk", e);
		}
	}

}
