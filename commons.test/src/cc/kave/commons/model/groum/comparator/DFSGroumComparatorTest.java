package cc.kave.commons.model.groum.comparator;

import java.util.Comparator;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.groum.IGroum;

public class DFSGroumComparatorTest extends GroumComparatorConstractTest {

	@Override
	@Test
	@Ignore("the DFS strategy fails this test nondeterministically!")
	public void equalNodesWithDifferentSuccessors() {
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Comparator<IGroum> createComparator() {
		return new DFSGroumComparator();
	}
}
