package cc.kave.commons.model.groum.nodes;

import cc.kave.commons.model.groum.INode;

public interface IControlNode extends INode {
	public final static String FOR_NODE = "FOR";
	public final static String WHILE_NODE = "WHILE";
	public final static String DO_WHILE_NODE = "DO_WHILE";
	public final static String IF_NODE = "IF";

	public String getKind();

}
