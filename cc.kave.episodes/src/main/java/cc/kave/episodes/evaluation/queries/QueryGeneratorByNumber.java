package cc.kave.episodes.evaluation.queries;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.model.Query;
import cc.kave.episodes.model.QueryTarget;

public class QueryGeneratorByNumber {

	private SubsetsGenerator generator = new SubsetsGenerator();
	
	public Map<Integer, List<Query>> generateQueries(QueryTarget queryTarget, int removeEvents) {
		Map<Integer, List<Query>> generatedQueries = new HashMap<Integer, List<Query>>();
		
		Iterable<Fact> allFacts = queryTarget.getFacts();
	
		List<List<Fact>> subsets = generator.generateSubsets((Set<Fact>) allFacts, removeEvents);
		
		for (List<Fact> query : subsets) {
			Query q = createEpisode(queryTarget, query);
			if (generatedQueries.size() == 0) {
				generatedQueries.put(removeEvents, new LinkedList<Query>());
			}
			generatedQueries.get(removeEvents).add(q);
		}
		
		return generatedQueries;
	}

	private Query createEpisode(QueryTarget queryTarget, List<Fact> queryFacts) {
		Query query = new Query();
		
		for (Fact f : queryFacts) {
			query.addFact(f);
		}
		if (queryFacts.size() > 2) {
			for (int first = 0; first < queryFacts.size() - 1; first++) {
				for (int second = first + 1; second < queryFacts.size(); second++) {
					query.addFact(new Fact(queryFacts.get(first), queryFacts.get(second)));
				}
			}
		}
		return query;
	}
}
