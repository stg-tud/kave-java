package c.kave.commons.evaluation.queries;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Fact;

public class QueryGeneratorByNumber {

	private SubsetsGenerator generator = new SubsetsGenerator();
	
	public Map<Integer, List<Episode>> generateQueries(Episode originalEpisode, int removeEvents) {
		Map<Integer, List<Episode>> generatedQueries = new HashMap<Integer, List<Episode>>();
		
		Iterable<Fact> allFacts = originalEpisode.getFacts();
		List<Fact> events = new LinkedList<Fact>();
		List<Fact> relations = new LinkedList<Fact>();
		
		for (Fact fact : allFacts) {
			if (fact.isRelation()) {
				relations.add(fact);
			} else {
				events.add(fact);
			}
		}
		List<List<Fact>> subsets = generator.generateSubsets(events, removeEvents);
		
		for (List<Fact> query : subsets) {
			Episode q = createEpisode(originalEpisode, query);
			if (generatedQueries.size() == 0) {
				generatedQueries.put(removeEvents, new LinkedList<Episode>());
			}
			generatedQueries.get(removeEvents).add(q);
		}
		
		return generatedQueries;
	}

	private Episode createEpisode(Episode episode, List<Fact> queryFacts) {
		Episode query = new Episode();
		query.setFrequency(1);
		query.setNumEvents(queryFacts.size());
		query.setContext(episode.getContext());
		
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
