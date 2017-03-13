package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.RepositoriesParser;
import cc.kave.episodes.io.ValidationDataIO;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.Triplet;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EnclosingMethods;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class PatternsValidation {

	private EventStreamIo eventStream;
	private ValidationDataIO validationIO;
	private RepositoriesParser repoParser;

	@Inject
	public PatternsValidation(EventStreamIo eventStream,
			ValidationDataIO validationIO, RepositoriesParser reposParser) {
		this.eventStream = eventStream;
		this.validationIO = validationIO;
		this.repoParser = reposParser;
	}

	public Map<Integer, Set<Triplet<Episode, Integer, Integer>>> validate(
			Map<Integer, Set<Episode>> episodes, int frequency, int foldNum)
			throws Exception {
		Map<Integer, Set<Triplet<Episode, Integer, Integer>>> results = Maps
				.newLinkedHashMap();

		Logger.log("Reading events ...");
		List<Event> trainEvents = eventStream.readMapping(frequency, foldNum);
		Logger.log("Reading training stream ...");
		List<Tuple<Event, List<Fact>>> streamContexts = eventStream
				.parseStream(frequency, foldNum);

		Logger.log("Reading repositories - enclosing method declarations mapper!");
		repoParser.generateReposEvents();
		Map<String, Set<ITypeName>> repoCtxMapper = repoParser
				.getRepoTypesMapper();

		Logger.log("Reading validation data ...");
		List<Event> valData = validationIO.read(frequency, foldNum);
		Map<Event, Integer> eventsMap = mergeEventsToMap(trainEvents, valData);
		List<Event> eventsList = Lists.newArrayList(eventsMap.keySet());
		List<List<Fact>> valStream = validationIO.streamOfFacts(valData,
				eventsMap);

		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			if (entry.getKey() < 2) {
				continue;
			}
			Set<Triplet<Episode, Integer, Integer>> pattsVal = Sets
					.newLinkedHashSet();
			Logger.log("Processing episodes with %d-nodes ...", entry.getKey());
			for (Episode episode : entry.getValue()) {
				int numReposOccur = getReposOcc(episode, streamContexts,
						repoCtxMapper);
				int valOccs = getValOcc(episode, eventsList, valStream);

				pattsVal.add(new Triplet<Episode, Integer, Integer>(episode,
						numReposOccur, valOccs));
			}
			results.put(entry.getKey(), pattsVal);
		}
		return results;
	}

	private int getValOcc(Episode episode, List<Event> eventsList,
			List<List<Fact>> valStream) {

		EnclosingMethods enclMethods = new EnclosingMethods(true);

		for (List<Fact> method : valStream) {
			if (method.size() < 2) {
				continue;
			}
			if (method.containsAll(episode.getEvents())) {
				Event methodCtx = getMethodName(method, eventsList);
				enclMethods.addMethod(episode, method, methodCtx);
			}
		}
		return enclMethods.getOccurrences();
	}

	private Event getMethodName(List<Fact> method, List<Event> events) {
		for (Fact fact : method) {
			Event event = events.get(fact.getFactID());
			if (event.getKind() == EventKind.METHOD_DECLARATION) {
				return event;
			}
		}
		return null;
	}

	private int getReposOcc(Episode episode,
			List<Tuple<Event, List<Fact>>> streamContexts,
			Map<String, Set<ITypeName>> repoCtxMapper) {

		EnclosingMethods methodsOrderRelation = new EnclosingMethods(true);

		for (Tuple<Event, List<Fact>> tuple : streamContexts) {
			List<Fact> method = tuple.getSecond();
			if (method.size() < 2) {
				continue;
			}
			if (method.containsAll(episode.getEvents())) {
				methodsOrderRelation.addMethod(episode, method,
						tuple.getFirst());
			}
		}
		int trainOcc = methodsOrderRelation.getOccurrences();
		assertTrue(trainOcc >= episode.getFrequency(),
				"Episode is not found sufficient number of times in the Training Data!");

		Set<ITypeName> methodOcc = methodsOrderRelation.getTypeNames(trainOcc);
		Set<String> repositories = Sets.newLinkedHashSet();

		for (Map.Entry<String, Set<ITypeName>> entry : repoCtxMapper.entrySet()) {
			for (ITypeName methodName : entry.getValue()) {
				if (methodOcc.contains(methodName)) {
					repositories.add(entry.getKey());
					break;
				}
			}
		}
		return repositories.size();
	}

	private Map<Event, Integer> mergeEventsToMap(List<Event> lista,
			List<Event> listb) {
		Map<Event, Integer> events = Maps.newLinkedHashMap();
		int id = 0;

		for (Event event : lista) {
			events.put(event, id);
			id++;
		}
		for (Event event : listb) {
			if (!events.containsKey(event)) {
				events.put(event, id);
				id++;
			}
		}
		return events;
	}
}
