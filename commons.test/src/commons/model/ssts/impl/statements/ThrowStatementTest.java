package commons.model.ssts.impl.statements;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;
import cc.kave.commons.model.ssts.references.IVariableReference;
import commons.model.ssts.impl.SSTTestHelper;

public class ThrowStatementTest {

	public IVariableReference someVarRef(String s) {
		VariableReference ref = new VariableReference();
		ref.setIdentifier(s);
		return ref;
	}

	@Test
	public void testDefaultValues() {
		ThrowStatement sut = new ThrowStatement();

		assertThat(new VariableReference(), equalTo(sut.getReference()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		ThrowStatement sut = new ThrowStatement();
		sut.setReference(someVarRef("e"));
		assertThat(someVarRef("e"), equalTo(sut.getReference()));
	}

	@Test
	public void testEqualityDefault() {
		ThrowStatement a = new ThrowStatement();
		ThrowStatement b = new ThrowStatement();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		ThrowStatement a = new ThrowStatement();
		ThrowStatement b = new ThrowStatement();
		a.setReference(someVarRef("e"));
		b.setReference(someVarRef("e"));

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentException() {
		ThrowStatement a = new ThrowStatement();
		ThrowStatement b = new ThrowStatement();
		a.setReference(someVarRef("e1"));
		b.setReference(someVarRef("e2"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		ThrowStatement sut = new ThrowStatement();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
