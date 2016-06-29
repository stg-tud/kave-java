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

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import cc.kave.commons.pointsto.AdvancedPointsToAnalysisFactory;
import cc.kave.commons.pointsto.PointsToAnalysisFactory;
import cc.kave.commons.pointsto.SimplePointsToAnalysisFactory;
import cc.kave.commons.pointsto.analysis.FieldSensitivity;
import cc.kave.commons.pointsto.analysis.ReferenceBasedAnalysis;
import cc.kave.commons.pointsto.analysis.TypeBasedAnalysis;
import cc.kave.commons.pointsto.analysis.inclusion.InclusionAnalysis;
import cc.kave.commons.pointsto.analysis.unification.UnificationAnalysis;

public abstract class AbstractEvaluation {

	protected static final Path BASE_DIR = Paths.get("E:\\Coding\\MT");
	protected static final Path CONTEXT_DIR = BASE_DIR.resolve("Contexts");
	protected static final Path EVALUATION_RESULTS_DIR = BASE_DIR.resolve("EvaluationResults");

	protected static final int MAX_USAGES = 30000;

	protected static final List<PointsToAnalysisFactory> POINTS_TO_FACTORIES = Arrays.asList(
			new SimplePointsToAnalysisFactory<>(ReferenceBasedAnalysis.class),
			new SimplePointsToAnalysisFactory<>(TypeBasedAnalysis.class),
			new AdvancedPointsToAnalysisFactory<>(UnificationAnalysis.class, FieldSensitivity.FULL),
			new SimplePointsToAnalysisFactory<>(InclusionAnalysis.class));

	protected void log(String format, Object... args) {
		Logger.log(format, args);
	}
}
