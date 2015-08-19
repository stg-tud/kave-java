package commons.model.ssts.impl.blocks;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Test;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.SwitchBlock;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;

import com.google.common.collect.Lists;
import commons.model.ssts.impl.SSTBaseTest;
import commons.model.ssts.impl.SSTTestHelper;

public class SwitchBlockTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		SwitchBlock sut = new SwitchBlock();

		assertThat(new VariableReference(), equalTo(sut.getReference()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getSections()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getDefaultSection()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		SwitchBlock sut = new SwitchBlock();
		sut.setReference(someVarRef("a"));
		sut.getSections().add(new CaseBlock());
		sut.getDefaultSection().add(new ReturnStatement());

		assertThat(someVarRef("a"), equalTo(sut.getReference()));
		assertThat(Lists.newArrayList(new CaseBlock()), equalTo(sut.getSections()));
		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getDefaultSection()));
	}

	@Test
	public void testEqualityDefault() {
		SwitchBlock a = new SwitchBlock();
		SwitchBlock b = new SwitchBlock();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		SwitchBlock a = new SwitchBlock();
		SwitchBlock b = new SwitchBlock();
		a.setReference(someVarRef("a"));
		a.getSections().add(new CaseBlock());
		a.getDefaultSection().add(new ReturnStatement());
		b.setReference(someVarRef("a"));
		b.getSections().add(new CaseBlock());
		b.getDefaultSection().add(new ReturnStatement());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentReference() {
		SwitchBlock a = new SwitchBlock();
		SwitchBlock b = new SwitchBlock();
		a.setReference(someVarRef("a"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentSections() {
		SwitchBlock a = new SwitchBlock();
		SwitchBlock b = new SwitchBlock();
		a.getSections().add(new CaseBlock());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentDefaultSection() {
		SwitchBlock a = new SwitchBlock();
		SwitchBlock b = new SwitchBlock();
		a.getDefaultSection().add(new ReturnStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		SwitchBlock sut = new SwitchBlock();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testVisitorWithReturnIsImplemented() {
		// TODO: Visitor Test
	}
}
