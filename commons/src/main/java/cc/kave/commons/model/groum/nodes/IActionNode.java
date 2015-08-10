package cc.kave.commons.model.groum.nodes;

import cc.kave.commons.model.groum.INode;

public interface IActionNode extends INode {
	public static final String CONTRUCTOR = "<init>";

	public String getClazz();

	public String getCallee();

}
