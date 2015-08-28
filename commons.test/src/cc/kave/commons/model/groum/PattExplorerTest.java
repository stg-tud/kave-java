package cc.kave.commons.model.groum;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.SubGroum;
import cc.kave.commons.model.groum.nodes.ActionNode;
import cc.kave.commons.model.pattexplore.PattExplorer;

public class PattExplorerTest {

	@Test
	public void findsOneNodePatterns() {
		Groum groum1 = new Groum();
		ActionNode node1a = new ActionNode("A", "A");
		groum1.addVertex(node1a);

		Groum groum2 = new Groum();
		ActionNode node1b = new ActionNode("A", "A");
		groum2.addVertex(node1b);

		PattExplorer uut = new PattExplorer(2);

		List<SubGroum> patterns = uut.explorePatterns(Arrays.asList(groum1, groum2));

		// Target patterns:
		Groum testgroum = new Groum();
		testgroum.addVertex(new ActionNode("A", "A"));

		PatternAssert.assertContainsPatterns(patterns, testgroum);
	}

	@Test
	public void findsTwoNodePatterns() {
		Groum groum1 = new Groum();
		ActionNode node1a = new ActionNode("A", "A");
		ActionNode node2a = new ActionNode("B", "B");
		groum1.addVertex(node1a);
		groum1.addVertex(node2a);
		groum1.addEdge(node1a, node2a);

		Groum groum2 = new Groum();
		ActionNode node1b = new ActionNode("A", "A");
		ActionNode node2b = new ActionNode("B", "B");
		groum2.addVertex(node1b);
		groum2.addVertex(node2b);
		groum2.addEdge(node1b, node2b);

		PattExplorer uut = new PattExplorer(2);

		List<SubGroum> patterns = uut.explorePatterns(Arrays.asList(groum1, groum2));

		// Target patterns:
		Groum patternA = new Groum();
		ActionNode nodea = new ActionNode("A", "A");
		patternA.addVertex(nodea);

		Groum patternB = new Groum();
		ActionNode nodeb = new ActionNode("B", "B");
		patternB.addVertex(nodeb);

		Groum patternAB = new Groum();
		patternAB.addVertex(nodea);
		patternAB.addVertex(nodeb);
		patternAB.addEdge(nodea, nodeb);

		PatternAssert.assertContainsPatterns(patterns, patternA, patternB, patternAB);

	}

	@Test
	public void findsNoInversePatterns() {
		Groum groum1 = new Groum();
		ActionNode node1a = new ActionNode("A", "A");
		ActionNode node2a = new ActionNode("B", "B");
		groum1.addVertex(node1a);
		groum1.addVertex(node2a);
		groum1.addEdge(node1a, node2a);

		Groum groum2 = new Groum();
		ActionNode node1b = new ActionNode("B", "B");
		ActionNode node2b = new ActionNode("A", "A");
		groum2.addVertex(node1b);
		groum2.addVertex(node2b);
		groum2.addEdge(node1b, node2b);

		PattExplorer uut = new PattExplorer(2);
		List<SubGroum> patterns = uut.explorePatterns(Arrays.asList(groum1, groum2));

		// Target patterns:
		Groum patternA = new Groum();
		ActionNode nodea = new ActionNode("A", "A");
		patternA.addVertex(nodea);

		Groum patternB = new Groum();
		ActionNode nodeb = new ActionNode("B", "B");
		patternB.addVertex(nodeb);

		PatternAssert.assertContainsPatterns(patterns, patternA, patternB);
	}

	@Test
	public void findsTwoNodePatternsGenerated() {
		Groum groum1 = Fixture.createConnectedGroumOfSize(2);
		Groum groum2 = Fixture.createConnectedGroumOfSize(2);

		PattExplorer uut = new PattExplorer(2);

		List<SubGroum> patterns = uut.explorePatterns(Arrays.asList(groum1, groum2));

		// Target patterns:
		Groum pattern1 = new Groum();
		ActionNode nodea = new ActionNode("1", "1");
		pattern1.addVertex(nodea);

		Groum pattern2 = new Groum();
		ActionNode nodeb = new ActionNode("2", "2");
		pattern2.addVertex(nodeb);

		Groum pattern12 = new Groum();
		pattern12.addVertex(nodea);
		pattern12.addVertex(nodeb);
		pattern12.addEdge(nodea, nodeb);

		PatternAssert.assertContainsPatterns(patterns, pattern1, pattern2, pattern12);
	}

