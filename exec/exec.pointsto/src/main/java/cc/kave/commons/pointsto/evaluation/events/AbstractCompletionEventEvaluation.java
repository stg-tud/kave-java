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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.inject.Guice;
import com.google.inject.Injector;

import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.kave.commons.pointsto.AdvancedPointsToAnalysisFactory;
import cc.kave.commons.pointsto.PointsToAnalysisFactory;
import cc.kave.commons.pointsto.SimplePointsToAnalysisFactory;
import cc.kave.commons.pointsto.analysis.FieldSensitivity;
import cc.kave.commons.pointsto.analysis.ReferenceBasedAnalysis;
import cc.kave.commons.pointsto.analysis.TypeBasedAnalysis;
import cc.kave.commons.pointsto.analysis.unification.UnificationAnalysis;
import cc.kave.commons.pointsto.evaluation.Logger;
import cc.kave.commons.pointsto.evaluation.ResultExporter;
import cc.kave.commons.pointsto.io.ZipArchive;
import cc.kave.commons.utils.json.JsonUtils;

public abstract class AbstractCompletionEventEvaluation {

	protected void log(String format, Object... args) {
		Logger.log(format, args);
	}

	public void run(Path completionEventsArchive, List<PointsToAnalysisFactory> ptFactories) throws IOException {
		List<ICompletionEvent> complectionEvents;
		int totalNumberOfEvents;
		try (ZipArchive archive = new ZipArchive(completionEventsArchive)) {
			complectionEvents = archive.stream(ICompletionEvent.class, JsonUtils::fromJson).parallel()
					.filter(new CompletionEventFilter()).collect(Collectors.toList());
			totalNumberOfEvents = archive.countFiles();
		}
		log("%d/%d (%.2f%%) events used for evaluation\n", complectionEvents.size(), totalNumberOfEvents,
				complectionEvents.size() / (double) totalNumberOfEvents * 100);

		evaluate(complectionEvents, ptFactories);
	}

	protected abstract void evaluate(List<ICompletionEvent> completionEvents, List<PointsToAnalysisFactory> ptFactories)
			throws IOException;

	public static void main(String[] args) throws IOException {
		Path baseDir = Paths.get("E:\\Coding\\MT");
		Path completionEventsArchive = baseDir.resolve("CompletionEvents.zip");
		List<PointsToAnalysisFactory> ptFactories = Arrays
				.asList(new SimplePointsToAnalysisFactory<>(ReferenceBasedAnalysis.class),
						new SimplePointsToAnalysisFactory<>(
								TypeBasedAnalysis.class),
				new AdvancedPointsToAnalysisFactory<>(UnificationAnalysis.class,
						FieldSensitivity.FULL)/*
												 * , new
												 * SimplePointsToAnalysisFactory
												 * <>(InclusionAnalysis.class)
												 */);
		Path evaluationResultsDir = baseDir.resolve("EvaluationResults");

		Injector injector;
		injector = Guice.createInjector(new StoreModule());
		MRREvaluation evaluation = injector.getInstance(MRREvaluation.class);
		evaluation.run(completionEventsArchive, ptFactories);
		evaluation.exportResults(evaluationResultsDir.resolve(evaluation.getClass().getSimpleName() + ".txt"),
				injector.getInstance(ResultExporter.class));
		evaluation.close();

		// injector = Guice.createInjector(new Module());
		// TimeEvaluation evaluation =
		// injector.getInstance(TimeEvaluation.class);
		// evaluation.run(completionEventsArchive, ptFactories);
		// evaluation.exportResults(evaluationResultsDir,
		// injector.getInstance(ResultExporter.class));
	}
}
