package cc.kave.commons.model.ssts.impl.blocks;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;

import com.google.common.collect.Lists;

public class WhileLoopTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		WhileLoop sut = new WhileLoop();

		assertThat(new UnknownExpression(), equalTo(sut.getCondition()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		WhileLoop sut = new WhileLoop();
		sut.getBody().add(new ReturnStatement());
		sut.setCondition(new ConstantValueExpression());

		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getBody()));
		assertThat(new ConstantValueExpression(), equalTo(sut.getCondition()));
	}

	@Test
	public void testChildrenIdentity() {
		WhileLoop sut = new WhileLoop();
		sut.getBody().add(new ReturnStatement());
		sut.setCondition(new ConstantValueExpression());

		assertChildren(sut, sut.getCondition(), sut.getBody().get(0));
	}

	@Test
	public void testEqualityDefault() {
		WhileLoop a = new WhileLoop();
		WhileLoop b = new WhileLoop();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		WhileLoop a = new WhileLoop();
		WhileLoop b = new WhileLoop();
		a.setCondition(new ConstantValueExpression());
		a.getBody().add(new ReturnStatement());
		b.setCondition(new ConstantValueExpression());
		b.getBody().add(new ReturnStatement());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentCondition() {
		WhileLoop a = new WhileLoop();
		WhileLoop b = new WhileLoop();
		a.setCondition(new ConstantValueExpression());
		b.setCondition(new ReferenceExpression());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentBody() {
		WhileLoop a = new WhileLoop();
		WhileLoop b = new WhileLoop();
		a.getBody().add(new GotoStatement());
		b.getBody().add(new ContinueStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		WhileLoop sut = new WhileLoop();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testVisitorWithReturnIsImplemented() {
		// TODO: Visitor Test
	}
}
