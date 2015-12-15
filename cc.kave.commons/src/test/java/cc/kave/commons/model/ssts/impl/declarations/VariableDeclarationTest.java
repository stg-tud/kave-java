package cc.kave.commons.model.ssts.impl.declarations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;

public class VariableDeclarationTest {

	@Test
	public void testDefaultValues() {
		VariableDeclaration sut = new VariableDeclaration();

		assertThat(new VariableReference(), equalTo(sut.getReference()));
		assertThat(CsTypeName.UNKNOWN_NAME, equalTo(sut.getType()));
		assertThat(true, equalTo(sut.isMissing()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		VariableDeclaration sut = new VariableDeclaration();
		sut.setReference(SSTUtil.variableReference("a"));
		sut.setType(CsTypeName.UNKNOWN_NAME);

		assertThat(false, equalTo(sut.isMissing()));
		assertThat(SSTUtil.variableReference("a"), equalTo(sut.getReference()));
		assertThat(CsTypeName.UNKNOWN_NAME, equalTo(sut.getType()));
	}

	@Test
	public void testEqualityDefault() {
		VariableDeclaration a = new VariableDeclaration();
		VariableDeclaration b = new VariableDeclaration();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		VariableDeclaration a = new VariableDeclaration();
		VariableDeclaration b = new VariableDeclaration();
		a.setReference(SSTUtil.variableReference("a"));
		a.setType(CsTypeName.newTypeName("T1,P1"));
		b.setReference(SSTUtil.variableReference("a"));
		b.setType(CsTypeName.newTypeName("T1,P1"));

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentReference() {
		VariableDeclaration a = new VariableDeclaration();
		VariableDeclaration b = new VariableDeclaration();
		a.setReference(SSTUtil.variableReference("a"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentType() {
		VariableDeclaration a = new VariableDeclaration();
		VariableDeclaration b = new VariableDeclaration();
		a.setType(CsTypeName.newTypeName("T1,P1"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		VariableDeclaration sut = new VariableDeclaration();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
