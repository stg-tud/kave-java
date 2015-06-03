package commons.model.ssts.impl.statements;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.LabelledStatement;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;

import commons.model.ssts.impl.SSTTestHelper;

public class LabelledStatementTest {

	@Test
	public void testDefaultValues() {
		LabelledStatement sut = new LabelledStatement();

		assertThat("", equalTo(sut.getLabel()));
		assertThat(new UnknownStatement(), equalTo(sut.getStatement()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		LabelledStatement sut = new LabelledStatement();
		sut.setLabel("a");
		sut.setStatement(new BreakStatement());

		assertThat(new BreakStatement(), equalTo(sut.getStatement()));
		assertThat("a", equalTo(sut.getLabel()));
	}

	@Test
	public void testEqualityDefault() {
		LabelledStatement a = new LabelledStatement();
		LabelledStatement b = new LabelledStatement();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		LabelledStatement a = new LabelledStatement();
		LabelledStatement b = new LabelledStatement();
		a.setLabel("a");
		b.setLabel("a");
		a.setStatement(new BreakStatement());
		b.setStatement(new BreakStatement());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentLabel() {
		LabelledStatement a = new LabelledStatement();
		LabelledStatement b = new LabelledStatement();
		a.setLabel("a");
		b.setLabel("b");

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentStatement() {
		LabelledStatement a = new LabelledStatement();
		LabelledStatement b = new LabelledStatement();
		a.setStatement(new BreakStatement());
		b.setStatement(new ContinueStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		LabelledStatement sut = new LabelledStatement();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
