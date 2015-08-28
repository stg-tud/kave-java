package cc.kave.commons.model.groum;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.INode;
import cc.kave.commons.model.groum.SubGroum;
import cc.kave.commons.model.groum.comparator.GroumComparator;
import cc.kave.commons.model.groum.comparator.GroumIdentComparator;
import cc.kave.commons.model.groum.comparator.SubGroumComparator;
import cc.kave.commons.model.groum.comparator.SubGroumIdentComparator;
import cc.kave.commons.model.groum.nodes.ActionNode;
import cc.kave.commons.model.pattexplore.Utils;

import com.google.common.collect.BoundType;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultimap;
import com.google.common.collect.TreeMultiset;

public class GroumMultimapTest {

	@Test
	public void comparatorWorksForGroums() {

		TreeMultimap<Groum, Groum> treemap = TreeMultimap.create(new GroumComparator(), new GroumIdentComparator());

		Groum groum1 = Fixture.createConnectedGroumOfSize(1);
		Groum groum2 = Fixture.createConnectedGroumOfSize(2);
		Groum groum3 = Fixture.createConnectedGroumOfSize(3);
		Groum groum4 = Fixture.createConnectedGroumOfSize(1);
		Groum groum5 = Fixture.createConnectedGroumOfSize(2);
		Groum groum6 = Fixture.createConnectedGroumOfSize(3);
		treemap.put(groum1, groum1);
		treemap.put(groum2, groum2);
		treemap.put(groum3, groum3);
		treemap.put(groum4, groum4);
		treemap.put(groum5, groum5);
		treemap.put(groum6, groum6);
		assertTrue(treemap.size() == 6 && treemap.get(groum2).size() == 2);

	}

	@Test
	public void removesSubset() {
		TreeMultiset<Groum> treeset = TreeMultiset.create(new GroumComparator());
		Groum groum1 = Fixture.createConnectedGroumOfSize(1);
		Groum groum2 = Fixture.createConnectedGroumOfSize(2);
		Groum groum3 = Fixture.createConnectedGroumOfSize(2);
		Groum groum4 = Fixture.createConnectedGroumOfSize(3);
		Groum groum5 = Fixture.createConnectedGroumOfSize(3);
		Groum groum6 = Fixture.createConnectedGroumOfSize(3);
		Groum groum7 = Fixture.createConnectedGroumOfSize(3);

		treeset.addAll(Arrays.asList(groum1, groum2, groum3, groum4, groum5, groum6, groum7));
		treeset.removeAll(Arrays.asList(groum6));
		assertTrue(treeset.size() == 3);
	}

	@Test
	public void removesSeveralSubsets() {
		TreeMultiset<Groum> treeset = TreeMultiset.create(new GroumComparator());
		Groum groum1 = Fixture.createConnectedGroumOfSize(1);
		Groum groum2 = Fixture.createConnectedGroumOfSize(2);
		Groum groum3 = Fixture.createConnectedGroumOfSize(2);
		Groum groum4 = Fixture.createConnectedGroumOfSize(3);
		Groum groum5 = Fixture.createConnectedGroumOfSize(3);
		Groum groum6 = Fixture.createConnectedGroumOfSize(3);
		Groum groum7 = Fixture.createConnectedGroumOfSize(4);
		Groum groum8 = Fixture.createConnectedGroumOfSize(4);
		Groum groum9 = Fixture.createConnectedGroumOfSize(4);
		Groum groum10 = Fixture.createConnectedGroumOfSize(4);

		treeset.addAll(Arrays.asList(groum1, groum2, groum3, groum4, groum5, groum6, groum7, groum8, groum9, groum10));
		treeset.removeAll(Arrays.asList(groum4, groum9));

		assertTrue(treeset.size() == 3 && treeset.elementSet().size() == 2);
	}

	@Test
	public void iteratesOverOccurences() {
		TreeMultiset<Groum> treeset = TreeMultiset.create(new GroumComparator());
		Groum groum1 = Fixture.createConnectedGroumOfSize(1);
		Groum groum2 = Fixture.createConnectedGroumOfSize(2);
		Groum groum3 = Fixture.createConnectedGroumOfSize(2);
		Groum groum4 = Fixture.createConnectedGroumOfSize(3);
		Groum groum5 = Fixture.createConnectedGroumOfSize(3);
		Groum groum6 = Fixture.createConnectedGroumOfSize(3);
		Groum groum7 = Fixture.createConnectedGroumOfSize(4);
		Groum groum8 = Fixture.createConnectedGroumOfSize(4);
		Groum groum9 = Fixture.createConnectedGroumOfSize(4);
		Groum groum10 = Fixture.createConnectedGroumOfSize(4);
		treeset.addAll(Arrays.asList(groum1, groum2, groum3, groum4, groum5, groum6, groum7, groum8, groum9, groum10));

		int i = 0;
		for (Groum groum : treeset) {
			i++;
		}

		assertTrue(i == 10);
	}

