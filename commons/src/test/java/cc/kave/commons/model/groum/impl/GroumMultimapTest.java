package cc.kave.commons.model.groum.impl;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.INode;
import cc.kave.commons.model.groum.ISubGroum;
import cc.kave.commons.model.groum.impl.comparator.GroumComparator;
import cc.kave.commons.model.groum.impl.comparator.GroumIdentComparator;
import cc.kave.commons.model.groum.impl.comparator.SubGroumComparator;
import cc.kave.commons.model.groum.impl.comparator.SubGroumIdentComparator;
import cc.kave.commons.model.groum.nodes.impl.ActionNode;
import cc.kave.commons.model.pattexplore.NaivSubgraphStrategy;
import cc.kave.commons.model.pattexplore.Utils;

import com.google.common.collect.BoundType;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultimap;
import com.google.common.collect.TreeMultiset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GroumMultimapTest {

	@Test
	public void comparatorWorksForGroums() {

		TreeMultimap<IGroum, IGroum> treemap = TreeMultimap.create(new GroumComparator(), new GroumIdentComparator());

		IGroum groum1 = Fixture.createConnectedGroumOfSize(1);
		IGroum groum2 = Fixture.createConnectedGroumOfSize(2);
		IGroum groum3 = Fixture.createConnectedGroumOfSize(3);
		IGroum groum4 = Fixture.createConnectedGroumOfSize(1);
		IGroum groum5 = Fixture.createConnectedGroumOfSize(2);
		IGroum groum6 = Fixture.createConnectedGroumOfSize(3);
		treemap.put(groum1, groum1);
		treemap.put(groum2, groum2);
		treemap.put(groum3, groum3);
		treemap.put(groum4, groum4);
		treemap.put(groum5, groum5);
		treemap.put(groum6, groum6);
		assertTrue(treemap.size() == 6 && treemap.get(groum2).size() == 2);

	}

	@Test
	public void comparatorWorksForSubgroums() {
		TreeMultiset<ISubGroum> treeset = TreeMultiset.create(new SubGroumComparator());
		IGroum groum1 = Fixture.createConnectedGroumOfSize(2);
		((Groum) groum1).setSubgraphStrategy(new NaivSubgraphStrategy());
		ISubGroum subGroum1 = groum1.getSubgraphs(Fixture.createConnectedGroumOfSize(2)).get(0);

		IGroum groum2 = Fixture.createConnectedGroumOfSize(2);
		((Groum) groum2).setSubgraphStrategy(new NaivSubgraphStrategy());
		ISubGroum subGroum2 = groum2.getSubgraphs(Fixture.createConnectedGroumOfSize(2)).get(0);

		IGroum groum3 = Fixture.createConnectedGroumOfSize(2);
		((Groum) groum3).setSubgraphStrategy(new NaivSubgraphStrategy());
		ISubGroum subGroum3 = groum3.getSubgraphs(Fixture.createConnectedGroumOfSize(2)).get(0);

		IGroum groum4 = Fixture.createConnectedGroumOfSize(1);
		((Groum) groum4).setSubgraphStrategy(new NaivSubgraphStrategy());
		ISubGroum subGroum4 = groum4.getSubgraphs(Fixture.createConnectedGroumOfSize(1)).get(0);

		treeset.add(subGroum1);
		treeset.add(subGroum2);
		treeset.add(subGroum3);
		treeset.add(subGroum4);
		assertTrue(treeset.size() == 4 && treeset.count(subGroum1) == 3);

	}

	@Test
	public void removesSubset() {
		TreeMultiset<IGroum> treeset = TreeMultiset.create(new GroumComparator());
		IGroum groum1 = Fixture.createConnectedGroumOfSize(1);
		IGroum groum2 = Fixture.createConnectedGroumOfSize(2);
		IGroum groum3 = Fixture.createConnectedGroumOfSize(2);
		IGroum groum4 = Fixture.createConnectedGroumOfSize(3);
		IGroum groum5 = Fixture.createConnectedGroumOfSize(3);
		IGroum groum6 = Fixture.createConnectedGroumOfSize(3);
		IGroum groum7 = Fixture.createConnectedGroumOfSize(3);

		treeset.addAll(Arrays.asList(groum1, groum2, groum3, groum4, groum5, groum6, groum7));
		treeset.removeAll(Arrays.asList(groum6));
		assertTrue(treeset.size() == 3);
	}

	@Test
	public void removesSeveralSubsets() {
		TreeMultiset<IGroum> treeset = TreeMultiset.create(new GroumComparator());
		IGroum groum1 = Fixture.createConnectedGroumOfSize(1);
		IGroum groum2 = Fixture.createConnectedGroumOfSize(2);
		IGroum groum3 = Fixture.createConnectedGroumOfSize(2);
		IGroum groum4 = Fixture.createConnectedGroumOfSize(3);
		IGroum groum5 = Fixture.createConnectedGroumOfSize(3);
		IGroum groum6 = Fixture.createConnectedGroumOfSize(3);
		IGroum groum7 = Fixture.createConnectedGroumOfSize(4);
		IGroum groum8 = Fixture.createConnectedGroumOfSize(4);
		IGroum groum9 = Fixture.createConnectedGroumOfSize(4);
		IGroum groum10 = Fixture.createConnectedGroumOfSize(4);

		treeset.addAll(Arrays.asList(groum1, groum2, groum3, groum4, groum5, groum6, groum7, groum8, groum9, groum10));
		treeset.removeAll(Arrays.asList(groum4, groum9));

		assertTrue(treeset.size() == 3 && treeset.elementSet().size() == 2);
	}

	@Test
	public void iteratesOverOccurences() {
		TreeMultiset<IGroum> treeset = TreeMultiset.create(new GroumComparator());
		IGroum groum1 = Fixture.createConnectedGroumOfSize(1);
		IGroum groum2 = Fixture.createConnectedGroumOfSize(2);
		IGroum groum3 = Fixture.createConnectedGroumOfSize(2);
		IGroum groum4 = Fixture.createConnectedGroumOfSize(3);
		IGroum groum5 = Fixture.createConnectedGroumOfSize(3);
		IGroum groum6 = Fixture.createConnectedGroumOfSize(3);
		IGroum groum7 = Fixture.createConnectedGroumOfSize(4);
		IGroum groum8 = Fixture.createConnectedGroumOfSize(4);
		IGroum groum9 = Fixture.createConnectedGroumOfSize(4);
		IGroum groum10 = Fixture.createConnectedGroumOfSize(4);
		treeset.addAll(Arrays.asList(groum1, groum2, groum3, groum4, groum5, groum6, groum7, groum8, groum9, groum10));

		int i = 0;
		for (IGroum groum : treeset) {
			i++;
		}

		assertTrue(i == 10);
	}

	@Test
	@Ignore
	public void copiesAllOccurences() {
		TreeMultiset<IGroum> treeset = TreeMultiset.create(new GroumComparator());
		IGroum groum1 = Fixture.createConnectedGroumOfSize(1);
		IGroum groum2 = Fixture.createConnectedGroumOfSize(2);
		IGroum groum3 = Fixture.createConnectedGroumOfSize(2);
		IGroum groum4 = Fixture.createConnectedGroumOfSize(3);
		IGroum groum5 = Fixture.createConnectedGroumOfSize(3);
		IGroum groum6 = Fixture.createConnectedGroumOfSize(3);
		IGroum groum7 = Fixture.createConnectedGroumOfSize(4);
		IGroum groum8 = Fixture.createConnectedGroumOfSize(4);
		IGroum groum9 = Fixture.createConnectedGroumOfSize(4);
		IGroum groum10 = Fixture.createConnectedGroumOfSize(4);
		treeset.addAll(Arrays.asList(groum1, groum2, groum3, groum4, groum5, groum6, groum7, groum8, groum9, groum10));

		TreeMultiset<IGroum> treesetCopy = TreeMultiset.create(new GroumComparator());
		treesetCopy.addAll(treeset);
		assertTrue(treesetCopy.size() == 10);
	}

	@Test
	@Ignore
	public void retrievesSubSet() {
		TreeMultiset<IGroum> treeset = TreeMultiset.create(new GroumComparator());
		IGroum groum1 = Fixture.createConnectedGroumOfSize(1);
		IGroum groum2 = Fixture.createConnectedGroumOfSize(2);
		IGroum groum3 = Fixture.createConnectedGroumOfSize(2);
		IGroum groum4 = Fixture.createConnectedGroumOfSize(3);
		IGroum groum5 = Fixture.createConnectedGroumOfSize(3);
		IGroum groum6 = Fixture.createConnectedGroumOfSize(3);
		IGroum groum7 = Fixture.createConnectedGroumOfSize(4);
		IGroum groum8 = Fixture.createConnectedGroumOfSize(4);
		IGroum groum9 = Fixture.createConnectedGroumOfSize(4);
		IGroum groum10 = Fixture.createConnectedGroumOfSize(4);
		treeset.addAll(Arrays.asList(groum1, groum2, groum3, groum4, groum5, groum6, groum7, groum8, groum9, groum10));

		SortedMultiset<IGroum> subMultiset = treeset.subMultiset(groum5, BoundType.CLOSED, groum5, BoundType.CLOSED);
		int size = subMultiset.size();
		assertTrue(size == 3);

	}

	@Test
	@Ignore
	public void preservesParents() {
		List<IGroum> groums = Fixture.getListOfXGroums(10);
		List<ISubGroum> subgroums = new LinkedList<>();
		for (IGroum groum : groums) {
			subgroums.addAll(Utils.breakdown(groum));
		}

		TreeMultimap<ISubGroum, ISubGroum> multiset = TreeMultimap.create(new SubGroumComparator(),
				new SubGroumIdentComparator());
		for (ISubGroum subgroum : subgroums) {
			multiset.put(subgroum, subgroum);
		}

		for (ISubGroum subgroum : multiset.keySet()) {
			System.out.println("\n" + subgroum + "--> ");
			for (ISubGroum contrasubgroum : multiset.get(subgroum)) {
				System.out.println(contrasubgroum.getParent());
			}
		}
	}

	@Test
	public void putsUnorderedGroumIntoSameBucket() {
		TreeMultimap<ISubGroum, ISubGroum> treemap = TreeMultimap.create(new SubGroumComparator(),
				new SubGroumIdentComparator());

		INode node1 = new ActionNode("1", "1");
		INode node2 = new ActionNode("2", "2");
		INode node3 = new ActionNode("3", "3");
		ISubGroum a = new SubGroum(null);
		a.addVertex(node1);
		a.addVertex(node2);
		a.addVertex(node3);
		a.addEdge(node1, node2);
		a.addEdge(node1, node3);

		INode node1b = new ActionNode("1", "1");
		INode node2b = new ActionNode("2", "2");
		INode node3b = new ActionNode("3", "3");
		ISubGroum b = new SubGroum(null);
		b.addVertex(node1b);
		b.addVertex(node3b);
		b.addEdge(node1b, node3b);
		b.addVertex(node2b);
		b.addEdge(node1b, node2b);

		// treemap.put(a, a);
		// treemap.put(b, b);

		treemap.putAll(a, Arrays.asList(a));
		treemap.putAll(b, Arrays.asList(b));
		// System.out.println(a);
		// System.out.println(b);
		// System.out.println(treemap);
		assertTrue(treemap.keySet().size() == 1);

	}

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

}
