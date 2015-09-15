package cc.kave.commons.model.groum;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

import cc.kave.commons.model.groum.comparator.DFSGroumComparator;

import static org.junit.Assert.assertEquals;

public class PatternAssert {
	
	static void assertContainsPatterns(List<SubGroum> actuals, IGroum... expecteds) {
		TreeSet<IGroum> actual = new TreeSet<IGroum>(new DFSGroumComparator());
		actual.addAll(actuals);
		TreeSet<IGroum> expected = new TreeSet<IGroum>(new DFSGroumComparator());
		expected.addAll(Arrays.asList(expecteds));
		assertEquals(expected, actual);
	}

	static List<SubGroum> filterBySize(List<SubGroum> actuals, int size) {
		return actuals.stream().filter(g -> (g.getAllNodes().size() == size)).collect(Collectors.toList());
	}

}
