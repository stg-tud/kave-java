package cc.kave.commons.model.ssts.impl.expressions.simple;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;

public class ReferenceExpressionTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		ReferenceExpression sut = new ReferenceExpression();

		assertThat(new UnknownReference(), equalTo(sut.getReference()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		ReferenceExpression sut = new ReferenceExpression();
		sut.setReference(someVarRef("a"));

		assertThat(someVarRef("a"), equalTo(sut.getReference()));
	}

	@Test
	public void testEqualityDefault() {
		ReferenceExpression a = new ReferenceExpression();
		ReferenceExpression b = new ReferenceExpression();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		ReferenceExpression a = new ReferenceExpression();
		ReferenceExpression b = new ReferenceExpression();
		a.setReference(someVarRef("a"));
		b.setReference(someVarRef("a"));

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentReference() {
		ReferenceExpression a = new ReferenceExpression();
		ReferenceExpression b = new ReferenceExpression();
		a.setReference(someVarRef("a"));
		b.setReference(someVarRef("b"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		ReferenceExpression sut = new ReferenceExpression();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
