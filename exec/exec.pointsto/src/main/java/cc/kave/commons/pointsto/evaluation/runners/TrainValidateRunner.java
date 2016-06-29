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
import java.util.Locale;
import java.util.concurrent.ExecutorService;

import com.google.inject.Guice;
import com.google.inject.Injector;

import cc.kave.commons.pointsto.evaluation.DefaultModule;
import cc.kave.commons.pointsto.evaluation.ProjectTrainValidateEvaluation;
import cc.kave.commons.pointsto.evaluation.ResultExporter;

public class TrainValidateRunner {

	public static void main(String[] args) throws IOException {
		Locale.setDefault(Locale.US);

		Path usageStoresDir = Paths.get(".", "Usages");
		Path evaluationResultsDir = Paths.get(".", "EvaluationResults");

		Injector injector = Guice.createInjector(new DefaultModule());
		ProjectTrainValidateEvaluation evaluator = injector.getInstance(ProjectTrainValidateEvaluation.class);
		evaluator.run(usageStoresDir);
		evaluator.export(evaluationResultsDir, injector.getInstance(ResultExporter.class));

		injector.getInstance(ExecutorService.class).shutdown();
	}

}
