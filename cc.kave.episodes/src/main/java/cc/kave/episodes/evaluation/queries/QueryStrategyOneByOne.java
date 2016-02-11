package cc.kave.episodes.evaluation.queries;

import java.util.LinkedList;
import java.util.List;

import cc.kave.commons.model.episodes.Episode;

public class QueryStrategyOneByOne {

	private int numberOfEventsToRemove = 0;
	
	public void setNumberOfEventsToRemove(int noToRemove) {
		this.numberOfEventsToRemove = noToRemove;
	}
	
	public int getNumberOfEventsToRemove() {
		return numberOfEventsToRemove;
	}
	
	public List<Episode> generateQuery(Episode episode) {
		List<Episode> results = new LinkedList<Episode>();
		return results;
	}
}
