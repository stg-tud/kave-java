package cc.kave.commons.model.groum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.Node;
import cc.kave.commons.model.groum.nodes.ActionNode;
import cc.kave.commons.model.groum.nodes.ControlNode;

public class LegacyGroumTest {

	

	@Test
	public void equalityEqualGroums() {
		Groum a = Fixture.getPapersExampleGroum();
		Groum b = Fixture.getPapersExampleGroum();
		assertEquals(a, b);
	}

	@Test
	public void equalityUnequalNodes() {
		ActionNode a = new ActionNode("A", "1");
		ActionNode c = new ActionNode("A", "2");

		Groum groum = new Groum();
		groum.addNode(a);

		Groum groum2 = new Groum();
		groum2.addNode(c);

		assertNotEquals(groum, groum2);
	}

	@Test
	public void equalityUnequalEdges() {
		ActionNode a1 = new ActionNode("A", "1");
		ActionNode a2 = new ActionNode("A", "1");
		ActionNode b1 = new ActionNode("B", "2");
		ActionNode b2 = new ActionNode("B", "2");
		ActionNode c1 = new ActionNode("C", "3");
		ActionNode c2 = new ActionNode("C", "3");

		Groum groum = new Groum();
		groum.addNode(a1);
		groum.addNode(b1);
		groum.addNode(c1);
		groum.addEdge(a1, b1);

		Groum groum2 = new Groum();
		groum2.addNode(a2);
		groum2.addNode(b2);
		groum2.addNode(c2);
		groum2.addEdge(b2, c2);

		assertNotEquals(groum, groum2);
	}

	@Test
	public void equalityWorksFineForMultipleEdges() {
		ActionNode a1 = new ActionNode("A", "1");
		ActionNode a2 = new ActionNode("A", "1");
		ActionNode b1 = new ActionNode("B", "2");
		ActionNode b2 = new ActionNode("B", "2");
		ActionNode c1 = new ActionNode("C", "3");
		ActionNode c2 = new ActionNode("C", "3");

		Groum groum = new Groum();
		groum.addNode(a1);
		groum.addNode(b1);
		groum.addNode(c1);
		groum.addEdge(a1, c1);
		groum.addEdge(a1, b1);

		Groum groum2 = new Groum();
		groum2.addNode(a2);
		groum2.addNode(c2);
		groum2.addNode(b2);
		groum2.addEdge(a2, b2);
		groum2.addEdge(a2, c2);

		assertEquals(groum, groum2);
	}
	
	@Test
	public void equalityWorksUnordered() {
		Node node1 = new ActionNode("1", "1");		
		Node node2 = new ActionNode("2", "2");
		Node node3 = new ActionNode("3", "3");		
		Groum a = new Groum();
		a.addNode(node1);
		a.addNode(node2);
		a.addNode(node3);
		a.addEdge(node1, node2);
		a.addEdge(node1, node3);
		
		Node node1b = new ActionNode("1", "1");		
		Node node2b = new ActionNode("2", "2");
		Node node3b = new ActionNode("3", "3");		
		Groum b = new Groum();
		b.addNode(node1b);
		b.addNode(node3b);
		b.addNode(node2b);
		b.addEdge(node1b, node3b);
		b.addEdge(node1b, node2b);
		
		assertEquals(a,b);				
	}

	@Test
	public void identityWorksFine() {
		Groum a = Fixture.getPapersExampleGroum();
		Groum b = Fixture.getPapersExampleGroum();
		assertFalse(a == b);
	}

	@Test
	public void canContainEqualNodes() {
		Groum groum = new Groum();
		ActionNode aNode = new ActionNode("System.out", "println");
		ActionNode aNode2 = new ActionNode("System.out", "println");
		groum.addNode(aNode);
		groum.addNode(aNode2);
		
		assertEquals(aNode, aNode2);
		assertEquals(2, groum.getNodeCount());
	}

	@Test
	public void findsSuccessors() {
		ActionNode a = new ActionNode("root", "root");
		ActionNode b = new ActionNode("B", "1");
		ActionNode c = new ActionNode("C", "1");
		ControlNode d = new ControlNode("WHILE");

		Groum groum = new Groum();
		groum.addNode(a);
		groum.addNode(b);
		groum.addNode(c);
		groum.addNode(d);

		groum.addEdge(a, b);
		groum.addEdge(b, c);
		groum.addEdge(b, d);

		assertTrue(groum.getSuccessors(b).contains(c) && groum.getSuccessors(b).contains(d));
	}

