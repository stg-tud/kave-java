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

public class AbstractHistory {
	
	private List<Interaction> abstractHistory;

	private Set<ConcreteHistory> historySet;
	
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
		final int prime = 31;
		int result = 1;
		result = prime * result + ((abstractHistory == null) ? 0 : abstractHistory.hashCode());
		result = prime * result + ((historySet == null) ? 0 : historySet.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractHistory other = (AbstractHistory) obj;
		if (abstractHistory == null) {
			if (other.abstractHistory != null)
				return false;
		} else if (!abstractHistory.equals(other.abstractHistory))
			return false;
		if (historySet == null) {
			if (other.historySet != null)
				return false;
		} else {
			// TODO: test custom equals method for AbstractHistory
			if(historySet.size() != other.historySet.size()) return false;
			for (ConcreteHistory concreteHistory : historySet) {
				if(!other.historySet.contains(concreteHistory)) return false;
			}
		}
		return true;
	}

	public void mergeAbstractHistory(AbstractHistory other) {
		if(abstractHistory.equals(other)) return;
		
		mergeAbstractHistory(other.getAbstractHistory());
		mergeHistorySet(other.getHistorySet());
	}

	private void mergeHistorySet(Set<ConcreteHistory> otherHistorySet) {
		historySet.addAll(otherHistorySet);
	}

	private void mergeAbstractHistory(List<Interaction> otherAbstractHistory) {
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
