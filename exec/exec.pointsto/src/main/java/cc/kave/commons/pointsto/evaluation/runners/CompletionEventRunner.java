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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;

import cc.kave.commons.pointsto.AdvancedPointsToAnalysisFactory;
import cc.kave.commons.pointsto.InliningPointsToAnalysisFactory;
import cc.kave.commons.pointsto.PointsToAnalysisFactory;
import cc.kave.commons.pointsto.SimplePointsToAnalysisFactory;
import cc.kave.commons.pointsto.analysis.FieldSensitivity;
import cc.kave.commons.pointsto.analysis.ReferenceBasedAnalysis;
import cc.kave.commons.pointsto.analysis.TypeBasedAnalysis;
import cc.kave.commons.pointsto.analysis.inclusion.InclusionAnalysis;
import cc.kave.commons.pointsto.analysis.unification.UnificationAnalysis;
import cc.kave.commons.pointsto.evaluation.ResultExporter;
import cc.kave.commons.pointsto.evaluation.events.MRREvaluation;
import cc.kave.commons.pointsto.evaluation.events.StoreModule;

public class CompletionEventRunner {

	private static final Path BASE_DIR = Paths.get(".");
	private static final Path USAGES_DIR = BASE_DIR.resolve("Usages");
	private static final Path CE_DIR = BASE_DIR.resolve("CompletionEvents");
	private static final Path EVAL_RESULTS_DIR = BASE_DIR.resolve("EvaluationResults");

	public static void main(String[] args) throws IOException {
		List<PointsToAnalysisFactory> ptFactories = Arrays.asList(
				new SimplePointsToAnalysisFactory<>(ReferenceBasedAnalysis.class),
				new SimplePointsToAnalysisFactory<>(TypeBasedAnalysis.class),
				new AdvancedPointsToAnalysisFactory<>(UnificationAnalysis.class, FieldSensitivity.FULL),
				new SimplePointsToAnalysisFactory<>(InclusionAnalysis.class),
				new InliningPointsToAnalysisFactory(
						new AdvancedPointsToAnalysisFactory<>(UnificationAnalysis.class, FieldSensitivity.FULL)),
				new InliningPointsToAnalysisFactory(new SimplePointsToAnalysisFactory<>(InclusionAnalysis.class)));

		Injector injector;
		injector = Guice.createInjector(new StoreModule(USAGES_DIR));
		MRREvaluation evaluation = injector.getInstance(MRREvaluation.class);
		evaluation.run(CE_DIR, ptFactories);
		evaluation.exportResults(EVAL_RESULTS_DIR, injector.getInstance(ResultExporter.class));
		evaluation.close();
	}

}
