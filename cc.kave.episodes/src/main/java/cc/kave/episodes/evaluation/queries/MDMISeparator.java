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
package cc.kave.episodes.evaluation.queries;

import static cc.kave.commons.assertions.Asserts.assertTrue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;

public class MDMISeparator {
	
	public Tuple<Fact, Set<Fact>> separateFacts(Episode episode) {
		assertTrue(episode.getNumEvents() > 1, "Episode contains only one event!");
		
		Fact methodDecl = getMethodDecl(episode);
		
		Set<Fact> bodyFacts = new HashSet<Fact>();
		for (Fact fact : episode.getFacts()) {
			if (!fact.equals(methodDecl) && !fact.isRelation()) {
				bodyFacts.add(fact);
			}
		}
		return Tuple.newTuple(methodDecl, bodyFacts);
	}
	
	public Set<Fact> getEpisodeBody(Episode episode) {
		if (episode.getNumEvents() == 1) {
			return Sets.newHashSet();
		}
		Fact methodDecl = getMethodDecl(episode);
		Set<Fact> episodeBody = new HashSet<Fact>();
		
		for (Fact fact : episode.getFacts()) {
			if (fact.isRelation()) {
				if ((!methodDecl.isContained(fact))) {
					episodeBody.add(fact);
				}
			} else if (!fact.equals(methodDecl)) {
				episodeBody.add(fact);
			}
		}
		return episodeBody;
	} 
	
	private Fact getMethodDecl(Episode episode) {
		Map<Fact, Integer> orderCounter = new HashMap<Fact, Integer>();
		
		for (Fact fact : episode.getFacts()) {
			if (fact.isRelation()) {
				Tuple<Fact, Fact> tuple = fact.getRelationFacts();
				Fact existanceFact = tuple.getFirst();
				if (orderCounter.containsKey(existanceFact)) {
					int counter = orderCounter.get(existanceFact);
					orderCounter.put(existanceFact, counter + 1);
				} else {
					orderCounter.put(existanceFact, 1);
				}
			}
		}
		
		Fact methodDecl = new Fact();
		for (Map.Entry<Fact, Integer> entry : orderCounter.entrySet()) {
			if (entry.getValue() == (episode.getNumEvents() - 1)) {
				methodDecl = entry.getKey();
			}
		}
		return methodDecl;
	}
}
