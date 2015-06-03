package commons.model.ssts.impl.statements;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;

import commons.model.ssts.impl.SSTTestHelper;

public class ThrowStatementTest {

	@Test
	public void testDefaultValues() {
		ThrowStatement sut = new ThrowStatement();

		assertThat(CsTypeName.UNKNOWN_NAME, equalTo(sut.getException()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		ThrowStatement sut = new ThrowStatement();
		sut.setException(CsTypeName.UNKNOWN_NAME);
		assertThat(CsTypeName.UNKNOWN_NAME, equalTo(sut.getException()));
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
		a.setException(CsTypeName.UNKNOWN_NAME);
		b.setException(CsTypeName.UNKNOWN_NAME);

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentException() {
		ThrowStatement a = new ThrowStatement();
		ThrowStatement b = new ThrowStatement();
		a.setException(CsTypeName.UNKNOWN_NAME);
		b.setException(CsTypeName.newTypeName("System.Int32, mscore, 4.0.0.0"));

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
