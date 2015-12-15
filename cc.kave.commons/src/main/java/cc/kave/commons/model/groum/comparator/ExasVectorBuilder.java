package cc.kave.commons.model.groum.comparator;

import java.util.Set;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.Node;

import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

public class ExasVectorBuilder {

	/**
	 * The Exas paper (Accurate and efficient structural characteristic feature
	 * extraction for clone detection) computes all sub-paths for the vectors.
	 * I'm not sure we need that, as I could come up with an example where it
	 * makes a difference. For now, I followed the paper to have an exact
	 * reimplementation. However, we should evaluate whether it makes a
	 * difference setting this switch to false.
	 */
	private static final boolean ComputeSubPaths = true;

	public ExasVector build(IGroum groum) {
		return new ExasVector(buildNPaths(groum), buildPQNodes(groum));
	}

	private SortedMultiset<Path> buildNPaths(IGroum groum) {
		return buildNPaths(groum, new Path(), groum.getRoot());
	}

	private SortedMultiset<Path> buildNPaths(IGroum groum, Path base, Node newNode) {
		SortedMultiset<Path> paths = TreeMultiset.create();
		Path extension = base.getExtension(newNode);
		Set<Node> successors = groum.getSuccessors(newNode);

		if (ComputeSubPaths) {
			Path tail = extension;
			do {
				paths.add(tail);
				tail = tail.getTail();
			} while (!tail.isEmpty());
		} else if (successors.isEmpty()) {
			paths.add(extension);
		}

		for (Node suc : successors) {
			paths.addAll(buildNPaths(groum, extension, suc));
		}
		return paths;
	}

	private SortedMultiset<PQNode> buildPQNodes(IGroum groum) {
		return buildPQNodes(groum, groum.getRoot());
	}

	private SortedMultiset<PQNode> buildPQNodes(IGroum groum, Node node) {
		SortedMultiset<PQNode> pqNodes = TreeMultiset.create();
		Set<Node> successors = groum.getSuccessors(node);
		int numberOfPredecessors = groum.getPredecessors(node).size();
		pqNodes.add(new PQNode(node, numberOfPredecessors, successors.size()));
		for (Node successor : successors) {
			pqNodes.addAll(buildPQNodes(groum, successor));
		}
		return pqNodes;
	}
}