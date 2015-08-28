package cc.kave.commons.model.groum.nodes;

import cc.kave.commons.model.groum.INode;

public class ControlNode extends Node implements IControlNode {
	private String kind;

	public ControlNode() {
		super();
	}

	public ControlNode(String kind) {
		this();
		this.kind = kind;
	}

	@Override
	public boolean equals(Object anotherNode) {
		if (!(anotherNode instanceof ControlNode))
			return false;
		else {
			ControlNode cNode = (ControlNode) anotherNode;
			if ((cNode.getKind().equals(this.kind)) && (this.dependencies.equals(cNode.getDataDependencies())))
				return true;
			else
				return false;
		}
	}

	@Override
	public String getKind() {
		return kind;
	}

	@Override
	public String toString() {
		return String.format("(%s)", kind);
	}

	@Override
	public int compareTo(INode o) {
		return toString().compareTo(o.toString());
	}
}
