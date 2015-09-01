package cc.kave.commons.model.groum.nodes;

import cc.kave.commons.model.groum.Node;

public class ActionNode extends Node {
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
	public String getId() {
		return String.format("%s.%s", clazz, callee);
	}
}
