/**
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.utils.exec;

import java.io.File;

import org.apache.commons.io.FileUtils;

public class run_me {

	private static String root = "/Volumes/Data/";
	private static String dirEventsCompletion = root + "Events/OnlyCompletion/";
	private static String dirEventsCompletionInlined = root + "Events/OnlyCompletion-inlined/";
	private static String dirContexts = root + "Contexts/";
	private static String dirContextsInlined = root + "Contexts-inlined/";
	private static String zipTestCases = root + "AnalysisTestCases.sln-contexts.zip";

	public static void main(String[] args) {
		/* data preparation */
		runContextBatchInlining();
		// runConmpletionEventBatchInlining();

		/* evaluation results */
		// runSSTComparison();
	}

	private static void runContextBatchInlining() {
		cleanDirs(dirContextsInlined);
		new ContextBatchInlining(dirContexts, dirContextsInlined).run();
	}

	private static void runConmpletionEventBatchInlining() {
		cleanDirs(dirEventsCompletionInlined);
		new CompletionEventProcessor(dirEventsCompletion, dirEventsCompletionInlined).run();
	}

	private static void runSSTComparison() {
		new SSTComparison(zipTestCases).run();
	}

	private static void cleanDirs(String... dirs) {
		for (String dir : dirs) {
			File f = new File(dir);
			FileUtils.deleteQuietly(f);
			f.mkdirs();
		}
	}
}