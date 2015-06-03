package commons.model.ssts.impl.statements;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;

import commons.model.ssts.impl.SSTTestHelper;

public class ContinueStatementTest {

	@Test
	public void testEqualityDefault() {
		ContinueStatement a = new ContinueStatement();
		ContinueStatement b = new ContinueStatement();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
		assertThat(0, not(equalTo(a.hashCode())));
		assertThat(1, not(equalTo(a.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		ContinueStatement sut = new ContinueStatement();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
