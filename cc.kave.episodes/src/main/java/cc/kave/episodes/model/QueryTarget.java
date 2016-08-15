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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;

public class QueryTarget extends Pattern {
	
	private Fact methodDecl;

	public Fact getMethodDecl() {
		Tuple<Fact, Set<Fact>> tuple = separateFacts(this.getFacts());
		return methodDecl;
	}

	public void setMethodDecl(Fact methodDecl) {
		this.methodDecl = methodDecl;
	}
	
	public boolean equals(QueryTarget queryTarget) {
		if (!this.methodDecl.equals(queryTarget.getMethodDecl())) {
			return false;
		}
		if (!this.getFacts().equals(queryTarget.getFacts())) {
			return false;
		}
		return true;
	}
	
	private Tuple<Fact, Set<Fact>> separateFacts(Iterable<Fact> facts) {
		Map<Fact, Integer> orderCounter = new HashMap<Fact, Integer>();
		
		for (Fact fact : facts) {
			if (fact.isRelation()) {
				Tuple<Fact, Fact> tuple = fact.getRelationFacts();
				Fact fact1 = tuple.getFirst();
				Fact fact2 = tuple.getSecond();
				if (orderCounter.containsKey(fact1)) {
					int counter = orderCounter.get(fact1);
					orderCounter.put(fact1, counter + 1);
				} else {
					orderCounter.put(fact1, 1);
				}
				if (orderCounter.containsKey(fact2)) {
					int counter = orderCounter.get(fact2);
					orderCounter.put(fact2, counter + 1);
				} else {
					orderCounter.put(fact2, 1);
				}
			}
		}
		Set<Fact> bodyFacts = new HashSet<Fact>();
		Fact methodDecl = new Fact();
		for (Map.Entry<Fact, Integer> entry : orderCounter.entrySet()) {
			if (entry.getValue() == (orderCounter.size() - 1)) {
				methodDecl = entry.getKey();
			} else {
				bodyFacts.add(entry.getKey());
			}
		}
		return Tuple.newTuple(methodDecl, bodyFacts);
	} 
}
