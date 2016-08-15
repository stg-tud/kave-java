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
package cc.kave.episodes.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.recommenders.datastructures.Tuple;

public class EnclosingMethods {
	
	private boolean order;
	private Map<IMethodName, Integer> methods = Maps.newLinkedHashMap();
	
	@Inject
	public EnclosingMethods(boolean order) {
		this.order = order;
	}

	public void addMethod(Episode episode, List<Fact> method, List<Event> events) throws Exception {
		int counter = 0;

		if (order) {
			counter = getOrderCounter(episode, method);
		} else {
			counter = getSetCounter(episode, method);
		}
		if (counter > 0) {
			IMethodName enclosingMethod = getEnclosingMethod(method, events);
			methods.put(enclosingMethod, counter);
		}
	}
	
	public Set<IMethodName> getMethodNames(int numberOfMethods) {
		Set<IMethodName> someMethods = Sets.newLinkedHashSet();
		
		for (Map.Entry<IMethodName, Integer> entry : methods.entrySet()) {
			someMethods.add(entry.getKey());
			if (someMethods.size() == numberOfMethods) {
				break;
			}
		}
		return someMethods;
	} 
	
	public int getOccurrences() {
		int counter = 0;
		
		for (Map.Entry<IMethodName, Integer> entry : methods.entrySet()) {
			counter += entry.getValue();
		}
		return counter;
	}

	private IMethodName getEnclosingMethod(List<Fact> method, List<Event> events) throws Exception {
		for (Fact fact : method) {
			Event event = events.get(fact.getFactID());
			if (event.getKind() == EventKind.METHOD_DECLARATION) {
				return event.getMethod();
			}
		}
		throw new Exception("Method does not have enclosing method!");
	}

	private int getOrderCounter(Episode episode, List<Fact> method) {
		Map<Fact, Set<Integer>> eventIndices = getEventIndices(episode, method);
		Set<Fact> episodeRelations = episode.getRelations();
		int previousCounter = getSetCounter(episode, method);
		int counter = 0;
		
		for (Fact relation : episodeRelations) {
			Tuple<Fact, Fact> tuple = relation.getRelationFacts();
			Set<Integer> firstEventIdices = eventIndices.get(tuple.getFirst());
			Set<Integer> secondEventIndices = eventIndices.get(tuple.getSecond());
			Set<Integer> countedIndices = Sets.newLinkedHashSet();

			for (int firstIndex : firstEventIdices) {
				for (int secondIndex : secondEventIndices) {
					if ((firstIndex < secondIndex) && !countedIndices.contains(secondIndex)) {
						counter++;
						countedIndices.add(secondIndex);
					}
				}
			}
			if (counter > 0) {
				previousCounter = Math.min(previousCounter, counter);
				counter = 0;
			} else {
				return counter;
			}
		}
		return previousCounter;
	}

	private Map<Fact, Set<Integer>> getEventIndices(Episode episode, List<Fact> method) {
		Set<Fact> episodeEvents = episode.getEvents();
		Map<Fact, Set<Integer>> eventIndices = Maps.newLinkedHashMap();

		for (int i = 0; i < method.size(); i++) {
			Fact methodFact = method.get(i);
			if (episodeEvents.contains(methodFact)) {
				if (eventIndices.containsKey(methodFact)) {
					eventIndices.get(methodFact).add(i);
				} else {
					Set<Integer> indices = Sets.newLinkedHashSet();
					indices.add(i);
					eventIndices.put(methodFact, indices);
				}
			}
		}
		return eventIndices;
	}

	private int getSetCounter(Episode episode, List<Fact> method) {
		Map<Fact, Integer> eventsOccurrence = Maps.newLinkedHashMap();
		Set<Fact> episodeEvents = episode.getEvents();

		for (Fact methodFact : method) {
			if (episodeEvents.contains(methodFact)) {
				if (eventsOccurrence.containsKey(methodFact)) {
					int counter = eventsOccurrence.get(methodFact);
					eventsOccurrence.put(methodFact, counter + 1);
				} else {
					eventsOccurrence.put(methodFact, 1);
				}
			}
		}
		return getMinOccurrence(eventsOccurrence);
	}

	private int getMinOccurrence(Map<Fact, Integer> eventsOccurrence) {
		int minimum = Integer.MAX_VALUE;

		for (Map.Entry<Fact, Integer> entry : eventsOccurrence.entrySet()) {
			if (entry.getValue() < minimum) {
				minimum = entry.getValue();
			}
		}
		return minimum;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public boolean equals(EnclosingMethods enclMethods) {
		if (!this.methods.equals(enclMethods.methods)) {
			return false;
		}
		return true;
	}
}
