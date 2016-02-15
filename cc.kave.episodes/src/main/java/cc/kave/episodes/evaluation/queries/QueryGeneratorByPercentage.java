package cc.kave.episodes.evaluation.queries;

import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.episodes.model.Query;
import cc.kave.episodes.model.QueryTarget;

public class QueryGeneratorByPercentage {

	private SubsetsGenerator generator = new SubsetsGenerator();
	
	public Set<Query> generateQueries(QueryTarget queryTarget) {
		Set<Query> queries = Sets.newHashSet();
		return queries;
	}
}
