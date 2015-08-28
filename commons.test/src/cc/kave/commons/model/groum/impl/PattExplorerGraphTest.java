package cc.kave.commons.model.groum.impl;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.INode;
import cc.kave.commons.model.groum.ISubGroum;
import cc.kave.commons.model.groum.nodes.ActionNode;
import cc.kave.commons.model.pattexplore.IPattExplorer;
import cc.kave.commons.model.pattexplore.PattExplorer;
import static cc.kave.commons.model.groum.impl.PatternAssert.assertContainsPatterns;
import static cc.kave.commons.model.groum.impl.PatternAssert.patternsOfSize;

import static org.junit.Assert.assertTrue;

public class PattExplorerGraphTest {

	@Test
	public void distinguishesPatternsByStructure() {
		//      1          1     1
		//    /   \        |     | \
		//   2     3  =>   2  ,  2  3
		//   |             |
		//   3             3
		
		Groum subject = new Groum();
		ActionNode node1 = new ActionNode("1", "1");
		subject.addVertex(node1);
		ActionNode node2 = new ActionNode("2", "2");
		subject.addVertex(node2);
		ActionNode node3a = new ActionNode("3", "3");
		subject.addVertex(node3a);
		ActionNode node3b = new ActionNode("3", "3");
		subject.addVertex(node3b);

		subject.addEdge(node1, node2);
		subject.addEdge(node1, node3a);
		subject.addEdge(node2, node3b);

		PattExplorer uut = new PattExplorer(1);
		List<ISubGroum> actuals = uut.explorePatterns(Arrays.asList(subject));

		Groum patternA = new Groum();
		patternA.addVertex(node1);
		patternA.addVertex(node2);
		patternA.addVertex(node3a);
		patternA.addEdge(node1, node2);
		patternA.addEdge(node1, node3a);

		Groum patternB = new Groum();
		patternB.addVertex(node1);
		patternB.addVertex(node2);
		patternB.addVertex(node3b);
		patternB.addEdge(node1, node2);
		patternB.addEdge(node2, node3b);
		List<ISubGroum> patternsOfSize = patternsOfSize(actuals, 3);
		assertContainsPatterns(patternsOfSize, patternA, patternB);
	}

	/*
	 * 			1    			1			1
	 * 		 /     \ 			|		   / \
	 *      2       3 (!)		2		  2   3
	 *    /   \   /     		|		
	 *   3  ->  4      			3			   
	 *           \				|	
	 *            5				4	
	 * 							|
	 * 							5
	 */
	@Test
	public void findsTwoNodesPattern() {
		IPattExplorer uut = new PattExplorer(2);
		IGroum listGroum = Fixture.createConnectedGroumOfSize(5);

		IGroum complexGroum = new Groum();
		INode node1 = new ActionNode("1", "1");
		INode node2 = new ActionNode("2", "2");
		INode node3a = new ActionNode("3", "3");
		INode node3b = new ActionNode("3", "3");
		INode node4 = new ActionNode("4", "4");
		INode node5 = new ActionNode("5", "5");
		complexGroum.addVertex(node1);
		complexGroum.addVertex(node2);
		complexGroum.addVertex(node3a);
		complexGroum.addVertex(node3b);
		complexGroum.addVertex(node4);
		complexGroum.addVertex(node5);
		complexGroum.addEdge(node1, node2);
		complexGroum.addEdge(node1, node3a);
		complexGroum.addEdge(node2, node4);
		complexGroum.addEdge(node2, node3b);
		complexGroum.addEdge(node3a, node4);
		complexGroum.addEdge(node3b, node4);
		complexGroum.addEdge(node4, node5);

		IGroum structuredGroum = new Groum();
		structuredGroum.addVertex(node1);
		structuredGroum.addVertex(node2);
		structuredGroum.addVertex(node3a);
		structuredGroum.addEdge(node1, node2);
		structuredGroum.addEdge(node1, node3a);

		IGroum patternA = Fixture.createConnectedGroumOfSize(3);
		IGroum patternB = Fixture.createConnectedGroumOfSize(3, 5);

		List<ISubGroum> actuals = uut.explorePatterns(Arrays.asList(complexGroum, structuredGroum, listGroum));
		List<ISubGroum> patternsOfSize = patternsOfSize(actuals, 3);
		assertContainsPatterns(patternsOfSize, patternA, patternB, structuredGroum);
	}

