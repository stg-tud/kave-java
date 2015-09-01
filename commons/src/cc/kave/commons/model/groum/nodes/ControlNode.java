package cc.kave.commons.model.groum.nodes;

import cc.kave.commons.model.groum.INode;

public class ControlNode implements INode {
	private String kind;

	public ControlNode(String kind) {
		this.kind = kind;
	}

	public String getKind() {
		return kind;
	}

	@Override
	public String toString() {
		return String.format("%s", kind);
	}

	@Override
	public int compareTo(INode o) {
		return toString().compareTo(o.toString());
	}
}
