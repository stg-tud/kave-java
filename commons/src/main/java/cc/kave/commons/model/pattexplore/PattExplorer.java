package cc.kave.commons.model.pattexplore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.ISubGroum;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

public class PattExplorer implements IPattExplorer {
	int threshold;

	public PattExplorer(int threshold) {
		this.threshold = threshold;
	}

	@Override
	public List<ISubGroum> explorePatterns(List<IGroum> D) {
		List<ISubGroum> L = new LinkedList<>();

		for (IGroum groum : D) {
			L.addAll(Utils.breakdown(groum));
		}

		Multiset<ISubGroum> a = TreeMultiset.create(new GroumComparator());

		List<List<ISubGroum>> isomorphGroups = Utils.makeIsomorphGroups(L);
		for (List<ISubGroum> aList : isomorphGroups) {
			if (aList.size() < threshold) {
				L = Utils.removeEqualPatterns(aList.get(0), L);
			}
		}

		List<ISubGroum> patterns = new ArrayList<>();
		for (ISubGroum pattern : L) {
			patterns.addAll(explore(pattern, L, D));
		}

		return patterns;

	}

	private List<ISubGroum> explore(ISubGroum P, List<ISubGroum> L, List<IGroum> D) {
		List<ISubGroum> patterns = new ArrayList<>();
		patterns.addAll(L);
		for (ISubGroum U : L) {
			if (U.getAllNodes().size() != 1)
				break;

			ISubGroum Q = P.extensibleWith(U);
			if (Q != null) {
				List<ISubGroum> candidates = new LinkedList<>();

				for (ISubGroum pattern : L) {
					if (pattern.equals(P)) {
						ISubGroum candidate = pattern.extensibleWith(U);
						if (candidate != null)
							candidates.add(candidate);
					}
				}
				if (patterns.size() >= threshold) {
					patterns.addAll(patterns);
					patterns.addAll(explore(Q, patterns, D));
				}
			}

		}
		return patterns;
	}

}
