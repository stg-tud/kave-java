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
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import cc.kave.commons.pointsto.evaluation.AbstractEvaluation;
import cc.kave.commons.pointsto.evaluation.UsageEvaluation;

public class UsageEvaluationRunner extends AbstractEvaluation {

	private static final Path USAGE_STORES_DIR = Paths.get(".", "Usages");
	private static final Path EVALUATION_RESULTS_DIR = Paths.get(".", "EvaluationResults");

	public static void main(String[] args) {
		Locale.setDefault(Locale.US);

		try {
			for (Path store : getStoreDirs(args)) {
				try {
					UsageEvaluation.run(store, EVALUATION_RESULTS_DIR.resolve(store.getFileName() + ".txt"));
				} catch (IOException e) {
					System.err.printf("Failed to evaluate usages in %s\n", store.toString());
					e.printStackTrace(System.err);
				}
			}
		} finally {
			UsageEvaluation.shutdown();
		}
	}

	private static Collection<Path> getStoreDirs(String[] args) {
		List<Path> storeDirs = new ArrayList<>();
		if (args.length > 0) {
			for (String store : args) {
				storeDirs.add(Paths.get(store));
			}
		} else {
			try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(USAGE_STORES_DIR)) {
				for (Path dir : dirStream) {
					if (Files.isDirectory(dir)) {
						storeDirs.add(dir);
					}
				}
			} catch (IOException e) {
				System.err.println("Failed to find usage stores:");
				e.printStackTrace(System.err);
			}
		}
		return storeDirs;
	}

}
