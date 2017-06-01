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

import org.apache.commons.io.FileUtils;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.statistics.StreamStatistics;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class EventsFrequencies {

	private Directory rootDir;
	private File rootFolder;
	private StreamStatistics statistics;

	@Inject
	public EventsFrequencies(@Named("contexts") Directory directory, @Named("statistics") File folder,
			StreamStatistics statistics) {
		assertTrue(folder.exists(), "Contexts folder does not exist");
		assertTrue(folder.isDirectory(), "Contexts is not a folder, but a file");
		this.rootDir = directory;
		this.rootFolder = folder;
		this.statistics = statistics;
	}

	public void frequencies() throws ZipException, IOException {
		EventStreamNotGenerated generator = new EventStreamNotGenerated();

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
		Map<Event, Integer> freqs = statistics.getFrequencies(es);
		getOutlierEvent(freqs);
		
//		Map<Integer, Integer> distr = statistics.getFreqDistr(freqs);
//		storeFreqs(freqs);
//		storeDistr(distr);
	}

	private void getOutlierEvent(Map<Event, Integer> freqs) {
		for (Map.Entry<Event, Integer> entry : freqs.entrySet()) {
			if (entry.getValue() == 1390000) {
				Logger.log("%s", entry.getKey().getMethod().getDeclaringType().toString());
				break;
			}
		}
		
	}

	private void storeFreqs(Map<Event, Integer> occurrences) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		for (Map.Entry<Event, Integer> entry : occurrences.entrySet()) {
			sb.append(entry.getValue());
			sb.append("\n");
		}
		FileUtils.writeStringToFile(new File(getFreqsFile()), sb.toString());
	}
	
	private void storeDistr(Map<Integer, Integer> distributions) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("Frequency\t#Events\n");
		
		for (Map.Entry<Integer, Integer> entry : distributions.entrySet()) {
			sb.append(entry.getKey());
			sb.append("\t");
			sb.append(entry.getValue());
			sb.append("\n");
		}
		FileUtils.writeStringToFile(new File(getDistrFile()), sb.toString());
	}

	private String getFreqsFile() {
		File freqsFile = new File(rootFolder.getAbsolutePath() + "/frequences.txt");
		return freqsFile.getAbsolutePath();
	}
	
	private String getDistrFile() {
		File distrFile = new File(rootFolder.getAbsolutePath() + "/freqsDistr.txt");
		return distrFile.getAbsolutePath();
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
