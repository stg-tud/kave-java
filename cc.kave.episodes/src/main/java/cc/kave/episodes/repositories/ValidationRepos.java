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
package cc.kave.episodes.repositories;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.episodes.export.EventStreamGenerator;
import cc.kave.episodes.mining.reader.FileReader;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

public class ValidationRepos {

	private Directory contextsDir;
	private File reposFolder;
	private FileReader reader;

	@Inject
	public ValidationRepos(@Named("contexts") Directory directory, @Named("events") File folder, FileReader reader) {
		assertTrue(folder.exists(), "Events folder does not exist");
		assertTrue(folder.isDirectory(), "Events is not a folder, but a file");
		this.contextsDir = directory;
		this.reposFolder = folder;
		this.reader = reader;
	}
	
	public List<Event> getOtherStream(int numbRepos) {
		List<String> learningRepos = reader.readFile(getReposPath(numbRepos));
		EventStreamGenerator generator = new EventStreamGenerator();
		
		for (String zip : findZips(contextsDir)) {
			
			Logger.log("Reading zip file %s", zip.toString());
		}
		return null;
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

	private File getReposPath(int numbRepos) {
		File pathName = new File(reposFolder.getAbsolutePath() + "/" + numbRepos + "Repos");
		if (!pathName.isDirectory()) {
			pathName.mkdir();
		}
		String fileName = pathName.getAbsolutePath() + "/repositories.txt";
		return new File(fileName);
	}
}
