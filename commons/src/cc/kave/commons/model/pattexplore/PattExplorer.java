package cc.kave.commons.model.pattexplore;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.SubGroum;
import cc.kave.commons.model.groum.comparator.DFSGroumComparator;
import cc.kave.commons.model.groum.comparator.HashCodeComparator;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

public class PattExplorer {
	int threshold;

	public PattExplorer(int threshold) {
		this.threshold = threshold;
	}

	public List<SubGroum> explorePatterns(List<Groum> D) {
		TreeMultimap<SubGroum, SubGroum> L = createGroumMap();

		for (Groum groum : D) {
			List<SubGroum> subgroums = Utils.breakdown(groum);
			for (SubGroum subgroum : subgroums) {
				L.put(subgroum, subgroum);
			}
		}

		TreeMultimap<SubGroum, SubGroum> L2 = createGroumMap();
		for (SubGroum groum : L.keySet()) {
			if (getGroumFrequency(L, groum) >= threshold) {
				L2.putAll(groum, L.get(groum));
			}
		}

		TreeMultimap<SubGroum, SubGroum> explored = createGroumMap();
		for (SubGroum pattern : L2.keySet()) {
			explored.putAll(explore(pattern, L2, D));
		}
		L2.putAll(explored);

		return new ArrayList<>(L2.keySet());

	}

	private int getGroumFrequency(TreeMultimap<SubGroum, SubGroum> L, SubGroum groum) {
		return L.get(groum).size();
	}

	private TreeMultimap<SubGroum, SubGroum> createGroumMap() {
		return TreeMultimap.create(new DFSGroumComparator(), new HashCodeComparator());
	}

	private Multimap<SubGroum, SubGroum> explore(SubGroum P, Multimap<SubGroum, SubGroum> L, List<Groum> D) {
		TreeMultimap<SubGroum, SubGroum> patterns = createGroumMap();
		Multimap<SubGroum, SubGroum> newPatterns = createGroumMap();

		patterns.putAll(L);

		for (SubGroum U : L.keySet()) {
			if (U.getAllNodes().size() == 1) {
				TreeMultimap<SubGroum, SubGroum> Q = createGroumMap();

				for (SubGroum occurrence : patterns.get(P)) {
					List<SubGroum> candidates = occurrence.extensibleWith(U);
					if (candidates != null)
						for (SubGroum candidate : candidates) {
							Q.put(candidate, candidate);
						}
				}

				for (SubGroum candidate : Q.keySet()) {
					if (getGroumFrequency(Q, candidate) >= threshold) {
						newPatterns.putAll(candidate, Q.get(candidate));
						patterns.putAll(candidate, Q.get(candidate));

						Multimap<SubGroum, SubGroum> explored = explore(candidate, patterns, D);
						newPatterns.putAll(explored);
					}
				}
			}
		}
		return newPatterns;
	}
}
