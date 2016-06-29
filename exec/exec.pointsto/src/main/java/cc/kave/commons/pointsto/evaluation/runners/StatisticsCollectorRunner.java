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

import static cc.kave.commons.pointsto.evaluation.Logger.log;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import cc.kave.commons.pointsto.evaluation.PointsToUsageFilter;
import cc.kave.commons.pointsto.statistics.TypeStatisticsCollector;
import cc.kave.commons.pointsto.statistics.UsageStatisticsCollector;
import cc.kave.commons.pointsto.stores.ProjectUsageStore;
import cc.kave.commons.pointsto.stores.UsageStore;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.Usage;

public class StatisticsCollectorRunner {

	public static void main(String[] args) throws IOException {
		final Path baseDir = (args.length == 1) ? Paths.get(args[0]) : Paths.get(".");
		final Path usageStoresDir = baseDir.resolve("Usages");
		final Path statisticsDir = baseDir.resolve("Statistics");

		UsageStatisticsCollector statisticsCollector = new TypeStatisticsCollector(new PointsToUsageFilter());

		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(usageStoresDir)) {
			for (Path dir : dirStream) {
				if (!Files.isDirectory(dir)) {
					continue;
				}

				statisticsCollector = statisticsCollector.create();
				log("Collecting %s...\n", dir.toString());

				try (UsageStore usageStore = new ProjectUsageStore(dir)) {
					int numTypes = usageStore.getAllTypes().size();
					log("\tStore contains %d types\n", numTypes);

					for (ICoReTypeName type : usageStore.getAllTypes()) {
						List<Usage> usages = usageStore.load(type);
						statisticsCollector.process(usages);
						usageStore.flush();
					}
				}

				String fileName = statisticsCollector.getClass().getSimpleName() + ".txt";
				statisticsCollector.output(statisticsDir.resolve(dir.getFileName()).resolve(fileName));
			}
		}
	}

}
