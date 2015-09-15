package cc.kave.commons.model.groum;

import java.util.HashSet;
import java.util.Set;

import cc.kave.commons.model.groum.comparator.DFSGroumComparator;
import cc.kave.commons.model.groum.comparator.HashCodeComparator;

import com.google.common.collect.TreeMultimap;

public class SubGroumMultiSet {
	private TreeMultimap<IGroum, SubGroum> data = TreeMultimap.create(new DFSGroumComparator(),
			new HashCodeComparator());

	public SubGroumMultiSet() {
	}

	public SubGroumMultiSet(SubGroumMultiSet other) {
		data.putAll(other.data);
	}

	public void add(SubGroum instance) {
		data.put(instance, instance);
	}

	public void addAll(Iterable<SubGroum> instances) {
		instances.forEach(instance -> add(instance));
	}

	public void addAll(SubGroumMultiSet other) {
		data.putAll(other.data);
	}

	public Set<IGroum> getPatterns() {
		return new HashSet<>(data.keySet());
	}

	public SubGroumMultiSet getFrequentSubSet(int threshold) {
		SubGroumMultiSet subset = new SubGroumMultiSet();
		for (IGroum pattern : getPatterns()) {
			if (getPatternFrequency(pattern) >= threshold) {
				subset.addAll(getPatternInstances(pattern));
			}
		}
		return subset;
	}

	private int getPatternFrequency(IGroum pattern) {
		return data.get(pattern).size();
	}

	public Set<SubGroum> getAllInstances() {
		return new HashSet<>(data.values());
	}

	public Set<SubGroum> getPatternInstances(IGroum pattern) {
		return new HashSet<>(data.get(pattern));
	}
}