package cc.kave.commons.model.groum.nodes;

import cc.kave.commons.model.groum.INode;

public class ControlNode implements INode {
	private String kind;

	public ControlNode(String kind) {
		this.kind = kind;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kind == null) ? 0 : kind.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ControlNode other = (ControlNode) obj;
		if (kind == null) {
			if (other.kind != null)
				return false;
		} else if (!kind.equals(other.kind))
			return false;
		return true;
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
