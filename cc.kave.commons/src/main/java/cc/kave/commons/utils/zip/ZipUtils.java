/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.utils.zip;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

public class ZipUtils {

	public static Set<ZipFolder> getAllZips(String path, String inputPath, String outputPath) {
		File directory = new File(path);
		Set<ZipFolder> files = new LinkedHashSet<>();
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile() && file.getName().contains(".zip")) {
				files.add(new ZipFolder(inputPath, directory.getAbsolutePath().replace(inputPath, ""), outputPath));
			} else if (file.isDirectory()) {
				files.addAll(getAllZips(file.getAbsolutePath(), inputPath, outputPath));
			}
		}
		return files;
	}
}
