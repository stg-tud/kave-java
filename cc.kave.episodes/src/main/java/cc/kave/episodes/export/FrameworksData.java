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
package cc.kave.episodes.export;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IAssemblyName;
import cc.kave.commons.model.names.csharp.AssemblyVersion;
import cc.kave.episodes.statistics.StreamStatistics;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

public class FrameworksData {

	private Directory rootDir;
	private File rootFolder;
	private StreamStatistics statistics;

	@Inject
	public FrameworksData(@Named("contexts") Directory directory, @Named("rootDir") File folder,
			StreamStatistics stat) {
		assertTrue(folder.exists(), "Data folder does not exist");
		assertTrue(folder.isDirectory(), "Data folder is not a folder, but a file");
		this.rootDir = directory;
		this.rootFolder = folder;
		this.statistics = stat;
	}

	public void getFrameworksDistribution() throws IOException {

		List<Event> allEvents = getAllEvents();
		Map<Event, Integer> frequencies = statistics.getFrequences(allEvents);
		
//		for (Map.Entry<Event, Integer> entry : frequencies.entrySet()) {
//			Logger.log("Type: %s", entry.getKey().getMethod().getDeclaringType().getFullName());
//		}

		Frameworks distribution = getEventsAndTypes(frequencies);
		storeFrameworks(distribution);
	}

	private List<Event> getAllEvents() throws IOException {
		EventStreamGenerator generator = new EventStreamGenerator();

		for (String zip : findZips()) {
			Logger.log("Reading zip file %s", zip.toString());
			ReadingArchive ra = rootDir.getReadingArchive(zip);

			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				if (ctx == null) {
					continue;
				}
				generator.add(ctx);
			}
			ra.close();
		}
		List<Event> allEvents = generator.getEventStream();
		return allEvents;
	}

	private Frameworks getEventsAndTypes(Map<Event, Integer> frequencies) throws IOException {
		Frameworks frameworks = new Frameworks();

		for (Map.Entry<Event, Integer> entry : frequencies.entrySet()) {
			if (entry.getValue() == 1) {
				continue;
			}
			// number of events per framework
			// + number of types for each framework
			IAssemblyName asm = entry.getKey().getMethod().getDeclaringType().getAssembly();
			String frameworkName = asm.getIdentifier();
			String typeName = entry.getKey().getMethod().getDeclaringType().getFullName();
			if(AssemblyVersion.UNKNOWN_NAME.equals(asm.getVersion())) {
				continue;
			}
			if (frameworks.events.containsKey(frameworkName)) {
				Map<Event, Integer> frameworkEvents = frameworks.events.get(frameworkName);
				if (!frameworkEvents.containsKey(entry.getKey())) {
					frameworkEvents.put(entry.getKey(), entry.getValue());
				}
				frameworks.events.put(frameworkName, frameworkEvents);

				if (!frameworks.types.get(frameworkName).contains(typeName)) {
					frameworks.types.get(frameworkName).add(typeName);
				}
			} else {
				Map<Event, Integer> newFrameworkEvent = Maps.newHashMap();
				newFrameworkEvent.put(entry.getKey(), entry.getValue());
				frameworks.events.put(frameworkName, newFrameworkEvent);

				frameworks.types.put(frameworkName, Lists.newArrayList(typeName));
			}
		}
		return frameworks;
	}

	private class Frameworks {
		private Map<String, Map<Event, Integer>> events = Maps.newHashMap();
		private Map<String, List<String>> types = Maps.newHashMap();
	}

	private void storeFrameworks(Frameworks distribution) throws IOException {
		StringBuilder eventsBuilder = new StringBuilder();
		StringBuilder typesBuilder = new StringBuilder();

		for (Map.Entry<String, Map<Event, Integer>> framework : distribution.events.entrySet()) {
			eventsBuilder.append(framework.getKey() + "\t" + framework.getValue().size() + "\t");

			int freqTotal = 0;
			for (Map.Entry<Event, Integer> events : framework.getValue().entrySet()) {
				freqTotal += events.getValue();
			}
			eventsBuilder.append(freqTotal);
			eventsBuilder.append("\n");

			typesBuilder.append(framework.getKey() + "\t" + framework.getValue().size() + "\n");
		}
		FileUtils.writeStringToFile(new File(getEventsPath()), eventsBuilder.toString());
		FileUtils.writeStringToFile(new File(getTypesPath()), typesBuilder.toString());
	}

	private String getEventsPath() {
		String fileName = rootFolder.getAbsolutePath() + "/eventsPerFramework.txt";
		return fileName;
	}

	private String getTypesPath() {
		String fileName = rootFolder.getAbsolutePath() + "/typesPerFramework.txt";
		return fileName;
	}

	private Set<String> findZips() {
		Set<String> zips = rootDir.findFiles(new Predicate<String>() {

			@Override
			public boolean apply(String arg0) {
				return arg0.endsWith(".zip");
			}
		});
		return zips;
	}
}
