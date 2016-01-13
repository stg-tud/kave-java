package cc.kave.commons.model.groum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

public class SubGroum implements IGroum {
	private final Groum parent;
	private final Set<Node> nodes = new HashSet<>();
	private Node root;

	public static SubGroumMultiSet getAtomicSubGroums(Groum groum) {
		SubGroumMultiSet atomics = new SubGroumMultiSet();
		for (Node node : groum.getAllNodes()) {
			Set<Node> nodes = Collections.singleton(node);
			SubGroum subGroum = new SubGroum(groum, nodes);
			atomics.add(subGroum);
		}
		return atomics;
	}

	public SubGroum(Groum parent, Set<Node> nodes) {
		this.parent = parent;
		nodes.forEach(node -> addNode(node));
	}

	private void addNode(Node node) {
		if (!parent.containsNode(node))
			throw new IllegalArgumentException("cannot add a node that is not part of the parent");
		nodes.add(node);
	}

	@Override
	public int getNodeCount() {
		return nodes.size();
	}

	@Override
	public Set<Node> getPredecessors(Node node) {
		Set<Node> predecessors = new HashSet<Node>();
		Set<Node> parentPredecessors = parent.getPredecessors(node);
		for (Node predecessor : parentPredecessors) {
			if (nodes.contains(predecessor)) {
				predecessors.add(predecessor);
			}
		}
		return predecessors;
	}

	@Override
	public Set<Node> getSuccessors(Node node) {
		Set<Node> successors = new HashSet<Node>();
		Set<Node> parentSuccessors = parent.getSuccessors(node);
		for (Node successor : parentSuccessors) {
			if (nodes.contains(successor)) {
				successors.add(successor);
			}
		}
		return successors;
	}

	public List<SubGroum> computeExtensions(Node newNode) {
		List<SubGroum> extensions = new ArrayList<>();
		Set<Node> neighborNodes = getAllNodesReachableInOneHop();
		for (Node candidate : neighborNodes) {
			if (candidate.compareTo(newNode) == 0) {
				extensions.add(createExtension(candidate));
			}
		}
		return extensions;
	}

	private Set<Node> getAllNodesReachableInOneHop() {
		Set<Node> neighborNodes = new HashSet<>();
		for (Node node : nodes) {
			neighborNodes.addAll(parent.getSuccessors(node));
		}
		neighborNodes.removeAll(nodes);
		return neighborNodes;
	}

	@Override
	public Node getRoot() {
		if (root == null) {
			root = findRoot();
		}
		return root;
	}

	private Node findRoot() {
		HashSet<Node> candidates = new HashSet<>(nodes);
		for (Node node : nodes) {
			Set<Node> successors = getSuccessors(node);
			candidates.removeAll(successors);
		}
		if (candidates.size() == 1) {
			return candidates.iterator().next();
		} else {
			throw new IllegalStateException("Groum has no uniquly identifiable root (candidates: " + candidates + ")");
		}
	}

	private SubGroum createExtension(Node extnode) {
		SubGroum extension = new SubGroum(parent, nodes);
		extension.nodes.add(extnode);
		return extension;
	}

	private Set<Pair<Node, Node>> getAllEdges() {
		Set<Pair<Node, Node>> edges = new HashSet<>();
		for (Node node : nodes) {
			Set<Node> successors = parent.getSuccessors(node);
			for (Node suc : successors) {
				if (nodes.contains(suc)) {
					edges.add(Pair.of(node, suc));
				}
			}
		}
		return edges;
	}

	@Override
	public String toString() {
		return String.format("(%s, %s)", nodes, getAllEdges());
	}
}
