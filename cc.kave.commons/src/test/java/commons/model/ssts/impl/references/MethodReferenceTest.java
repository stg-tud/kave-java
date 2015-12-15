package commons.model.ssts.impl.references;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import commons.model.ssts.impl.SSTBaseTest;
import commons.model.ssts.impl.SSTTestHelper;

public class MethodReferenceTest extends SSTBaseTest {

	private static MethodName someMethod() {
		return CsMethodName.newMethodName("[T1,P1] [T2,P2].E()");
	}

	@Test
	public void testDefaultValues() {
		MethodReference sut = new MethodReference();

		assertThat(new VariableReference(), equalTo(sut.getReference()));
		assertThat(CsMethodName.UNKNOWN_NAME, equalTo(sut.getMethodName()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		MethodReference sut = new MethodReference();
		sut.setMethodName(someMethod());

		assertThat(someMethod(), equalTo(sut.getMethodName()));
	}

	@Test
	public void testEqualityDefault() {
		MethodReference a = new MethodReference();
		MethodReference b = new MethodReference();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		MethodReference a = new MethodReference();
		MethodReference b = new MethodReference();
		a.setMethodName(someMethod());
		b.setMethodName(someMethod());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentName() {
		MethodReference a = new MethodReference();
		MethodReference b = new MethodReference();
		a.setMethodName(someMethod());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentReference() {
		MethodReference a = new MethodReference();
		MethodReference b = new MethodReference();
		a.setReference(someVarRef());
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		MethodReference sut = new MethodReference();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

}
