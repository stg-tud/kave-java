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
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.mutable.MutableLong;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.base.Stopwatch;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.pointsto.PointsToAnalysisFactory;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.evaluation.ContextSampleEvaluation;
import cc.kave.commons.pointsto.evaluation.ContextSampler;
import cc.kave.commons.pointsto.evaluation.DefaultModule;
import cc.kave.commons.pointsto.evaluation.ResultExporter;
import cc.kave.commons.pointsto.evaluation.StatementCounterVisitor;

public class TimeEvaluation extends ContextSampleEvaluation {

	private static final int WARM_UP_RUNS = 5;
	private static final int MEASUREMENT_RUNS = 3;

	private Map<String, DescriptiveStatistics> analysisStatistics = new HashMap<>();
	private List<AnalysisTimeEntry> analysisTimes;

	private Map<Context, Integer> stmtCounts = new IdentityHashMap<>();

	@Inject
	public TimeEvaluation(ContextSampler sampler) {
		super(sampler);
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
				analysisTimes.stream().map(entry -> new String[] { entry.analysisName, entry.contextType.getFullName(),
						Integer.toString(entry.numStmts), format.apply(entry.time) }));
	}

	public void run(Path contextsDir, List<PointsToAnalysisFactory> ptFactories) throws IOException {
		run(getSamples(contextsDir), ptFactories);
	}

	public void run(List<Context> contexts, List<PointsToAnalysisFactory> ptFactories) throws IOException {
		initializeStmtCountTimes(ptFactories, contexts);
		log("Using %d contexts for time measurement\n", contexts.size());

		Map<Pair<String, ITypeName>, AnalysisTimeEntry> timesRegistry = new LinkedHashMap<>(
				contexts.size() * ptFactories.size());
		for (int i = 0; i < WARM_UP_RUNS + MEASUREMENT_RUNS; ++i) {
			if (i == WARM_UP_RUNS) {
				timesRegistry.clear();
			}

			for (PointsToAnalysisFactory ptFactory : ptFactories) {
				analysisStatistics.put(ptFactory.getName(),
						measurePointerAnalysis(contexts, ptFactory, new MutableLong()));
			}
			updateTimesRegistry(timesRegistry);
		}

		analysisTimes = timesRegistry.values().stream().map(entry -> new AnalysisTimeEntry(entry.analysisName,
				entry.contextType, entry.numStmts, entry.time / MEASUREMENT_RUNS)).collect(Collectors.toList());
	}

	private void initializeStmtCountTimes(Collection<PointsToAnalysisFactory> ptFactories, List<Context> contexts) {
		analysisTimes = new ArrayList<>(ptFactories.size() * contexts.size());

		List<Context> zeroStmtsContexts = new ArrayList<>();
		StatementCounterVisitor counter = new StatementCounterVisitor();
		for (Context context : contexts) {
			int count = context.getSST().accept(counter, null);
			if (count == 0) {
				zeroStmtsContexts.add(context);
			} else {
				stmtCounts.put(context, count);
			}
		}

		contexts.removeAll(zeroStmtsContexts);
		log("Removed %d contexts with zero statements\n", zeroStmtsContexts.size());
	}

	private void updateTimesRegistry(Map<Pair<String, ITypeName>, AnalysisTimeEntry> registry) {
		if (registry.isEmpty()) {
			for (AnalysisTimeEntry timeEntry : analysisTimes) {
				registry.put(ImmutablePair.of(timeEntry.analysisName, timeEntry.contextType), timeEntry);
			}
		} else {
			for (AnalysisTimeEntry timeEntry : analysisTimes) {
				Pair<String, ITypeName> key = ImmutablePair.of(timeEntry.analysisName, timeEntry.contextType);
				AnalysisTimeEntry currentEntry = registry.get(key);
				registry.put(key, new AnalysisTimeEntry(timeEntry.analysisName, timeEntry.contextType,
						timeEntry.numStmts, timeEntry.time + currentEntry.time));
			}
		}
		analysisTimes.clear();
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

			analysisTimes.add(new AnalysisTimeEntry(ptFactory.getName(),
					context.getTypeShape().getTypeHierarchy().getElement(), stmtCounts.get(context), time));
		}

		return stats;
	}

	private static class AnalysisTimeEntry {
		public final String analysisName;
		public final ITypeName contextType;
		public final int numStmts;
		public final double time;

		public AnalysisTimeEntry(String analysisName, ITypeName contextType, int numStmts, double time) {
			this.analysisName = analysisName;
			this.contextType = contextType;
			this.numStmts = numStmts;
			this.time = time;
		}

	}

	public static void main(String[] args) throws IOException {
		Injector injector = Guice.createInjector(new DefaultModule());

		TimeEvaluation evaluation = injector.getInstance(TimeEvaluation.class);
		evaluation.run(CONTEXT_DIR, POINTS_TO_FACTORIES);
		evaluation.exportResults(EVALUATION_RESULTS_DIR, injector.getInstance(ResultExporter.class));
	}
}
