package commons.model.ssts.impl.blocks;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.declarations.VariableDeclaration;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;

import com.google.common.collect.Lists;
import commons.model.ssts.impl.SSTBaseTest;

public class CatchBlockTest extends SSTBaseTest {
	@Test
	public void testDefaultValues() {
		CatchBlock sut = new CatchBlock();

		assertThat(new VariableDeclaration(), equalTo(sut.getException()));
		assertThat(Lists.newArrayList(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		CatchBlock sut = new CatchBlock();
		sut.setException(this.someDeclaration());
		sut.getBody().add(new ReturnStatement());

		assertThat(this.someDeclaration(), equalTo(sut.getException()));
		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getBody()));
	}

	@Test
	public void testEqualityDefault() {
		CatchBlock a = new CatchBlock();
		CatchBlock b = new CatchBlock();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		CatchBlock a = new CatchBlock();
		CatchBlock b = new CatchBlock();
		a.setException(this.someDeclaration());
		a.getBody().add(new ReturnStatement());
		b.setException(this.someDeclaration());
		b.getBody().add(new ReturnStatement());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentException() {
		CatchBlock a = new CatchBlock();
		CatchBlock b = new CatchBlock();
		a.setException(this.someDeclaration());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentBody() {
		CatchBlock a = new CatchBlock();
		CatchBlock b = new CatchBlock();
		a.getBody().add(new ReturnStatement());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}
}
