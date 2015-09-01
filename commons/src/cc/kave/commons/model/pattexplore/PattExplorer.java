package cc.kave.commons.model.pattexplore;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.SubGroum;
import cc.kave.commons.model.groum.comparator.DFSGroumComparator;

import com.google.common.collect.BoundType;
import com.google.common.collect.Multiset;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

public class PattExplorer {
	int threshold;

	public PattExplorer(int threshold) {
		this.threshold = threshold;
	}

	public List<SubGroum> explorePatterns(List<Groum> D) {
		TreeMultiset<SubGroum> L2 = TreeMultiset.create(new DFSGroumComparator());

		for (Groum groum : D) {
			List<SubGroum> subgroums = Utils.breakdown(groum);
			for (SubGroum subgroum : subgroums) {
				L2.add(subgroum);
			}
		}

		for (Groum groum : L2.elementSet()) {
			if (L2.count(groum) < threshold) {
				L2.remove(groum, threshold);
			}
		}

		TreeMultiset<SubGroum> explored2 = TreeMultiset.create(new DFSGroumComparator());

		for (SubGroum pattern : L2.elementSet()) {
			explored2.addAll(explore(pattern, L2, D));
		}

		L2.addAll(explored2);

		return new ArrayList<>(L2.elementSet());

	}

	private Multiset<SubGroum> explore(SubGroum P, Multiset<SubGroum> L, List<Groum> D) {
		TreeMultiset<SubGroum> patterns = TreeMultiset.create(new DFSGroumComparator());
		Multiset<SubGroum> newPatterns = TreeMultiset.create(new DFSGroumComparator());

		patterns.addAll(L);

		for (SubGroum U : L.elementSet()) {
			if (U.getAllNodes().size() == 1) {

				TreeMultiset<SubGroum> Q = TreeMultiset.create(new DFSGroumComparator());

				for (SubGroum occurrence : getInstances(P, patterns)) {
					List<SubGroum> candidates = occurrence.extensibleWith(U);
					if (candidates != null)
						for (SubGroum candidate : candidates) {
							Q.add(candidate);
						}
				}

				for (SubGroum candidate : Q.elementSet()) {
					if (Q.count(candidate) >= threshold) {
						newPatterns.addAll(getInstances(candidate, Q));
						patterns.addAll(getInstances(candidate, Q));

						Multiset<SubGroum> explored = explore(candidate, patterns, D);
						newPatterns.addAll(explored);
					}
				}
			}
		}
		return newPatterns;
	}

	private SortedMultiset<SubGroum> getInstances(SubGroum P, TreeMultiset<SubGroum> patterns) {
		SortedMultiset<SubGroum> instances = patterns.subMultiset(P, BoundType.CLOSED, P, BoundType.CLOSED);
		return instances;
	}
}
