package cc.kave.commons.model.groum.impl;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.ISubGroum;
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

		List<ISubGroum> patterns = uut.explorePatterns(Arrays.asList(groum1, groum2));

		System.out.println(patterns);
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

		List<ISubGroum> patterns = uut.explorePatterns(Arrays.asList(groum1, groum2));

		System.out.println(patterns);
	}

	@Ignore
	@Test
	public void firstTry() {
		PattExplorer explorer = new PattExplorer(2);

		List<ISubGroum> patterns = explorer.explorePatterns(Fixture_Groumtest.getTestList());
		System.out.println(patterns);
		System.out.println(patterns.size());
		// assertEquals(patterns.size(), 8);

	}

}
