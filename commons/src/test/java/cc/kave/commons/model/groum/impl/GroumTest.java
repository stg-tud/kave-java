package cc.kave.commons.model.groum.impl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.ISubGroum;
import cc.kave.commons.model.groum.nodes.IControlNode;
import cc.kave.commons.model.groum.nodes.impl.ActionNode;
import cc.kave.commons.model.groum.nodes.impl.ControlNode;

public class GroumTest {

	@Test
	public void groumDrawsItself() {
		System.out.println(Fixture_Groumtest.getPapersExampleGroum().toString());
	}

	@Test
	public void equalityWorksFine() {
		IGroum a = Fixture_Groumtest.getPapersExampleGroum();
		IGroum b = Fixture_Groumtest.getPapersExampleGroum();
		assertTrue(a.equals(b));
	}

	@Test
	public void equalityWorksFineForUnequal() {
		ActionNode a = new ActionNode("A", "1");
		ActionNode b = new ActionNode("A", "1");
		ActionNode c = new ActionNode("A", "2");
		ActionNode d = new ActionNode("A", "2");

		ISubGroum groum = new SubGroum(null);
		groum.addVertex(a);
		// groum.addVertex(b);

		// groum.addEdge(a, b);

		ISubGroum groum2 = new SubGroum(null);
		groum2.addVertex(c);
		// groum2.addVertex(d);

		// groum2.addEdge(c, d);

		assertFalse(groum.equals(groum2));
	}

	@Test
	public void findsLeaf() {
		IGroum groum = Fixture_Groumtest.getPapersExampleGroum();

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
		IGroum a = Fixture_Groumtest.getPapersExampleGroum();
		IGroum b = Fixture_Groumtest.getPapersExampleGroum();
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
		IGroum a = Fixture_Groumtest.getExampleGroum();
		IGroum b = Fixture_Groumtest.getExampleGroum();
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

}
