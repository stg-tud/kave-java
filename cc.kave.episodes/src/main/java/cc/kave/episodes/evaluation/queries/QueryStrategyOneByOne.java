package cc.kave.episodes.evaluation.queries;

import java.util.LinkedList;
import java.util.List;

import cc.kave.episodes.model.PatternWithFreq;

public class QueryStrategyOneByOne {

	private int numberOfEventsToRemove = 0;
	
	public void setNumberOfEventsToRemove(int noToRemove) {
		this.numberOfEventsToRemove = noToRemove;
	}
	
	public int getNumberOfEventsToRemove() {
		return numberOfEventsToRemove;
	}
	
	public List<PatternWithFreq> generateQuery(PatternWithFreq episode) {
		List<PatternWithFreq> results = new LinkedList<PatternWithFreq>();
		return results;
	}
}
