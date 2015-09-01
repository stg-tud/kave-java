package cc.kave.commons.model.groum;

import cc.kave.commons.model.groum.Node;

public class TestNode extends Node {

	private String id;

	public TestNode(String id) {
		this.id = id;
	}
	
	@Override
	public String getId() {
		return id;
	}
}
