/**
 * Copyright 2016 Technische Universität Darmstadt
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
package exec.recommender_reimplementation.raychev_analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.google.common.collect.Lists;

import cc.kave.commons.utils.ToStringUtils;

public class ConcreteHistory {

	private List<Interaction> history;
	
	public ConcreteHistory() {
		history = new ArrayList<>();
	}
	
	public ConcreteHistory(List<Interaction> history) {
		this.history = history;
	}
	
	public ConcreteHistory(Interaction... interactions) {
		history = Lists.newArrayList(interactions);
	}
	
	public void add(Interaction interaction) {
		history.add(interaction);
	}
	
	public List<Interaction> getHistory() {
		return history;
	}

	public void setHistory(List<Interaction> history) {
		this.history = history;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(history).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof ConcreteHistory)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		ConcreteHistory other = (ConcreteHistory) obj;
		return new EqualsBuilder().append(this.history, other.history).isEquals();
	}
	
	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}
	
	public ConcreteHistory clone() {
		return new ConcreteHistory(new ArrayList<>(history));
	}	
}
