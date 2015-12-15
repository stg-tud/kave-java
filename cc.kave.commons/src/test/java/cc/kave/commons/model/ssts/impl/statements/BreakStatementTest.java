package cc.kave.commons.model.ssts.impl.statements;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;

public class BreakStatementTest {

	@Test
	public void testEqualityDefault() {
		BreakStatement a = new BreakStatement();
		BreakStatement b = new BreakStatement();
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
		assertThat(0, not(equalTo(a.hashCode())));
		assertThat(1, not(equalTo(a.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		BreakStatement sut = new BreakStatement();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

}
