package commons.model.ssts.impl.blocks;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsParameterName;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;

import com.google.common.collect.Lists;
import commons.model.ssts.impl.SSTBaseTest;

public class CatchBlockTest extends SSTBaseTest {
	@Test
	public void testDefaultValues() {
		CatchBlock sut = new CatchBlock();

		assertThat(CsParameterName.UNKNOWN_NAME, equalTo(sut.getParameter()));
		assertThat(Lists.newArrayList(), equalTo(sut.getBody()));
		assertThat(false, equalTo(sut.isGeneral()));
		assertThat(false, equalTo(sut.isUnnamed()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		CatchBlock sut = new CatchBlock();
		sut.setParameter(someParameter());
		sut.getBody().add(new ReturnStatement());
		sut.setGeneral(true);
		sut.setUnnamed(true);

		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getBody()));
		assertThat(someParameter(), equalTo(sut.getParameter()));
		assertThat(true, equalTo(sut.isGeneral()));
		assertThat(true, equalTo(sut.isUnnamed()));
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
		a.setGeneral(true);
		a.setUnnamed(true);
		b.setParameter(someParameter());
		b.getBody().add(new ReturnStatement());
		b.setGeneral(true);
		b.setUnnamed(true);

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
	public void testEqualityDifferentIsGeneral() {
		CatchBlock a = new CatchBlock();
		CatchBlock b = new CatchBlock();
		a.setGeneral(true);

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentIsUnnamed() {
		CatchBlock a = new CatchBlock();
		CatchBlock b = new CatchBlock();
		a.setUnnamed(true);

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}
}
