package cc.kave.commons.model.groum;

import cc.kave.commons.model.groum.INode;

public class TestNode implements INode {

	private String id;

	public TestNode(String id) {
		this.id = id;
	}
	
	@Override
	public int compareTo(INode o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
