package cc.kave.commons.model.groum;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SubGroum extends Groum {
	protected Groum parent;

	public SubGroum(Groum parent) {
		super();
		this.parent = parent;
	}

	public SubGroum() {
		super();
		this.parent = null;
	}

	public void setParent(Groum parent) {
		this.parent = parent;
	}

	public Groum getParent() {
		return parent;
	}

	public List<SubGroum> extensibleWith(SubGroum groum) {
		Node extendingNode = groum.getAllNodes().iterator().next();

		if (extendingNode == null)
			return null;

		List<SubGroum> extendedGroums = new LinkedList<>();
		List<Node> extendingNodes = new LinkedList<>();

		for (Node node : this.getAllNodes()) {
			Set<Node> successors = parent.getSuccessors(node);
			if (successors.size() == 0)
				continue;
			else {
				for (Node candidate : successors) {
					if (!(this.containsNode(candidate)))
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
			SubGroum extendedSubgroum = (SubGroum) this.clone();
			extendedSubgroum.addNode(extnode);
			for (Node node : this.getAllNodes()) {
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

		if (extendedGroums.isEmpty())
			return null;
		else
			return extendedGroums;
	}

	@Deprecated
	public SubGroum extensibleWithOneNodeMultipleEdge(SubGroum groum) {
		Node extendingNode = groum.getAllNodes().iterator().next();

		if (extendingNode == null)
			return null;

		SubGroum extendedSubgroum = (SubGroum) this.clone();
		boolean extended = false;
		for (Node node : this.getAllNodes()) {

			Set<Node> successors = parent.getSuccessors(node);
			if (successors.size() == 0)
				continue;
			else {
				for (Node candidate : successors) {
					if (!(this.containsNode(candidate)))
						if (candidate.equals(extendingNode)) {
							if (!extendedSubgroum.containsNode(candidate)) {
								extendedSubgroum.addNode(candidate);
								extended = true;
							}
							extendedSubgroum.addEdge(node, candidate);
						} else {
							continue;
						}
				}
			}
		}
		if (extended)
			return extendedSubgroum;
		else
			return null;
	}
}
