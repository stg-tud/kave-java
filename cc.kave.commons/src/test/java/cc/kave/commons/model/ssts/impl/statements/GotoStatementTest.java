package cc.kave.commons.model.ssts.impl.statements;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;

public class GotoStatementTest {

	@Test
	public void testDefaultValues() {
		GotoStatement sut = new GotoStatement();

		assertThat("", equalTo(sut.getLabel()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingDefault() {
		GotoStatement sut = new GotoStatement();
		sut.setLabel("a");

		assertThat("a", equalTo(sut.getLabel()));
	}

	@Test
	public void testEqualityDefault() {
		GotoStatement a = new GotoStatement();
		GotoStatement b = new GotoStatement();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		GotoStatement a = new GotoStatement();
		GotoStatement b = new GotoStatement();
		a.setLabel("a");
		b.setLabel("a");

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentLabel() {
		GotoStatement a = new GotoStatement();
		GotoStatement b = new GotoStatement();
		a.setLabel("a");
		b.setLabel("b");

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		GotoStatement sut = new GotoStatement();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
