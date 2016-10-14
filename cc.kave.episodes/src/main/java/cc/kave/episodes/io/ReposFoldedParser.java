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

import java.io.IOException;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.episodes.export.EventStreamGenerator;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ReposFoldedParser {

	private Directory contextsDir;

	@Inject
	public ReposFoldedParser(@Named("contexts") Directory directory) {
		this.contextsDir = directory;
	}

	public List<List<EventStreamGenerator>> generateFoldedEvents(int numFolds)
			throws IOException {
		List<EventStreamGenerator> generator = generateFoldedStream();
		List<List<EventStreamGenerator>> events = initializeFolds(numFolds);

		int i = 0;
		for (EventStreamGenerator stream : generator) {
			events.get(i).add(stream);
			i = (i + 1) % numFolds;
		}
		return events;
	}

	private List<List<EventStreamGenerator>> initializeFolds(int numFolds) {
		List<List<EventStreamGenerator>> events = Lists.newLinkedList();

		for (int i = 0; i < numFolds; i++) {
			events.add(Lists.newLinkedList());
		}
		return events;
	}

	private List<EventStreamGenerator> generateFoldedStream()
			throws IOException {
		List<EventStreamGenerator> reposStreams = Lists.newLinkedList();
		EventStreamGenerator generator = new EventStreamGenerator();
		String repoName = "";

		for (String zip : findZips(contextsDir)) {
			Logger.log("Reading zip file %s", zip.toString());
			if (repoName.equalsIgnoreCase("") || (!zip.startsWith(repoName))) {
				repoName = getRepoName(zip);
				if (!generator.getEventStream().isEmpty()) {
					reposStreams.add(generator);
					generator = new EventStreamGenerator();
				}
			}
			ReadingArchive ra = contextsDir.getReadingArchive(zip);
			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				if (ctx == null) {
					continue;
				}
				generator.add(ctx);
			}
			ra.close();
		}
		reposStreams.add(generator);
		return reposStreams;
	}

	private String getRepoName(String zipName) {
		int index = zipName.indexOf("/",
				zipName.indexOf("/", zipName.indexOf("/") + 1) + 1);
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
}
