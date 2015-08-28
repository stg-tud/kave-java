package cc.kave.commons.model.pattexplore;

import java.util.LinkedList;
import java.util.List;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.SubGroum;
import cc.kave.commons.model.groum.comparator.SubGroumComparator;
import cc.kave.commons.model.groum.comparator.SubGroumIdentComparator;

import com.google.common.collect.TreeMultimap;

public class PattExplorer {
	int threshold;

	public PattExplorer(int threshold) {
		this.threshold = threshold;
	}

	public List<SubGroum> explorePatterns(List<Groum> D) {

		TreeMultimap<SubGroum, SubGroum> L = TreeMultimap.create(new SubGroumComparator(),
				new SubGroumIdentComparator());

		for (Groum groum : D) {

			List<SubGroum> subgroums = Utils.breakdown(groum);
			for (SubGroum subgroum : subgroums) {
				L.put(subgroum, subgroum);
			}
		}

		List<SubGroum> smallPatterns = new LinkedList<>();
		for (SubGroum subgroum : L.keySet()) {
			if (L.get(subgroum).size() < threshold) {
				smallPatterns.add(subgroum);
			}
		}

		for (SubGroum smallpattern : smallPatterns) {
			L.removeAll(smallpattern);
		}

		TreeMultimap<SubGroum, SubGroum> explored = TreeMultimap.create(new SubGroumComparator(),
				new SubGroumIdentComparator());

		for (SubGroum pattern : L.keySet()) {
			explored.putAll(explore(pattern, L, D));
		}

		L.putAll(explored);

		LinkedList<SubGroum> distinctPatterns = new LinkedList<>();
		distinctPatterns.addAll(L.keySet());
		return distinctPatterns;

	}

	private TreeMultimap<SubGroum, SubGroum> explore(SubGroum P, TreeMultimap<SubGroum, SubGroum> L, List<Groum> D) {
		TreeMultimap<SubGroum, SubGroum> patterns = TreeMultimap.create(new SubGroumComparator(),
				new SubGroumIdentComparator());
		TreeMultimap<SubGroum, SubGroum> newPatterns = TreeMultimap.create(new SubGroumComparator(),
				new SubGroumIdentComparator());

		patterns.putAll(L);

		for (SubGroum U : L.keySet()) {
			if (U.getAllNodes().size() == 1) {

				TreeMultimap<SubGroum, SubGroum> Q = TreeMultimap.create(new SubGroumComparator(),
						new SubGroumIdentComparator());

				for (SubGroum occurrence : patterns.get(P)) {
					List<SubGroum> candidates = occurrence.extensibleWith(U);
					if (candidates != null)
						for (SubGroum candidate : candidates) {
							Q.put(candidate, candidate);
						}
				}

				for (SubGroum candidate : Q.keySet()) {
					if (Q.get(candidate).size() >= threshold) {
						newPatterns.putAll(candidate, Q.get(candidate));

						patterns.putAll(candidate, Q.get(candidate));

						TreeMultimap<SubGroum, SubGroum> explored = explore(candidate, patterns, D);
						newPatterns.putAll(explored);
					}
				}
			}
		}
		return newPatterns;
	}
}
