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
package cc.kave.commons.pointsto.evaluation.events;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.gson.JsonParseException;

import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.kave.commons.pointsto.PointsToAnalysisFactory;
import cc.kave.commons.pointsto.evaluation.AbstractEvaluation;
import cc.kave.commons.pointsto.io.IOHelper;
import cc.kave.commons.pointsto.io.ZipArchive;
import cc.kave.commons.utils.io.json.JsonUtils;

public abstract class AbstractCompletionEventEvaluation extends AbstractEvaluation {

	public void run(Path completionEventsArchive, List<PointsToAnalysisFactory> ptFactories) throws IOException {
		List<Path> zipFiles;
		if (Files.isRegularFile(completionEventsArchive)) {
			zipFiles = Arrays.asList(completionEventsArchive);
		} else {
			zipFiles = IOHelper.getZipFiles(completionEventsArchive);
		}

		List<ICompletionEvent> completionEvents = new ArrayList<>();
		int totalNumberOfEvents = loadEvents(zipFiles, completionEvents);

		log("%d/%d (%.2f%%) events used for evaluation\n", completionEvents.size(), totalNumberOfEvents,
				completionEvents.size() / (double) totalNumberOfEvents * 100);

		long startTime = System.nanoTime();
		evaluate(completionEvents, ptFactories);
		long diff = System.nanoTime() - startTime;
		log("evaluation took %d min\n", TimeUnit.MINUTES.convert(diff, TimeUnit.NANOSECONDS));
	}

	protected Predicate<ICompletionEvent> createCompletionEventFilter() {
		return new CompletionEventFilter();
	}

	private int loadEvents(List<Path> zipFiles, List<ICompletionEvent> completionEvents) throws IOException {
		int totalNumberOfEvents = 0;
		for (Path file : zipFiles) {
			try (ZipArchive archive = new ZipArchive(file)) {
				completionEvents
						.addAll(archive.stream(ICompletionEvent.class, JsonUtils::fromJson, JsonParseException.class)
								.parallel().filter(createCompletionEventFilter()).collect(Collectors.toList()));
				totalNumberOfEvents += archive.countFiles();
			}
		}
		return totalNumberOfEvents;
	}

	protected abstract void evaluate(List<ICompletionEvent> completionEvents, List<PointsToAnalysisFactory> ptFactories)
			throws IOException;

}
