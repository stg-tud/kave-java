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

	private static String root = "/Users/seb/evaluation/csharp/";
	private static String dirCompletionEvents = root + "Events/OnlyCompletion/";
	private static String dirCompletionEventsInlined = root + "Events/OnlyCompletion-inlined/";
	private static String dirContexts = root + "Context/Github";
	private static String dirContextsInlined = root + "Context/Github-inlined";
	private static String zipTestCases = root + "AnalysisTestCases.sln-contexts.zip";

	public static void main(String[] args) {
		cleanDirs(dirCompletionEventsInlined);
		// new BatchInlining().run(dirContexts, dirContextsInlined);
		// new CompletionEventProcessor(dirCompletionEvents,
		// dirCompletionEventsInlined).run();
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