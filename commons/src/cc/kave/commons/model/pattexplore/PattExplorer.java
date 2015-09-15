package cc.kave.commons.model.pattexplore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.SubGroum;
import cc.kave.commons.model.groum.comparator.DFSGroumComparator;
import cc.kave.commons.model.groum.comparator.HashCodeComparator;

import com.google.common.collect.TreeMultimap;

public class PattExplorer {
	int threshold;

	public PattExplorer(int threshold) {
		this.threshold = threshold;
	}

	private static class SubGroumMultiSet {
		private TreeMultimap<SubGroum, SubGroum> data = TreeMultimap.create(new DFSGroumComparator(),
				new HashCodeComparator());

		public void add(SubGroum instance) {
			data.put(instance, instance);
		}

		public void addAll(Iterable<SubGroum> instances) {
			instances.forEach(instance -> add(instance));
		}

		public void addAll(SubGroumMultiSet other) {
			addAll(other.data.values());
		}

		public SubGroumMultiSet copy() {
			SubGroumMultiSet clone = new SubGroumMultiSet();
			clone.data.putAll(data);
			return clone;
		}

		public Set<SubGroum> getPatterns() {
			return new HashSet<>(data.keySet());
		}

		public int getPatternFrequency(SubGroum pattern) {
			return data.get(pattern).size();
		}

		public Set<SubGroum> getPatternInstances(SubGroum pattern) {
			return new HashSet<>(data.get(pattern));
		}
	}

	public List<SubGroum> explorePatterns(List<Groum> D) {
		SubGroumMultiSet L = new SubGroumMultiSet();

		for (Groum groum : D) {
			// TODO make getAtomicSubGroums return a SubGroumMultiSet
			L.addAll(groum.getAtomicSubGroums().values());
		}

		SubGroumMultiSet L2 = new SubGroumMultiSet();
		for (SubGroum pattern : L.getPatterns()) {
			if (L.getPatternFrequency(pattern) >= threshold) {
				L2.addAll(L.getPatternInstances(pattern));
			}
		}

		SubGroumMultiSet explored = new SubGroumMultiSet();
		for (SubGroum pattern : L2.getPatterns()) {
			explored.addAll(explore(pattern, L2, D));
		}
		L2.addAll(explored);

		return new ArrayList<>(L2.getPatterns());

	}

	private SubGroumMultiSet explore(SubGroum P, SubGroumMultiSet L, List<Groum> D) {
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

				for (SubGroum candidate : Q.getPatterns()) {
					if (Q.getPatternFrequency(candidate) >= threshold) {
						patterns.addAll(Q.getPatternInstances(candidate));

						SubGroumMultiSet explored = explore(candidate, patterns, D);

						newPatterns.addAll(Q.getPatternInstances(candidate));
						newPatterns.addAll(explored);
					}
				}
			}
		}
		return newPatterns;
	}
}
