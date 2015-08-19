package cc.kave.commons.model.groum.impl;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.ISubGroum;
import cc.kave.commons.model.groum.impl.comparator.GroumComparator;
import cc.kave.commons.model.groum.impl.comparator.GroumIdentComparator;
import cc.kave.commons.model.groum.impl.comparator.SubGroumComparator;
import cc.kave.commons.model.groum.impl.comparator.SubGroumIdentComparator;
import cc.kave.commons.model.pattexplore.NaivSubgraphStrategy;
import cc.kave.commons.model.pattexplore.Utils;

import com.google.common.collect.BoundType;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultimap;
import com.google.common.collect.TreeMultiset;

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
	@Ignore
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
	@Ignore
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
	@Ignore
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
	@Ignore
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

}
