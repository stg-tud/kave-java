/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.recommender_reimplementation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;
import cc.recommenders.io.ReadingArchive;

public class ContextReader {
	
	public static final String ZIP_FILE_ENDING = ".zip";
	public static final String JSON_FILE_ENDING = ".json";

	public static List<Context> GetContexts(Path folderPath) throws IOException {
		List<Context> contextList = Lists.newLinkedList();
		List<Path> zipList = GetAllZipFiles(folderPath);
		for (Path path : zipList) {
			contextList.addAll(readType(path, Context.class));
		}
		return contextList;
	}
	
	public static List<CompletionEvent> GetCompletionEvents(Path folderPath) throws IOException {
		List<CompletionEvent> contextList = Lists.newLinkedList();
		List<Path> pathList = GetAllZipFiles(folderPath);
		pathList.addAll(GetAllJsonFiles(folderPath));
		for (Path path : pathList) {
			contextList.addAll(readType(path, CompletionEvent.class));
		}
		return contextList;
	}

	public static <T> List<T> readType(Path path, Class<T> type) {
		List<T> res = Lists.newLinkedList();
		try {
			ReadingArchive ra = new ReadingArchive(new File(path.toString()));
			while (ra.hasNext()) {
				res.add(ra.getNext(type));
			}
			ra.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public static List<Path> GetAllZipFiles(Path folderPath) throws IOException {
		return Files.walk(folderPath).filter(path -> Files.isRegularFile(path) && path.toString().endsWith(ZIP_FILE_ENDING))
				.collect(Collectors.toList());
	}

	public static List<Path> GetAllJsonFiles(Path folderPath) throws IOException {
		return Files.walk(folderPath).filter(path -> Files.isRegularFile(path) && path.toString().endsWith(JSON_FILE_ENDING))
				.collect(Collectors.toList());
	}
	
}
