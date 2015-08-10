package cc.kave.commons.model.pattexplore;

import java.util.LinkedList;
import java.util.List;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.ISubGroum;

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
		List<List<ISubGroum>> isomorphGroups = Utils.makeIsomorphGroups(L);
		for (List<ISubGroum> aList : isomorphGroups) {
			if (aList.size() < threshold) {
				L = Utils.removeEqualPatterns(aList.get(0), L);
			}
		}

		for (ISubGroum pattern : L) {
			explore(pattern, L, D);
		}

		return L;

	}

	private void explore(ISubGroum P, List<ISubGroum> L, List<IGroum> D) {
		for (ISubGroum U : L) {
			if (U.getAllNodes().size() != 1)
				break;

			ISubGroum Q = P.extensibleWith(U);
			if (Q != null) {
				List<ISubGroum> patterns = new LinkedList<>();

				for (ISubGroum pattern : L) {
					if (pattern.equals(P)) {
						ISubGroum candidate = pattern.extensibleWith(U);
						if (candidate != null)
							patterns.add(candidate);
					}
				}
				if (patterns.size() >= threshold) {
					L.addAll(patterns);
					explore(Q, L, D);
				}
			}

		}
	}

}
