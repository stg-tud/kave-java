package cc.kave.episodes.evaluation.queries;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.model.Query;
import cc.kave.episodes.model.QueryTarget;

public class QueryGeneratorByPercentage {

	private SubsetsGenerator generator = new SubsetsGenerator();
	
	public Set<Query> generateQueries(QueryTarget queryTarget) {
		Set<Query> queries = Sets.newHashSet();
		
		if (queryTarget.getNumEvents() > 1) {
			List<Fact> events = getMethodInv(queryTarget);
			
			List<Query> queries25P = generate25PQueries(events);
		}
		return queries;
	}

	private List<Query> generate25PQueries(List<Fact> events) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Fact> getMethodInv(QueryTarget queryTarget) {
		List<Fact> events = new LinkedList<Fact>();
		for (Fact fact : queryTarget.getFacts()) {
			if (!fact.isRelation()) {
				events.add(fact);
			}
		}
		return events;
	}
}
