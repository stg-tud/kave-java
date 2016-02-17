package cc.kave.episodes.evaluation.queries;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.episodes.Fact;

public class SubsetsGenerator {

	public List<List<Fact>> generateSubsets(Set<Fact> setOfFacts, int subsetLength) {
		
		int N = setOfFacts.size();
		List<List<Fact>> results = new LinkedList<List<Fact>>();
		
		assertTrue("You cannot remove more events than are present in episode", N >= subsetLength);
		
		if (N == subsetLength) {
			return results;
		}

		int[] binary = new int[(int) Math.pow(2, N)];
		for (int i = 0; i < Math.pow(2, N); i++) {
			int b = 1;
			binary[i] = 0;
			int num = i, count = 0;
			while (num > 0) {
				if (num % 2 == 1)
					count++;
				binary[i] += (num % 2) * b;
				num /= 2;
				b = b * 10;
			}

			if (count == subsetLength) {
				List<Fact> subset = new LinkedList<>();
				for (Fact fact : setOfFacts) {
					if (binary[i] % 10 == 1) {
						subset.add(fact);
					}
					binary[i] /= 10;
				}
				results.add(subset);
			}
		}
		return results;
	}
}
