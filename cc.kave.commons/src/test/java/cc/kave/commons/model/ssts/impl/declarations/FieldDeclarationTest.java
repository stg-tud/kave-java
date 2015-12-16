package cc.kave.commons.model.ssts.impl.declarations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;

public class FieldDeclarationTest {

	@Test
	public void testDefaultValues() {
		FieldDeclaration sut = new FieldDeclaration();

		assertThat(FieldName.UNKNOWN_NAME, equalTo(sut.getName()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		FieldDeclaration sut = new FieldDeclaration();
		sut.setName(FieldName.newFieldName("[T1,P1] [T2,P2].Field"));
		assertThat(FieldName.newFieldName("[T1,P1] [T2,P2].Field"), equalTo(sut.getName()));
	}

	@Test
	public void testEqualityDefault() {
		FieldDeclaration a = new FieldDeclaration();
		FieldDeclaration b = new FieldDeclaration();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		FieldDeclaration a = new FieldDeclaration();
		FieldDeclaration b = new FieldDeclaration();
		a.setName(FieldName.newFieldName("[T1,P1] [T2,P2].Field"));
		b.setName(FieldName.newFieldName("[T1,P1] [T2,P2].Field"));

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentName() {
		FieldDeclaration a = new FieldDeclaration();
		FieldDeclaration b = new FieldDeclaration();
		a.setName(FieldName.newFieldName("[T1,P1] [T2,P2].Field"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		FieldDeclaration sut = new FieldDeclaration();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
