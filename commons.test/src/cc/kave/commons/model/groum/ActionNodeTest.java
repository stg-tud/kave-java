package cc.kave.commons.model.groum;

import static org.junit.Assert.*;

import org.junit.Test;

import cc.kave.commons.model.groum.nodes.ActionNode;

public class ActionNodeTest {

	@Test
	public void isEqual() {
		ActionNode first = new ActionNode("String", "length()");
		ActionNode second = new ActionNode("String", "length()");
		
		assertEquals(first ,second);
	}
	
	@Test
	public void isNotEqual() {
		ActionNode first = new ActionNode("String", "length()");
		ActionNode second = new ActionNode("String", "toString()");
		
		assertNotEquals(first ,second);
	}

	@Test
	public void serializes() {
		ActionNode first = new ActionNode("String", "length()");
		
		assertEquals("String.length()", first.toString());
	}

	@Test
	public void differentHashcode() {
		ActionNode first = new ActionNode("String", "length()");
		ActionNode second = new ActionNode("String", "length()");
		
		assertNotEquals(first.hashCode(), second.hashCode());
	}

}
