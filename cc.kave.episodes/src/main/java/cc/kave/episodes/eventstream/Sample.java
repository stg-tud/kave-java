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
package cc.kave.episodes.eventstream;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

public class Sample {

	private Directory rootDir;
	private File rootFolder;

	@Inject
	public Sample(@Named("contexts") Directory directory, @Named("rootDir") File folder) {
		assertTrue(folder.exists(), "Contexts folder does not exist");
		assertTrue(folder.isDirectory(), "Contexts is not a folder, but a file");
		this.rootDir = directory;
		this.rootFolder = folder;
	}

	public void sample() throws ZipException, IOException {
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
		List<Event> es = generator.getEventStream();
		// EventStream stream = EventsFilter.filter(es);
		// event, frequency
		Map<Event, Integer> occurrences = getFrequences(es);
		// frequency, number of events
		Map<Integer, Integer> freqCount = Maps.newHashMap();

		for (Map.Entry<Event, Integer> entry : occurrences.entrySet()) {
			if (freqCount.containsKey(entry.getValue())) {
				int eventCounter = freqCount.get(entry.getValue());
				freqCount.put(entry.getValue(), eventCounter + 1);
			} else {
				freqCount.put(entry.getValue(), 1);
			}
		}
		int totalStream = 0;
		int totalSample = 0;
		for (Map.Entry<Integer, Integer> entry : freqCount.entrySet()) {
			if (entry.getKey() > 1) {
				totalStream += entry.getKey() * entry.getValue();
				totalSample += entry.getKey();
			}
		}
		Logger.log("Sample / Stream = %d / %d ", totalSample, totalStream);

		// EventStreamIo.write(stream, getStreamPath(), getMappingPath());
		//
		// Logger.log("After filtering out one time events!");
		// Logger.log("Number of unique events is: %d",
		// stream.getEventNumber());
		// Logger.log("Length of event stream data is: %d",
		// stream.getStreamLength());
	}

	private Map<Event, Integer> getFrequences(List<Event> stream) {
		Map<Event, Integer> occurrences = Maps.newHashMap();

		for (Event e : stream) {
			if (occurrences.keySet().contains(e)) {
				int freq = occurrences.get(e);
				occurrences.put(e, freq + 1);
			} else {
				occurrences.put(e, 1);
			}
		}
		return occurrences;
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

	private String getStreamPath() {
		File streamFile = new File(rootFolder.getAbsolutePath() + "/eventStream.txt");
		return streamFile.getAbsolutePath();
	}

	private String getMappingPath() {
		File mappingFile = new File(rootFolder.getAbsolutePath() + "/eventMapping.txt");
		return mappingFile.getAbsolutePath();
	}
}
