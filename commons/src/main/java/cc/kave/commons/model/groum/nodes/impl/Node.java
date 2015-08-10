package cc.kave.commons.model.groum.nodes.impl;

import java.util.Set;
import java.util.TreeSet;

import cc.kave.commons.model.groum.INode;

public abstract class Node implements INode, Comparable<INode> {
	Set<String> dependencies;

	@Override
	public Set<String> getDataDependencies() {
		return dependencies;
	}

	public Node() {
		dependencies = new TreeSet<>();
	}

	@Override
	public boolean hasDataDependencyTo(INode anotherNode) {
		for (String dep : dependencies) {
			if (anotherNode.getDataDependencies().contains(dep))
				return true;
		}
		return false;
	}

	@Override
	public void addDependency(String dep) {
		dependencies.add(dep);
	}

	@Override
	public abstract int compareTo(INode o);

}
