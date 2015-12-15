package cc.kave.commons.model.ssts.impl.declarations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsDelegateTypeName;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.declarations.DelegateDeclaration;

public class DelegateDeclarationTest {

	@Test
	public void testDefaultValues() {
		DelegateDeclaration sut = new DelegateDeclaration();

		assertThat(CsDelegateTypeName.UNKNOWN_NAME, equalTo(sut.getName()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		DelegateDeclaration sut = new DelegateDeclaration();
		sut.setName(CsDelegateTypeName.newDelegateTypeName("d:SomeType,P"));

		assertThat(CsDelegateTypeName.newDelegateTypeName("d:SomeType,P"), equalTo(sut.getName()));
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
		a.setName(CsDelegateTypeName.newDelegateTypeName("d:SomeType,P"));
		b.setName(CsDelegateTypeName.newDelegateTypeName("d:SomeType,P"));

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentType() {
		DelegateDeclaration a = new DelegateDeclaration();
		DelegateDeclaration b = new DelegateDeclaration();
		a.setName(CsDelegateTypeName.newDelegateTypeName("d:SomeType,P"));

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
