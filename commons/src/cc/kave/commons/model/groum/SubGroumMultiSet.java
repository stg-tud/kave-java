package cc.kave.commons.model.groum;

import java.util.HashSet;
import java.util.Set;

import cc.kave.commons.model.groum.comparator.DFSGroumComparator;
import cc.kave.commons.model.groum.comparator.HashCodeComparator;

import com.google.common.collect.TreeMultimap;

public class SubGroumMultiSet {
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

	public Set<SubGroum> getAllInstances() {
		return new HashSet<>(data.values());
	}

	public int getPatternFrequency(SubGroum pattern) {
		return data.get(pattern).size();
	}

	public Set<SubGroum> getPatternInstances(SubGroum pattern) {
		return new HashSet<>(data.get(pattern));
	}
}