	@Test
	public void comparesEqualGroums() {
		Groum a = Fixture.getExampleGroum();
		Groum b = Fixture.getExampleGroum();
		assertTrue(a.compareTo(b) == 0);
	}

	@Test
	public void comparesEqualGroumsWithDifferentSorting() {
		ActionNode a = new ActionNode("A", "1");
		ActionNode b = new ActionNode("B", "2");
		ActionNode c = new ActionNode("C", "3");
		ActionNode d = new ActionNode("D", "4");

		Groum groum = new Groum();
		groum.addNode(a);
		groum.addNode(b);
		groum.addNode(c);
		groum.addNode(d);

		groum.addEdge(a, b);
		groum.addEdge(b, c);
		groum.addEdge(b, d);

		Groum groum2 = new Groum();
		groum2.addNode(a);
		groum2.addNode(c);
		groum2.addNode(b);
		groum2.addNode(d);

		groum2.addEdge(b, c);
		groum2.addEdge(a, b);
		groum2.addEdge(b, d);
		assertTrue(groum.compareTo(groum2) == 0);
	}

	@Test
	public void sortsDifferentGroum() {
		ActionNode a = new ActionNode("A", "1");
		ActionNode b = new ActionNode("A", "1");
		ActionNode c = new ActionNode("A", "2");
		ActionNode d = new ActionNode("A", "2");

		Groum groum = new Groum();
		groum.addNode(a);
		groum.addNode(b);
		groum.addNode(c);

		groum.addEdge(a, b);
		groum.addEdge(b, c);

		Groum groum2 = new Groum();
		groum2.addNode(b);
		groum2.addNode(c);
		groum2.addNode(d);

		groum2.addEdge(b, c);
		groum2.addEdge(c, d);

		assertTrue(groum.compareTo(groum2) == -1);
	}

	@Test
	public void canBeCloned() {
		ActionNode a = new ActionNode("A", "1");
		ActionNode b = new ActionNode("A", "1");

		Groum groum = new Groum();
		groum.addNode(a);
		groum.addNode(b);
		groum.addEdge(a, b);

		Groum groum3 = (Groum) groum.clone();

		List<Node> list = new LinkedList<>();
		list.addAll(groum.getAllNodes());
		int i = 0;
		boolean success = true;
		for (Node node : groum3.getAllNodes()) {
			if (node != list.get(i)) {
				success = false;
				break;
			}
			i++;
		}

		assertTrue(success);
	}

	@Test
	public void groumsCanBeGenerated() {
		Groum groum = Fixture.createConnectedGroumOfSize(10);
		assertTrue(groum.getAllNodes().size() == 10 && groum.getEdgeCount() == 9);
	}

	@Test
	public void groumsCanBeGeneratedLowEqualsHigh() {
		Groum groum = Fixture.createConnectedGroumOfSize(5, 5);
		Groum target = new Groum();
		Node node = new ActionNode("5", "5");
		target.addNode(node);
		assertTrue(groum.getAllNodes().size() == 1 && groum.equals(target));
	}

	@Test
	public void groumsCanBeGeneratedLowHigh() {
		Groum groum = Fixture.createConnectedGroumOfSize(6, 10);
		Node node6 = new ActionNode("6", "6");
		Node node10 = new ActionNode("10", "10");
		boolean contains6 = false;
		boolean contains10 = false;

		for (Node node : groum.getAllNodes()) {
			if (node.equals(node6))
				contains6 = true;
			if (node.equals(node10))
				contains10 = true;
		}
		assertTrue(groum.getAllNodes().size() == 5 && contains6 && contains10);
	}

	@Test
	public void listOfGroumsCanBeGenerated() {
		List<Groum> groums = Fixture.getListOfXGroums(10);
		assertTrue(groums.size() == 10 && groums.get(0).getAllNodes().size() == 1);
	}

	@Test
	public void listOfGroumsCanBeGeneratedReversely() {
		List<Groum> groums = Fixture.getListOfXGroumsReverse(10);
		assertTrue(groums.size() == 10 && groums.get(0).getAllNodes().size() == 10);
	}

}
