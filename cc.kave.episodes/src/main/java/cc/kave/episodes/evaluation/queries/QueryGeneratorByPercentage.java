package cc.kave.episodes.evaluation.queries;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math.util.MathUtils;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.model.Episode;
import cc.recommenders.datastructures.Tuple;

public class QueryGeneratorByPercentage {

	private SubsetsGenerator generator = new SubsetsGenerator();
	private Separator separator = new Separator();
	
	public Map<Double, Set<Episode>> generateQueries(Episode target) {
		assertTrue(target.getNumEvents() > 2, "Not valid episode for query generation!");
		
		Map<Double, Set<Episode>> queries = new HashMap<Double, Set<Episode>>();
		Tuple<Fact, Set<Fact>> declInv = separator.separateFacts(target);
		
		int numInvs = declInv.getSecond().size();
		Map<Double, Integer> removals = calcPercNumbers(numInvs);
		
		for (Map.Entry<Double, Integer> entry : removals.entrySet()) {
			int selectionLength = numInvs - entry.getValue();
			Set<Set<Fact>> currentSubsets = generator.generateSubsets(declInv.getSecond(), selectionLength);
			for (Set<Fact> subset : currentSubsets) {
				Episode query = createQuery(target, declInv.getFirst(), subset);
				if (queries.containsKey(entry.getKey())) {
					queries.get(entry.getKey()).add(query);
				} else {
					queries.put(entry.getKey(), Sets.newHashSet(query));
				}
			}
		}
		return queries;
	}

	private Episode createQuery(Episode target, Fact methodDecl, Set<Fact> subset) {
		Episode query = new Episode();
		query.addFact(methodDecl);
		for (Fact fact : subset) {
			query.addFact(fact);
			query.addFact(new Fact(methodDecl, fact));
		}
		
		for (Fact fact : target.getFacts()) {
			if (fact.isRelation()) {
				Tuple<Fact, Fact> pairFacts = fact.getRelationFacts();
				if (subset.contains(pairFacts.getFirst()) && subset.contains(pairFacts.getSecond())) {
					query.addFact(fact);
				}
			}
		}
		return query;
	}

	private Map<Double, Integer> calcPercNumbers(int size) {
		Map<Double, Integer> removals = new HashMap<Double, Integer>();
		
		for (double perc = 0.10; perc < 1.0; perc += 0.10) {
			int p = (int) Math.ceil(perc * (double) size);
			if (!removals.values().contains(p) && (p > 0.0 && p < size)) {
				removals.put(MathUtils.round(perc, 2), p);
			}
		}
		return removals;
	}
}
