package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EnclosingMethods;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PatternValidation {

	public Map<Integer, Set<Tuple<Episode, Integer>>> validate(
			Map<Integer, Set<Episode>> patterns,
			List<Tuple<Event, List<Fact>>> stream,
			Map<String, Set<IMethodName>> repoCtxMapper, List<Event> events)
			throws Exception {
		Map<Integer, Set<Tuple<Episode, Integer>>> results = Maps
				.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			Set<Tuple<Episode, Integer>> validations = Sets.newLinkedHashSet();

			for (Episode pattern : entry.getValue()) {
				int numReposOccur = getRepoOccs(pattern, stream,
						repoCtxMapper);
				validations.add(Tuple.newTuple(pattern, numReposOccur));
			}
			results.put(entry.getKey(), validations);
		}
		return results;
	}

	private int getRepoOccs(Episode pattern,
			List<Tuple<Event, List<Fact>>> stream,
			Map<String, Set<IMethodName>> repoCtxMapper) {

		EnclosingMethods methodsOrderRelation = new EnclosingMethods(true);

		for (Tuple<Event, List<Fact>> tuple : stream) {
			List<Fact> method = tuple.getSecond();
			if (method.size() < 2) {
				continue;
			}
			if (method.containsAll(pattern.getEvents())) {
				Event ctx = tuple.getFirst();
				methodsOrderRelation.addMethod(pattern, method, ctx);
			}
		}
		int numOccs = methodsOrderRelation.getOccurrences();
		assertTrue(numOccs != pattern.getFrequency(),
				"Frequency and numbOccs do not match!");

		Set<IMethodName> methodOcc = methodsOrderRelation
				.getMethodNames(numOccs);
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
