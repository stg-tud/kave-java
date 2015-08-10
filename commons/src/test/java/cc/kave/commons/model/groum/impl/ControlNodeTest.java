package cc.kave.commons.model.groum.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.model.groum.nodes.IControlNode;
import cc.kave.commons.model.groum.nodes.impl.ControlNode;

public class ControlNodeTest {

	@Test
	public void nodesAreEqual() {
		ControlNode first = new ControlNode(IControlNode.FOR_NODE);
		ControlNode second = new ControlNode(IControlNode.FOR_NODE);
		assertTrue(first.equals(second));
	}

	@Test
	public void nodesNotAreEqual() {
		ControlNode first = new ControlNode(IControlNode.FOR_NODE);
		ControlNode second = new ControlNode(IControlNode.WHILE_NODE);
		assertFalse(first.equals(second));
	}

	@Test
	public void nodesHaveDifferentDependencies() {
		ControlNode first = new ControlNode(IControlNode.FOR_NODE);
		first.addDependency("fieldName");
		ControlNode second = new ControlNode(IControlNode.FOR_NODE);
		assertFalse(first.equals(second));
	}

	@Test
	public void nodesHaveSameDependencies() {
		ControlNode first = new ControlNode(IControlNode.FOR_NODE);
		first.addDependency("fieldName");
		ControlNode second = new ControlNode(IControlNode.FOR_NODE);
		second.addDependency("fieldName");
		assertTrue(first.equals(second));
	}
}
