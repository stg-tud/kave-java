package commons.model.ssts.impl.expressions.assignable;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;

import commons.model.ssts.impl.SSTBaseTest;
import commons.model.ssts.impl.SSTTestHelper;

public class IfElseExpressionTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		IfElseExpression sut = new IfElseExpression();

		assertThat(new UnknownExpression(), equalTo(sut.getCondition()));
		assertThat(new UnknownExpression(), equalTo(sut.getElseExpression()));
		assertThat(new UnknownExpression(), equalTo(sut.getThenExpression()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		IfElseExpression sut = new IfElseExpression();
		sut.setCondition(new ConstantValueExpression());
		sut.setElseExpression(new ReferenceExpression());
		sut.setThenExpression(new NullExpression());

		assertThat(new ConstantValueExpression(), equalTo(sut.getCondition()));
		assertThat(new NullExpression(), equalTo(sut.getThenExpression()));
		assertThat(new ReferenceExpression(), equalTo(sut.getElseExpression()));
	}

	@Test
	public void testEqualityDefault() {
		IfElseExpression a = new IfElseExpression();
		IfElseExpression b = new IfElseExpression();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		IfElseExpression a = new IfElseExpression();
		IfElseExpression b = new IfElseExpression();
		a.setCondition(new ConstantValueExpression());
		a.setElseExpression(new ReferenceExpression());
		a.setThenExpression(new ConstantValueExpression());
		b.setCondition(new ConstantValueExpression());
		b.setElseExpression(new ReferenceExpression());
		b.setThenExpression(new ConstantValueExpression());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentCondition() {
		IfElseExpression a = new IfElseExpression();
		IfElseExpression b = new IfElseExpression();
		a.setCondition(new ConstantValueExpression());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentElseExpression() {
		IfElseExpression a = new IfElseExpression();
		IfElseExpression b = new IfElseExpression();
		a.setElseExpression(new ConstantValueExpression());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentThenExpression() {
		IfElseExpression a = new IfElseExpression();
		IfElseExpression b = new IfElseExpression();
		a.setThenExpression(new ConstantValueExpression());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		IfElseExpression sut = new IfElseExpression();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
