package commons.model.ssts.impl.references;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;

import commons.model.ssts.impl.SSTTestHelper;

public class FieldReferenceTest {

	private static FieldName someField() {
		return CsFieldName.newFieldName("[T1,P1] [T2,P2].E");
	}

	@Test
	public void testDefaultValues() {
		FieldReference sut = new FieldReference();

		assertThat(new VariableReference(), equalTo(sut.getReference()));
		assertThat(CsFieldName.UNKNOWN_NAME, equalTo(sut.getFieldName()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		FieldReference sut = new FieldReference();
		sut.setFieldName(someField());

		assertThat(someField(), equalTo(sut.getFieldName()));
	}

	@Test
	public void testEqualityDefault() {
		FieldReference a = new FieldReference();
		FieldReference b = new FieldReference();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		FieldReference a = new FieldReference();
		FieldReference b = new FieldReference();
		a.setFieldName(someField());
		b.setFieldName(someField());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentName() {
		FieldReference a = new FieldReference();
		FieldReference b = new FieldReference();
		a.setFieldName(someField());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		FieldReference sut = new FieldReference();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
