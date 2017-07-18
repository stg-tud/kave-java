/**
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.episodes.model.events;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import cc.kave.commons.assertions.Asserts;
import cc.recommenders.datastructures.Tuple;

public class Fact {

	private String rawFact = "";

	public Fact(String rawFact) {
		Asserts.assertNotNull(rawFact);
		this.rawFact = rawFact;
	}

	public Fact(int num) {
		rawFact = String.valueOf(num);
	}

	public Fact(Fact first, Fact after) {
		rawFact = first.rawFact + ">" + after.rawFact;
	}

	public Fact() {
		rawFact = null;
	}

	public boolean isRelation() {
		return rawFact.contains(">");
	}
	
	public boolean isLabel() {
		return rawFact.contains("\\l");
	}
	
	public boolean isContained(Fact fact) {
		if (fact.isRelation() && fact.rawFact.contains(this.rawFact)) {
			return true;
		} else {
			return false;
		}
	}
	
	public Tuple<Fact, Fact> getRelationFacts() {
		if (this.isRelation()) {
			String order = this.rawFact;
			String[] facts = order.split(">");
			return Tuple.newTuple(new Fact(facts[0]), new Fact(facts[1]));
		} else {
			return Tuple.newTuple(this, this);
		}
	}

	public boolean containsEvent(int eventId) {
		String eventStr = String.valueOf(eventId);
		if (rawFact.contains(eventStr)) {
			return true;
		}
		return false;
	}
	
	public int getFactID() {
		if (!(this.isRelation())) {
			return Integer.parseInt(this.rawFact);
		} else {
			return -1;
		}
	}

	@Override
	public String toString() {
		return String.format("%s", rawFact);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
}