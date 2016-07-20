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
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.episodes.model.Episode;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

public class PatternExtractor {

	public Map<Episode, List<IMethodName>> getMethodsFromCode(Map<Integer, Set<Episode>> processedPatterns,
			List<List<Fact>> stream, List<Event> events, boolean orderRelations) throws Exception {
		Map<Episode, List<IMethodName>> results = Maps.newHashMap();
		Set<Episode> patterns = getPatternsAsSet(processedPatterns);
		int numbMethods = 0;

		for (List<Fact> method : stream) {
			if (method.size() < 3) {
				continue;
			}
			for (Episode episode : patterns) {
				if (method.containsAll(episode.getEvents())) {
					IMethodName enclosingMethod = getEnclosingMethod(method, events);
					
					if (!orderRelations || respectOrderings(method, episode)) {
						if (results.containsKey(episode)) {
							results.get(episode).add(enclosingMethod);
						} else {
							results.put(episode, Lists.newArrayList(enclosingMethod));
						}
					}
				}
			}
			numbMethods++;
			Logger.log("Percentage of stream processed is %d/%d", numbMethods, stream.size());
		}
		return results;
	}

	private boolean respectOrderings(List<Fact> method, Episode episode) {
		Set<Fact> relations = episode.getRelations();
		for (Fact r : relations) {
			Tuple<Fact, Fact> tuple = r.getRelationFacts();
			if (method.indexOf(tuple.getFirst()) > method.indexOf(tuple.getSecond())) {
				return false;
			}
		}
		return true;
	}

	private Set<Episode> getPatternsAsSet(Map<Integer, Set<Episode>> processedPatterns) {
		Set<Episode> patterns = Sets.newHashSet();
		for (Map.Entry<Integer, Set<Episode>> entry : processedPatterns.entrySet()) {
			if (entry.getKey() == 1) {
				continue;
			}
			patterns.addAll(entry.getValue());
		}
		return patterns;
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
