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
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.eventstream.EventStreamGenerator;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class IndivReposParser {
	
	private Directory contextsDir;
	
	@Inject
	public IndivReposParser(@Named("contexts") Directory directory) {
		this.contextsDir = directory;
	}
	
	public Map<String, EventStreamGenerator> generateReposEvents() throws ZipException, IOException {
		EventStreamGenerator repoGen = new EventStreamGenerator();
		Map<String, EventStreamGenerator> allEvents = Maps.newLinkedHashMap();
		String repoName = "";

		for (String zip : findZips(contextsDir)) {
			Logger.log("Reading zip file %s", zip.toString());
			if ((repoName.equalsIgnoreCase("")) || (!zip.startsWith(repoName))) {
				if (!repoGen.getEventStream().isEmpty()) {
					allEvents.put(repoName, repoGen);
					repoGen = new EventStreamGenerator();
				}
				repoName = getRepoName(zip);
			} 
			ReadingArchive ra = contextsDir.getReadingArchive(zip);

			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				if (ctx == null) {
					continue;
				}
				repoGen.add(ctx);
			}
			ra.close();
		}
		allEvents.put(repoName, repoGen);
		return allEvents;
	}
	
	public Map<String, Set<IMethodName>> getRepoCtxMapper()
			throws ZipException, IOException {
		Map<String, Set<IMethodName>> results = Maps.newLinkedHashMap();

		Map<String, EventStreamGenerator> repoCtxs = generateReposEvents();

		for (Map.Entry<String, EventStreamGenerator> entry : repoCtxs
				.entrySet()) {
			List<Event> events = entry.getValue().getEventStream();
			Set<IMethodName> ctx = Sets.newLinkedHashSet();

			for (Event e : events) {
				if (e.getKind() == EventKind.METHOD_DECLARATION) {
					ctx.add(e.getMethod());
				}
			}
			results.put(entry.getKey(), ctx);
		}
		repoCtxs.clear();
		return results;
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
}
