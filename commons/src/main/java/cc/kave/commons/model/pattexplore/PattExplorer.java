package cc.kave.commons.model.pattexplore;

import java.util.LinkedList;
import java.util.List;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.ISubGroum;
import cc.kave.commons.model.groum.impl.comparator.SubGroumComparator;
import cc.kave.commons.model.groum.impl.comparator.SubGroumIdentComparator;

import com.google.common.collect.TreeMultimap;

public class PattExplorer implements IPattExplorer {
	int threshold;

	public PattExplorer(int threshold) {
		this.threshold = threshold;
	}

	@Override
	public List<ISubGroum> explorePatterns(List<IGroum> D) {

		TreeMultimap<ISubGroum, ISubGroum> L = TreeMultimap.create(new SubGroumComparator(),
				new SubGroumIdentComparator());

		for (IGroum groum : D) {

			List<ISubGroum> subgroums = Utils.breakdown(groum);
			for (ISubGroum subgroum : subgroums) {
				L.put(subgroum, subgroum);
			}
		}

		List<ISubGroum> smallPatterns = new LinkedList<>();
		for (ISubGroum subgroum : L.keySet()) {
			if (L.get(subgroum).size() < threshold) {
				smallPatterns.add(subgroum);
			}
		}

		for (ISubGroum smallpattern : smallPatterns) {
			L.removeAll(smallpattern);
		}

		TreeMultimap<ISubGroum, ISubGroum> explored = TreeMultimap.create(new SubGroumComparator(),
				new SubGroumIdentComparator());

		for (ISubGroum pattern : L.keySet()) {
			explored.putAll(explore(pattern, L, D));
		}
		L.putAll(explored);
		LinkedList<ISubGroum> distinctPatterns = new LinkedList<>();
		distinctPatterns.addAll(L.keySet());
		return distinctPatterns;

	}

	private TreeMultimap<ISubGroum, ISubGroum> explore(ISubGroum P, TreeMultimap<ISubGroum, ISubGroum> L, List<IGroum> D) {
		TreeMultimap<ISubGroum, ISubGroum> patterns = TreeMultimap.create(new SubGroumComparator(),
				new SubGroumIdentComparator());
		TreeMultimap<ISubGroum, ISubGroum> newPatterns = TreeMultimap.create(new SubGroumComparator(),
				new SubGroumIdentComparator());

		patterns.putAll(L);

		for (ISubGroum U : L.keySet()) {
			if (U.getAllNodes().size() == 1) {

				TreeMultimap<ISubGroum, ISubGroum> Q = TreeMultimap.create(new SubGroumComparator(),
						new SubGroumIdentComparator());

				for (ISubGroum occurrence : patterns.get(P)) {
					ISubGroum candidate = occurrence.extensibleWith(U);
					if (candidate != null)
						Q.put(candidate, candidate);
				}

				for (ISubGroum candidate : Q.keySet()) {
					if (Q.get(candidate).size() >= threshold) {
						newPatterns.putAll(candidate, Q.get(candidate));
						patterns.putAll(newPatterns);

						newPatterns.putAll(explore(candidate, patterns, D));
					}
				}
			}
		}
		return newPatterns;
	}
}
