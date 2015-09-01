package cc.kave.commons.model.groum;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public abstract class NodeContractTest {

	protected abstract Node createNode(String id);

	@Test
	public void isReferenceEquals() {
		Node node = createNode("A");

		assertEquals(node, node);
	}

	@Test
	public void isNotValueEquals() {
		Node first = createNode("A");
		Node second = createNode("A");

		assertNotEquals(first, second);
	}

	@Test
	public void isNotEqual() {
		Node first = createNode("A");
		Node second = createNode("B");

		assertNotEquals(first, second);
	}

	@Test
	public void differentHashcode() {
		Node first = createNode("A");
		Node second = createNode("A");

		assertNotEquals(first.hashCode(), second.hashCode());
	}
}
