package cc.kave.commons.model.groum;

import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.SubGroum;
import static cc.kave.commons.model.groum.GroumTestUtils.*;
import static cc.kave.commons.model.groum.PatternAssert.*;
import static cc.kave.commons.model.groum.GroumBuilder.*;

public class PattExplorerTest {

	@Test
	public void findsSingeNodePatterns() {
		Groum groum = createGroum("A", "B");

		List<SubGroum> patterns = findPatternsWithMinFrequency(1, groum);

		assertContainsPatterns(patterns, createGroum("A"), createGroum("B"));
	}

	@Test
	public void filtersPatternsByFrequency() {
		Groum groum1 = createGroum("A", "B");
		Groum groum2 = createGroum("A");

		List<SubGroum> patterns = findPatternsWithMinFrequency(2, groum1,
				groum2);

		assertContainsPatterns(patterns, createGroum("A"));
	}

	@Test
	public void findsTwoNodePattern() {
		Node[] nodes = createNodes("A", "B");
		Groum groum = buildGroum(nodes).withEdge(nodes[0], nodes[1]).build();

		List<SubGroum> patterns = findPatternsWithMinFrequency(1, groum);

		List<SubGroum> patternsOfSize2 = filterBySize(patterns, 2);
		assertContainsPatterns(patternsOfSize2, groum);
	}

	@Test
	public void filtersTwoNodePatternsByFrequency() {
		Node[] nodes1 = createNodes("A", "B");
		Groum groum1 = buildGroum(nodes1).withEdge(nodes1[0], nodes1[1]).build();
		Node[] nodes2 = createNodes("A", "C");
		Groum groum2 = buildGroum(nodes2).withEdge(nodes2[0], nodes2[1]).build();
		Node[] nodes3 = createNodes("A", "B");
		Groum groum3 = buildGroum(nodes3).withEdge(nodes3[0], nodes3[1]).build();

		List<SubGroum> patterns = findPatternsWithMinFrequency(2, groum1,
				groum2, groum3);

		List<SubGroum> patternsOfSize2 = filterBySize(patterns, 2);
		assertContainsPatterns(patternsOfSize2, groum1);
	}

	/**
	 * Since structure is considered, two different patterns of size 3 should
	 * be found in the following Groum:
	 * 
	 * <pre>
	 *     1          1     1
	 *   /   \        |     | \
	 *  2     3  =>   2  ,  2  3
	 *  |             |
	 *  3             3
	 * </pre>
	 */
	@Test
	public void considersEdges() {
		Node[] nodes = createNodes("1", "2", "3", "3");
		Groum groum = buildGroum(nodes)
				.withEdge(nodes[0], nodes[1])
				.withEdge(nodes[0], nodes[2])
				.withEdge(nodes[1], nodes[3]).build();

		List<SubGroum> patterns = findPatternsWithMinFrequency(1, groum);

		Node[] nodes2 = createNodes("1", "2", "3");
		Groum pattern1 = buildGroum(nodes2)
				.withEdge(nodes2[0], nodes2[1])
				.withEdge(nodes2[0], nodes2[2]).build();

		Node[] nodes3 = createNodes("1", "2", "3");
		Groum pattern2 = buildGroum(nodes3)
				.withEdge(nodes3[0], nodes3[1])
				.withEdge(nodes3[1], nodes3[2]).build();

		List<SubGroum> patternsOfSize3 = filterBySize(patterns, 3);
		assertContainsPatterns(patternsOfSize3, pattern1, pattern2);
	}

	@Test
	public void considersEdgeDirection() {
		// A -> B
		Node[] nodes1 = createNodes("A", "B");
		Groum groum1 = buildGroum(nodes1).withEdge(nodes1[0], nodes1[1]).build();

		// B -> A
		Node[] nodes2 = createNodes("A", "B");
		Groum groum2 = buildGroum(nodes2).withEdge(nodes2[1], nodes2[0]).build();

		List<SubGroum> patterns = findPatternsWithMinFrequency(2, groum1,
				groum2);

		List<SubGroum> patternsOfSize2 = filterBySize(patterns, 2);
		assertContainsPatterns(patternsOfSize2);
	}

