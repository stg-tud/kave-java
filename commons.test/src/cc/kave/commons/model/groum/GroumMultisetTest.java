package cc.kave.commons.model.groum;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.SubGroum;
import cc.kave.commons.model.groum.comparator.GroumComparator;
import cc.kave.commons.model.groum.comparator.SubGroumComparator;
import cc.kave.commons.model.pattexplore.Utils;

import com.google.common.collect.BoundType;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

public class GroumMultisetTest {

	@Test
	public void comparatorWorksForGroums() {
		TreeMultiset<Groum> treeset = TreeMultiset.create(new GroumComparator());
		Groum groum1 = Fixture.createConnectedGroumOfSize(2);
		Groum groum2 = Fixture.createConnectedGroumOfSize(2);
		Groum groum3 = Fixture.createConnectedGroumOfSize(2);
		Groum groum4 = Fixture.createConnectedGroumOfSize(1);
		treeset.add(groum1);
		treeset.add(groum2);
		treeset.add(groum3);
		treeset.add(groum4);
		assertTrue(treeset.size() == 4 && treeset.count(groum1) == 3);

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

		TreeMultiset<SubGroum> multiset = TreeMultiset.create(new SubGroumComparator());
		multiset.addAll(subgroums);

		for (SubGroum subgroum : multiset) {
			System.out.println(subgroum + "--> " + subgroum.getParent());
		}
	}

}
