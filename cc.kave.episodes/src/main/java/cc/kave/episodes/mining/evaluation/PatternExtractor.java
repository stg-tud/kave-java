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
package cc.kave.episodes.mining.evaluation;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.episodes.model.Episode;

public class PatternExtractor {

	public Set<IMethodName> getMethods(Episode episode, List<List<Fact>> stream, List<Event> events) {
		Set<IMethodName> enclosingMethods = Sets.newLinkedHashSet();
		Set<Fact> episodeFacts = episode.getEvents();

		for (List<Fact> method : stream) {
			if (method.containsAll(episodeFacts)) {
				IMethodName methodName = getEnclosingMethod(method, events);
				if (methodName != null) {
					enclosingMethods.add(methodName);
				}
			}
		}
		return enclosingMethods;
	}

	private IMethodName getEnclosingMethod(List<Fact> method, List<Event> events) {
		List<Event> methodEvents = toEvents(method, events);
		for (Event e : methodEvents) { 
			if (e.getKind() == EventKind.METHOD_DECLARATION) {
				return e.getMethod();
			}
		}
		return null;
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
