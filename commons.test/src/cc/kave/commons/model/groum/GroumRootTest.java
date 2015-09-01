package cc.kave.commons.model.groum;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static cc.kave.commons.model.groum.GroumTestUtils.*;

public class GroumRootTest {

	@Test
	public void singleNodeIsRoot() {
		TestNode node = new TestNode("A");
		Groum groum = createGroum(node);

		assertRoot(node, groum);
	}

	@Test
	public void nodeWithoutIncomingEdgeIsRoot() {
		TestNode node1 = new TestNode("A");
		TestNode node2 = new TestNode("B");
		TestNode node3 = new TestNode("C");
		Groum groum = createGroum(node2, node1, node3);
		groum.addEdge(node1, node2);
		groum.addEdge(node1, node3);
		
		assertRoot(node1, groum);
	}
	
	@Test(expected=IllegalStateException.class)
	public void multipleRootsFails() {
		Groum groum = createGroum(new TestNode("A"), new TestNode("B"));
		
		groum.getRoot();
	}

	private void assertRoot(TestNode node, Groum groum) {
		INode root = groum.getRoot();
		assertEquals(node, root);
	}
}
