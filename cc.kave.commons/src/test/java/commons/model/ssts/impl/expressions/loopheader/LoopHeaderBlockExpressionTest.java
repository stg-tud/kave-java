package commons.model.ssts.impl.expressions.loopheader;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Test;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;

import commons.model.ssts.impl.SSTBaseTest;
import commons.model.ssts.impl.SSTTestHelper;

public class LoopHeaderBlockExpressionTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		LoopHeaderBlockExpression sut = new LoopHeaderBlockExpression();

		assertThat(new ArrayList<IStatement>(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		LoopHeaderBlockExpression sut = new LoopHeaderBlockExpression();
		sut.getBody().add(new ReturnStatement());

		ArrayList<IStatement> expected = new ArrayList<IStatement>();
		expected.add(new ReturnStatement());

		assertThat(expected, equalTo(sut.getBody()));
	}

	@Test
	public void testEqualityDefault() {
		LoopHeaderBlockExpression a = new LoopHeaderBlockExpression();
		LoopHeaderBlockExpression b = new LoopHeaderBlockExpression();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		LoopHeaderBlockExpression a = new LoopHeaderBlockExpression();
		LoopHeaderBlockExpression b = new LoopHeaderBlockExpression();
		a.getBody().add(new ReturnStatement());
		b.getBody().add(new ReturnStatement());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentBody() {
		LoopHeaderBlockExpression a = new LoopHeaderBlockExpression();
		LoopHeaderBlockExpression b = new LoopHeaderBlockExpression();
		a.getBody().add(new ReturnStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		LoopHeaderBlockExpression sut = new LoopHeaderBlockExpression();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

}