	@Test
	public void findsGeneratedThreeNodePatterns() {
		Groum groum1 = Fixture.createConnectedGroumOfSize(3);
		Groum groum2 = Fixture.createConnectedGroumOfSize(3);
		Groum groum3 = Fixture.createConnectedGroumOfSize(3);
		PattExplorer uut = new PattExplorer(1);
		List<Groum> list = Arrays.asList(groum1, groum2, groum3);
		List<SubGroum> patterns = uut.explorePatterns(list);

		// Target patterns:
		Groum pattern1 = new Groum();
		ActionNode node1 = new ActionNode("1", "1");
		pattern1.addVertex(node1);

		Groum pattern2 = new Groum();
		ActionNode node2 = new ActionNode("2", "2");
		pattern2.addVertex(node2);

		Groum pattern3 = new Groum();
		ActionNode node3 = new ActionNode("3", "3");
		pattern3.addVertex(node3);

		Groum pattern12 = new Groum();
		pattern12.addVertex(node1);
		pattern12.addVertex(node2);
		pattern12.addEdge(node1, node2);

		Groum pattern23 = new Groum();
		pattern23.addVertex(node2);
		pattern23.addVertex(node3);
		pattern23.addEdge(node2, node3);

		Groum pattern123 = new Groum();
		pattern123.addVertex(node1);
		pattern123.addVertex(node2);
		pattern123.addVertex(node3);
		pattern123.addEdge(node1, node2);
		pattern123.addEdge(node2, node3);

		PatternAssert.assertContainsPatterns(patterns, pattern1, pattern2, pattern3, pattern12, pattern23, pattern123);
	}

	@Test
	public void findsFourNodePatterns() {
		Groum groum1 = Fixture.createConnectedGroumOfSize(4);
		Groum groum2 = Fixture.createConnectedGroumOfSize(4);
		Groum groum3 = Fixture.createConnectedGroumOfSize(4);
		Groum groum4 = Fixture.createConnectedGroumOfSize(4);
		PattExplorer uut = new PattExplorer(1);
		List<Groum> list = Arrays.asList(groum1, groum2, groum3, groum4);
		List<SubGroum> patterns = uut.explorePatterns(list);

		// Target patterns:
		Groum pattern1 = new Groum();
		ActionNode node1 = new ActionNode("1", "1");
		pattern1.addVertex(node1);

		Groum pattern2 = new Groum();
		ActionNode node2 = new ActionNode("2", "2");
		pattern2.addVertex(node2);

		Groum pattern3 = new Groum();
		ActionNode node3 = new ActionNode("3", "3");
		pattern3.addVertex(node3);

		Groum pattern4 = new Groum();
		ActionNode node4 = new ActionNode("4", "4");
		pattern4.addVertex(node4);

		Groum pattern12 = new Groum();
		pattern12.addVertex(node1);
		pattern12.addVertex(node2);
		pattern12.addEdge(node1, node2);

		Groum pattern23 = new Groum();
		pattern23.addVertex(node2);
		pattern23.addVertex(node3);
		pattern23.addEdge(node2, node3);

		Groum pattern34 = new Groum();
		pattern34.addVertex(node3);
		pattern34.addVertex(node4);
		pattern34.addEdge(node3, node4);

		Groum pattern123 = new Groum();
		pattern123.addVertex(node1);
		pattern123.addVertex(node2);
		pattern123.addVertex(node3);
		pattern123.addEdge(node1, node2);
		pattern123.addEdge(node2, node3);

		Groum pattern234 = new Groum();
		pattern234.addVertex(node2);
		pattern234.addVertex(node3);
		pattern234.addVertex(node4);
		pattern234.addEdge(node2, node3);
		pattern234.addEdge(node3, node4);

		Groum pattern1234 = new Groum();
		pattern1234.addVertex(node1);
		pattern1234.addVertex(node2);
		pattern1234.addVertex(node3);
		pattern1234.addVertex(node4);
		pattern1234.addEdge(node1, node2);
		pattern1234.addEdge(node2, node3);
		pattern1234.addEdge(node3, node4);

		PatternAssert.assertContainsPatterns(patterns, pattern1, pattern2, pattern3, pattern4, pattern12, pattern23, pattern34, pattern123, pattern234, pattern1234);
	}

