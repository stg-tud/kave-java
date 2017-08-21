package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.Triplet;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EnclosingMethods;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PatternsValidationFolded {

	public Map<Integer, Set<Triplet<Episode, Integer, Integer>>> validate(
			Map<Integer, Set<Episode>> episodes,
			List<Tuple<Event, List<Fact>>> streamContexts,
			Map<String, Set<IMethodName>> repoCtxMapper, List<Event> events,
			List<List<Fact>> valStream) throws Exception {
		Map<Integer, Set<Triplet<Episode, Integer, Integer>>> results = Maps
				.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entry : episodes.entrySet()) {
			Set<Triplet<Episode, Integer, Integer>> pattsVal = Sets
					.newLinkedHashSet();
			for (Episode episode : entry.getValue()) {
				int numReposOccur = getReposOcc(episode, streamContexts,
						repoCtxMapper);
				int valOccs = getValOcc(episode, events, valStream);

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
			Map<String, Set<IMethodName>> repoCtxMapper) {

		EnclosingMethods methodsOrderRelation = new EnclosingMethods(true);

		for (Tuple<Event, List<Fact>> tuple : streamContexts) {
			List<Fact> method = tuple.getSecond();
			if (method.size() < 2) {
				continue;
			}
			if (method.containsAll(episode.getEvents())) {
				Event ctx = tuple.getFirst();
				methodsOrderRelation.addMethod(episode, method, ctx);
			}
		}
		int trainOcc = methodsOrderRelation.getOccurrences();
		assertTrue(trainOcc >= episode.getFrequency(),
				"Episode is not found sufficient number of times in the Training Data!");

		Set<IMethodName> methodOcc = methodsOrderRelation.getMethodNames(trainOcc);
		Set<String> repositories = Sets.newLinkedHashSet();

		for (Map.Entry<String, Set<IMethodName>> entry : repoCtxMapper
				.entrySet()) {
			for (IMethodName methodName : entry.getValue()) {
				if (methodOcc.contains(methodName)) {
					repositories.add(entry.getKey());
					break;
				}
			}
		}
		return repositories.size();
	}
}
