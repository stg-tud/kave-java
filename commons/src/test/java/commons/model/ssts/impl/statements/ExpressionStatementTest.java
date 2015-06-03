package commons.model.ssts.impl.statements;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;

import commons.model.ssts.impl.SSTTestHelper;

public class ExpressionStatementTest {

	@Test
	public void testDefaultValues() {
		ExpressionStatement sut = new ExpressionStatement();

		assertThat(new UnknownExpression(), equalTo(sut.getExpression()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		ExpressionStatement sut = new ExpressionStatement();
		sut.setExpression(new NullExpression());

		assertThat(new NullExpression(), equalTo(sut.getExpression()));
	}

	@Test
	public void testEqualityDefault() {
		ExpressionStatement a = new ExpressionStatement();
		ExpressionStatement b = new ExpressionStatement();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		ExpressionStatement a = new ExpressionStatement();
		ExpressionStatement b = new ExpressionStatement();
		a.setExpression(new NullExpression());
		b.setExpression(new NullExpression());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentExpression() {
		ExpressionStatement a = new ExpressionStatement();
		ExpressionStatement b = new ExpressionStatement();
		a.setExpression(new NullExpression());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		ExpressionStatement sut = new ExpressionStatement();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
