package cc.kave.commons.model.groum.impl;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.nodes.impl.ActionNode;
import cc.kave.commons.model.pattexplore.PattExplorer;

public class PattExplorerTest {

	@Test
	public void findsOneNodePatterns() {
		IGroum groum1 = new Groum();
		ActionNode node1a = new ActionNode("A", "A");
		groum1.addVertex(node1a);

		IGroum groum2 = new Groum();
		ActionNode node1b = new ActionNode("A", "A");
		groum2.addVertex(node1b);

		PattExplorer uut = new PattExplorer(2);

		List patterns = uut.explorePatterns(Arrays.asList(groum1, groum2));
		assertTrue(patterns.size() == 1);

	}

	@Test
	public void findsTwoNodePatterns() {
		IGroum groum1 = new Groum();
		ActionNode node1a = new ActionNode("A", "A");
		ActionNode node2a = new ActionNode("B", "B");
		groum1.addVertex(node1a);
		groum1.addVertex(node2a);
		groum1.addEdge(node1a, node2a);

		IGroum groum2 = new Groum();
		ActionNode node1b = new ActionNode("A", "A");
		ActionNode node2b = new ActionNode("B", "B");
		groum2.addVertex(node1b);
		groum2.addVertex(node2b);
		groum2.addEdge(node1b, node2b);

		PattExplorer uut = new PattExplorer(2);

		List patterns = uut.explorePatterns(Arrays.asList(groum1, groum2));
		assertTrue(patterns.size() == 3);

	}

	@Test
	public void findsTwoNodePatternsGenerated() {
		IGroum groum1 = Fixture_Groumtest.createConnectedGroumOfSize(2);
		IGroum groum2 = Fixture_Groumtest.createConnectedGroumOfSize(2);

		PattExplorer uut = new PattExplorer(2);

		List patterns = uut.explorePatterns(Arrays.asList(groum1, groum2));
		assertTrue(patterns.size() == 3);

	}

	@Test
	public void findsGeneratedThreeNodePatterns() {
		IGroum groum1 = Fixture_Groumtest.createConnectedGroumOfSize(3);
		IGroum groum2 = Fixture_Groumtest.createConnectedGroumOfSize(3);
		IGroum groum3 = Fixture_Groumtest.createConnectedGroumOfSize(3);
		PattExplorer uut = new PattExplorer(1);
		List list = Arrays.asList(groum1, groum2, groum3);
		List patterns = uut.explorePatterns(list);
		assertTrue(patterns.size() == 6);
	}

	@Test
	public void findsFourNodePatterns() {
		IGroum groum1 = Fixture_Groumtest.createConnectedGroumOfSize(4);
		IGroum groum2 = Fixture_Groumtest.createConnectedGroumOfSize(4);
		IGroum groum3 = Fixture_Groumtest.createConnectedGroumOfSize(4);
		IGroum groum4 = Fixture_Groumtest.createConnectedGroumOfSize(4);
		PattExplorer uut = new PattExplorer(1);
		List list = Arrays.asList(groum1, groum2, groum3, groum4);
		List patterns = uut.explorePatterns(list);
		assertTrue(patterns.size() == 10);
	}

	@Test
	public void findsGeneratedFourNodesPatterns() {
		PattExplorer uut = new PattExplorer(1);
		List<IGroum> listOfXGroums = Fixture_Groumtest.getListOfXGroums(4);
		List patterns = uut.explorePatterns(listOfXGroums);
		assertTrue(patterns.size() == 10);
	}

	@Test
	public void findsGeneratedFourNodesPatternsReverse() {
		PattExplorer uut = new PattExplorer(1);
		List<IGroum> listOfXGroumsReverse = Fixture_Groumtest.getListOfXGroumsReverse(4);
		List patterns = uut.explorePatterns(listOfXGroumsReverse);
		assertTrue(patterns.size() == 10);
	}

	@Test
	public void findsGeneratedTenNodesPatterns() {
		PattExplorer uut = new PattExplorer(1);
		List<IGroum> listOfXGroums = Fixture_Groumtest.getListOfXGroums(10);
		List patterns = uut.explorePatterns(listOfXGroums);
		assertTrue(patterns.size() == 55);
	}

	@Test
	public void findPatternsOfSize5() {
		PattExplorer uut = new PattExplorer(5);
		List<IGroum> listOfXGroums = Fixture_Groumtest.getListOfXGroums(10);
		List patterns = uut.explorePatterns(listOfXGroums);
		System.out.println(patterns);

		assertTrue(patterns.size() == 21);
	}

	@Test
	@Ignore
	public void findsGenerated30NodesPatterns() {
		PattExplorer uut = new PattExplorer(1);
		List<IGroum> listOfXGroums = Fixture_Groumtest.getListOfXGroums(30);
		List patterns = uut.explorePatterns(listOfXGroums);
		assertTrue(patterns.size() == 465);
	}

}
