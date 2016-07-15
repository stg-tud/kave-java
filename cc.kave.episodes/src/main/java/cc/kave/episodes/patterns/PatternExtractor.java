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
package cc.kave.episodes.patterns;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.episodes.model.Episode;
import cc.recommenders.datastructures.Tuple;

public class PatternExtractor {

	public Tuple<Set<IMethodName>, Integer> getMethodsFromCode(Episode episode, List<List<Fact>> stream, List<Event> events,
			boolean orderRelations) throws Exception {
		Set<IMethodName> enclosingMethods = Sets.newHashSet();
		int counter = 0;
		List<List<Fact>> episodeOccurrences = getMethodsOccurrences(episode, stream, orderRelations);

		for (List<Fact> method : episodeOccurrences) {
			IMethodName methodName = getEnclosingMethod(method, events);
			if (methodName != null) {
				enclosingMethods.add(methodName);
				counter++;
			}
		}
		return Tuple.newTuple(enclosingMethods, counter);
	}

	private List<List<Fact>> getMethodsOccurrences(Episode episode, List<List<Fact>> stream, boolean ordering) {
		List<List<Fact>> methodsOccurrences = new LinkedList<>();
		Set<Fact> episodeFacts = episode.getEvents();

		for (List<Fact> method : stream) {
			if (method.containsAll(episodeFacts)) {
				methodsOccurrences.add(method);
			}
		}
		if (ordering) { 
			return getMethodWithOrderings(episode, methodsOccurrences);
		}
		return methodsOccurrences;
	}

	private List<List<Fact>> getMethodWithOrderings(Episode episode, List<List<Fact>> allMethods) {
		List<List<Fact>> methodsWithOrdering = new LinkedList<>();
		Set<Fact> relations = episode.getRelations();
		boolean validOrder = true;

		for (List<Fact> method : allMethods) {
			for (Fact r : relations) {
				Tuple<Fact, Fact> tuple = r.getRelationFacts();
				if (method.indexOf(tuple.getFirst()) > method.indexOf(tuple.getSecond())) {
					validOrder = false;
					break;
				}
			}
			if (validOrder) {
				methodsWithOrdering.add(method);
			}
		}
		return methodsWithOrdering;
	}

	private IMethodName getEnclosingMethod(List<Fact> method, List<Event> events) throws Exception {
		List<Event> methodEvents = toEvents(method, events);
		for (Event e : methodEvents) {
			if (e.getKind() == EventKind.METHOD_DECLARATION) {
				return e.getMethod();
			}
		}
		throw new Exception("Method does not have enclosing method!");
	}

	private List<Event> toEvents(List<Fact> method, List<Event> events) {
		List<Event> methodEvents = new LinkedList<Event>();
		for (Fact fact : method) {
			Event event = events.get(fact.getFactID());
			methodEvents.add(event);
		}
		return methodEvents;
	}
}
