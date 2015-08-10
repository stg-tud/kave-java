package cc.kave.commons.model.groum.impl;

import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.groum.ISubGroum;
import cc.kave.commons.model.pattexplore.PattExplorer;

public class PattExplorerTest {

	@Test
	public void firstTry() {
		PattExplorer explorer = new PattExplorer(7);

		List<ISubGroum> patterns = explorer.explorePatterns(Fixture_Groumtest.getTestList());
		System.out.println(patterns);
		System.out.println(patterns.size());
		// assertEquals(patterns.size(), 8);

	}

}
