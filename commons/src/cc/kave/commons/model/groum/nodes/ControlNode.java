package cc.kave.commons.model.groum.nodes;

import cc.kave.commons.model.groum.Node;

public class ControlNode extends Node {
	private String kind;

	public ControlNode(String kind) {
		this.kind = kind;
	}

	public String getKind() {
		return kind;
	}

	@Override
	public String getId() {
		return kind;
	}
}