	@Test
	@Ignore
	public void copiesAllOccurences() {
		TreeMultiset<Groum> treeset = TreeMultiset.create(new GroumComparator());
		Groum groum1 = Fixture.createConnectedGroumOfSize(1);
		Groum groum2 = Fixture.createConnectedGroumOfSize(2);
		Groum groum3 = Fixture.createConnectedGroumOfSize(2);
		Groum groum4 = Fixture.createConnectedGroumOfSize(3);
		Groum groum5 = Fixture.createConnectedGroumOfSize(3);
		Groum groum6 = Fixture.createConnectedGroumOfSize(3);
		Groum groum7 = Fixture.createConnectedGroumOfSize(4);
		Groum groum8 = Fixture.createConnectedGroumOfSize(4);
		Groum groum9 = Fixture.createConnectedGroumOfSize(4);
		Groum groum10 = Fixture.createConnectedGroumOfSize(4);
		treeset.addAll(Arrays.asList(groum1, groum2, groum3, groum4, groum5, groum6, groum7, groum8, groum9, groum10));

		TreeMultiset<Groum> treesetCopy = TreeMultiset.create(new GroumComparator());
		treesetCopy.addAll(treeset);
		assertTrue(treesetCopy.size() == 10);
	}

	@Test
	@Ignore
	public void retrievesSubSet() {
		TreeMultiset<Groum> treeset = TreeMultiset.create(new GroumComparator());
		Groum groum1 = Fixture.createConnectedGroumOfSize(1);
		Groum groum2 = Fixture.createConnectedGroumOfSize(2);
		Groum groum3 = Fixture.createConnectedGroumOfSize(2);
		Groum groum4 = Fixture.createConnectedGroumOfSize(3);
		Groum groum5 = Fixture.createConnectedGroumOfSize(3);
		Groum groum6 = Fixture.createConnectedGroumOfSize(3);
		Groum groum7 = Fixture.createConnectedGroumOfSize(4);
		Groum groum8 = Fixture.createConnectedGroumOfSize(4);
		Groum groum9 = Fixture.createConnectedGroumOfSize(4);
		Groum groum10 = Fixture.createConnectedGroumOfSize(4);
		treeset.addAll(Arrays.asList(groum1, groum2, groum3, groum4, groum5, groum6, groum7, groum8, groum9, groum10));

		SortedMultiset<Groum> subMultiset = treeset.subMultiset(groum5, BoundType.CLOSED, groum5, BoundType.CLOSED);
		int size = subMultiset.size();
		assertTrue(size == 3);

	}

	@Test
	@Ignore
	public void preservesParents() {
		List<Groum> groums = Fixture.getListOfXGroums(10);
		List<SubGroum> subgroums = new LinkedList<>();
		for (Groum groum : groums) {
			subgroums.addAll(Utils.breakdown(groum));
		}

		TreeMultimap<SubGroum, SubGroum> multiset = TreeMultimap.create(new SubGroumComparator(),
				new SubGroumIdentComparator());
		for (SubGroum subgroum : subgroums) {
			multiset.put(subgroum, subgroum);
		}

		for (SubGroum subgroum : multiset.keySet()) {
			System.out.println("\n" + subgroum + "--> ");
			for (SubGroum contrasubgroum : multiset.get(subgroum)) {
				System.out.println(contrasubgroum.getParent());
			}
		}
	}

	@Test
	public void putsUnorderedGroumIntoSameBucket() {
		TreeMultimap<SubGroum, SubGroum> treemap = TreeMultimap.create(new SubGroumComparator(),
				new SubGroumIdentComparator());

		INode node1 = new ActionNode("1", "1");
		INode node2 = new ActionNode("2", "2");
		INode node3 = new ActionNode("3", "3");
		SubGroum a = new SubGroum(null);
		a.addVertex(node1);
		a.addVertex(node2);
		a.addVertex(node3);
		a.addEdge(node1, node2);
		a.addEdge(node1, node3);

		INode node1b = new ActionNode("1", "1");
		INode node2b = new ActionNode("2", "2");
		INode node3b = new ActionNode("3", "3");
		SubGroum b = new SubGroum(null);
		b.addVertex(node1b);
		b.addVertex(node3b);
		b.addEdge(node1b, node3b);
		b.addVertex(node2b);
		b.addEdge(node1b, node2b);

		treemap.putAll(a, Arrays.asList(a));
		treemap.putAll(b, Arrays.asList(b));
		assertTrue(treemap.keySet().size() == 1);

	}
}
