package cc.kave.commons.model.groum.comparator;

import java.util.Comparator;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.groum.IGroum;

public class ExasGroumComparatorTest extends GroumComparatorConstractTest {

	@Override
	protected Comparator<IGroum> createComparator() {
		return new ExasGroumComparator();
	}

	@Override
	@Test
	@Ignore("is indistinguishable by this strategy (same paths and same pq-nodes)")
	public void successorsOnDifferentEqualNodes2() {
	}

}
