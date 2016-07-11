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
package cc.kave.episodes.mining.reader;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.utils.json.JsonUtils;

public class MappingParser {

	private File rootFolder;

	@Inject
	public MappingParser(@Named("events") File directory) {
		assertTrue(directory.exists(), "Event mapping folder does not exist");
		assertTrue(directory.isDirectory(), "Event mapping folder is not a folder, but a file");
		this.rootFolder = directory;
	}

	public List<Event> parse(int numbRepos) {

		@SuppressWarnings("serial")
		Type listType = new TypeToken<LinkedList<Event>>() {
		}.getType();
		List<Event> events = JsonUtils.fromJson(getFilePath(numbRepos), listType);
		return events;
	}

	private File getFilePath(int numbRepos) {
		String fileName = rootFolder.getAbsolutePath() + "/" + numbRepos + "Repos/mapping.txt";
		File file = new File(fileName);
		return file;
	}
}
