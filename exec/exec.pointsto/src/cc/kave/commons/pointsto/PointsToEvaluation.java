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
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import cc.kave.commons.pointsto.analysis.SimplePointerAnalysisFactory;
import cc.kave.commons.pointsto.analysis.TypeAliasedAnalysis;
import cc.kave.commons.pointsto.dummies.DummyUsage;

public class PointsToEvaluation {
	
	private static final Logger LOGGER = Logger.getLogger(PointsToEvaluation.class.getName());

	private static final Path SRC_PATH = Paths.get("E:\\Coding\\MT\\TestContexts");
	private static final Path CONTEXT_DEST = Paths.get("E:\\Coding\\MT\\annotatedContexts");
	private static final Path USAGE_DEST = Paths.get("E:\\Coding\\MT\\Usages");
	
	public static void main(String[] args) {
		new PointsToEvaluation()
				.generateUsages(Arrays.asList(new SimplePointerAnalysisFactory<>(TypeAliasedAnalysis.class)));

	}

	private Map<PointerAnalysisFactory, List<DummyUsage>> generateUsages(List<PointerAnalysisFactory> factories) {
		try {
			PointsToUsageGenerator generator = new PointsToUsageGenerator(factories, SRC_PATH, CONTEXT_DEST, USAGE_DEST);
			
			return generator.getUsages();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error during usage generation", e);
		}
		
		return new HashMap<>();
	}

}
