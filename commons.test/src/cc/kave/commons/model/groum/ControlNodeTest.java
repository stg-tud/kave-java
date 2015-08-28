package cc.kave.commons.model.groum;

import static org.junit.Assert.*;

import org.junit.Test;

import cc.kave.commons.model.groum.nodes.ControlNode;

public class ControlNodeTest {

	@Test
	public void isEqual() {
		ControlNode first = new ControlNode("WHILE");
		ControlNode second = new ControlNode("WHILE");
		assertEquals(first, second);
	}

	@Test
	public void isNotEqual() {
		ControlNode first = new ControlNode("WHILE");
		ControlNode second = new ControlNode("IF");
		assertNotEquals(first, second);
	}

	@Test
	public void serializes() {
		ControlNode first = new ControlNode("WHILE");

		assertEquals("WHILE", first.toString());
	}

	@Test
	public void differentHashcode() {
		ControlNode first = new ControlNode("WHILE");
		ControlNode second = new ControlNode("WHILE");

		assertNotEquals(first.hashCode(), second.hashCode());
	}

}
