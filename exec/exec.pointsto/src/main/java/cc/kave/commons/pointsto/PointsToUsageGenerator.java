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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Iterables;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.pointsto.analysis.PointerAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.exceptions.UnexpectedSSTNodeException;
import cc.kave.commons.pointsto.extraction.NopUsageStatisticsCollector;
import cc.kave.commons.pointsto.extraction.PointsToUsageExtractor;
import cc.kave.commons.pointsto.extraction.UsageStatisticsCollector;
import cc.kave.commons.pointsto.io.StreamingZipReader;
import cc.kave.commons.pointsto.io.ZipWriter;
import cc.kave.commons.utils.json.JsonUtils;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.usages.Usage;
import cc.recommenders.utils.gson.GsonUtil;

public class PointsToUsageGenerator {

	private static final String ZIP_FILE_ENDING = ".zip";
	private static final Logger LOGGER = LoggerFactory.getLogger(PointsToUsageGenerator.class);

	private List<PointerAnalysisFactory> factories;

	private Path srcDir;
	private List<Path> sources;
	private Path pointstoDestDir;
	private Path usagesDestDir;

	private Map<PointerAnalysisFactory, UsageStatisticsCollector> statisticsCollectors = new HashMap<>();

	public PointsToUsageGenerator(List<PointerAnalysisFactory> factories, Path srcDirectory, Path pointstoDestDir,
			Path usagesDestDir) throws IOException {
		this(factories, srcDirectory, pointstoDestDir, usagesDestDir, new NopUsageStatisticsCollector());
	}

	public PointsToUsageGenerator(List<PointerAnalysisFactory> factories, Path srcDirectory, Path pointstoDestDir,
			Path usagesDestDir, UsageStatisticsCollector statisticsCollector) throws IOException {
		this.factories = factories;
		this.srcDir = srcDirectory;
		this.sources = getZipFiles(srcDirectory);
		this.pointstoDestDir = pointstoDestDir;
		this.usagesDestDir = usagesDestDir;

		for (PointerAnalysisFactory factory : factories) {
			statisticsCollectors.put(factory, statisticsCollector.create());
		}
	}

	public Map<PointerAnalysisFactory, UsageStatisticsCollector> getStatisticsCollectors() {
		return Collections.unmodifiableMap(statisticsCollectors);
	}

	public Map<PointerAnalysisFactory, List<Usage>> getUsages() {
		Map<PointerAnalysisFactory, List<Usage>> usages = new HashMap<>();

		for (Path zipFile : sources) {
			try {
				usages.putAll(processZipFile(zipFile));
			} catch (IOException e) {
				LOGGER.error("Failed to process zip " + zipFile.toString(), e);
			}
		}

		return usages;
	}

	private Map<PointerAnalysisFactory, List<Usage>> processZipFile(Path path) throws IOException {
		final Map<PointerAnalysisFactory, List<Usage>> usages = new HashMap<>();
		final PointsToUsageExtractor extractor = new PointsToUsageExtractor();

		final Map<PointerAnalysisFactory, ZipWriter<PointsToContext>> annotatedContextWriters = new HashMap<>(
				factories.size());
		final Map<PointerAnalysisFactory, ZipWriter<Usage>> usageWriters = new HashMap<>(factories.size());

		// initialize writers for the annotated contexts and usages to TARGET/FACTORY_NAME/RELATIVE_INPUT
		final Path relativeInput = srcDir.relativize(path);
		initializeWriters(relativeInput, annotatedContextWriters, usageWriters);

		StreamingZipReader reader = new StreamingZipReader(path.toFile());
		reader.stream(Context.class).forEach((Context context) -> {

			for (PointerAnalysisFactory factory : this.factories) {
				PointerAnalysis pa = factory.create();
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
					writeEntry(ptContext, factory, annotatedContextWriters);
				} catch (Exception e) {
					LOGGER.error("Failed to serialize an annotated context from " + relativeInput.toString(), e);
				}

				extractor.setStatisticsCollector(statisticsCollectors.get(factory));
				List<Usage> extractedUsages = extractor.extract(ptContext);
				for (Usage usage : extractedUsages) {
					try {
						writeEntry(usage, factory, usageWriters);
					} catch (Exception e) {
						LOGGER.error("Failed to serialize an extracted usage from " + relativeInput.toString(), e);
					}
				}

				usages.getOrDefault(factory, new ArrayList<>()).addAll(extractedUsages);
			}
		});

		// close writers
		for (ZipWriter<?> writer : Iterables.concat(annotatedContextWriters.values(), usageWriters.values())) {
			writer.close();
		}

		return usages;
	}

	private void initializeWriters(final Path relativeInput,
			final Map<PointerAnalysisFactory, ZipWriter<PointsToContext>> annotatedContextWriters,
			final Map<PointerAnalysisFactory, ZipWriter<Usage>> usageWriters) throws IOException {

		for (PointerAnalysisFactory factory : factories) {
			if (pointstoDestDir != null) {
				File contextsFile = pointstoDestDir.resolve(factory.getName()).resolve(relativeInput).toFile();
				com.google.common.io.Files.createParentDirs(contextsFile);
				annotatedContextWriters.put(factory, new ZipWriter<>(contextsFile, ptCtxt -> JsonUtils.toJson(ptCtxt)));
			}

			if (usagesDestDir != null) {
				File usagesFile = usagesDestDir.resolve(factory.getName()).resolve(relativeInput).toFile();
				com.google.common.io.Files.createParentDirs(usagesFile);
				usageWriters.put(factory, new ZipWriter<>(usagesFile, usage -> GsonUtil.serialize(usage)));
			}
		}
	}

	private <T> void writeEntry(T entry, PointerAnalysisFactory factory,
			Map<PointerAnalysisFactory, ZipWriter<T>> writers) throws IOException {

		ZipWriter<T> writer = writers.get(factory);
		if (writer != null) {
			writer.add(entry);
		}
	}

	private List<Path> getZipFiles(Path directory) throws IOException {
		return Files.walk(directory)
				.filter((Path path) -> Files.isRegularFile(path) && path.toString().endsWith(ZIP_FILE_ENDING))
				.collect(Collectors.toList());
	}

}
