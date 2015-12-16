package cc.kave.commons.model.ssts.impl.expressions.assignable;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;

import com.google.common.collect.Lists;

public class InvocationExpressionTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		InvocationExpression sut = new InvocationExpression();

		assertThat(new VariableReference(), equalTo(sut.getReference()));
		assertThat(MethodName.UNKNOWN_NAME, equalTo(sut.getMethodName()));
		assertThat(new ArrayList<ISimpleExpression>(), equalTo(sut.getParameters()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		InvocationExpression sut = new InvocationExpression();
		sut.setMethodName(getMethod("b"));
		sut.setParameters(Lists.newArrayList(new NullExpression()));
		sut.setReference(someVarRef("a"));

		assertThat(someVarRef("a"), equalTo(sut.getReference()));
		assertThat(getMethod("b"), equalTo(sut.getMethodName()));
		assertThat(Lists.newArrayList(new NullExpression()), equalTo(sut.getParameters()));
	}

	@Test
	public void testEqualityDefault() {
		InvocationExpression a = new InvocationExpression();
		InvocationExpression b = new InvocationExpression();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		assertThat(getMethod("a"), equalTo(getMethod("a")));
		String[] refs = { "a", "b", "c" };
		InvocationExpression a = (InvocationExpression) SSTUtil.invocationExpression("o", getMethod("A"),
				refExprs(refs).iterator());
		InvocationExpression b = (InvocationExpression) SSTUtil.invocationExpression("o", getMethod("A"),
				refExprs(refs).iterator());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentReference() {
		InvocationExpression a = new InvocationExpression();
		InvocationExpression b = new InvocationExpression();
		a.setReference(someVarRef("a"));
		b.setReference(someVarRef("b"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentMethod() {
		InvocationExpression a = new InvocationExpression();
		InvocationExpression b = new InvocationExpression();
		a.setMethodName(getMethod("A"));
		b.setMethodName(getMethod("B"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentParameters() {
		InvocationExpression a = new InvocationExpression();
		InvocationExpression b = new InvocationExpression();
		String[] refs = { "a" };
		String[] refs2 = { "b" };
		a.setParameters(refExprs(refs));
		b.setParameters(refExprs(refs2));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		InvocationExpression sut = new InvocationExpression();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

}
