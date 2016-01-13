package commons.model.ssts.impl.declarations;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsPropertyName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;

import com.google.common.collect.Lists;
import commons.model.ssts.impl.SSTTestHelper;

public class PropertyDeclarationTest {

	@Test
	public void testDefaultValues() {
		PropertyDeclaration sut = new PropertyDeclaration();

		assertThat(CsPropertyName.UNKNOWN_NAME, equalTo(sut.getName()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getGet()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getSet()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		PropertyDeclaration sut = new PropertyDeclaration();
		sut.setName(CsPropertyName.newPropertyName("[T1,P1] [T2,P2].Property"));
		sut.setGet(Lists.newArrayList(new ReturnStatement()));
		sut.setSet(Lists.newArrayList(new ContinueStatement()));

		assertThat(CsPropertyName.newPropertyName("[T1,P1] [T2,P2].Property"), equalTo(sut.getName()));
		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getGet()));
		assertThat(Lists.newArrayList(new ContinueStatement()), equalTo(sut.getSet()));
	}

	@Test
	public void testEqualityDefault() {
		PropertyDeclaration a = new PropertyDeclaration();
		PropertyDeclaration b = new PropertyDeclaration();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		PropertyDeclaration a = new PropertyDeclaration();
		PropertyDeclaration b = new PropertyDeclaration();
		a.setName(CsPropertyName.newPropertyName("[T1,P1] [T2,P2].Property"));
		b.setName(CsPropertyName.newPropertyName("[T1,P1] [T2,P2].Property"));

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentType() {
		PropertyDeclaration a = new PropertyDeclaration();
		PropertyDeclaration b = new PropertyDeclaration();
		a.setName(CsPropertyName.newPropertyName("[T1,P1] [T2,P2].Property"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentGet() {
		PropertyDeclaration a = new PropertyDeclaration();
		PropertyDeclaration b = new PropertyDeclaration();
		a.getGet().add(new ReturnStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentSet() {
		PropertyDeclaration a = new PropertyDeclaration();
		PropertyDeclaration b = new PropertyDeclaration();
		a.getSet().add(new ReturnStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		PropertyDeclaration sut = new PropertyDeclaration();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
