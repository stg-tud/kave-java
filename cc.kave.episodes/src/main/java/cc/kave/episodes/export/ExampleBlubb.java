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
package cc.kave.episodes.export;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;

public class ExampleBlubb {

	private IMethodName enclosignMethod = MethodName.UNKNOWN_NAME;
	private IMethodName nextEnclosignMethod = MethodName.UNKNOWN_NAME;
	private boolean isFirstEnclosingMethod = true;
	
	public Set<IMethodName> findEnclosignMethods(List<Fact> stream, List<Event> eventMapping,
			Set<Fact> patternExistenceFacts) {

		List<Event> streamEvents = getStreamEvents(stream, eventMapping);

		Set<Event> patternEvents = getPatternsEvents(patternExistenceFacts, eventMapping);

		return findOccurences(streamEvents, patternEvents);
	}

	private Set<Event> getPatternsEvents(Set<Fact> PatternsExistenceFacts, List<Event> eventMapping) {
		Set<Event> result = Sets.newHashSet();
		for (Fact f : PatternsExistenceFacts) {
			Event e = eventMapping.get(f.getFactID());
			if ((e.getKind() == EventKind.FIRST_DECLARATION) || (e.getKind() == EventKind.INVOCATION)) {
				result.add(e);
			}
		}
		return result;
	}
	
	private Set<IMethodName> findOccurences(List<Event> streamEvents, Set<Event> patternEvents) {
		Set<IMethodName> results = Sets.newLinkedHashSet();
		Set<Event> completeMethod = Sets.newLinkedHashSet();
		
		for (Event event : streamEvents) {
			if (event.getKind() == EventKind.FIRST_DECLARATION) {
				completeMethod.add(event);
			}
			if (event.getKind() == EventKind.METHOD_DECLARATION) {
				if (isFirstEnclosingMethod) {
					enclosignMethod = event.getMethod();
					isFirstEnclosingMethod = false;
				} else {
					nextEnclosignMethod = event.getMethod();
					if (completeMethod.containsAll(streamEvents)) {
						results.add(enclosignMethod);
						completeMethod = Sets.newLinkedHashSet();
					}
					enclosignMethod = nextEnclosignMethod;
				}
			}
			if (event.getKind() == EventKind.INVOCATION) {
				completeMethod.add(event);
			}
		}
		return results;
	}

//	private Set<IMethodName> findOccurences(Iterable<Event> events, Set<IMethodName> calls) {
//		Set<IMethodName> result = Sets.newHashSet();
//		Iterator<Event> it = events.iterator();
//
//		while (it.hasNext()) {
//			Set<IMethodName> body = getBody(it);
//			if (!body.isEmpty() && body.containsAll(calls)) {
//				result.add(enclosignMethod);
//			}
//			enclosignMethod = nextEnclosignMethod;
//		}
//
//		return result;
//	}
//
//	private Set<IMethodName> getBody(Iterator<Event> it) {
//		Set<IMethodName> callsInBody = Sets.newHashSet();
//		while (it.hasNext()) {
//			Event cur = it.next();
//			if (cur.getKind() == EventKind.METHOD_DECLARATION) {
//				nextEnclosignMethod = cur.getMethod();
//				return callsInBody;
//			}
//			if (cur.getKind() == EventKind.INVOCATION) {
//				callsInBody.add(cur.getMethod());
//			}
//		}
//		nextEnclosignMethod = MethodName.UNKNOWN_NAME;
//		return callsInBody;
//	}

	private List<Event> getStreamEvents(List<Fact> stream, List<Event> eventMapping) {
		List<Event> events = Lists.newArrayList();
		for (Fact f : stream) {
			events.add(eventMapping.get(f.getFactID()));
		}
		return events;
	}
}