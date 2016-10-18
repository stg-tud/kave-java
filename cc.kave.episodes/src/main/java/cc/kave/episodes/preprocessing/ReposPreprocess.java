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
package cc.kave.episodes.preprocessing;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;

import cc.kave.episodes.export.EventStreamIo;
import cc.kave.episodes.export.EventsFilter;
import cc.kave.episodes.io.IndivReposParser;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ReposPreprocess {

	private File eventsFolder;
	private IndivReposParser repos;

	@Inject
	public ReposPreprocess(@Named("events") File folder, IndivReposParser repos) {
		assertTrue(folder.exists(), "Events folder does not exist");
		assertTrue(folder.isDirectory(), "Events is not a folder, but a file");
		this.eventsFolder = folder;
		this.repos = repos;
	}

	public void generate(int freqThresh) throws ZipException, IOException {
		Map<String, List<Event>> reposEvents = repos.generateReposEvents();
		int repoId = 0;
		
		for (Map.Entry<String, List<Event>> entry : reposEvents.entrySet()) {
			String repository = entry.getKey();
			List<Event> events = entry.getValue();
			EventStream es = EventsFilter.filterStream(events, freqThresh);
			
			FileUtils.writeStringToFile(new File(getPath(repoId).repoPath), repository);
			EventStreamIo.write(es, getPath(repoId).streamPath, getPath(repoId).mappingPath);
			
			repoId++;
		}
//		debugStream(stream.getMapping().keySet());
//		List<Event> mapping = new MappingParser(eventsFolder).parse(1);
//		debugStream(mapping);
	}

	private void debugStream(Iterable<Event> keySet) {
		int i = 0;
		for (Event e : keySet) {
			if (i > 1230 && i < 1240) {
				if (e.getKind() == EventKind.METHOD_DECLARATION) {
					System.out.printf("%d -> %s\n", i, e.getMethod());
				}
			}
			i++;
		}
	}

	private FilePaths getPath(int repoID) {
		File pathName = new File(eventsFolder.getAbsolutePath() + "/repo" + repoID);
		if (!pathName.isDirectory()) {
			pathName.mkdir();
		}
		FilePaths paths = new FilePaths();
		paths.repoPath = pathName.getAbsolutePath() + "/repository.txt";
		paths.streamPath = pathName.getAbsolutePath() + "/stream.txt";
		paths.mappingPath = pathName.getAbsolutePath() + "/mapping.txt";

		return paths;
	}

	private class FilePaths {
		private String repoPath = "";
		private String streamPath = "";
		private String mappingPath = "";
	}
}
