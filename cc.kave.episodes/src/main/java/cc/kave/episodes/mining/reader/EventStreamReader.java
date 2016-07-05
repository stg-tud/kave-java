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
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;

public class EventStreamReader {

	private File rootFolder;
	private FileReader reader;
	private EventMappingParser eventMapper;

	@Inject
	public EventStreamReader(@Named("events") File directory, FileReader reader, EventMappingParser eventMapping) {
		assertTrue(directory.exists(), "Event Stream folder does not exist");
		assertTrue(directory.isDirectory(), "Event Stream folder is not a folder, but a file");
		this.rootFolder = directory;
		this.reader = reader;
		this.eventMapper = eventMapping;
	}

	public void read(int numbRepos) {
		File filePath = getFilePath();
		List<Event> allEvents = eventMapper.parse(numbRepos);
		List<String> lines = reader.readFile(filePath);

		int counter = 0;

		for (String line : lines) {
			String[] lineValues = line.split(",");
			int eventIndex = Integer.parseInt(lineValues[0]);
			if (eventIndex == 4440) {
				counter++;
				continue;
			}
			System.out.println("--- " + (counter++) + " ---------------------");
			System.out.println("--- " + (eventIndex) + " ---------------------");
			System.out.println(allEvents.get(eventIndex));
			System.out.println(allEvents.get(eventIndex).getMethod().getDeclaringType().toString());
		}
		System.out.printf("found %d events:\n", allEvents.size());
	}

	private File getFilePath() {
		String fileName = rootFolder.getAbsolutePath() + "/eventstream.txt";
		File file = new File(fileName);
		return file;
	}
}
