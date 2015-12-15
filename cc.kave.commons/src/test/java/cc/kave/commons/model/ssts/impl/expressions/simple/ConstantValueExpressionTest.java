package cc.kave.commons.model.ssts.impl.expressions.simple;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;

public class ConstantValueExpressionTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		ConstantValueExpression sut = new ConstantValueExpression();

		assertThat(null, equalTo(sut.getValue()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		ConstantValueExpression sut = new ConstantValueExpression();
		sut.setValue("a");
		assertThat("a", equalTo(sut.getValue()));
	}

	@Test
	public void testEqualityDefault() {
		ConstantValueExpression a = new ConstantValueExpression();
		ConstantValueExpression b = new ConstantValueExpression();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		ConstantValueExpression a = new ConstantValueExpression();
		ConstantValueExpression b = new ConstantValueExpression();
		a.setValue("a");
		b.setValue("a");

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentValue() {
		ConstantValueExpression a = new ConstantValueExpression();
		ConstantValueExpression b = new ConstantValueExpression();
		a.setValue("a");

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		ConstantValueExpression sut = new ConstantValueExpression();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

}
