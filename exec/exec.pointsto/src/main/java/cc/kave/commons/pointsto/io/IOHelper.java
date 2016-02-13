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
package cc.kave.commons.pointsto.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IOHelper {

	public static final String ZIP_FILE_ENDING = ".zip";

	public static void createParentDirs(Path file) throws IOException {
		if (Files.isDirectory(file)) {
			Files.createDirectories(file);
		} else {
			Path path = file.getParent();
			if (path != null) {
				Files.createDirectories(path);
			}
		}
	}

	public static List<Path> getFiles(String pattern, Path directory) throws IOException {
		return streamFiles(pattern, directory).collect(Collectors.toList());
	}

	public static Stream<Path> streamFiles(String pattern, Path directory) throws IOException {
		return Files.walk(directory)
				.filter(path -> Files.isRegularFile(path) && path.getFileName().toString().matches(pattern));
	}

	public static List<Path> getZipFiles(Path directory) throws IOException {
		return Files.walk(directory)
				.filter(path -> Files.isRegularFile(path) && path.toString().endsWith(ZIP_FILE_ENDING))
				.collect(Collectors.toList());
	}
}