	/*
	 * 							1			1
	 * 						   / \			|
	 * 					      2   3			2
	 *                       / \   \		|
	 *                      4   3   4		3
	 *                         /			|
	 *                        4				4
	 *                       /				|
	 *                      5				5
	 *                       
	 */
	@Test
	public void findsPatternsInTwoGroums() {
		IPattExplorer uut = new PattExplorer(3);
		IGroum listGroum = Fixture.createConnectedGroumOfSize(5);
		IGroum complexGroum = new Groum();
		INode node1 = new ActionNode("1", "1");
		INode node2 = new ActionNode("2", "2");
		INode node3a = new ActionNode("3", "3");
		INode node3b = new ActionNode("3", "3");
		INode node4a = new ActionNode("4", "4");
		INode node4b = new ActionNode("4", "4");
		INode node5 = new ActionNode("5", "5");
		complexGroum.addVertex(node1);
		complexGroum.addVertex(node2);
		complexGroum.addVertex(node3a);
		complexGroum.addVertex(node3b);
		complexGroum.addVertex(node4a);
		complexGroum.addVertex(node4b);
		complexGroum.addVertex(node5);

		complexGroum.addEdge(node1, node2);
		complexGroum.addEdge(node1, node3a);
		complexGroum.addEdge(node2, node4a);
		complexGroum.addEdge(node2, node3b);
		complexGroum.addEdge(node3b, node4a);
		complexGroum.addEdge(node3a, node4b);
		complexGroum.addEdge(node4a, node5);

		List<ISubGroum> patterns = uut.explorePatterns(Arrays.asList(listGroum, complexGroum));
		assertTrue(patterns.size() == 3);
	}

	/*
	 * 							1		  		   1				 1		
	 * 						   / \			  	  / \		        / \		
	 * 					     2     2		    2     2	          2     2	
	 *                      / \   / \		   / \   / \	     / \   / \	
	 *                     3   3 3 	 3	      3   3 3 	3	    3   3 3   3	
	 *                         	  \	             /						   \
	 *                        	   4            4		                    4         				
	 */
	@Test
	public void findsPatternsInThreeGroums() {
		IPattExplorer uut = new PattExplorer(6);
		IGroum groumA = new Groum();
		IGroum groumB = new Groum();
		IGroum groumC = new Groum();

		INode nodeA_1 = new ActionNode("1", "1");
		INode nodeA_2a = new ActionNode("2", "2");
		INode nodeA_2b = new ActionNode("2", "2");
		INode nodeA_3a = new ActionNode("3", "3");
		INode nodeA_3b = new ActionNode("3", "3");
		INode nodeA_3c = new ActionNode("3", "3");
		INode nodeA_3d = new ActionNode("3", "3");
		INode nodeA_4 = new ActionNode("4", "4");
		groumA.addVertex(nodeA_1);
		groumA.addVertex(nodeA_2a);
		groumA.addVertex(nodeA_2b);
		groumA.addVertex(nodeA_3a);
		groumA.addVertex(nodeA_3b);
		groumA.addVertex(nodeA_3c);
		groumA.addVertex(nodeA_3d);
		groumA.addVertex(nodeA_4);
		groumA.addEdge(nodeA_1, nodeA_2a);
		groumA.addEdge(nodeA_1, nodeA_2a);
		groumA.addEdge(nodeA_2a, nodeA_3a);
		groumA.addEdge(nodeA_2a, nodeA_3b);
		groumA.addEdge(nodeA_2b, nodeA_3c);
		groumA.addEdge(nodeA_2b, nodeA_3d);
		groumA.addEdge(nodeA_3c, nodeA_4);

		INode nodeB_1 = new ActionNode("1", "1");
		INode nodeB_2a = new ActionNode("2", "2");
		INode nodeB_2b = new ActionNode("2", "2");
		INode nodeB_3a = new ActionNode("3", "3");
		INode nodeB_3b = new ActionNode("3", "3");
		INode nodeB_3c = new ActionNode("3", "3");
		INode nodeB_3d = new ActionNode("3", "3");
		INode nodeB_4 = new ActionNode("4", "4");
		groumB.addVertex(nodeB_1);
		groumB.addVertex(nodeB_2a);
		groumB.addVertex(nodeB_2b);
		groumB.addVertex(nodeB_3a);
		groumB.addVertex(nodeB_3b);
		groumB.addVertex(nodeB_3c);
		groumB.addVertex(nodeB_3d);
		groumB.addVertex(nodeB_4);
		groumB.addEdge(nodeB_1, nodeB_2a);
		groumB.addEdge(nodeB_1, nodeB_2a);
		groumB.addEdge(nodeB_2a, nodeB_3a);
		groumB.addEdge(nodeB_2a, nodeB_3b);
		groumB.addEdge(nodeB_2b, nodeB_3c);
		groumB.addEdge(nodeB_2b, nodeB_3d);
		groumB.addEdge(nodeB_3b, nodeB_4);

		INode nodeC_1 = new ActionNode("1", "1");
		INode nodeC_2a = new ActionNode("2", "2");
		INode nodeC_2b = new ActionNode("2", "2");
		INode nodeC_3a = new ActionNode("3", "3");
		INode nodeC_3b = new ActionNode("3", "3");
		INode nodeC_3c = new ActionNode("3", "3");
		INode nodeC_3d = new ActionNode("3", "3");
		INode nodeC_4 = new ActionNode("4", "4");
		groumC.addVertex(nodeC_1);
		groumC.addVertex(nodeC_2a);
		groumC.addVertex(nodeC_2b);
		groumC.addVertex(nodeC_3a);
		groumC.addVertex(nodeC_3b);
		groumC.addVertex(nodeC_3c);
		groumC.addVertex(nodeC_3d);
		groumC.addVertex(nodeC_4);
		groumC.addEdge(nodeC_1, nodeC_2a);
		groumC.addEdge(nodeC_1, nodeC_2a);
		groumC.addEdge(nodeC_2a, nodeC_3a);
		groumC.addEdge(nodeC_2a, nodeC_3b);
		groumC.addEdge(nodeC_2b, nodeC_3c);
		groumC.addEdge(nodeC_2b, nodeC_3d);
		groumC.addEdge(nodeC_3d, nodeC_4);

		List<ISubGroum> patterns = uut.explorePatterns(Arrays.asList(groumA, groumB, groumC));
		assertTrue(patterns.size() == 3);
	}

