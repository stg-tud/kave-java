package cc.kave.commons.model.groum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.ISubGroum;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PatternAssert {

	static void assertContainsPatterns(List<ISubGroum> actuals, IGroum... expected) {
		List<IGroum> expecteds = Arrays.asList(expected);
		assertEquals(String.format("%s, %s", actuals, expecteds), expecteds.size(), actuals.size());
		assertTrue(actuals.containsAll(expecteds));
	}

	static List<ISubGroum> patternsOfSize(List<ISubGroum> actuals, int size) {
		return actuals.stream().filter(g -> (g.getAllNodes().size() == size)).collect(Collectors.toList());
	}

}
