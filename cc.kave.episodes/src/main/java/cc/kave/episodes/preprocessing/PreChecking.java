package cc.kave.episodes.preprocessing;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.episodes.eventstream.EventStreamGenerator;
import cc.kave.episodes.eventstream.EventsFilter;
import cc.kave.episodes.io.RepositoriesParser;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class PreChecking {

	private RepositoriesParser reposParser;

	@Inject
	public PreChecking(RepositoriesParser parser) {
		this.reposParser = parser;
	}

	public void reposInfo() throws Exception {
		reposParser.generateReposEvents();
		Tuple<Integer, Integer> ctxOverlaps = reposParser.getReposInfo();

		Logger.log("Unique contexts %d, duplicated contexts %d!",
				ctxOverlaps.getFirst(), ctxOverlaps.getSecond());
	}

	public void typeOverlaps() throws Exception {
		reposParser.generateReposEvents();
		Map<String, Set<ITypeName>> reposTypesMapper = reposParser
				.getRepoTypesMapper();
		Set<ITypeName> types = Sets.newLinkedHashSet();

		for (Map.Entry<String, Set<ITypeName>> entry : reposTypesMapper
				.entrySet()) {
			int numTypes = entry.getValue().size();

			if (entry.getValue().size() > 1) {
				Logger.log("Repository %s contains %d types!", entry.getKey(),
						numTypes);
			}
			for (ITypeName t : entry.getValue()) {

				if (types.contains(t)) {
					throw new Exception(
							"Type already exists in the repositories!");
				} else {
					types.add(t);
				}
			}
		}
		Logger.log("There are no duplicated types in the repositories!");
	}

	public void methodsOverlap() throws Exception {
		Logger.log("Parsing repositories ...");
		Map<String, EventStreamGenerator> repos = reposParser
				.generateReposEvents();

		Logger.log("Completed repositories parsing!");
		List<Event> events = Lists.newLinkedList();

		Logger.log("Getting stream data ...");
		for (Map.Entry<String, EventStreamGenerator> entry : repos.entrySet()) {
			events.addAll(entry.getValue().getEventStream());
		}
		EventStream eventStream = EventsFilter.filterStream(events, 400);

		List<Tuple<Event, String>> streamData = eventStream.getStreamData();
		Set<Tuple<Event, String>> checkStream = Sets.newLinkedHashSet();

		for (Tuple<Event, String> method : streamData) {
			assertTrue(
					method.getFirst().getKind() == EventKind.METHOD_DECLARATION,
					"Method context is not method element!");
			assertTrue(!checkStream.contains(method),
					"Duplicate code in event stream!");
			checkStream.add(method);
		}
		Logger.log("Stream data size: %d - %d", streamData.size(),
				checkStream.size());
	}
}
