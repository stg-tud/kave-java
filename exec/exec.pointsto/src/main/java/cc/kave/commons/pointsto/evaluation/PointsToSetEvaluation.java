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

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.PointsToQuery;
import cc.kave.commons.pointsto.analysis.inclusion.InclusionAnalysis;
import cc.kave.commons.pointsto.extraction.PointsToUsageExtractor;

public class PointsToSetEvaluation extends ContextSampleEvaluation {

	private final List<Integer> results = new ArrayList<>();

	@Inject
	public PointsToSetEvaluation(ContextSampler sampler) {
		super(sampler);
	}

	public void exportResults(Path evaluationDir, ResultExporter exporter) throws IOException {
		exporter.export(evaluationDir.resolve(getClass().getSimpleName() + ".txt"),
				results.stream().map(setSize -> new String[] { setSize.toString() }));
	}

	public void run(Path contextsDir) throws IOException {
		StatementCounterVisitor stmtCounterVisitor = new StatementCounterVisitor();
		List<Context> contexts = getSamples(contextsDir).stream()
				.filter(cxt -> cxt.getSST().accept(stmtCounterVisitor, null) > 0).collect(Collectors.toList());
		log("Using %d contexts for evaluation\n", contexts.size());

		PointsToUsageExtractor extractor = new PointsToUsageExtractor();
		for (Context context : contexts) {
			PointstoSetSizeAnalysis analysis = new PointstoSetSizeAnalysis();
			extractor.extract(analysis.compute(context));
			results.addAll(analysis.getSetSizes());
		}

		DescriptiveStatistics statistics = new DescriptiveStatistics();
		for (Integer setSize : results) {
			statistics.addValue(setSize.doubleValue());
		}
		log("mean: %.2f\n", statistics.getMean());
		log("stddev: %.2f\n", statistics.getStandardDeviation());
		log("min/max: %.2f/%.2f\n", statistics.getMin(), statistics.getMax());
	}

	private static class PointstoSetSizeAnalysis extends InclusionAnalysis {
		private final List<Integer> setSizes = new ArrayList<>();

		public List<Integer> getSetSizes() {
			return setSizes;
		}

		@Override
		public Set<AbstractLocation> query(PointsToQuery query) {
			Set<AbstractLocation> queryResult = super.query(query);
			setSizes.add(queryResult.size());
			return queryResult;
		}
	}

	public static void main(String[] args) throws IOException {
		Injector injector = Guice.createInjector(new DefaultModule());
		PointsToSetEvaluation evaluation = injector.getInstance(PointsToSetEvaluation.class);
		evaluation.run(CONTEXT_DIR);
		evaluation.exportResults(EVALUATION_RESULTS_DIR, injector.getInstance(ResultExporter.class));
	}

}
