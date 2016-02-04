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
package cc.kave.commons.model.episodes;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Lists;

public class Method {

	private String methodName = "";
	private List<Fact> facts = Lists.newLinkedList();
	private int numberOfInvocations;

	public Iterable<Fact> getFacts() {
		return facts;
	}

	public Fact get(int i) {
		return facts.get(i);
	}

	public int getNumberOfInvocations() {
		return facts.size();
	}

	public void setNumberOfInvocations(int numberOfEvents) {
		this.numberOfInvocations = numberOfEvents;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void addStringsOfFacts(String... rawFacts) {
		for (String rawFact : rawFacts) {
			addFact(rawFact);
		}
	}

	public void addFact(String rawFact) {
		facts.add(new Fact(rawFact));
	}

	public boolean containsInvocations(Fact fact1) {
		if (facts.contains(fact1)) {
			return true;
		}
		return false;
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

	public boolean equals(Method method) {
		if (!(this.methodName.equalsIgnoreCase(method.methodName))) {
			return false;
		}
		if (this.facts.size() != method.facts.size()) {
			return false;
		}
		for (Fact fact : this.facts) {
			if (!method.facts.contains(fact)) {
				return false;
			}
		}
		return true;
	}
}
