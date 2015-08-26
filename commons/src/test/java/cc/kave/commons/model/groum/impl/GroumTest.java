package cc.kave.commons.model.groum.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.INode;
import cc.kave.commons.model.groum.nodes.IActionNode;
import cc.kave.commons.model.groum.nodes.IControlNode;
import cc.kave.commons.model.groum.nodes.impl.ActionNode;
import cc.kave.commons.model.groum.nodes.impl.ControlNode;

public class GroumTest {

	@Test
	public void groumDrawsItself() {
		assertTrue(Fixture.getPapersExampleGroum().toString() != null);
	}

	@Test
	public void equalityEqualGroums() {
		IGroum a = Fixture.getPapersExampleGroum();
		IGroum b = Fixture.getPapersExampleGroum();
		assertEquals(a, b);
	}

	@Test
	public void equalityUnequalNodes() {
		ActionNode a = new ActionNode("A", "1");
		ActionNode c = new ActionNode("A", "2");

		IGroum groum = new Groum();
		groum.addVertex(a);

		IGroum groum2 = new Groum();
		groum2.addVertex(c);

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

		IGroum groum = new Groum();
		groum.addVertex(a1);
		groum.addVertex(b1);
		groum.addVertex(c1);
		groum.addEdge(a1, b1);

		IGroum groum2 = new Groum();
		groum2.addVertex(a2);
		groum2.addVertex(b2);
		groum2.addVertex(c2);
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

		IGroum groum = new Groum();
		groum.addVertex(a1);
		groum.addVertex(b1);
		groum.addVertex(c1);
		groum.addEdge(a1, c1);
		groum.addEdge(a1, b1);

		IGroum groum2 = new Groum();
		groum2.addVertex(a2);
		groum2.addVertex(c2);
		groum2.addVertex(b2);
		groum2.addEdge(a2, b2);
		groum2.addEdge(a2, c2);

		assertEquals(groum, groum2);
	}
	
	@Test
	public void equalityWorksUnordered() {
		INode node1 = new ActionNode("1", "1");		
		INode node2 = new ActionNode("2", "2");
		INode node3 = new ActionNode("3", "3");		
		IGroum a = new Groum();
		a.addVertex(node1);
		a.addVertex(node2);
		a.addVertex(node3);
		a.addEdge(node1, node2);
		a.addEdge(node1, node3);
		
		INode node1b = new ActionNode("1", "1");		
		INode node2b = new ActionNode("2", "2");
		INode node3b = new ActionNode("3", "3");		
		IGroum b = new Groum();
		b.addVertex(node1b);
		b.addVertex(node3b);
		b.addVertex(node2b);
		b.addEdge(node1b, node3b);
		b.addEdge(node1b, node2b);
		
		assertEquals(a,b);				
	}

	@Test
	public void findsLeaf() {
		IGroum groum = Fixture.getPapersExampleGroum();

		ActionNode brClose = new ActionNode("BufferedReader", "close");
		brClose.addDependency("in");
		groum.addVertex(brClose);

		assertTrue(groum.getLeaf().equals(brClose));
	}

	@Test
	public void findsLeafInSoloGroum() {
		IGroum groum = new Groum();

		ActionNode brClose = new ActionNode("BufferedReader", "close");
		brClose.addDependency("in");
		groum.addVertex(brClose);

		assertTrue(groum.getLeaf().equals(brClose));
	}

	@Test
	public void findsRootInSoloGroum() {
		IGroum groum = new Groum();

		ActionNode brClose = new ActionNode("BufferedReader", "close");
		brClose.addDependency("in");
		groum.addVertex(brClose);

		assertTrue(groum.getRoot().equals(brClose));
	}

	@Test
	public void identityWorksFine() {
		IGroum a = Fixture.getPapersExampleGroum();
		IGroum b = Fixture.getPapersExampleGroum();
		assertFalse(a == b);
	}

	@Test
	public void canContainEqualNodes() {
		IGroum groum = new Groum();
		ActionNode aNode = new ActionNode("System.out", "println");
		ActionNode aNode2 = new ActionNode("System.out", "println");
		groum.addVertex(aNode);
		groum.addVertex(aNode2);
		assertTrue((aNode.equals(aNode2)) && (groum.getVertexCount() == 2));
	}

	@Test
	public void findsIdenticNode() {
		ActionNode aNode = new ActionNode("BufferedReader", "readLine");
		ActionNode aNode2 = new ActionNode("System.out", "println");
		IGroum groum = new Groum();
		groum.addVertex(aNode);
		groum.addVertex(aNode2);
		assertTrue(groum.getNode(aNode) == aNode);
	}

