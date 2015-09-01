package cc.kave.commons.model.groum.nodes;

import cc.kave.commons.model.groum.INode;

public class ActionNode implements INode {
	private String clazz;
	private String callee;

	public ActionNode(String clazz, String callee) {
		this.clazz = clazz;
		this.callee = callee;
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