	@Test
	public void findsGeneratedFourNodesPatterns() {
		PattExplorer uut = new PattExplorer(1);
		List<Groum> listOfXGroums = Fixture.getListOfXGroums(4);
		List<SubGroum> patterns = uut.explorePatterns(listOfXGroums);

		// Target patterns:
		Groum pattern1 = new Groum();
		ActionNode node1 = new ActionNode("1", "1");
		pattern1.addVertex(node1);

		Groum pattern2 = new Groum();
		ActionNode node2 = new ActionNode("2", "2");
		pattern2.addVertex(node2);

		Groum pattern3 = new Groum();
		ActionNode node3 = new ActionNode("3", "3");
		pattern3.addVertex(node3);

		Groum pattern4 = new Groum();
		ActionNode node4 = new ActionNode("4", "4");
		pattern4.addVertex(node4);

		Groum pattern12 = new Groum();
		pattern12.addVertex(node1);
		pattern12.addVertex(node2);
		pattern12.addEdge(node1, node2);

		Groum pattern23 = new Groum();
		pattern23.addVertex(node2);
		pattern23.addVertex(node3);
		pattern23.addEdge(node2, node3);

		Groum pattern34 = new Groum();
		pattern34.addVertex(node3);
		pattern34.addVertex(node4);
		pattern34.addEdge(node3, node4);

		Groum pattern123 = new Groum();
		pattern123.addVertex(node1);
		pattern123.addVertex(node2);
		pattern123.addVertex(node3);
		pattern123.addEdge(node1, node2);
		pattern123.addEdge(node2, node3);

		Groum pattern234 = new Groum();
		pattern234.addVertex(node2);
		pattern234.addVertex(node3);
		pattern234.addVertex(node4);
		pattern234.addEdge(node2, node3);
		pattern234.addEdge(node3, node4);

		Groum pattern1234 = new Groum();
		pattern1234.addVertex(node1);
		pattern1234.addVertex(node2);
		pattern1234.addVertex(node3);
		pattern1234.addVertex(node4);
		pattern1234.addEdge(node1, node2);
		pattern1234.addEdge(node2, node3);
		pattern1234.addEdge(node3, node4);

		PatternAssert.assertContainsPatterns(patterns,pattern1,pattern2,pattern3,pattern4,pattern12,pattern23,pattern34,pattern123,pattern234,pattern1234);
	}

	@Test
	public void findsGeneratedFourNodesPatternsReverse() {
		PattExplorer uut = new PattExplorer(1);
		List<Groum> listOfXGroumsReverse = Fixture.getListOfXGroumsReverse(4);
		List<SubGroum> patterns = uut.explorePatterns(listOfXGroumsReverse);

		// Target patterns:
		Groum pattern1 = new Groum();
		ActionNode node1 = new ActionNode("1", "1");
		pattern1.addVertex(node1);

		Groum pattern2 = new Groum();
		ActionNode node2 = new ActionNode("2", "2");
		pattern2.addVertex(node2);

		Groum pattern3 = new Groum();
		ActionNode node3 = new ActionNode("3", "3");
		pattern3.addVertex(node3);

		Groum pattern4 = new Groum();
		ActionNode node4 = new ActionNode("4", "4");
		pattern4.addVertex(node4);

		Groum pattern12 = new Groum();
		pattern12.addVertex(node1);
		pattern12.addVertex(node2);
		pattern12.addEdge(node1, node2);

		Groum pattern23 = new Groum();
		pattern23.addVertex(node2);
		pattern23.addVertex(node3);
		pattern23.addEdge(node2, node3);

		Groum pattern34 = new Groum();
		pattern34.addVertex(node3);
		pattern34.addVertex(node4);
		pattern34.addEdge(node3, node4);

		Groum pattern123 = new Groum();
		pattern123.addVertex(node1);
		pattern123.addVertex(node2);
		pattern123.addVertex(node3);
		pattern123.addEdge(node1, node2);
		pattern123.addEdge(node2, node3);

		Groum pattern234 = new Groum();
		pattern234.addVertex(node2);
		pattern234.addVertex(node3);
		pattern234.addVertex(node4);
		pattern234.addEdge(node2, node3);
		pattern234.addEdge(node3, node4);

		Groum pattern1234 = new Groum();
		pattern1234.addVertex(node1);
		pattern1234.addVertex(node2);
		pattern1234.addVertex(node3);
		pattern1234.addVertex(node4);
		pattern1234.addEdge(node1, node2);
		pattern1234.addEdge(node2, node3);
		pattern1234.addEdge(node3, node4);

		PatternAssert.assertContainsPatterns(patterns,pattern1,pattern2,pattern3,pattern4,pattern12,
				pattern23,pattern34,pattern123,pattern234,pattern1234);
	}