	@Test
	public void countsOverlappingInstanesOnlyOnce1() {
		// 1 -> 2 -> 2
		Node[] nodes = createNodes("1", "2", "2");
		Groum overlappingGroum = buildGroum(nodes)
				.withEdge(nodes[0], nodes[1])
				.withEdge(nodes[1], nodes[2]).build();

		List<SubGroum> patterns = findPatternsWithMinFrequency(2,
				overlappingGroum);

		Groum pattern1 = createGroum("2");
		assertContainsPatterns(patterns, pattern1);
	}

	@Test
	public void countsOverlappingInstanesOnlyOnce2() {
		// 1 -> 2
		// |
		// 2
		Node[] nodes = createNodes("1", "2", "2");
		Groum overlappingGroum = buildGroum(nodes)
				.withEdge(nodes[0], nodes[1])
				.withEdge(nodes[0], nodes[2]).build();

		List<SubGroum> patterns = findPatternsWithMinFrequency(2,
				overlappingGroum);

		Groum pattern1 = createGroum("2");
		assertContainsPatterns(patterns, pattern1);
	}

	@Test
	public void findsMultipleInstanceInOneGraph() {
		// 1 -> 2
		// |
		// 1 -> 2
		Node[] nodes = createNodes("1", "1", "2", "2");
		Groum overlappingGroum = buildGroum(nodes)
				.withEdge(nodes[0], nodes[1])
				.withEdge(nodes[0], nodes[2])
				.withEdge(nodes[1], nodes[3]).build();

		List<SubGroum> patterns = findPatternsWithMinFrequency(2,
				overlappingGroum);

		Node[] nodes2 = createNodes("1", "2");
		Groum pattern1 = buildGroum(nodes2).withEdge(nodes2[0], nodes2[1]).build();

		patterns = filterBySize(patterns, 2);
		assertContainsPatterns(patterns, pattern1);
	}

	@Test
	public void findsGraphIsomorphism() {
		// 1 -> 2
		// | /
		// 3
		Node[] nodes1 = createNodes("1", "2", "3");
		Groum groum1 = buildGroum(nodes1)
				.withEdge(nodes1[0], nodes1[1])
				.withEdge(nodes1[0], nodes1[2])
				.withEdge(nodes1[1], nodes1[2]).build();

		// 1 -> 2
		// | |
		// 3 3
		Node[] nodes2 = createNodes("1", "2", "3", "3");
		Groum groum2 = buildGroum(nodes2)
				.withEdge(nodes2[0], nodes2[1])
				.withEdge(nodes2[0], nodes2[2])
				.withEdge(nodes2[1], nodes2[3]).build();

		List<SubGroum> patterns = findPatternsWithMinFrequency(2, groum1,
				groum2);

		Node[] nodes3 = createNodes("1", "2", "3");
		Groum pattern1 = buildGroum(nodes3)
				.withEdge(nodes3[0], nodes3[1])
				.withEdge(nodes3[0], nodes3[2])
				.withEdge(nodes3[1], nodes3[2]).build();

		patterns = filterBySize(patterns, 3);
		assertContainsPatterns(patterns, pattern1);
	}

	@Test
	public void includesAllEdgesBetweenAllIncludedNodes() {
		Node[] nodes = createNodes("1", "2", "3");
		Groum groum = buildGroum(nodes)
				.withEdge(nodes[0], nodes[1])
				.withEdge(nodes[0], nodes[2])
				.withEdge(nodes[1], nodes[2]).build();

		List<SubGroum> patterns = findPatternsWithMinFrequency(1, groum);

		patterns = filterBySize(patterns, 3);
		assertContainsPatterns(patterns, groum);
	}
}
