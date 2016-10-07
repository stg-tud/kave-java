package cc.kave.episodes.similarityMetrics;

import java.util.Set;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.evaluation.data.Measure;

import com.google.common.collect.Sets;

public class F1Facts {

	public double calcF1Facts(Episode query, Episode pattern) {
		if (isEmptyEpisode(query) || isEmptyEpisode(pattern)) {
			return 0.0;
		}
		int episodeRelations = pattern.getRelations().size();
		int episodeEvents = pattern.getNumEvents();
		Set<Fact> episodeFacts = pattern.getFacts();
		Set<Fact> queryFacts = query.getFacts();

		if (episodeRelations < triangleNumber(episodeEvents)) {
			queryFacts = getQueryFacts(query, episodeFacts);
		}
		Measure m = Measure.newMeasure(queryFacts, episodeFacts);
		double f1 = m.getF1();
		return f1;
	}

	private Set<Fact> getQueryFacts(Episode query, Set<Fact> epFacts) {
		Set<Fact> queryFacts = Sets.newHashSet();
		queryFacts.addAll(query.getFacts());
		Set<Fact> queryRel = query.getRelations();
		
		for (Fact rel : queryRel) {
			Tuple<Fact, Fact> relFacts = rel.getRelationFacts();
			Fact order1 = new Fact(relFacts.getFirst() + ">"
					+ relFacts.getSecond());
			Fact order2 = new Fact(relFacts.getSecond() + ">"
					+ relFacts.getFirst());
			if (epFacts.contains(relFacts.getFirst())
					&& epFacts.contains(relFacts.getSecond())
					&& !(epFacts.contains(order1) || epFacts.contains(order2))) {
				queryFacts.remove(rel);
			}
		}
		return queryFacts;
	}
	
	public int triangleNumber(int num) {
		if (num < 2) {
			return 0;
		} else if (num == 2) {
			return 1;
		} else {
			return (num - 1) + triangleNumber(num - 1);
		}
	}
	
	private boolean isEmptyEpisode(Episode episode) {
		if (episode.getNumFacts() == 0) {
			return true;
		} else {
			return false;
		}
	}
}
