package cc.kave.episodes.similarityMetrics;

import java.util.Set;

import cc.kave.episodes.model.events.Fact;

import com.google.common.collect.Sets;

public class Mapo {

	public static double calcMapo(Set<Fact> query, Set<Fact> pattern) {
		if (query.isEmpty() && pattern.isEmpty()) {
			return 1.0;
		}
		
		Set<Fact> conjunction = getConjunctionFacts(query, pattern);
		Set<Fact> disjunction = getDisjunctionFacts(query, pattern);

		return fract(conjunction.size(), disjunction.size());
	}
	
	private static Set<Fact> getConjunctionFacts(Set<Fact> query, Set<Fact> pattern) {
		
		
		Set<Fact> conjuction = Sets.newHashSet();

		for (Fact fact : pattern) {
			if (query.contains(fact)) {
				conjuction.add(fact);
			}
		}
		return conjuction;
	}
	
	private static Set<Fact> getDisjunctionFacts(Set<Fact> query, Set<Fact> pattern) {
		Set<Fact> disjunction = Sets.newHashSet();

		disjunction.addAll(query);
		disjunction.addAll(pattern);
		return disjunction;
	}
	
	private static Double fract(double numerator, double denominator) {
		return numerator / denominator;
	}
}
