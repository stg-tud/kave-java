package cc.kave.commons.model.ssts.impl.expressions.assignable;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;

public class CompletionExpressionTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		CompletionExpression sut = new CompletionExpression();

		assertThat("", equalTo(sut.getToken()));
		assertThat(null, equalTo(sut.getObjectReference()));
		assertThat(null, equalTo(sut.getTypeReference()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		CompletionExpression sut = new CompletionExpression();
		sut.setObjectReference(someVarRef("i"));
		sut.setTypeReference(CsTypeName.UNKNOWN_NAME);
		sut.setToken("t");

		assertThat("t", equalTo(sut.getToken()));
		assertThat(someVarRef("i"), equalTo(sut.getObjectReference()));
		assertThat(CsTypeName.UNKNOWN_NAME, equalTo(sut.getTypeReference()));
	}

	@Test
	public void testEqualityDefault() {
		CompletionExpression a = new CompletionExpression();
		CompletionExpression b = new CompletionExpression();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		CompletionExpression a = new CompletionExpression();
		CompletionExpression b = new CompletionExpression();
		a.setObjectReference(someVarRef("i"));
		a.setToken("t");
		a.setTypeReference(CsTypeName.UNKNOWN_NAME);
		b.setObjectReference(someVarRef("i"));
		b.setToken("t");
		b.setTypeReference(CsTypeName.UNKNOWN_NAME);

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentObjectReference() {
		CompletionExpression a = new CompletionExpression();
		CompletionExpression b = new CompletionExpression();
		a.setObjectReference(someVarRef("i"));
		b.setObjectReference(someVarRef("j"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentToken() {
		CompletionExpression a = new CompletionExpression();
		CompletionExpression b = new CompletionExpression();
		a.setToken("i");
		b.setToken("j");

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentTypeReference() {
		CompletionExpression a = new CompletionExpression();
		CompletionExpression b = new CompletionExpression();
		a.setTypeReference(CsTypeName.UNKNOWN_NAME);
		b.setTypeReference(CsTypeName.newTypeName("System.Int32, mscore, 4.0.0.0"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		CompletionExpression sut = new CompletionExpression();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
