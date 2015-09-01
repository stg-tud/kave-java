package cc.kave.commons.model.groum;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public abstract class NodeContractTest {

	protected abstract INode createNode(String id);

	@Test
	public void isReferenceEquals() {
		INode node = createNode("A");

		assertEquals(node, node);
	}

	@Test
	public void isNotValueEquals() {
		INode first = createNode("A");
		INode second = createNode("A");

		assertNotEquals(first, second);
	}

	@Test
	public void isNotEqual() {
		INode first = createNode("A");
		INode second = createNode("B");

		assertNotEquals(first, second);
	}

	@Test
	public void differentHashcode() {
		INode first = createNode("A");
		INode second = createNode("A");

		assertNotEquals(first.hashCode(), second.hashCode());
	}
}
