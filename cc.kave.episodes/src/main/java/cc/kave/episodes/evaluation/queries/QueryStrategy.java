package cc.kave.episodes.evaluation.queries;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math.util.MathUtils;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Fact;
import cc.kave.episodes.model.Episode;
import cc.recommenders.datastructures.Tuple;

public class QueryStrategy {

	private SubsetsGenerator generator = new SubsetsGenerator();
	private Separator separator = new Separator();
	private Tuple<Fact, Set<Fact>> declInv = Tuple.newTuple(new Fact(), Sets.newHashSet());
	private int numInvs = 0;
	
	public Map<Double, Set<Episode>> byPercentage(Episode target) {
		preprocessing(target);
		Map<Double, Set<Episode>> queries = new LinkedHashMap<Double, Set<Episode>>();
		Map<Double, Integer> removals = percentRemoved(numInvs);
		
		for (Map.Entry<Double, Integer> entry : removals.entrySet()) {
			int selectionLength = numInvs - entry.getValue();
			Set<Set<Fact>> currentSubsets = generator.generateSubsets(declInv.getSecond(), selectionLength, 10);
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
	
	public Map<Double, Set<Episode>> byNumber(Episode target) {
		preprocessing(target);
		Map<Double, Set<Episode>> queries = new LinkedHashMap<Double, Set<Episode>>();
		List<Integer> removals = numberRemoved(numInvs);
		
		for (Integer number : removals) {
			int selectionLength = numInvs - number;
			Set<Set<Fact>> currentSubsets = generator.generateSubsets(declInv.getSecond(), selectionLength, 10);
			for (Set<Fact> subset : currentSubsets) {
				Episode query = createQuery(target, declInv.getFirst(), subset);
				double key = number * 1.0;
				if (queries.containsKey(key)) {
					queries.get(key).add(query);
				} else {
					queries.put(key, Sets.newHashSet(query));
				}
			}
		}
		return queries;
	}
	
	private List<Integer> numberRemoved(int numInvs) {
		List<Integer> numbers = new LinkedList<Integer>();
		for (int num = 1; num < numInvs; num++) {
			numbers.add(num);
		}
		return numbers;
	}

	private void preprocessing(Episode target) {
		assertTrue(target.getNumEvents() > 2, "Not valid episode for query generation!");
		declInv = separator.separateFacts(target);
		numInvs = declInv.getSecond().size();
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

	private Map<Double, Integer> percentRemoved(int size) {
		Map<Double, Integer> removals = new LinkedHashMap<Double, Integer>();
		
		for (double perc = 0.10; perc < 1.0; perc += 0.10) {
			int p = (int) Math.ceil(perc * (double) size);
			if (!removals.values().contains(p) && (p > 0.0 && p < size)) {
				removals.put(MathUtils.round(perc, 2), p);
			}
		}
		return removals;
	}
}
