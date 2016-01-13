package cc.kave.commons.model.groum.comparator;

import java.util.Comparator;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.groum.IGroum;

public class DFSGroumComparatorTest extends GroumComparatorConstractTest {

	@Override
	@Test
	@Ignore("fails this test nondeterministically (depends on the order of equal nodes in the underlying data structures)")
	public void equalNodesWithDifferentSuccessors() {
	}
	
	@Override
	@Test
	@Ignore("paths are equal -> indistinguishable")
	public void samePathsDifferentStructure() {
	}
	
	@Override
	@Test
	@Ignore("paths are equal -> indistinguishable")
	public void successorsOnDifferentEqualNodes2() {
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Comparator<IGroum> createComparator() {
		return new DFSGroumComparator();
	}
}
