package cc.kave.commons.model.groum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SubGroum implements IGroum {
	private final Groum parent;
	private final Set<Node> nodes = new HashSet<>();

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
}
