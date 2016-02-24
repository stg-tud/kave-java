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
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.exceptions.UnexpectedSSTNodeException;
import cc.kave.commons.pointsto.extraction.PointsToUsageExtractor;
import cc.kave.commons.pointsto.io.IOHelper;
import cc.kave.commons.pointsto.io.StreamingZipReader;
import cc.kave.commons.pointsto.io.ZipArchive;
import cc.kave.commons.pointsto.statistics.NopUsageStatisticsCollector;
import cc.kave.commons.pointsto.statistics.UsageStatisticsCollector;
import cc.kave.commons.pointsto.stores.UsageStore;
import cc.kave.commons.utils.json.JsonUtils;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.usages.Usage;

public class PointsToUsageGenerator {

	private static final Logger LOGGER = LoggerFactory.getLogger(PointsToUsageGenerator.class);

	private List<PointsToAnalysisFactory> factories;

	private Path srcDir;
	private List<Path> sources;
	private Path pointstoDestDir;
	private Map<PointsToAnalysisFactory, UsageStore> usageStores = new HashMap<>();

	private Map<PointsToAnalysisFactory, UsageStatisticsCollector> statisticsCollectors = new HashMap<>();

	public PointsToUsageGenerator(List<PointsToAnalysisFactory> factories, Path srcDirectory, Path pointstoDestDir,
			Function<PointsToAnalysisFactory, UsageStore> usageStoreFactory) throws IOException {
		this(factories, srcDirectory, pointstoDestDir, usageStoreFactory, new NopUsageStatisticsCollector());
	}

	public PointsToUsageGenerator(List<PointsToAnalysisFactory> factories, Path srcDirectory, Path pointstoDestDir,
			Function<PointsToAnalysisFactory, UsageStore> usageStoreFactory,
			UsageStatisticsCollector statisticsCollector) throws IOException {
		this.factories = factories;
		this.srcDir = srcDirectory;
		this.sources = IOHelper.getZipFiles(srcDirectory);
		this.pointstoDestDir = pointstoDestDir;

		for (PointsToAnalysisFactory factory : factories) {
			usageStores.put(factory, usageStoreFactory.apply(factory));
			statisticsCollectors.put(factory, statisticsCollector.create());
		}
	}

	public Map<PointsToAnalysisFactory, UsageStatisticsCollector> getStatisticsCollectors() {
		return Collections.unmodifiableMap(statisticsCollectors);
	}

	public void generateUsages() {
		for (Path zipFile : sources) {
			try {
				processZipFile(zipFile);
			} catch (IOException e) {
				LOGGER.error("Failed to process zip " + zipFile.toString(), e);
			}
		}

		// close stores
		for (UsageStore store : usageStores.values()) {
			try {
				store.close();
			} catch (IOException e) {
				LOGGER.error("Failed to close a UsageStore", e);
			}
		}
	}

	private void processZipFile(Path inputZipFile) throws IOException {
		final PointsToUsageExtractor extractor = new PointsToUsageExtractor();

		final Map<PointsToAnalysisFactory, ZipArchive> annotatedContextWriters = new HashMap<>(factories.size());

		// initialize writers for the annotated contexts to TARGET/FACTORY_NAME/RELATIVE_INPUT
		final Path relativeInput = srcDir.relativize(inputZipFile);
		initializeWriters(relativeInput, annotatedContextWriters);

		StreamingZipReader reader = new StreamingZipReader(inputZipFile.toFile());
		reader.stream(Context.class).forEach((Context context) -> {
			if (context == null) {
				LOGGER.debug("Found a 'null' context");
				return;
			}

			for (PointsToAnalysisFactory factory : this.factories) {
				PointsToAnalysis pa = factory.create();
				PointsToContext ptContext = null;

				// guard against exception in MethodName:getSignature()
				try {
					ptContext = pa.compute(context);
				} catch (UnexpectedSSTNodeException | AssertionException | ClassCastException | NullPointerException
						| ConcurrentModificationException | StackOverflowError ex) {
					throw ex;
				} catch (RuntimeException ex) {
					LOGGER.error("Failed to compute pointer analysis " + factory.getName(), ex);
					continue;
				}

				try {
					writePointsToContext(ptContext, factory, annotatedContextWriters);
				} catch (Exception e) {
					LOGGER.error("Failed to serialize an annotated context from " + relativeInput.toString(), e);
				}

				extractor.setStatisticsCollector(statisticsCollectors.get(factory));
				List<Usage> extractedUsages = extractor.extract(ptContext);
				UsageStore usageStore = usageStores.get(factory);
				try {
					usageStore.store(extractedUsages, relativeInput);
				} catch (Exception e) {
					LOGGER.error("Failed to serialize an extracted usage from " + relativeInput.toString(), e);
				}
			}
		});

		// close writers
		for (ZipArchive archive : annotatedContextWriters.values()) {
			archive.close();
		}

		for (UsageStore store : usageStores.values()) {
			store.flush();
		}
	}

	private void initializeWriters(final Path relativeInput,
			final Map<PointsToAnalysisFactory, ZipArchive> annotatedContextWriters) throws IOException {

		for (PointsToAnalysisFactory factory : factories) {
			if (pointstoDestDir != null) {
				Path contextsFile = pointstoDestDir.resolve(factory.getName()).resolve(relativeInput);
				IOHelper.createParentDirs(contextsFile);
				annotatedContextWriters.put(factory, new ZipArchive(contextsFile));
			}
		}
	}

	private void writePointsToContext(PointsToContext ptCtxt, PointsToAnalysisFactory factory,
			Map<PointsToAnalysisFactory, ZipArchive> writers) throws IOException {

		ZipArchive archive = writers.get(factory);
		if (archive != null) {
			archive.store(ptCtxt, PointsToContext.class, JsonUtils::toJson);
		}
	}

}
