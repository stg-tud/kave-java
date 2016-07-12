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

import java.util.List;
import java.util.Set;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.episodes.model.Episode;

public class EpisodeExtraction {

	public StringBuilder getMethods(Episode episode, Set<Set<Fact>> stream, List<Event> events) {
		StringBuilder sb = new StringBuilder();
		Set<Fact> episodeFacts = episode.getEvents();

		for (Set<Fact> streamFacts : stream) {
			if (streamFacts.containsAll(episodeFacts)) {
				String methodName = getSuperMethod(streamFacts, events);
				if (!methodName.equals("")) {
					sb.append(methodName + "\n");
				}
			}
		}
		return sb;
	}

	private String getSuperMethod(Set<Fact> streamFacts, List<Event> events) {
		String firstMethod = "";
		String method = "";
		for (Fact fact : streamFacts) {
			int eventID = fact.getFactID();
			Event event = events.get(eventID);
			if (event.getKind() == EventKind.SUPER_DECLARATION) {
				String superMethod = toLabel(event);
				return superMethod;
			}
			if (event.getKind() == EventKind.FIRST_DECLARATION) {
				if (!event.getMethod().equals(MethodName.UNKNOWN_NAME)) { 
					firstMethod = toLabel(event);
				}
			}
			if (event.getKind() == EventKind.METHOD_DECLARATION) {
				
			}
		}
		return null;
	}

	private String toLabel(Event event) {
		StringBuilder sb = new StringBuilder();
		IMethodName method = event.getMethod();
		EventKind kind = event.getKind();
		
		sb.append(kind + ": ");
		sb.append(method.getDeclaringType().getFullName());
		sb.append('.');
		sb.append(method.getName());
		return sb.toString();
	}
}
