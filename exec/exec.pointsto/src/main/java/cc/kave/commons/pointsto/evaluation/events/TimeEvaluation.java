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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.mutable.MutableLong;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.base.Stopwatch;
import com.google.inject.Inject;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.kave.commons.pointsto.PointsToAnalysisFactory;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.evaluation.ResultExporter;
import cc.kave.commons.pointsto.evaluation.StatementCounterVisitor;

public class TimeEvaluation extends AbstractCompletionEventEvaluation {

	private static final int MAX_COMPLETION_EVENTS = 10000;

	private static final int WARM_UP_RUNS = 5;

	private final RandomGenerator rndGenerator;

	private Map<String, DescriptiveStatistics> analysisStatistics = new HashMap<>();
	private Map<String, List<Pair<Integer, Double>>> analysisTimes = new HashMap<>();

	private Map<Context, Integer> stmtCounts = new IdentityHashMap<>();

	@Inject
	public TimeEvaluation(RandomGenerator rndGenerator) {
		this.rndGenerator = rndGenerator;
	}

	public void exportResults(Path outputDir, ResultExporter exporter) throws IOException {
		Function<Double, String> format = number -> String.format(Locale.US, "%.3f", number);
		exporter.export(outputDir.resolve("TimeStatistics.txt"), analysisStatistics.entrySet().stream().map(entry -> {
			DescriptiveStatistics stats = entry.getValue();
			return new String[] { entry.getKey(), format.apply(stats.getMin()),
					format.apply(stats.getStandardDeviation()), format.apply(stats.getMean()),
					format.apply(stats.getMax()) };
		}));
		exporter.export(
				outputDir
						.resolve(
								"StmtCountTimes.txt"),
				analysisTimes.entrySet().stream().flatMap(entry -> entry.getValue().stream().map(value -> new String[] {
						entry.getKey(), value.getLeft().toString(), format.apply(value.getRight()) })));
	}

	@Override
	protected void evaluate(List<ICompletionEvent> completionEvents, List<PointsToAnalysisFactory> ptFactories)
			throws IOException {
		List<Context> contexts = sampleEvents(completionEvents);
		log("Using %d contexts for time measurement\n", contexts.size());

		initializeStmtCountTimes(ptFactories, contexts);

		for (int i = 0; i < WARM_UP_RUNS + 1; ++i) {
			clearStmtCountTimes();

			for (PointsToAnalysisFactory ptFactory : ptFactories) {
				analysisStatistics.put(ptFactory.getName(),
						measurePointerAnalysis(contexts, ptFactory, new MutableLong()));
			}
		}
	}

	private List<Context> sampleEvents(List<ICompletionEvent> completionEvents) {
		Random rnd = new Random(rndGenerator.nextLong());
		Collections.shuffle(completionEvents, rnd);
		Stream<ICompletionEvent> eventStream;
		if (completionEvents.size() > MAX_COMPLETION_EVENTS) {
			eventStream = completionEvents.subList(0, MAX_COMPLETION_EVENTS).stream();
		} else {
			eventStream = completionEvents.stream();
		}
		return eventStream.map(event -> event.getContext()).collect(Collectors.toList());
	}

	private void initializeStmtCountTimes(Collection<PointsToAnalysisFactory> ptFactories, List<Context> contexts) {
		for (PointsToAnalysisFactory ptFactory : ptFactories) {
			analysisTimes.put(ptFactory.getName(), new ArrayList<>(contexts.size()));
		}

		StatementCounterVisitor counter = new StatementCounterVisitor();
		for (Context context : contexts) {
			stmtCounts.put(context, context.getSST().accept(counter, null));
		}
	}

	private void clearStmtCountTimes() {
		for (List<?> times : analysisTimes.values()) {
			times.clear();
		}
	}

	private DescriptiveStatistics measurePointerAnalysis(List<Context> contexts, PointsToAnalysisFactory ptFactory,
			MutableLong sink) {
		DescriptiveStatistics stats = new DescriptiveStatistics();

		for (Context context : contexts) {
			PointsToAnalysis ptAnalysis = ptFactory.create();
			Stopwatch watch = Stopwatch.createStarted();
			PointsToContext ptContext = ptAnalysis.compute(context);
			watch.stop();
			sink.add(ptContext.hashCode());
			long time = watch.elapsed(TimeUnit.MICROSECONDS);
			stats.addValue(time);

			analysisTimes.get(ptFactory.getName()).add(ImmutablePair.of(stmtCounts.get(context), (double) time));
		}

		return stats;
	}
}
