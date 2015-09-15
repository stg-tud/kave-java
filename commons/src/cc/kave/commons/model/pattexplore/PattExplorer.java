package cc.kave.commons.model.pattexplore;

import java.util.List;
import java.util.Set;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.SubGroum;
import cc.kave.commons.model.groum.SubGroumMultiSet;

public class PattExplorer {
	int threshold;

	public PattExplorer(int threshold) {
		this.threshold = threshold;
	}

	public Set<IGroum> explorePatterns(List<Groum> D) {
		SubGroumMultiSet L = new SubGroumMultiSet();
		for (Groum groum : D) {
			L.addAll(groum.getAtomicSubGroums());
		}

		SubGroumMultiSet L2 = new SubGroumMultiSet();
		for (IGroum pattern : L.getFrequentPatterns(threshold)) {
			L2.addAll(L.getPatternInstances(pattern));
		}

		SubGroumMultiSet explored = new SubGroumMultiSet();
		for (IGroum pattern : L2.getPatterns()) {
			explored.addAll(explore(pattern, L2, D));
		}
		L2.addAll(explored);

		return L2.getPatterns();
	}

	private SubGroumMultiSet explore(IGroum P, SubGroumMultiSet L, List<Groum> D) {
		SubGroumMultiSet patterns = L.copy();
		SubGroumMultiSet newPatterns = new SubGroumMultiSet();

		for (IGroum U : L.getPatterns()) {
			if (U.getNodeCount() == 1) {
				SubGroumMultiSet Q = new SubGroumMultiSet();

				for (SubGroum occurrence : patterns.getPatternInstances(P)) {
					List<SubGroum> candidates = occurrence.computeExtensions(U.getRoot());
					for (SubGroum candidate : candidates) {
						Q.add(candidate);
					}
				}

				for (IGroum candidate : Q.getFrequentPatterns(threshold)) {
					patterns.addAll(Q.getPatternInstances(candidate));

					SubGroumMultiSet explored = explore(candidate, patterns, D);

					newPatterns.addAll(Q.getPatternInstances(candidate));
					newPatterns.addAll(explored);
				}
			}
		}
		return newPatterns;
	}
}
