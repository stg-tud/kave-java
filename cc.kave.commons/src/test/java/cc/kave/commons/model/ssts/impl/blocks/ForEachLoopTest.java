package cc.kave.commons.model.ssts.impl.blocks;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;

import com.google.common.collect.Lists;

public class ForEachLoopTest extends SSTBaseTest {
	@Test
	public void testDefaultValues() {
		ForEachLoop sut = new ForEachLoop();

		assertThat(new VariableDeclaration(), equalTo(sut.getDeclaration()));
		assertThat(new VariableReference(), equalTo(sut.getLoopedReference()));
		assertThat(Lists.newArrayList(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		ForEachLoop sut = new ForEachLoop();
		sut.setDeclaration(someDeclaration());
		sut.setLoopedReference(this.someVarRef("a"));
		sut.getBody().add(new ReturnStatement());

		assertThat(this.someDeclaration(), equalTo(sut.getDeclaration()));
		assertThat(this.someVarRef("a"), equalTo(sut.getLoopedReference()));
		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getBody()));
	}

	@Test
	public void testEqualityDefault() {
		ForEachLoop a = new ForEachLoop();
		ForEachLoop b = new ForEachLoop();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		ForEachLoop a = new ForEachLoop();
		ForEachLoop b = new ForEachLoop();
		a.setDeclaration(someDeclaration());
		a.setLoopedReference(this.someVarRef("a"));
		a.getBody().add(new ReturnStatement());
		b.setDeclaration(someDeclaration());
		b.setLoopedReference(this.someVarRef("a"));
		b.getBody().add(new ReturnStatement());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentDeclaration() {
		ForEachLoop a = new ForEachLoop();
		ForEachLoop b = new ForEachLoop();
		a.setDeclaration(someDeclaration());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentBody() {
		ForEachLoop a = new ForEachLoop();
		ForEachLoop b = new ForEachLoop();
		a.getBody().add(new ReturnStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentLoopedReference() {
		ForEachLoop a = new ForEachLoop();
		ForEachLoop b = new ForEachLoop();
		a.setLoopedReference(this.someVarRef("a"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		ForEachLoop sut = new ForEachLoop();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
