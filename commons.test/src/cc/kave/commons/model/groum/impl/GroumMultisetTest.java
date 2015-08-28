package cc.kave.commons.model.groum.impl;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.ISubGroum;
import cc.kave.commons.model.groum.comparator.GroumComparator;
import cc.kave.commons.model.groum.comparator.SubGroumComparator;
import cc.kave.commons.model.pattexplore.NaivSubgraphStrategy;
import cc.kave.commons.model.pattexplore.Utils;

import com.google.common.collect.BoundType;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

public class GroumMultisetTest {

	@Test
	public void comparatorWorksForGroums() {
		TreeMultiset<IGroum> treeset = TreeMultiset.create(new GroumComparator());
		IGroum groum1 = Fixture.createConnectedGroumOfSize(2);
		IGroum groum2 = Fixture.createConnectedGroumOfSize(2);
		IGroum groum3 = Fixture.createConnectedGroumOfSize(2);
		IGroum groum4 = Fixture.createConnectedGroumOfSize(1);
		treeset.add(groum1);
		treeset.add(groum2);
		treeset.add(groum3);
		treeset.add(groum4);
		assertTrue(treeset.size() == 4 && treeset.count(groum1) == 3);

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

		TreeMultiset<ISubGroum> multiset = TreeMultiset.create(new SubGroumComparator());
		multiset.addAll(subgroums);

		for (ISubGroum subgroum : multiset) {
			System.out.println(subgroum + "--> " + subgroum.getParent());
		}
	}

}
