package cc.kave.commons.model.groum;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import cc.kave.commons.model.groum.comparator.DFSGroumComparator;
import static cc.kave.commons.model.groum.GroumTestUtils.*;

public class DFSGroumComparatorTest {

	@Test
	public void singleEqualNode() {
		assertEqualGroums(createGroum(new TestNode("A")),
				createGroum(new TestNode("A")));
	}

	@Test
	public void singleUnequalNode() {
		assertSmallerGroum(createGroum(new TestNode("A")),
				createGroum(new TestNode("B")));
	}
	
	@Test
	public void threeNodesEqual() {
		Node[] nodes1 = createNodes("A", "B", "C");
		Groum groum1 = createGroum(nodes1);
		groum1.addEdge(nodes1[0], nodes1[1]);
		groum1.addEdge(nodes1[0], nodes1[2]);

		Node[] nodes2 = createNodes("A", "B", "C");
		Groum groum2 = createGroum(nodes2);
		groum2.addEdge(nodes2[0], nodes2[1]);
		groum2.addEdge(nodes2[0], nodes2[2]);
		
		assertEqualGroums(groum1, groum2);
	}
	
	/*
	 *  A                       A
	 *  | \  is different from  | \
	 *  B  C                    B  D
	 *  
	 *  The first Groum is considered smaller, since node C is smaller than
	 *  node D.
	 */
	@Test
	public void threeNodesDifferentNodes() {
		Node[] nodes1 = createNodes("A", "B", "C");
		Groum groum1 = createGroum(nodes1);
		groum1.addEdge(nodes1[0], nodes1[1]);
		groum1.addEdge(nodes1[0], nodes1[2]);

		Node[] nodes2 = createNodes("A", "B", "D");
		Groum groum2 = createGroum(nodes2);
		groum2.addEdge(nodes2[0], nodes2[1]);
		groum2.addEdge(nodes2[0], nodes2[2]);
		
		assertSmallerGroum(groum1, groum2);
	}
	
	/*
	 *   A
	 *   | \  is different from  A -> B -> C
	 *   B  C
	 *   
	 *   The first Groum is considered smaller, since it's node B has less
	 *   successors.
	 */
	@Test
	public void threeNodesDifferentEdges() {
		Node[] nodes1 = createNodes("A", "B", "C");
		Groum groum1 = createGroum(nodes1);
		groum1.addEdge(nodes1[0], nodes1[1]);
		groum1.addEdge(nodes1[0], nodes1[2]);

		Node[] nodes2 = createNodes("A", "B", "C");
		Groum groum2 = createGroum(nodes2);
		groum2.addEdge(nodes2[0], nodes2[1]);
		groum2.addEdge(nodes2[1], nodes2[2]);
		
		assertSmallerGroum(groum1, groum2);
	}
	
	/*
	 *   A                                A -> C
	 *  |   \  is indistinguishable from  |
	 *  B -> C                            B -> C
	 */
	@Test
	public void indistinguishable() {
		Node[] nodes1 = createNodes("A", "B", "C");
		Groum groum1 = createGroum(nodes1);
		groum1.addEdge(nodes1[0], nodes1[1]);
		groum1.addEdge(nodes1[0], nodes1[2]);
		groum1.addEdge(nodes1[1], nodes1[2]);

		Node[] nodes2 = createNodes("A", "B", "C", "C");
		Groum groum2 = createGroum(nodes2);
		groum2.addEdge(nodes2[0], nodes2[1]);
		groum2.addEdge(nodes2[0], nodes2[2]);
		groum2.addEdge(nodes2[1], nodes2[3]);
		
		assertEqualGroums(groum1, groum2);
	}

	private static void assertSmallerGroum(Groum groum1, Groum groum2) {
		int comparison = new DFSGroumComparator().compare(groum1, groum2);
		assertEquals(-1, comparison);
	}

	private static void assertEqualGroums(Groum groum1, Groum groum2) {
		int comparison = new DFSGroumComparator().compare(groum1, groum2);
		assertEquals(0, comparison);
	}
}
