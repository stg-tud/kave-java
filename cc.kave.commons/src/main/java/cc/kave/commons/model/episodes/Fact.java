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
package cc.kave.commons.model.episodes;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import cc.recommenders.assertions.Asserts;

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
		rawFact = first.rawFact + ">" + after;
	}

	public Fact() {
		rawFact = null;
	}

	public boolean isRelation() {
		return rawFact.contains(">");
	}
	
	public boolean isRelatedTo(Fact f) {
		// check that f is a non relation
		
		return false;
	}

	/**
	 * this should not be used... use the "int" or the "Fact, Fact" version
	 * instead
	 */
//	@Deprecated
//	public void setFact(String rawFact) {
//		this.rawFact = rawFact;
//	}

	/**
	 * this should not be used... why do we have an abstraction, if we access
	 * the string directly?
	 */
	@Deprecated
	public String getRawFact() {
		return this.rawFact;
	}

	/**
	 * this should not be used... use the event number instead
	 */
	@Deprecated
	public boolean containsEvent(String eventId) {
		if (rawFact.contains(eventId)) {
			return true;
		}
		return false;
	}

	public boolean containsEvent(int eventId) {
		String eventStr = String.valueOf(eventId);
		if (rawFact.contains(eventStr)) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("[%s]", rawFact);
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