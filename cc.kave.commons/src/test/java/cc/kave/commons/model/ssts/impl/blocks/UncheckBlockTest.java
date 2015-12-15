package cc.kave.commons.model.ssts.impl.blocks;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Test;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.blocks.UncheckedBlock;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;

import com.google.common.collect.Lists;

public class UncheckBlockTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		UncheckedBlock sut = new UncheckedBlock();

		assertThat(new ArrayList<IStatement>(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		UncheckedBlock sut = new UncheckedBlock();
		sut.getBody().add(new BreakStatement());

		assertThat(Lists.newArrayList(new BreakStatement()), equalTo(sut.getBody()));
	}

	@Test
	public void testEqualityDefault() {
		UncheckedBlock a = new UncheckedBlock();
		UncheckedBlock b = new UncheckedBlock();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		UncheckedBlock a = new UncheckedBlock();
		UncheckedBlock b = new UncheckedBlock();
		a.getBody().add(new BreakStatement());
		b.getBody().add(new BreakStatement());
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentBody() {
		UncheckedBlock a = new UncheckedBlock();
		UncheckedBlock b = new UncheckedBlock();
		a.getBody().add(new BreakStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		UncheckedBlock sut = new UncheckedBlock();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testVisitorWithReturnIsImplemented() {
		// TODO: Visitor Test
	}
}
