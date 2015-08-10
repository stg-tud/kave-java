package cc.kave.commons.model.groum.nodes.impl;

import cc.kave.commons.model.groum.INode;
import cc.kave.commons.model.groum.nodes.IActionNode;

public class ActionNode extends Node implements IActionNode {
	private String clazz;
	private String callee;

	public ActionNode() {
		super();
	}

	public ActionNode(String clazz, String callee) {
		this();
		this.clazz = clazz;
		this.callee = callee;
	}

	// @Override
	// public boolean equals(Object obj) {
	// if (obj instanceof INode) {
	// return equals((INode) obj);
	// } else {
	// return false;
	// }
	// }
	//
	// @Override
	// public boolean equals(INode anotherNode) {
	// if (!(anotherNode instanceof ActionNode))
	// return false;
	// else {
	// ActionNode aNode = (ActionNode) anotherNode;
	// if ((aNode.getCallee().compareTo(this.callee) == 0) &&
	// (aNode.getClazz().compareTo(this.clazz) == 0)
	// && (getDataDependencies().equals(aNode.getDataDependencies())))
	// return true;
	// else
	// return false;
	// }
	// }

	@Override
	public boolean equals(Object anotherNode) {
		if (!(anotherNode instanceof ActionNode))
			return false;
		else {
			// if (this.hashCode() != anotherNode.hashCode())
			// return false;
			// else {
			ActionNode aNode = (ActionNode) anotherNode;
			if ((getCallee().equals(aNode.getCallee())) && (aNode.getClazz().equals(this.clazz))
					&& (getDataDependencies().equals(aNode.getDataDependencies())))
				return true;
			else
				return false;
			// }
		}
	}

	// @Override
	// public int hashCode() {
	// return System.identityHashCode(this);
	// }

	@Override
	public String getClazz() {
		return clazz;
	}

	@Override
	public String getCallee() {
		return callee;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public void setCallee(String callee) {
		this.callee = callee;
	}

	@Override
	public String toString() {
		return String.format("(%s.%s)", clazz, callee);
	}

	@Override
	public int compareTo(INode o) {
		return toString().compareTo(o.toString());
	}

}
