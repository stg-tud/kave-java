package cc.kave.commons.model.groum.nodes;

import cc.kave.commons.model.groum.INode;

public class ActionNode implements INode {
	private String clazz;
	private String callee;

	public ActionNode(String clazz, String callee) {
		this.clazz = clazz;
		this.callee = callee;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((callee == null) ? 0 : callee.hashCode());
		result = prime * result + ((clazz == null) ? 0 : clazz.hashCode());
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
		ActionNode other = (ActionNode) obj;
		if (callee == null) {
			if (other.callee != null)
				return false;
		} else if (!callee.equals(other.callee))
			return false;
		if (clazz == null) {
			if (other.clazz != null)
				return false;
		} else if (!clazz.equals(other.clazz))
			return false;
		return true;
	}

	public String getClazz() {
		return clazz;
	}

	public String getCallee() {
		return callee;
	}

	@Override
	public String toString() {
		return String.format("%s.%s", clazz, callee);
	}

	@Override
	public int compareTo(INode o) {
		return toString().compareTo(o.toString());
	}

}