	@Test
	public void findsSuccessors() {
		ActionNode a = new ActionNode("root", "root");
		ActionNode b = new ActionNode("B", "1");
		ActionNode c = new ActionNode("C", "1");
		ControlNode d = new ControlNode(IControlNode.WHILE_NODE);

		Groum groum = new Groum();
		groum.addVertex(a);
		groum.addVertex(b);
		groum.addVertex(c);
		groum.addVertex(d);

		groum.addEdge(a, b);
		groum.addEdge(b, c);
		groum.addEdge(b, d);

		assertTrue(groum.getSuccessors(b).contains(c) && groum.getSuccessors(b).contains(d));
	}

	@Test
	public void comparesEqualGroums() {
		IGroum a = Fixture.getExampleGroum();
		IGroum b = Fixture.getExampleGroum();
		assertTrue(a.compareTo(b) == 0);
	}

	@Test
	public void comparesEqualGroumsWithDifferentSorting() {
		ActionNode a = new ActionNode("A", "1");
		ActionNode b = new ActionNode("B", "2");
		ActionNode c = new ActionNode("C", "3");
		ActionNode d = new ActionNode("D", "4");

		Groum groum = new Groum();
		groum.addVertex(a);
		groum.addVertex(b);
		groum.addVertex(c);
		groum.addVertex(d);

		groum.addEdge(a, b);
		groum.addEdge(b, c);
		groum.addEdge(b, d);

		Groum groum2 = new Groum();
		groum2.addVertex(a);
		groum2.addVertex(c);
		groum2.addVertex(b);
		groum2.addVertex(d);

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
		groum.addVertex(a);
		groum.addVertex(b);
		groum.addVertex(c);

		groum.addEdge(a, b);
		groum.addEdge(b, c);

		Groum groum2 = new Groum();
		groum2.addVertex(b);
		groum2.addVertex(c);
		groum2.addVertex(d);

		groum2.addEdge(b, c);
		groum2.addEdge(c, d);

		assertTrue(groum.compareTo(groum2) == -1);
	}

	@Test
	public void canBeCloned() {
		ActionNode a = new ActionNode("A", "1");
		ActionNode b = new ActionNode("A", "1");
		ActionNode c = new ActionNode("A", "2");
		ActionNode d = new ActionNode("A", "2");

		Groum groum = new Groum();
		groum.addVertex(a);
		groum.addVertex(b);
		groum.addEdge(a, b);

		Groum groum3 = (Groum) groum.clone();

		List<INode> list = new LinkedList<>();
		list.addAll(groum.getAllNodes());
		int i = 0;
		boolean success = true;
		for (INode node : groum3.getAllNodes()) {
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
		IGroum groum = Fixture.createConnectedGroumOfSize(10);
		assertTrue(groum.getAllNodes().size() == 10 && groum.getEdgeCount() == 9);
	}

	@Test
	public void groumsCanBeGeneratedLowEqualsHigh() {
		IGroum groum = Fixture.createConnectedGroumOfSize(5, 5);
		IGroum target = new Groum();
		IActionNode node = new ActionNode("5", "5");
		target.addVertex(node);
		assertTrue(groum.getAllNodes().size() == 1 && groum.equals(target));
	}

	@Test
	public void groumsCanBeGeneratedLowHigh() {
		IGroum groum = Fixture.createConnectedGroumOfSize(6, 10);
		IActionNode node6 = new ActionNode("6", "6");
		IActionNode node10 = new ActionNode("10", "10");
		boolean contains6 = false;
		boolean contains10 = false;

		for (INode node : groum.getAllNodes()) {
			if (node.equals(node6))
				contains6 = true;
			if (node.equals(node10))
				contains10 = true;
		}
		assertTrue(groum.getAllNodes().size() == 5 && contains6 && contains10);
	}

	@Test
	public void generatedGroumsHaveCorrectLeaf() {
		IGroum groum = Fixture.createConnectedGroumOfSize(10);
		assertTrue(groum.getLeaf().equals(Fixture.createActionNodeInstance("10")));
	}

	@Test
	public void listOfGroumsCanBeGenerated() {
		List<IGroum> groums = Fixture.getListOfXGroums(10);
		assertTrue(groums.size() == 10 && groums.get(0).getAllNodes().size() == 1);
	}

	@Test
	public void listOfGroumsCanBeGeneratedReversely() {
		List<IGroum> groums = Fixture.getListOfXGroumsReverse(10);
		assertTrue(groums.size() == 10 && groums.get(0).getAllNodes().size() == 10);
	}

}
