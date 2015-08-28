package cc.kave.commons.model.groum.impl;

import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.INode;
import cc.kave.commons.model.groum.nodes.ActionNode;
import cc.kave.commons.model.groum.nodes.IActionNode;

public class GroumQueryTest {

	@Test
	public void findsEqualNodes() {
		ActionNode node1 = new ActionNode("BufferedReader", "readline");
		ActionNode node2 = new ActionNode("FileReader", "readline");
		ActionNode node3 = new ActionNode("BufferedReader", "readline");
		ActionNode node4 = new ActionNode("System.out", "println");
		ActionNode node5 = new ActionNode("BufferedReader", "readline");

		Groum groum = new Groum();
		groum.addVertex(node1);
		groum.addVertex(node2);
		groum.addVertex(node3);
		groum.addVertex(node4);
		groum.addVertex(node5);

		Set<INode> equalNodes = groum.getEqualNodes(node1);
		assertTrue(equalNodes.size() == 3);

	}

	@Test
	public void findsOtherEqualNodes() {
		ActionNode sbInit1 = new ActionNode("StringBuffer", IActionNode.CONTRUCTOR);
		sbInit1.addDependency("strbuf");

		Groum groum = (Groum) Fixture.getExampleGroum();
		assertTrue(groum.getEqualNodes(sbInit1).size() == 2);
	}

	@Test
	public void confirmsExistenceOfEqualNode() {
		ActionNode node1 = new ActionNode("BufferedReader", "readline");
		ActionNode node2 = new ActionNode("FileReader", "readline");
		ActionNode node3 = new ActionNode("BufferedReader", "readline");
		ActionNode node4 = new ActionNode("System.out", "println");
		ActionNode node5 = new ActionNode("BufferedReader", "readline");

		Groum groum = new Groum();
		groum.addVertex(node2);
		groum.addVertex(node3);
		groum.addVertex(node4);
		groum.addVertex(node5);

		assertTrue(groum.containsEqualNode(node1));
	}

}
