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
	private boolean relationshipFact;

	public Fact(String rawFact) {
		Asserts.assertNotNull(rawFact);
		this.rawFact = rawFact;
		this.relationshipFact = false;
	}

	public Fact(String rawFact, boolean isRelationshipFact) {
		Asserts.assertNotNull(rawFact);
		this.rawFact = rawFact;
		this.relationshipFact = isRelationshipFact;
	}

	public Fact() {
		rawFact = null;
	}

	public boolean isRelationship() {
		return relationshipFact;
	}

	public void setRelationshipFact(boolean isRelationshipFact) {
		relationshipFact = isRelationshipFact;
	}

	public void setFact(String rawFact) {
		this.rawFact = rawFact;
	}

	public String getRawFact() {
		return this.rawFact;
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