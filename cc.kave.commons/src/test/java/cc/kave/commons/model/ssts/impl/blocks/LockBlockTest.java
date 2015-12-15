package cc.kave.commons.model.ssts.impl.blocks;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Test;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.blocks.LockBlock;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;

import com.google.common.collect.Lists;

public class LockBlockTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		LockBlock sut = new LockBlock();

		assertThat(new VariableReference(), equalTo(sut.getReference()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		LockBlock sut = new LockBlock();
		sut.setReference(this.someVarRef("x"));
		sut.setBody(Lists.newArrayList(new BreakStatement()));

		assertThat(this.someVarRef("x"), equalTo(sut.getReference()));
		assertThat(Lists.newArrayList(new BreakStatement()), equalTo(sut.getBody()));
	}

	@Test
	public void testEqualityDefault() {
		LockBlock a = new LockBlock();
		LockBlock b = new LockBlock();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		LockBlock a = new LockBlock();
		a.setReference(this.someVarRef("x"));
		a.setBody(Lists.newArrayList(new BreakStatement()));
		LockBlock b = new LockBlock();
		b.setReference(this.someVarRef("x"));
		b.setBody(Lists.newArrayList(new BreakStatement()));

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentReference() {
		LockBlock a = new LockBlock();
		a.setReference(this.someVarRef("a"));
		LockBlock b = new LockBlock();
		b.setReference(this.someVarRef("b"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentBody() {
		LockBlock a = new LockBlock();
		a.setBody(Lists.newArrayList(new BreakStatement()));
		LockBlock b = new LockBlock();

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		LockBlock sut = new LockBlock();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testVisitorWithReturnIsImplemented() {
		// TODO: Visitor Test
	}

}
