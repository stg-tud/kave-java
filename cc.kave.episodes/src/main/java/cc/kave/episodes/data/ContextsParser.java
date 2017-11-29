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
package cc.kave.episodes.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.eventstream.Filters;
import cc.kave.episodes.eventstream.StreamFilterGenerator;
import cc.kave.episodes.eventstream.StreamRepoGenerator;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.statistics.StreamStatistics;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ContextsParser {

	private Directory contextsDir;

	@Inject
	public ContextsParser(@Named("contexts") Directory directory) {
		this.contextsDir = directory;
	}

	private Map<String, Set<IMethodName>> repoDecls = Maps.newLinkedHashMap();

	private List<Tuple<Event, List<Event>>> streamRepos = Lists.newLinkedList();
	private List<Tuple<Event, List<Event>>> streamFilters = Lists
			.newLinkedList();
	private Filters filters = new Filters();

	StreamStatistics statRepos = new StreamStatistics();
	StreamStatistics statGenerated = new StreamStatistics();
	StreamStatistics statUnknowns = new StreamStatistics();
	StreamStatistics statLocals = new StreamStatistics();
	StreamStatistics statOverlaps = new StreamStatistics();
	StreamStatistics statFrequent = new StreamStatistics();

	public List<Tuple<Event, List<Event>>> parse(int frequency)
			throws Exception {
		StreamRepoGenerator eventStreamRepo = new StreamRepoGenerator() {
		};
		StreamFilterGenerator eventStreamFilter = new StreamFilterGenerator();
		String repoName = "";

		for (String zip : findZips(contextsDir)) {
			Logger.log("Reading zip file %s", zip.toString());
			if (repoName.isEmpty() || !zip.startsWith(repoName)) {
				streamRepos.addAll(processStreamRepo(eventStreamRepo
						.getEventStream()));
				List<Tuple<Event, List<Event>>> partStream = processStreamFilter(eventStreamFilter
						.getEventStream());
				saveRepoCtx(repoName, partStream);
				streamFilters.addAll(partStream);

				eventStreamRepo.getEventStream().clear();
				eventStreamRepo = new StreamRepoGenerator() {
				};
				eventStreamFilter.getEventStream().clear();
				eventStreamFilter = new StreamFilterGenerator();
				repoName = getRepoName(zip);
			}
			ReadingArchive ra = contextsDir.getReadingArchive(zip);
			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				eventStreamRepo.add(ctx);
				eventStreamFilter.add(ctx);
			}
			ra.close();
		}
		streamRepos.addAll(processStreamRepo(eventStreamRepo.getEventStream()));
		List<Tuple<Event, List<Event>>> partStream = processStreamFilter(eventStreamFilter
				.getEventStream());
		saveRepoCtx(repoName, partStream);
		streamFilters.addAll(partStream);
		List<Tuple<Event, List<Event>>> streamFreq = filters.freqEvents(
				streamFilters, frequency);
		statFrequent.addStats(streamFreq);

		printStats();
		repoNumMethods();
		checkElementCtxs(streamFreq);
		
		return streamFreq;
	}

	public Map<String, Set<IMethodName>> getRepoCtxMapper() {
		return repoDecls;
	}

	private void checkElementCtxs(List<Tuple<Event, List<Event>>> stream) {
		Set<IMethodName> ctxsStream = Sets.newHashSet();
		for (Tuple<Event, List<Event>> tuple : stream) {
			ctxsStream.add(tuple.getFirst().getMethod());
		}
		Set<IMethodName> ctxsRepos = Sets.newHashSet();
		for (Map.Entry<String, Set<IMethodName>> entry : repoDecls.entrySet()) {
			ctxsRepos.addAll(entry.getValue());
		}
	}

	private void saveRepoCtx(String repoName,
			List<Tuple<Event, List<Event>>> stream) {
		if (repoName.isEmpty() && stream.isEmpty()) {
			return;
		}
		Set<IMethodName> methodNames = Sets.newHashSet();

		for (Tuple<Event, List<Event>> tuple : stream) {
			methodNames.add(tuple.getFirst().getMethod());
		}
		repoDecls.put(repoName, methodNames);
	}

	private void repoNumMethods() {
		Logger.log("\tRepository statistics:");
		Logger.log("\tRepository\tnumMethods");
		for (Map.Entry<String, Set<IMethodName>> entry : repoDecls.entrySet()) {
			Logger.log("\t%s\t%d", entry.getKey(), entry.getValue().size());
		}
	}

	private List<Tuple<Event, List<Event>>> processStreamRepo(
			List<Event> eventStream) {
		List<Tuple<Event, List<Event>>> streamStruct = filters
				.getStructStream(eventStream);
		statRepos.addStats(streamStruct);

		return streamStruct;
	}

	private List<Tuple<Event, List<Event>>> processStreamFilter(
			List<Event> eventStream) {
		List<Tuple<Event, List<Event>>> streamStruct = filters
				.getStructStream(eventStream);
		statGenerated.addStats(streamStruct);

		List<Tuple<Event, List<Event>>> streamUnknowns = filters
				.unknowns(streamStruct);
		statUnknowns.addStats(streamUnknowns);

		List<Tuple<Event, List<Event>>> streamLocals = filters
				.locals(streamUnknowns);
		statLocals.addStats(streamLocals);

		List<Tuple<Event, List<Event>>> streamOverlaps = filters
				.overlaps(streamLocals);
		statOverlaps.addStats(streamOverlaps);

		streamStruct.clear();
		streamUnknowns.clear();
		streamLocals.clear();
		
		return streamOverlaps;
	}

	private void printStats() {
		Logger.log("Number of repositories: %d", repoDecls.size());
		Logger.log("------");

		Logger.log("Repository statistics ...");
		statRepos.printStats();

		Logger.log("Filter generated code ...");
		statGenerated.printStats();

		Logger.log("Filter unknown events ...");
		statUnknowns.printStats();

		Logger.log("Filter local events ...");
		statLocals.printStats();

		Logger.log("Filter overlapping methods ...");
		statOverlaps.printStats();

		Logger.log("Filter infrequent events ...");
		statFrequent.printStats();
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
