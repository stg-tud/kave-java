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

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.utils.ToStringUtils;

public class HistoryMap extends HashMap<Set<AbstractLocation>, AbstractHistory>{

	private static final long serialVersionUID = 627359643067220473L;
	
	static final int HISTORY_THRESHOLD = 16;
	
	private Random randomizer;
	
	public HistoryMap() {
		super();
		randomizer = new Random();
	}
	
	@Override
	public HistoryMap clone() {
		HistoryMap clone = new HistoryMap();
		for (Entry<Set<AbstractLocation>, AbstractHistory> entry : entrySet()) {
			clone.put(entry.getKey(), entry.getValue().clone());
		}

		return clone;
	}
	
	public void mergeInto(HistoryMap clone) {
		for (Entry<Set<AbstractLocation>, AbstractHistory> entry : clone.entrySet()) {
			Set<AbstractLocation> key = entry.getKey();
			if (this.containsKey(key)) {
				get(key).mergeAbstractHistory(entry.getValue());
			} else {
				put(entry.getKey(), entry.getValue());
			}
		}

	}
	
	public void checkForAbstractHistoryThreshold() {
		if (size() > HISTORY_THRESHOLD) {
			evictRandomAbstractHistory();
		}
	}
	
	public void evictRandomAbstractHistory() {
		int indexRandomEntry = randomizer.nextInt(size());

		Entry<Set<AbstractLocation>, AbstractHistory> entryToRemove = null;

		int i = 0;
		for (Entry<Set<AbstractLocation>, AbstractHistory> entry : entrySet()) {
			if (i == indexRandomEntry) {
				entryToRemove = entry;
				break;
			}
			i++;
		}

		if (entryToRemove != null) {
			remove(entryToRemove.getKey(), entryToRemove.getValue());
		}
	}
	
	public AbstractHistory getOrCreateAbstractHistory(Set<AbstractLocation> abstractLocations) {
		if (containsKey(abstractLocations))
			return get(abstractLocations);

		AbstractHistory abstractHistory = new AbstractHistory();
		put(abstractLocations, abstractHistory);
		abstractHistory.getHistorySet().add(new ConcreteHistory());
		return abstractHistory;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(this).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return new EqualsBuilder().append(this, obj).isEquals();
	}
	
	@Override
	public String toString() {
		return ToStringUtils.toString(values());
	}
}
