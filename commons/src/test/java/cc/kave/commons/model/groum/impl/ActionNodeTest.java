package cc.kave.commons.model.groum.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import cc.kave.commons.model.groum.nodes.IActionNode;
import cc.kave.commons.model.groum.nodes.IControlNode;
import cc.kave.commons.model.groum.nodes.impl.ActionNode;
import cc.kave.commons.model.groum.nodes.impl.ControlNode;

public class ActionNodeTest {

	@Test
	public void nodesAreEqual() {
		ActionNode first = new ActionNode("String", IActionNode.CONTRUCTOR);
		ActionNode second = new ActionNode("String", IActionNode.CONTRUCTOR);
		assertTrue(first.equals(second));
	}

	@Test
	public void nodesAreIdentical() {
		ActionNode first = new ActionNode("String", IActionNode.CONTRUCTOR);
		ActionNode second = new ActionNode("String", IActionNode.CONTRUCTOR);
		assertFalse(first == second);
	}

	@Test
	public void nodesHaveDifferentDependencies() {
		ActionNode first = new ActionNode("String", IActionNode.CONTRUCTOR);
		first.addDependency("someField");
		ActionNode second = new ActionNode("String", IActionNode.CONTRUCTOR);
		second.addDependency("Somefield");
		assertFalse(first.equals(second));
	}

	@Test
	public void nodesHaveSameDependencies() {
		ActionNode first = new ActionNode("String", IActionNode.CONTRUCTOR);
		first.addDependency("someField");
		ActionNode second = new ActionNode("String", IActionNode.CONTRUCTOR);
		second.addDependency("someField");
		assertTrue(first.equals(second));
	}

	@Test
	public void testSetEquality() {
		Set<String> set1 = new TreeSet<>();
		Set<String> set2 = new TreeSet<>();
		set1.add("A");
		set1.add("B");
		set2.add("B");
		set2.add("A");

		assertTrue(set1.equals(set2));
	}

	@Test
	public void ActionNodedrawsString() {
		ActionNode first = new ActionNode("String", IActionNode.CONTRUCTOR);
		assertTrue(first.toString().equals("(String.<init>)"));
	}

	@Test
	public void ControlNodedrawsString() {
		ControlNode node = new ControlNode(IControlNode.DO_WHILE_NODE);
		assertTrue(node.toString().equals("(DO_WHILE)"));
	}

	@Test
	public void differentHashcode() {
		ActionNode first = new ActionNode("String", IActionNode.CONTRUCTOR);
		ActionNode second = new ActionNode("String", IActionNode.CONTRUCTOR);
		// System.out.println(String.format("%s - %s", first.hashCode(),
		// second.hashCode()));
		assertFalse(first.hashCode() == second.hashCode());
	}

}