	@Test
	public void countsOverlappingInstanesOnlyOnce1() {
		INode node1 = new ActionNode("1", "1");
		INode node2a = new ActionNode("2", "2");
		INode node2b = new ActionNode("2", "2");
		IGroum overlappingGroum = createGroum(node1, node2a, node2b);
		overlappingGroum.addEdge(node1, node2a);
		overlappingGroum.addEdge(node2a, node2b);

		List<ISubGroum> patterns = findPatternsWithMinFrequency(2, overlappingGroum);

		IGroum pattern1 = createGroum(node2a);
		assertContainsPatterns(patterns, pattern1);
	}

	@Test
	public void countsOverlappingInstanesOnlyOnce2() {
		INode node1 = new ActionNode("1", "1");
		INode node2a = new ActionNode("2", "2");
		INode node2b = new ActionNode("2", "2");
		IGroum overlappingGroum = createGroum(node1, node2a, node2b);
		overlappingGroum.addEdge(node1, node2a);
		overlappingGroum.addEdge(node1, node2b);

		List<ISubGroum> patterns = findPatternsWithMinFrequency(2, overlappingGroum);

		IGroum pattern1 = createGroum(node2a);
		assertContainsPatterns(patterns, pattern1);
	}

	@Test
	public void findsMultipleInstanceInOneGraph() {
		INode node1a = new ActionNode("1", "1");
		INode node1b = new ActionNode("1", "1");
		INode node2a = new ActionNode("2", "2");
		INode node2b = new ActionNode("2", "2");
		IGroum overlappingGroum = createGroum(node1a, node1b, node2a, node2b);
		overlappingGroum.addEdge(node1a, node2a);
		overlappingGroum.addEdge(node1a, node1b);
		overlappingGroum.addEdge(node1b, node2b);

		List<ISubGroum> patterns = findPatternsWithMinFrequency(2, overlappingGroum);

		IGroum pattern1 = createGroum(node1a, node2a);
		pattern1.addEdge(node1a, node2a);
		patterns = patternsOfSize(patterns, 2);
		assertContainsPatterns(patterns, pattern1);
	}

	@Test
	public void findsGraphIsomorphism() {
		INode node1 = new ActionNode("1", "1");
		INode node2 = new ActionNode("2", "2");
		INode node3a = new ActionNode("3", "3");
		INode node3b = new ActionNode("3", "3");
		IGroum groum1 = createGroum(node1, node2, node3a);
		groum1.addEdge(node1, node2);
		groum1.addEdge(node1, node3a);
		groum1.addEdge(node2, node3a);
		IGroum groum2 = createGroum(node1, node2, node3a, node3b);
		groum2.addEdge(node1, node2);
		groum2.addEdge(node1, node3a);
		groum2.addEdge(node2, node3b);

		List<ISubGroum> patterns = findPatternsWithMinFrequency(2, groum1, groum2);

		IGroum pattern1 = createGroum(node1, node2, node3a);
		pattern1.addEdge(node1, node2);
		pattern1.addEdge(node1, node3a);
		pattern1.addEdge(node2, node3a);
		patterns = patternsOfSize(patterns, 3);
		assertContainsPatterns(patterns, pattern1);
	}

	@Test
	public void includesAllEdgesBetweenAllIncludedNodes() {
		INode node1 = new ActionNode("1", "1");
		INode node2 = new ActionNode("2", "2");
		INode node3 = new ActionNode("3", "3");
		IGroum groum = createGroum(node1, node2, node3);
		groum.addEdge(node1, node2);
		groum.addEdge(node1, node3);
		groum.addEdge(node2, node3);

		List<ISubGroum> patterns = findPatternsWithMinFrequency(1, groum);

		patterns = patternsOfSize(patterns, 3);
		assertContainsPatterns(patterns, groum);
	}

	private List<ISubGroum> findPatternsWithMinFrequency(int threshold, IGroum... groums) {
		IPattExplorer uut = new PattExplorer(threshold);
		return uut.explorePatterns(Arrays.asList(groums));
	}

	private IGroum createGroum(INode... nodes) {
		Groum groum = new Groum();
		for (INode node : nodes) {
			groum.addVertex(node);
		}
		return groum;
	}

}
