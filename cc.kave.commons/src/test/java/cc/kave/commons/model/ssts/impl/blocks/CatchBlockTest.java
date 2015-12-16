package cc.kave.commons.model.ssts.impl.blocks;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.ParameterName;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;

import com.google.common.collect.Lists;

public class CatchBlockTest extends SSTBaseTest {
	@Test
	public void testDefaultValues() {
		CatchBlock sut = new CatchBlock();

		assertThat(ParameterName.UNKNOWN_NAME, equalTo(sut.getParameter()));
		assertThat(Lists.newArrayList(), equalTo(sut.getBody()));
		assertThat(CatchBlockKind.Default, equalTo(sut.getKind()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		CatchBlock sut = new CatchBlock();
		sut.setParameter(someParameter());
		sut.getBody().add(new ReturnStatement());
		sut.setKind(CatchBlockKind.General);

		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getBody()));
		assertThat(someParameter(), equalTo(sut.getParameter()));
		assertThat(CatchBlockKind.General, equalTo(sut.getKind()));
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
		a.setParameter(someParameter());
		a.getBody().add(new ReturnStatement());
		a.setKind(CatchBlockKind.General);
		b.setParameter(someParameter());
		b.getBody().add(new ReturnStatement());
		b.setKind(CatchBlockKind.General);
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentParameter() {
		CatchBlock a = new CatchBlock();
		CatchBlock b = new CatchBlock();
		a.setParameter(someParameter());

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

	@Test
	public void testEqualityDifferentKind() {
		CatchBlock a = new CatchBlock();
		CatchBlock b = new CatchBlock();
		a.setKind(CatchBlockKind.General);

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

}
