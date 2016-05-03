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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.statistics.StreamStatistics;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

public class StreamPartition {

	private Directory rootDir;
	private File rootFolder;

	@Inject
	public StreamPartition(@Named("contexts") Directory directory, @Named("rootDir") File folder) {
		assertTrue(folder.exists(), "Contexts folder does not exist");
		assertTrue(folder.isDirectory(), "Contexts is not a folder, but a file");
		this.rootDir = directory;
		this.rootFolder = folder;
	}

	public void partition() throws ZipException, IOException {
		EventStreamGenerator generator = new EventStreamGenerator();
		Set<Event> allEvents = getStream();
		String startPrefix = "";
		int partitionNo = 0;

		for (String zip : findZips()) {
			String zipName = zip.toString();

			if (startPrefix.equalsIgnoreCase("")) {
				partitionNo++;
				Logger.log("Partition %d", partitionNo);

				startPrefix = getPrefix(zipName);
			}
			if (!zipName.startsWith(startPrefix)) {
				partitionNo++;
				Logger.log("Partition %d", partitionNo);

				startPrefix = getPrefix(zipName);
				storeStream(generator, allEvents, partitionNo);

				generator = new EventStreamGenerator();
			}
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
		getNumberPartitions(partitionNo);
		Logger.log("Total number of partitions is: %d", partitionNo);
		
		storeStream(generator, allEvents, partitionNo);
	}

	private void storeStream(EventStreamGenerator generator, Set<Event> allEvents, int partitionNo) throws IOException {
		List<Event> stream = getPartitionStream(generator, allEvents);
		FilesPath path = getFiles(partitionNo);
		EventStreamIo.write(stream, path.streamFile, path.mappingFile);
	}

	public int getNumberPartitions(int partitions) {
		return partitions;
	}

	private List<Event> getPartitionStream(EventStreamGenerator generator, Set<Event> complStream) throws IOException {
		StreamStatistics statistics = new StreamStatistics();

		List<Event> es = generator.getEventStream();
		List<Event> fes = filterPartition(es, complStream);
		Map<Event, Integer> occurrences = statistics.getFrequences(fes);

		for (Map.Entry<Event, Integer> entry : occurrences.entrySet()) {
			if (entry.getValue() == 1) {
				Logger.log("%s", entry.getKey().getMethod().getName());
			}
		}
		return fes;
	}

	private String getPrefix(String zipName) {
		int index = zipName.indexOf("/", zipName.indexOf("/", zipName.indexOf("/") + 1) + 1);
		String startPrefix = zipName.substring(0, index);

		return startPrefix;
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

	private Set<Event> getStream() throws IOException {
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
		EventStream complStream = EventsFilter.filter(allEvents);

		return complStream.getMapping().keySet();
	}

	private List<Event> filterPartition(List<Event> partition, Set<Event> allEvents) throws IOException {
		List<Event> result = new LinkedList<Event>();

		for (Event event : partition) {
			if (allEvents.contains(event)) {
				result.add(event);
			}
		}
		return result;
	}

	private FilesPath getFiles(int number) {
		File folder = new File(rootFolder.getAbsolutePath() + "/partition" + number + "/");
		if (!folder.isDirectory()) {
			folder.mkdir();
		}
		File streamFile = new File(folder.getAbsolutePath() + "/eventStream.txt");
		File mappingFile = new File(folder.getAbsolutePath() + "/eventMapping.txt");

		FilesPath path = new FilesPath();
		path.streamFile = streamFile.getAbsolutePath();
		path.mappingFile = mappingFile.getAbsolutePath();

		return path;
	}

	private class FilesPath {
		private String streamFile;
		private String mappingFile;
	}
}