	@Test
	public void findsGeneratedTenNodesPatterns() {
		PattExplorer uut = new PattExplorer(1);
		List<Groum> listOfXGroums = Fixture.getListOfXGroums(10);
		List<SubGroum> patterns = uut.explorePatterns(listOfXGroums);

		// Target patterns:
		Groum pattern1 = new Groum();
		ActionNode node1 = new ActionNode("1", "1");
		pattern1.addVertex(node1);

		Groum pattern2 = new Groum();
		ActionNode node2 = new ActionNode("2", "2");
		pattern2.addVertex(node2);

		Groum pattern3 = new Groum();
		ActionNode node3 = new ActionNode("3", "3");
		pattern3.addVertex(node3);

		Groum pattern4 = new Groum();
		ActionNode node4 = new ActionNode("4", "4");
		pattern4.addVertex(node4);

		Groum pattern5 = new Groum();
		ActionNode node5 = new ActionNode("5", "5");
		pattern5.addVertex(node5);

		Groum pattern6 = new Groum();
		ActionNode node6 = new ActionNode("6", "6");
		pattern6.addVertex(node6);

		Groum pattern7 = new Groum();
		ActionNode node7 = new ActionNode("7", "7");
		pattern7.addVertex(node7);

		Groum pattern8 = new Groum();
		ActionNode node8 = new ActionNode("8", "8");
		pattern8.addVertex(node8);

		Groum pattern9 = new Groum();
		ActionNode node9 = new ActionNode("9", "9");
		pattern9.addVertex(node9);

		Groum pattern10 = new Groum();
		ActionNode node10 = new ActionNode("10", "10");
		pattern10.addVertex(node10);

		Groum pattern12 = new Groum();
		pattern12.addVertex(node1);
		pattern12.addVertex(node2);
		pattern12.addEdge(node1, node2);

		Groum pattern23 = new Groum();
		pattern23.addVertex(node2);
		pattern23.addVertex(node3);
		pattern23.addEdge(node2, node3);

		Groum pattern34 = new Groum();
		pattern34.addVertex(node3);
		pattern34.addVertex(node4);
		pattern34.addEdge(node3, node4);

		Groum pattern45 = new Groum();
		pattern45.addVertex(node4);
		pattern45.addVertex(node5);
		pattern45.addEdge(node4, node5);

		Groum pattern56 = new Groum();
		pattern56.addVertex(node5);
		pattern56.addVertex(node6);
		pattern56.addEdge(node5, node6);

		Groum pattern67 = new Groum();
		pattern67.addVertex(node6);
		pattern67.addVertex(node7);
		pattern67.addEdge(node6, node7);

		Groum pattern78 = new Groum();
		pattern78.addVertex(node7);
		pattern78.addVertex(node8);
		pattern78.addEdge(node7, node8);

		Groum pattern89 = new Groum();
		pattern89.addVertex(node8);
		pattern89.addVertex(node9);
		pattern89.addEdge(node8, node9);

		Groum pattern910 = new Groum();
		pattern910.addVertex(node9);
		pattern910.addVertex(node10);
		pattern910.addEdge(node9, node10);

		Groum pattern123 = new Groum();
		pattern123.addVertex(node1);
		pattern123.addVertex(node2);
		pattern123.addVertex(node3);
		pattern123.addEdge(node1, node2);
		pattern123.addEdge(node2, node3);

		Groum pattern234 = new Groum();
		pattern234.addVertex(node2);
		pattern234.addVertex(node3);
		pattern234.addVertex(node4);
		pattern234.addEdge(node2, node3);
		pattern234.addEdge(node3, node4);

		Groum pattern345 = new Groum();
		pattern345.addVertex(node3);
		pattern345.addVertex(node4);
		pattern345.addVertex(node5);
		pattern345.addEdge(node3, node4);
		pattern345.addEdge(node4, node5);

		Groum pattern456 = new Groum();
		pattern456.addVertex(node4);
		pattern456.addVertex(node5);
		pattern456.addVertex(node6);
		pattern456.addEdge(node4, node5);
		pattern456.addEdge(node5, node6);

		Groum pattern567 = new Groum();
		pattern567.addVertex(node5);
		pattern567.addVertex(node6);
		pattern567.addVertex(node7);
		pattern567.addEdge(node5, node6);
		pattern567.addEdge(node6, node7);

		Groum pattern678 = new Groum();
		pattern678.addVertex(node6);
		pattern678.addVertex(node7);
		pattern678.addVertex(node8);
		pattern678.addEdge(node6, node7);
		pattern678.addEdge(node7, node8);

		Groum pattern789 = new Groum();
		pattern789.addVertex(node7);
		pattern789.addVertex(node8);
		pattern789.addVertex(node9);
		pattern789.addEdge(node7, node8);
		pattern789.addEdge(node8, node9);

		Groum pattern8910 = new Groum();
		pattern8910.addVertex(node8);
		pattern8910.addVertex(node9);
		pattern8910.addVertex(node10);
		pattern8910.addEdge(node8, node9);
		pattern8910.addEdge(node9, node10);

		Groum pattern1234 = new Groum();
		pattern1234.addVertex(node1);
		pattern1234.addVertex(node2);
		pattern1234.addVertex(node3);
		pattern1234.addVertex(node4);
		pattern1234.addEdge(node1, node2);
		pattern1234.addEdge(node2, node3);
		pattern1234.addEdge(node3, node4);

		Groum pattern2345 = new Groum();
		pattern2345.addVertex(node2);
		pattern2345.addVertex(node3);
		pattern2345.addVertex(node4);
		pattern2345.addVertex(node5);
		pattern2345.addEdge(node2, node3);
		pattern2345.addEdge(node3, node4);
		pattern2345.addEdge(node4, node5);

		Groum pattern3456 = new Groum();
		pattern3456.addVertex(node3);
		pattern3456.addVertex(node4);
		pattern3456.addVertex(node5);
		pattern3456.addVertex(node6);
		pattern3456.addEdge(node3, node4);
		pattern3456.addEdge(node4, node5);
		pattern3456.addEdge(node5, node6);

		Groum pattern4567 = new Groum();
		pattern4567.addVertex(node4);
		pattern4567.addVertex(node5);
		pattern4567.addVertex(node6);
		pattern4567.addVertex(node7);
		pattern4567.addEdge(node4, node5);
		pattern4567.addEdge(node5, node6);
		pattern4567.addEdge(node6, node7);

		Groum pattern5678 = new Groum();
		pattern5678.addVertex(node5);
		pattern5678.addVertex(node6);
		pattern5678.addVertex(node7);
		pattern5678.addVertex(node8);
		pattern5678.addEdge(node5, node6);
		pattern5678.addEdge(node6, node7);
		pattern5678.addEdge(node7, node8);

		Groum pattern6789 = new Groum();
		pattern6789.addVertex(node6);
		pattern6789.addVertex(node7);
		pattern6789.addVertex(node8);
		pattern6789.addVertex(node9);
		pattern6789.addEdge(node6, node7);
		pattern6789.addEdge(node7, node8);
		pattern6789.addEdge(node8, node9);

		Groum pattern78910 = new Groum();
		pattern78910.addVertex(node7);
		pattern78910.addVertex(node8);
		pattern78910.addVertex(node9);
		pattern78910.addVertex(node10);
		pattern78910.addEdge(node7, node8);
		pattern78910.addEdge(node8, node9);
		pattern78910.addEdge(node9, node10);

		Groum pattern12345 = new Groum();
		pattern12345.addVertex(node1);
		pattern12345.addVertex(node2);
		pattern12345.addVertex(node3);
		pattern12345.addVertex(node4);
		pattern12345.addVertex(node5);
		pattern12345.addEdge(node1, node2);
		pattern12345.addEdge(node2, node3);
		pattern12345.addEdge(node3, node4);
		pattern12345.addEdge(node4, node5);

		Groum pattern23456 = new Groum();
		pattern23456.addVertex(node2);
		pattern23456.addVertex(node3);
		pattern23456.addVertex(node4);
		pattern23456.addVertex(node5);
		pattern23456.addVertex(node6);
		pattern23456.addEdge(node2, node3);
		pattern23456.addEdge(node3, node4);
		pattern23456.addEdge(node4, node5);
		pattern23456.addEdge(node5, node6);

		Groum pattern34567 = new Groum();
		pattern34567.addVertex(node3);
		pattern34567.addVertex(node4);
		pattern34567.addVertex(node5);
		pattern34567.addVertex(node6);
		pattern34567.addVertex(node7);
		pattern34567.addEdge(node3, node4);
		pattern34567.addEdge(node4, node5);
		pattern34567.addEdge(node5, node6);
		pattern34567.addEdge(node6, node7);

		Groum pattern45678 = new Groum();
		pattern45678.addVertex(node4);
		pattern45678.addVertex(node5);
		pattern45678.addVertex(node6);
		pattern45678.addVertex(node7);
		pattern45678.addVertex(node8);
		pattern45678.addEdge(node4, node5);
		pattern45678.addEdge(node5, node6);
		pattern45678.addEdge(node6, node7);
		pattern45678.addEdge(node7, node8);

		Groum pattern56789 = new Groum();
		pattern56789.addVertex(node5);
		pattern56789.addVertex(node6);
		pattern56789.addVertex(node7);
		pattern56789.addVertex(node8);
		pattern56789.addVertex(node9);
		pattern56789.addEdge(node5, node6);
		pattern56789.addEdge(node6, node7);
		pattern56789.addEdge(node7, node8);
		pattern56789.addEdge(node8, node9);

		Groum pattern678910 = new Groum();
		pattern678910.addVertex(node6);
		pattern678910.addVertex(node7);
		pattern678910.addVertex(node8);
		pattern678910.addVertex(node9);
		pattern678910.addVertex(node10);
		pattern678910.addEdge(node6, node7);
		pattern678910.addEdge(node7, node8);
		pattern678910.addEdge(node8, node9);
		pattern678910.addEdge(node9, node10);

		Groum pattern123456 = new Groum();
		pattern123456.addVertex(node1);
		pattern123456.addVertex(node2);
		pattern123456.addVertex(node3);
		pattern123456.addVertex(node4);
		pattern123456.addVertex(node5);
		pattern123456.addVertex(node6);
		pattern123456.addEdge(node1, node2);
		pattern123456.addEdge(node2, node3);
		pattern123456.addEdge(node3, node4);
		pattern123456.addEdge(node4, node5);
		pattern123456.addEdge(node5, node6);

		Groum pattern234567 = new Groum();
		pattern234567.addVertex(node2);
		pattern234567.addVertex(node3);
		pattern234567.addVertex(node4);
		pattern234567.addVertex(node5);
		pattern234567.addVertex(node6);
		pattern234567.addVertex(node7);
		pattern234567.addEdge(node2, node3);
		pattern234567.addEdge(node3, node4);
		pattern234567.addEdge(node4, node5);
		pattern234567.addEdge(node5, node6);
		pattern234567.addEdge(node6, node7);

		Groum pattern345678 = new Groum();
		pattern345678.addVertex(node3);
		pattern345678.addVertex(node4);
		pattern345678.addVertex(node5);
		pattern345678.addVertex(node6);
		pattern345678.addVertex(node7);
		pattern345678.addVertex(node8);
		pattern345678.addEdge(node3, node4);
		pattern345678.addEdge(node4, node5);
		pattern345678.addEdge(node5, node6);
		pattern345678.addEdge(node6, node7);
		pattern345678.addEdge(node7, node8);

		Groum pattern456789 = new Groum();
		pattern456789.addVertex(node4);
		pattern456789.addVertex(node5);
		pattern456789.addVertex(node6);
		pattern456789.addVertex(node7);
		pattern456789.addVertex(node8);
		pattern456789.addVertex(node9);
		pattern456789.addEdge(node4, node5);
		pattern456789.addEdge(node5, node6);
		pattern456789.addEdge(node6, node7);
		pattern456789.addEdge(node7, node8);
		pattern456789.addEdge(node8, node9);

		Groum pattern5678910 = new Groum();
		pattern5678910.addVertex(node5);
		pattern5678910.addVertex(node6);
		pattern5678910.addVertex(node7);
		pattern5678910.addVertex(node8);
		pattern5678910.addVertex(node9);
		pattern5678910.addVertex(node10);
		pattern5678910.addEdge(node5, node6);
		pattern5678910.addEdge(node6, node7);
		pattern5678910.addEdge(node7, node8);
		pattern5678910.addEdge(node8, node9);
		pattern5678910.addEdge(node9, node10);

		Groum pattern1234567 = new Groum();
		pattern1234567.addVertex(node1);
		pattern1234567.addVertex(node2);
		pattern1234567.addVertex(node3);
		pattern1234567.addVertex(node4);
		pattern1234567.addVertex(node5);
		pattern1234567.addVertex(node6);
		pattern1234567.addVertex(node7);
		pattern1234567.addEdge(node1, node2);
		pattern1234567.addEdge(node2, node3);
		pattern1234567.addEdge(node3, node4);
		pattern1234567.addEdge(node4, node5);
		pattern1234567.addEdge(node5, node6);
		pattern1234567.addEdge(node6, node7);

		Groum pattern2345678 = new Groum();
		pattern2345678.addVertex(node2);
		pattern2345678.addVertex(node3);
		pattern2345678.addVertex(node4);
		pattern2345678.addVertex(node5);
		pattern2345678.addVertex(node6);
		pattern2345678.addVertex(node7);
		pattern2345678.addVertex(node8);
		pattern2345678.addEdge(node2, node3);
		pattern2345678.addEdge(node3, node4);
		pattern2345678.addEdge(node4, node5);
		pattern2345678.addEdge(node5, node6);
		pattern2345678.addEdge(node6, node7);
		pattern2345678.addEdge(node7, node8);

		Groum pattern3456789 = new Groum();
		pattern3456789.addVertex(node3);
		pattern3456789.addVertex(node4);
		pattern3456789.addVertex(node5);
		pattern3456789.addVertex(node6);
		pattern3456789.addVertex(node7);
		pattern3456789.addVertex(node8);
		pattern3456789.addVertex(node9);
		pattern3456789.addEdge(node3, node4);
		pattern3456789.addEdge(node4, node5);
		pattern3456789.addEdge(node5, node6);
		pattern3456789.addEdge(node6, node7);
		pattern3456789.addEdge(node7, node8);
		pattern3456789.addEdge(node8, node9);

		Groum pattern45678910 = new Groum();
		pattern45678910.addVertex(node4);
		pattern45678910.addVertex(node5);
		pattern45678910.addVertex(node6);
		pattern45678910.addVertex(node7);
		pattern45678910.addVertex(node8);
		pattern45678910.addVertex(node9);
		pattern45678910.addVertex(node10);
		pattern45678910.addEdge(node4, node5);
		pattern45678910.addEdge(node5, node6);
		pattern45678910.addEdge(node6, node7);
		pattern45678910.addEdge(node7, node8);
		pattern45678910.addEdge(node8, node9);
		pattern45678910.addEdge(node9, node10);

		Groum pattern12345678 = new Groum();
		pattern12345678.addVertex(node1);
		pattern12345678.addVertex(node2);
		pattern12345678.addVertex(node3);
		pattern12345678.addVertex(node4);
		pattern12345678.addVertex(node5);
		pattern12345678.addVertex(node6);
		pattern12345678.addVertex(node7);
		pattern12345678.addVertex(node8);
		pattern12345678.addEdge(node1, node2);
		pattern12345678.addEdge(node2, node3);
		pattern12345678.addEdge(node3, node4);
		pattern12345678.addEdge(node4, node5);
		pattern12345678.addEdge(node5, node6);
		pattern12345678.addEdge(node6, node7);
		pattern12345678.addEdge(node7, node8);

		Groum pattern23456789 = new Groum();
		pattern23456789.addVertex(node2);
		pattern23456789.addVertex(node3);
		pattern23456789.addVertex(node4);
		pattern23456789.addVertex(node5);
		pattern23456789.addVertex(node6);
		pattern23456789.addVertex(node7);
		pattern23456789.addVertex(node8);
		pattern23456789.addVertex(node9);
		pattern23456789.addEdge(node2, node3);
		pattern23456789.addEdge(node3, node4);
		pattern23456789.addEdge(node4, node5);
		pattern23456789.addEdge(node5, node6);
		pattern23456789.addEdge(node6, node7);
		pattern23456789.addEdge(node7, node8);
		pattern23456789.addEdge(node8, node9);

		Groum pattern345678910 = new Groum();
		pattern345678910.addVertex(node3);
		pattern345678910.addVertex(node4);
		pattern345678910.addVertex(node5);
		pattern345678910.addVertex(node6);
		pattern345678910.addVertex(node7);
		pattern345678910.addVertex(node8);
		pattern345678910.addVertex(node9);
		pattern345678910.addVertex(node10);
		pattern345678910.addEdge(node3, node4);
		pattern345678910.addEdge(node4, node5);
		pattern345678910.addEdge(node5, node6);
		pattern345678910.addEdge(node6, node7);
		pattern345678910.addEdge(node7, node8);
		pattern345678910.addEdge(node8, node9);
		pattern345678910.addEdge(node9, node10);

		Groum pattern123456789 = new Groum();
		pattern123456789.addVertex(node1);
		pattern123456789.addVertex(node2);
		pattern123456789.addVertex(node3);
		pattern123456789.addVertex(node4);
		pattern123456789.addVertex(node5);
		pattern123456789.addVertex(node6);
		pattern123456789.addVertex(node7);
		pattern123456789.addVertex(node8);
		pattern123456789.addVertex(node9);
		pattern123456789.addEdge(node1, node2);
		pattern123456789.addEdge(node2, node3);
		pattern123456789.addEdge(node3, node4);
		pattern123456789.addEdge(node4, node5);
		pattern123456789.addEdge(node5, node6);
		pattern123456789.addEdge(node6, node7);
		pattern123456789.addEdge(node7, node8);
		pattern123456789.addEdge(node8, node9);

		Groum pattern2345678910 = new Groum();
		pattern2345678910.addVertex(node2);
		pattern2345678910.addVertex(node3);
		pattern2345678910.addVertex(node4);
		pattern2345678910.addVertex(node5);
		pattern2345678910.addVertex(node6);
		pattern2345678910.addVertex(node7);
		pattern2345678910.addVertex(node8);
		pattern2345678910.addVertex(node9);
		pattern2345678910.addVertex(node10);
		pattern2345678910.addEdge(node2, node3);
		pattern2345678910.addEdge(node3, node4);
		pattern2345678910.addEdge(node4, node5);
		pattern2345678910.addEdge(node5, node6);
		pattern2345678910.addEdge(node6, node7);
		pattern2345678910.addEdge(node7, node8);
		pattern2345678910.addEdge(node8, node9);
		pattern2345678910.addEdge(node9, node10);

		Groum pattern12345678910 = new Groum();
		pattern12345678910.addVertex(node1);
		pattern12345678910.addVertex(node2);
		pattern12345678910.addVertex(node3);
		pattern12345678910.addVertex(node4);
		pattern12345678910.addVertex(node5);
		pattern12345678910.addVertex(node6);
		pattern12345678910.addVertex(node7);
		pattern12345678910.addVertex(node8);
		pattern12345678910.addVertex(node9);
		pattern12345678910.addVertex(node10);
		pattern12345678910.addEdge(node1, node2);
		pattern12345678910.addEdge(node2, node3);
		pattern12345678910.addEdge(node3, node4);
		pattern12345678910.addEdge(node4, node5);
		pattern12345678910.addEdge(node5, node6);
		pattern12345678910.addEdge(node6, node7);
		pattern12345678910.addEdge(node7, node8);
		pattern12345678910.addEdge(node8, node9);
		pattern12345678910.addEdge(node9, node10);
		
		PatternAssert.assertContainsPatterns(patterns, pattern1, pattern2, pattern3, pattern4, pattern5, pattern6
				, pattern7, pattern8, pattern9, pattern10, pattern12, pattern23, pattern34, pattern45, pattern56, pattern67
				, pattern78, pattern89, pattern910, pattern123, pattern234, pattern345, pattern456, pattern567, pattern678, pattern789
				, pattern8910, pattern1234, pattern2345, pattern3456, pattern4567, pattern5678, pattern6789, pattern78910
				, pattern12345, pattern23456, pattern34567, pattern45678, pattern56789, pattern678910, pattern123456,
				pattern234567, pattern345678, pattern456789, pattern5678910, pattern1234567, pattern2345678, pattern3456789
				, pattern45678910, pattern12345678, pattern23456789, pattern345678910, pattern123456789, pattern2345678910
				, pattern12345678910);
	}

