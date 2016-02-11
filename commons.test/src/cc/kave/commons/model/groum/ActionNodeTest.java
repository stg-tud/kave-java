package cc.kave.commons.model.groum;

import static org.junit.Assert.*;

import org.junit.Test;

import cc.kave.commons.model.groum.nodes.ActionNode;

public class ActionNodeTest extends NodeContractTest {

	@Override
	protected Node createNode(String id) {
		return new ActionNode(id, "foo()");
	}

	@Test
	public void serializes() {
		Node first = createNode("A");

		assertEquals("A.foo()", first.toString());
	}
}