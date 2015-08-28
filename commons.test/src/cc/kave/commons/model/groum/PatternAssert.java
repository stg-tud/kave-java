package cc.kave.commons.model.groum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PatternAssert {

	static void assertContainsPatterns(List<SubGroum> actuals, Groum... expected) {
		List<Groum> expecteds = Arrays.asList(expected);
		assertEquals(String.format("%s, %s", actuals, expecteds), expecteds.size(), actuals.size());
		assertTrue(actuals.containsAll(expecteds));
	}

	static List<SubGroum> filterBySize(List<SubGroum> actuals, int size) {
		return actuals.stream().filter(g -> (g.getAllNodes().size() == size)).collect(Collectors.toList());
	}

}
