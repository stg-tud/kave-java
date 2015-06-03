package commons.model.ssts.impl.declarations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.impl.declarations.DelegateDeclaration;

import commons.model.ssts.impl.SSTTestHelper;

public class DelegateDeclarationTest {

	@Test
	public void testDefaultValues() {
		DelegateDeclaration sut = new DelegateDeclaration();

		assertThat(CsTypeName.UNKNOWN_NAME, equalTo(sut.getName()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		DelegateDeclaration sut = new DelegateDeclaration();
		sut.setName(CsTypeName.newTypeName("SoemType,P"));

		assertThat(CsTypeName.newTypeName("SoemType,P"), equalTo(sut.getName()));
	}

	@Test
	public void testEqualityDefault() {
		DelegateDeclaration a = new DelegateDeclaration();
		DelegateDeclaration b = new DelegateDeclaration();
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		DelegateDeclaration a = new DelegateDeclaration();
		DelegateDeclaration b = new DelegateDeclaration();
		a.setName(CsTypeName.newTypeName("SoemType,P"));
		b.setName(CsTypeName.newTypeName("SoemType,P"));

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentType() {
		DelegateDeclaration a = new DelegateDeclaration();
		DelegateDeclaration b = new DelegateDeclaration();
		a.setName(CsTypeName.newTypeName("SoemType,P"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		DelegateDeclaration sut = new DelegateDeclaration();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
