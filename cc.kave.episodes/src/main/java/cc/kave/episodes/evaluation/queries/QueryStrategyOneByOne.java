package cc.kave.episodes.evaluation.queries;

import java.util.LinkedList;
import java.util.List;

import cc.kave.episodes.model.Pattern;

public class QueryStrategyOneByOne {

	private int numberOfEventsToRemove = 0;
	
	public void setNumberOfEventsToRemove(int noToRemove) {
		this.numberOfEventsToRemove = noToRemove;
	}
	
	public int getNumberOfEventsToRemove() {
		return numberOfEventsToRemove;
	}
	
	public List<Pattern> generateQuery(Pattern episode) {
		List<Pattern> results = new LinkedList<Pattern>();
		return results;
	}
}
