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
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.utils.TypeErasure;
import cc.kave.episodes.eventstream.Filters;
import cc.kave.episodes.eventstream.Statistics;
import cc.kave.episodes.eventstream.StreamFilterGenerator;
import cc.kave.episodes.eventstream.StreamRepoGenerator;
import cc.kave.episodes.model.events.Event;
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
	private Filters filters;

	@Inject
	public ContextsParser(@Named("contexts") Directory directory,
			Filters filters) {
		this.contextsDir = directory;
		this.filters = filters;
	}

	private Map<String, Set<IMethodName>> repoDecls = Maps.newLinkedHashMap();
	private Set<IMethodName> seenMethods = Sets.newHashSet();

	private List<Tuple<Event, List<Event>>> streamRepos = Lists.newLinkedList();
	private List<Tuple<Event, List<Event>>> streamFilters = Lists
			.newLinkedList();

	Statistics statRepos = new Statistics();
	Statistics statGenerated = new Statistics();
	Statistics statUnknowns = new Statistics();
	Statistics statLocals = new Statistics();
	Statistics statOverlaps = new Statistics();

	public List<Tuple<Event, List<Event>>> parse() throws Exception {
		StreamRepoGenerator eventStreamRepo = new StreamRepoGenerator() {
		};
		StreamFilterGenerator eventStreamFilter = new StreamFilterGenerator();
		String repoName = "";

		for (String zip : findZips(contextsDir)) {
			Logger.log("Reading zip file %s", zip.toString());
			if (repoName.isEmpty() || !zip.startsWith(repoName)) {
				repoName = getRepoName(zip);
				repoDecls.put(repoName, Sets.newHashSet());

				streamRepos.addAll(processStreamRepo(eventStreamRepo
						.getEventStream()));
				streamFilters.addAll(processStreamFilter(eventStreamFilter
						.getEventStream()));

				eventStreamRepo = new StreamRepoGenerator() {
				};
				eventStreamFilter = new StreamFilterGenerator();
			}
			ReadingArchive ra = contextsDir.getReadingArchive(zip);
			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				saveElementCtx(repoName, ctx);
				eventStreamRepo.add(ctx);
				eventStreamFilter.add(ctx);
			}
			ra.close();
		}
		streamRepos.addAll(processStreamRepo(eventStreamRepo.getEventStream()));
		streamFilters.addAll(processStreamFilter(eventStreamFilter
				.getEventStream()));
		printStats();
		checkForEmptyRepos();
		checkElementCtxs(streamFilters);
		return streamFilters;
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
		System.out.println("Number of ctxs in stream: " + ctxsStream.size());
		System.out.println("Number of ctxs in repos: " + ctxsRepos.size());
		System.out.println("Repo-ctx mapper includes all stream ctx? "
				+ ctxsRepos.containsAll(ctxsStream));
	}

	private void saveElementCtx(String repoName, Context ctx) {
		Set<IMethodName> methods = Sets.newHashSet();

		ISST sst = ctx.getSST();
		Set<IMethodDeclaration> decls = sst.getMethods();
		for (IMethodDeclaration d : decls) {
			IMethodName name = d.getName();
			if (seenMethods.add(name)) {
				methods.add(TypeErasure.of(name));
			}
		}
		repoDecls.get(repoName).addAll(methods);
	}

	private void checkForEmptyRepos() {
		int numbEmptyRepos = 0;

		for (Map.Entry<String, Set<IMethodName>> entry : repoDecls.entrySet()) {
			if (entry.getValue().isEmpty()) {
				Logger.log("Empty repository: %s", entry.getKey());
				numbEmptyRepos++;
			}
		}
		Logger.log("------");
		Logger.log("Empty repositories counted: %d\n", numbEmptyRepos);
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

		return streamOverlaps;
	}

	private void printStats() {
		Logger.log("Number of repositories: %d", repoDecls.size());
		Logger.log("------");

		Logger.log("Repository statistics ...");
		statRepos.printStats();
		Logger.log("");

		Logger.log("Filter generated code ...");
		statGenerated.printStats();
		Logger.log("");

		Logger.log("Filter unknown events ...");
		statUnknowns.printStats();
		Logger.log("");

		Logger.log("Filter local events ...");
		statLocals.printStats();

		Logger.log("Filter overlapping methods ...");
		statOverlaps.printStats();
		Logger.log("");
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
