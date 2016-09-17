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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import cc.kave.commons.utils.ToStringUtils;

public class AbstractHistory {
	
	private List<Interaction> abstractHistory;

	private Set<ConcreteHistory> historySet;
	
	static final int MAXIMUM_HISTORY_LENGTH = 50;

	public AbstractHistory() {
		abstractHistory = new ArrayList<>();
		historySet = new HashSet<>();
	}
	
	public AbstractHistory(List<Interaction> abstractHistory, Set<ConcreteHistory> historySet) {
		this.abstractHistory = abstractHistory;
		this.historySet = historySet;
	}
	
	public List<Interaction> getAbstractHistory() {
		return abstractHistory;
	}

	public void setAbstractHistory(List<Interaction> abstractHistory) {
		this.abstractHistory = abstractHistory;
	}

	public Set<ConcreteHistory> getHistorySet() {
		return historySet;
	}

	public void setHistorySet(Set<ConcreteHistory> historySet) {
		this.historySet = historySet;
	}
	
	public AbstractHistory clone() {
		List<Interaction> abstractHistory = new ArrayList<>(this.abstractHistory);
		Set<ConcreteHistory> historySet = new HashSet<>();
		for (ConcreteHistory concreteHistory : this.historySet) {
			historySet.add(concreteHistory.clone());
		}
		
		return new AbstractHistory(abstractHistory, historySet);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(abstractHistory).append(historySet).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof AbstractHistory)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		AbstractHistory other = (AbstractHistory) obj;
		return new EqualsBuilder().append(this.abstractHistory, other.abstractHistory)
				.append(this.historySet, other.historySet).isEquals();
	}
	
	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}
	
	public void addInteraction(Interaction interaction) {
		abstractHistory.add(interaction);

		Set<ConcreteHistory> clonedHistory = new HashSet<>(historySet);
		historySet.clear();
		for (ConcreteHistory concreteHistory : clonedHistory) {
			if (!(historySet.size() > MAXIMUM_HISTORY_LENGTH)) {
				concreteHistory.add(interaction);
				historySet.add(concreteHistory);
			}
		}
	}

	public void mergeAbstractHistory(AbstractHistory other) {
		if(abstractHistory.equals(other.abstractHistory)) return;
		
		mergeAbstractHistory(other.getAbstractHistory());
		mergeHistorySet(other.getHistorySet());
	}

	private void mergeHistorySet(Set<ConcreteHistory> otherHistorySet) {
		historySet.addAll(otherHistorySet);
	}

	private void mergeAbstractHistory(List<Interaction> otherAbstractHistory) {
		if(abstractHistory.size() > otherAbstractHistory.size()) return;
		// findPrefix
		int i = 0;
		Interaction interaction = abstractHistory.get(i);
		Interaction other = otherAbstractHistory.get(i);
		while (interaction.equals(other)) {
			i++;
			if(i >= abstractHistory.size()) break;
			interaction = abstractHistory.get(i);
			other = otherAbstractHistory.get(i);
		}
		
		abstractHistory.addAll(otherAbstractHistory.subList(i, otherAbstractHistory.size()));
	}
	
	
}
