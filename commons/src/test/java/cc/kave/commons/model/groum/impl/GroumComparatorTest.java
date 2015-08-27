package cc.kave.commons.model.groum.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.ISubGroum;
import cc.kave.commons.model.groum.impl.comparator.GroumComparator;
import cc.kave.commons.model.groum.impl.comparator.SubGroumComparator;
import cc.kave.commons.model.groum.impl.comparator.SubGroumIdentComparator;
import cc.kave.commons.model.groum.nodes.IActionNode;
import cc.kave.commons.model.groum.nodes.impl.ActionNode;

import com.google.common.collect.TreeMultimap;

public class GroumComparatorTest {

	@Test
	public void comparatorProblem() {
		ActionNode node1 = new ActionNode("1", "1");
		ActionNode node2 = new ActionNode("2", "2");
		ISubGroum groum1 = new SubGroum();
		groum1.addVertex(node1);
		groum1.addVertex(node2);
		groum1.addEdge(node1, node2);

		ActionNode node1a = new ActionNode("1", "1");
		ActionNode node2a = new ActionNode("2", "2");
		ISubGroum groum2 = new SubGroum();
		groum2.addVertex(node2a);
		groum2.addVertex(node1a);
		groum2.addEdge(node1a, node2a);

		ActionNode node1b = new ActionNode("1", "1");
		ActionNode node2b = new ActionNode("3", "3");
		ISubGroum groum3 = new SubGroum();
		groum3.addVertex(node1b);
		groum3.addVertex(node2b);
		groum3.addEdge(node1b, node2b);

		TreeMultimap<ISubGroum, ISubGroum> treemap = TreeMultimap.create(new SubGroumComparator(),
				new SubGroumIdentComparator());
		treemap.put(groum3, groum3);
		treemap.put(groum1, groum1);
		treemap.put(groum2, groum2);

		assertEquals(2, treemap.keySet().size());
		assertTrue(treemap.keySet().containsAll(Arrays.asList(groum1, groum3)));
	}
	
	@Test
	public void NonemptyGroumIsGreaterThanEmptyGroum() {
		IGroum empty = new Groum();
		IGroum nonempty = Fixture.createConnectedGroumOfSize(2);
		GroumComparator uut = new GroumComparator();
		assertEquals(1, uut.compare(nonempty, empty));
	}
	
	@Test
	public void emptyGroumIsLessThanNonemptyGroum() {
		IGroum empty = new Groum();
		IGroum nonempty = Fixture.createConnectedGroumOfSize(2);
		GroumComparator uut = new GroumComparator();
		assertEquals(-1, uut.compare(empty, nonempty));
	}
	
	@Test
	public void emptyGroumIsEqualToEmptyGroum() {
		IGroum empty = new Groum();
		IGroum otherempty = new Groum();
		GroumComparator uut = new GroumComparator();
		assertEquals(0, uut.compare(empty, otherempty));
	}
	
	@Test
	public void findsEdgeCountDifference() {
		IGroum groum = Fixture.createConnectedGroumOfSize(3);
		
		IGroum groum_less_eges = new Groum();
		IActionNode a = new ActionNode("1","1");
		IActionNode b = new ActionNode("2","2");
		IActionNode c = new ActionNode("3","3");
		groum_less_eges.addVertex(a);
		groum_less_eges.addVertex(b);
		groum_less_eges.addVertex(c);
		groum_less_eges.addEdge(a, b);

		GroumComparator uut = new GroumComparator();
		assertEquals(1, uut.compare(groum, groum_less_eges));
	}
	
	@Test
	public void findsEqualGroumsWithDifferentOrder() {
		IActionNode node1 = new ActionNode("1","1");
		IActionNode node2 = new ActionNode("2","2");
		IActionNode node3 = new ActionNode("3","3");
		
		IGroum groum1 = new SubGroum();
		groum1.addVertex(node1);
		groum1.addVertex(node2);
		groum1.addVertex(node3);
		groum1.addEdge(node1, node2);
		groum1.addEdge(node2, node3);
		
		IGroum groum2 = new SubGroum();
		groum2.addVertex(node2);
		groum2.addVertex(node3);
		groum2.addEdge(node2, node3);
		groum2.addVertex(node1);
		groum2.addEdge(node1, node2);
		
		GroumComparator uut = new GroumComparator();
		assertEquals(0, uut.compare(groum1, groum2));
	}
	
	@Test
	public void findsDifferenceInSuccessorCount() {
		IActionNode node1 = new ActionNode("1","1");
		IActionNode node2 = new ActionNode("2","2");
		IActionNode node3 = new ActionNode("3","3");
		IActionNode node4 = new ActionNode("4","4");
		
		ISubGroum groum1 = new SubGroum();
		groum1.addVertex(node1);
		groum1.addVertex(node2);
		groum1.addVertex(node3);
		groum1.addVertex(node4);
		groum1.addEdge(node1, node2);
		groum1.addEdge(node2, node3);
		groum1.addEdge(node2, node4);
		
		ISubGroum groum2 = new SubGroum();
		groum2.addVertex(node1);
		groum2.addVertex(node2);
		groum2.addVertex(node3);
		groum2.addVertex(node4);
		groum2.addEdge(node1, node2);
		groum2.addEdge(node2, node3);
		groum2.addEdge(node3, node4);
		
		GroumComparator uut = new GroumComparator();
		assertEquals(1, uut.compare(groum1, groum2));
	}
	
	@Test
	public void findsDifferenceInSuccessor() {
		IActionNode node1 = new ActionNode("1","1");
		IActionNode node2 = new ActionNode("2","2");
		IActionNode node3 = new ActionNode("3","3");
		IActionNode node4 = new ActionNode("4","4");
		
		ISubGroum groum1 = new SubGroum();
		groum1.addVertex(node1);
		groum1.addVertex(node2);
		groum1.addVertex(node3);
		groum1.addVertex(node4);
		groum1.addEdge(node1, node2);
		groum1.addEdge(node1, node3);
		groum1.addEdge(node2, node4);
		
		ISubGroum groum2 = new SubGroum();
		groum2.addVertex(node4);
		groum2.addVertex(node3);
		groum2.addVertex(node2);
		groum2.addVertex(node1);
		groum2.addEdge(node1, node2);
		groum2.addEdge(node1, node3);
		groum2.addEdge(node3, node4);
		
		GroumComparator uut = new GroumComparator();
		assertEquals(1, uut.compare(groum1, groum2));
	}
}
