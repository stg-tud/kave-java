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
import java.util.Set;
import java.util.zip.ZipException;

import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.events.completionevents.Context;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

public class Preprocessing {

	private Directory rootDir;
	private File rootFolder;

	@Inject
	public Preprocessing(@Named("contexts") Directory directory, @Named("rootDir") File folder) {
		assertTrue(folder.exists(), "Contexts folder does not exist");
		assertTrue(folder.isDirectory(), "Contexts is not a folder, but a file");
		this.rootDir = directory;
		this.rootFolder = folder;
	}

	public void readAllContexts() throws ZipException, IOException {
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
		Logger.log("Getting the event stream data!");
		List<Event> es = generator.getEventStream();
		Logger.log("Writing event stream and event mapping files!");
		EventStreamIo.write(es, getStreamPath(), getMappingPath());
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
