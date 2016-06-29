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
import java.util.List;

import cc.kave.commons.pointsto.evaluation.AbstractEvaluation;
import cc.kave.commons.pointsto.io.IOHelper;
import cc.kave.commons.pointsto.io.ZipArchive;

public class SSTCounterRunner extends AbstractEvaluation {

	public static void main(String[] args) throws IOException {
		List<Path> zipFiles = IOHelper.getZipFiles(CONTEXT_DIR);
		long numSSTs = 0;

		for (Path file : zipFiles) {
			try (ZipArchive archive = new ZipArchive(file)) {
				numSSTs += archive.countFiles();
			}
		}

		System.out.println("#SSTs: " + numSSTs);
	}

}
