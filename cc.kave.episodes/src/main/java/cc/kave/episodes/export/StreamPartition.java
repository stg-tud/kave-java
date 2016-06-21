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
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.episodes.model.EventStream;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

public class StreamPartition {

	private Directory rootDir;
	private File rootFolder;
	
	private static final int REMFREQS = 1;

	@Inject
	public StreamPartition(@Named("contexts") Directory directory, @Named("rootDir") File folder) {
		assertTrue(folder.exists(), "Contexts folder does not exist");
		assertTrue(folder.isDirectory(), "Contexts is not a folder, but a file");
		this.rootDir = directory;
		this.rootFolder = folder;
	}
	
	private static String REPO = "";

	public Map<String, Integer> partition() throws ZipException, IOException {
		Map<String, Integer> mapping = Maps.newLinkedHashMap();
		EventStreamGenerator generator = new EventStreamGenerator();
		Map<Event, Integer> allEvents = getStream();
		
		String zipName = "";
		int partitionNo = 0;

		for (String zip : findZips()) {
			zipName = zip.toString();

			if (REPO.equalsIgnoreCase("")) {
				partitionNo++;
				Logger.log("Partition %d", partitionNo);

				REPO = getRepoName(zipName);
			}
			if (!zipName.startsWith(REPO)) {
				mapping.put(REPO, partitionNo);
				REPO = getRepoName(zipName);
				storeStream(generator, allEvents, partitionNo);
				
				partitionNo++;
				Logger.log("Partition %d", partitionNo);
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
		Logger.log("Total number of partitions is: %d", partitionNo);
		storeStream(generator, allEvents, partitionNo);
		mapping.put(REPO, partitionNo);
		return mapping;
	}

	private void storeStream(EventStreamGenerator generator, Map<Event, Integer> allEvents, int partitionNo) throws IOException {
		String partitionStream = getPartitionStream(generator, allEvents);
		FileUtils.writeStringToFile(new File(getPartitionPath(partitionNo)), partitionStream);
	}

	private String getPartitionStream(EventStreamGenerator generator, Map<Event, Integer> complStream) throws IOException {
		List<Event> es = generator.getEventStream();
		String partition = EventsFilter.filterPartition(es, complStream);

		return partition;
	}

	private String getRepoName(String zipName) {
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

	private Map<Event, Integer> getStream() throws IOException {
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
		EventStream complStream = EventsFilter.filterStream(allEvents, REMFREQS);
		EventStreamIo.write(complStream, getStreamPath(), getMappingPath());

		return complStream.getMapping();
	}

	private String getStreamPath() {
		File folder = new File(rootFolder.getAbsolutePath() + "/patterns/");
		if (!folder.isDirectory()) {
			folder.mkdir();
		}
		File streamFile = new File(folder.getAbsolutePath() + "/eventStream.txt");
		return streamFile.getAbsolutePath();
	}

	private String getMappingPath() {
		File folder = new File(rootFolder.getAbsolutePath() + "/patterns/");
		if (!folder.isDirectory()) {
			folder.mkdir();
		}
		File mappingFile = new File(folder.getAbsolutePath() + "/eventMapping.txt");
		return mappingFile.getAbsolutePath();
	}

	private String getPartitionPath(int number) {
		File folder = new File(rootFolder.getAbsolutePath() + "/patterns/partitions/");
		if (!folder.isDirectory()) {
			folder.mkdirs();
		}
		File partitionFile = new File(folder.getAbsolutePath() + "/eventStream" + number + ".txt");

		return partitionFile.getAbsolutePath();
	}
}
