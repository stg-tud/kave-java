package cc.kave.commons.model.groum;


import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.SubGroum;
import cc.kave.commons.model.pattexplore.PattExplorer;
import static cc.kave.commons.model.groum.GroumTestUtils.*;
import static cc.kave.commons.model.groum.PatternAssert.*;

public class PattExplorerTest {

	@Test
	public void findsSingeNodePatterns() {
		Groum groum = createGroum("A", "B");

		List<SubGroum> patterns = explorePatterns(1, groum);

		assertContainsPatterns(patterns, createGroum("A"), createGroum("B"));
	}

	@Test
	public void filtersPatternsByFrequency() {
		Groum groum1 = createGroum("A", "B");
		Groum groum2 = createGroum("A");

		List<SubGroum> patterns = explorePatterns(2, groum1, groum2);

		assertContainsPatterns(patterns, createGroum("A"));
	}

	@Test
	public void findsTwoNodePattern() {
		Node[] nodes = createNodes("A", "B");
		Groum groum = createGroum(nodes);
		groum.addEdge(nodes[0], nodes[1]);

		List<SubGroum> patterns = explorePatterns(1, groum);

		List<SubGroum> patternsOfSize2 = filterBySize(patterns, 2);
		assertContainsPatterns(patternsOfSize2, groum);
	}

	@Test
	public void filtersTwoNodePatternsByFrequency() {
		Node[] nodes1 = createNodes("A", "B");
		Groum groum1 = createGroum(nodes1);
		groum1.addEdge(nodes1[0], nodes1[1]);
		Node[] nodes2 = createNodes("A", "C");
		Groum groum2 = createGroum(nodes2);
		groum2.addEdge(nodes2[0], nodes2[1]);
		Node[] nodes3 = createNodes("A", "B");
		Groum groum3 = createGroum(nodes3);
		groum3.addEdge(nodes3[0], nodes3[1]);

		List<SubGroum> patterns = explorePatterns(2, groum1, groum2, groum3);

		List<SubGroum> patternsOfSize2 = filterBySize(patterns, 2);
		assertContainsPatterns(patternsOfSize2, groum1);
	}

	@Test
	public void considersEdgeDirection() {
		Node[] nodes1 = createNodes("A", "B");
		Groum groum1 = createGroum(nodes1);
		groum1.addEdge(nodes1[0], nodes1[1]);

		Node[] nodes2 = createNodes("A", "B");
		Groum groum2 = createGroum(nodes2);
		groum2.addEdge(nodes2[1], nodes2[0]);

		List<SubGroum> patterns = explorePatterns(2, groum1, groum2);

		List<SubGroum> patternsOfSize2 = filterBySize(patterns, 2);
		assertContainsPatterns(patternsOfSize2);
	}

	private List<SubGroum> explorePatterns(int threshold, Groum... groums) {
		PattExplorer uut = new PattExplorer(threshold);
		return uut.explorePatterns(Arrays.asList(groums));
	}
}
