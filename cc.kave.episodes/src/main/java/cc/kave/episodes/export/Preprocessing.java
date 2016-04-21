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
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.events.completionevents.Context;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

public class Preprocessing {

	private File rootDir;

	@Inject
	public Preprocessing(@Named("rootDir") File directory) {
		assertTrue(directory.exists(), "Contexts folder does not exist");
		assertTrue(directory.isDirectory(), "Contexts is not a folder, but a file");
		this.rootDir = directory;
	}

	public void readAllContexts() throws ZipException, IOException {
		List<File> zips = findAllZips(getContextsPath());
		
		for (File zip : zips) {
			Logger.log("Writing zip file %s\n", zip.toString());
			ReadingArchive ra = new ReadingArchive(zip);
			EventStreamGenerator generator = new EventStreamGenerator();
			
			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				System.out.println(ctx.toString());
				generator.add(ctx);
			}
			ra.close();
			List<Event> es = generator.getEventStream();
			EventStreamIo.write(es, getStreamPath(), getMappingPath());
		}
	}

	private List<File> findAllZips(String dir) {
		List<File> zips = Lists.newLinkedList();
		for (File f : FileUtils.listFiles(new File(dir), new String[] { "zip" }, true)) {
			zips.add(f);
		}
		return zips;
	}

	private String getContextsPath() {
		String path = rootDir.getAbsolutePath() + "/dataSet/SST/Github/";
		return path;
	}
	
	private String getStreamPath() {
		File streamFile = new File(rootDir.getAbsolutePath() + "/dataSet/eventStream.txt");
		return streamFile.getAbsolutePath();
	}
	
	private String getMappingPath() {
		File mappingFile = new File(rootDir.getAbsolutePath() + "/dataSet/eventStream.txt");
		return mappingFile.getAbsolutePath();
	}
}
