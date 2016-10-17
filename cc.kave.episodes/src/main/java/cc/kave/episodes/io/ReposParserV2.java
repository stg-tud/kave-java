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
package cc.kave.episodes.io;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.json.JsonUtils;
import cc.kave.episodes.export.EventStreamGenerator;
import cc.kave.episodes.export.EventsFilter;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ReposParserV2 {
	
	private Directory contextsDir;
	private File eventsFolder;
	
	@Inject
	public ReposParserV2(@Named("contexts") Directory directory, @Named("events") File folder) {
		assertTrue(folder.exists(), "Events folder does not exist");
		assertTrue(folder.isDirectory(), "Events is not a folder, but a file");
		this.contextsDir = directory;
		this.eventsFolder = folder;
	}
	
	public Map<String, List<Event>> learningStream(int numRepos, int freq) throws ZipException, IOException {
		EventStreamGenerator generator = new EventStreamGenerator();
		EventStreamGenerator repoGen = new EventStreamGenerator();
		StringBuilder repositories = new StringBuilder();
		Map<String, List<Event>> allEvents = Maps.newLinkedHashMap();
		String repoName = "";
		int repoID = 0;

		for (String zip : findZips(contextsDir)) {
			Logger.log("Reading zip file %s", zip.toString());
			if ((repoName.equalsIgnoreCase("")) || (!zip.startsWith(repoName))) {
				repoID++;
				if (repoID > numRepos) {
					break;
				}
				if (!repoGen.getEventStream().isEmpty()) {
					List<Event> repoEvents = repoGen.getEventStream();
					allEvents.put(repoName, repoEvents);
					repoGen = new EventStreamGenerator();
				}
				repoName = getRepoName(zip);
				repositories.append(repoName + "\n");
			} 
			ReadingArchive ra = contextsDir.getReadingArchive(zip);

			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				if (ctx == null) {
					continue;
				}
				generator.add(ctx);
				repoGen.add(ctx);
			}
			ra.close();
		}
		List<Event> repoEvents = repoGen.getEventStream();
		allEvents.put(repoName, repoEvents);
		FileUtils.writeStringToFile(new File(getReposPath(numRepos)), repositories.toString());
		
		List<Event> events = generator.getEventStream();
		EventStream es = EventsFilter.filterStream(events, freq);
		JsonUtils.toJson(es.getMapping().keySet(), new File(getMappingFile(numRepos)));
		return allEvents;
	}
	
	private String getRepoName(String zipName) {
		int index = zipName.indexOf("/", zipName.indexOf("/", zipName.indexOf("/") + 1) + 1);
		String startPrefix = zipName.substring(0, index);

		return startPrefix;
	}

	private Set<String> findZips(Directory contextsDir) {
		Set<String> zips = contextsDir.findFiles(new Predicate<String>() {

			@Override
			public boolean apply(String arg0) {
				return arg0.endsWith(".zip");
			}
		});
		return zips;
	}
	
	private String getReposPath(int numberOfRepos) {
		File pathName = new File(eventsFolder.getAbsolutePath() + "/" + numberOfRepos + "Repos");
		if (!pathName.isDirectory()) {
			pathName.mkdir();
		}
		String fileName = pathName.getAbsolutePath() + "/repositories.txt";
		return fileName;
	}
	
	private String getMappingFile(int numberOfRepos) {
		File pathName = new File(eventsFolder.getAbsolutePath() + "/" + numberOfRepos + "Repos");
		if (!pathName.isDirectory()) {
			pathName.mkdir();
		}
		String fileName = pathName.getAbsolutePath() + "/mapping.txt";
		return fileName;
	}
}
