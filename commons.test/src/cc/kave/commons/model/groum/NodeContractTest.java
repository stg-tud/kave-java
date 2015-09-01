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
	
	@Test
	public void comparesEqual() {
		Node node1 = createNode("A");
		Node node2 = createNode("A");
		
		assertEquals(0, node1.compareTo(node2));
	}
	
	@Test
	public void comparesSmaller() {
		Node node1 = createNode("A");
		Node node2 = createNode("B");
		
		assertEquals(-1, node1.compareTo(node2));
	}
	
	@Test
	public void comparesLarger() {
		Node node1 = createNode("B");
		Node node2 = createNode("A");
		
		assertEquals(1, node1.compareTo(node2));
	}
}
