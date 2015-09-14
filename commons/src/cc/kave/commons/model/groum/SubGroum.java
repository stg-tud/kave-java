package cc.kave.commons.model.groum;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SubGroum extends Groum {
	private final Groum parent;
	private final Set<Node> nodes = new HashSet<>();

	public SubGroum(Groum parent) {
		super();
		this.parent = parent;
	}

	public SubGroum() {
		super();
		this.parent = null;
	}

	@Override
	public void addNode(Node node) {
		if (!parent.containsNode(node))
			throw new IllegalArgumentException("cannot add a node that is not part of the parent");
		super.addNode(node);
		nodes.add(node);
	}

	@Override
	public Set<Node> getAllNodes() {
		return nodes;
	}

	@Override
	public void addEdge(Node source, Node target) {
		if (!parent.getSuccessors(source).contains(target)) {
			throw new IllegalArgumentException("cannot add an edge that is not part of the parent");
		}
		// super.addEdge(source, target);
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

	public List<SubGroum> computeExtensions(Node extendingNode) {
		List<SubGroum> extendedGroums = new LinkedList<>();
		List<Node> extendingNodes = new LinkedList<>();

		for (Node node : nodes) {
			Set<Node> successors = parent.getSuccessors(node);
			if (successors.size() == 0)
				continue;
			else {
				for (Node candidate : successors) {
					if (!(nodes.contains(candidate)))
						if (candidate.compareTo(extendingNode) == 0) {
							if (extendingNodes.isEmpty())
								extendingNodes.add(candidate);
							else {
								boolean isnewnode = true;
								for (Node extnode : extendingNodes) {
									if (extnode == candidate) {
										isnewnode = false;
										continue;
									}
								}
								if (isnewnode)
									extendingNodes.add(candidate);
							}
						}
				}
			}
		}
		// Build extended groums
		for (Node extnode : extendingNodes) {
			SubGroum extendedSubgroum = this.copy();
			extendedSubgroum.addNode(extnode);
			for (Node node : nodes) {
				Set<Node> nodesuccessors = parent.getSuccessors(node);
				for (Node successor : nodesuccessors) {
					if (successor == extnode) {
						extendedSubgroum.addEdge(node, extnode);
						continue;
					}
				}
			}
			extendedGroums.add(extendedSubgroum);
		}

		return extendedGroums;
	}

	@Override
	public Node getRoot() {
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

	private SubGroum copy() {
		SubGroum clone = new SubGroum(parent);
		clone.nodes.addAll(nodes);
		return clone;
	}
}
