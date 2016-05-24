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
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.events.completionevents.Context;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

public class FrameworksData {

	private Directory rootDir;
	private File rootFolder;

	@Inject
	public FrameworksData(@Named("contexts") Directory directory, @Named("rootDir") File folder) {
		assertTrue(folder.exists(), "Data folder does not exist");
		assertTrue(folder.isDirectory(), "Data folder is not a folder, but a file");
		this.rootDir = directory;
		this.rootFolder = folder;
	}

	public void getFrameworks() throws IOException {
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
		Map<String, Integer> distribution = frameworkDistribution(allEvents);
		storeFrameworks(distribution);
	}

	private Map<String, Integer> frameworkDistribution(List<Event> allEvents) throws IOException {
		Map<String, Integer> distribution = Maps.newHashMap();
		Logger.log("\nFrameworks:");
		
		for (Event event : allEvents) {
			String framework = event.getMethod().getDeclaringType().toString();
			Logger.log("%s", framework);
			
			if (distribution.containsKey(framework)) {
				int count = distribution.get(framework);
				distribution.put(framework, count + 1);
			} else {
				distribution.put(framework, 1);
			}
		}
		return distribution;
	}

	private void storeFrameworks(Map<String, Integer> distribution) throws IOException {
		StringBuilder sb = new StringBuilder();
		 
		for (Map.Entry<String, Integer> entry : distribution.entrySet()) {
			sb.append(entry.getKey());
			sb.append("\t");
			sb.append(entry.getValue());
			sb.append("\n");
		}
		FileUtils.writeStringToFile(new File(getPath()), sb.toString());
	}

	private String getPath() {
		String fileName = rootFolder.getAbsolutePath() + "frameworks.txt";
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