	@Test
	public void findPatternsOfSize5() {
		PattExplorer uut = new PattExplorer(5);
		List<Groum> listOfXGroums = Fixture.getListOfXGroums(10);
		List<SubGroum> patterns = uut.explorePatterns(listOfXGroums);

		// Target patterns:
		// Target patterns:
		Groum pattern1 = Fixture.createConnectedGroumOfSize(1, 1);
		Groum pattern2 = Fixture.createConnectedGroumOfSize(2, 2);
		Groum pattern3 = Fixture.createConnectedGroumOfSize(3, 3);
		Groum pattern4 = Fixture.createConnectedGroumOfSize(4, 4);
		Groum pattern5 = Fixture.createConnectedGroumOfSize(5, 5);
		Groum pattern6 = Fixture.createConnectedGroumOfSize(6, 6);

		Groum pattern12 = Fixture.createConnectedGroumOfSize(1, 2);
		Groum pattern23 = Fixture.createConnectedGroumOfSize(2, 3);
		Groum pattern34 = Fixture.createConnectedGroumOfSize(3, 4);
		Groum pattern45 = Fixture.createConnectedGroumOfSize(4, 5);
		Groum pattern56 = Fixture.createConnectedGroumOfSize(5, 6);

		Groum pattern123 = Fixture.createConnectedGroumOfSize(1, 3);
		Groum pattern234 = Fixture.createConnectedGroumOfSize(2, 4);
		Groum pattern345 = Fixture.createConnectedGroumOfSize(3, 5);
		Groum pattern456 = Fixture.createConnectedGroumOfSize(4, 6);

		Groum pattern1234 = Fixture.createConnectedGroumOfSize(1, 4);
		Groum pattern2345 = Fixture.createConnectedGroumOfSize(2, 5);
		Groum pattern3456 = Fixture.createConnectedGroumOfSize(3, 6);

		Groum pattern12345 = Fixture.createConnectedGroumOfSize(1, 5);
		Groum pattern23456 = Fixture.createConnectedGroumOfSize(1, 5);

		Groum pattern123456 = Fixture.createConnectedGroumOfSize(1, 6);

		PatternAssert.assertContainsPatterns(patterns, pattern1, pattern2, pattern3, pattern4, pattern5, pattern6
				, pattern12, pattern23, pattern34, pattern45, pattern56, pattern123, pattern234, pattern345, pattern456
				, pattern1234, pattern2345, pattern3456, pattern12345, pattern23456, pattern123456);		
	}

	@Test	
	@Ignore
	public void findsGenerated30NodesPatterns() {
		PattExplorer uut = new PattExplorer(1);
		List<Groum> listOfXGroums = Fixture.getListOfXGroums(30);
		List<SubGroum> patterns = uut.explorePatterns(listOfXGroums);
		assertTrue(patterns.size() == 465);
	}

}
