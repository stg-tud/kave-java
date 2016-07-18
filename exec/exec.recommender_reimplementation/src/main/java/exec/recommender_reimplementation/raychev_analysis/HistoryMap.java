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
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}
	
	@Override
	public String toString() {
		return ToStringUtils.toString(values());
	}
}
