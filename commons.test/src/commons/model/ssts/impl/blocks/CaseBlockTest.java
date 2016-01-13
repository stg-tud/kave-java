package commons.model.ssts.impl.blocks;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;

import org.junit.Test;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;

import com.google.common.collect.Lists;
import commons.model.ssts.impl.SSTBaseTest;

public class CaseBlockTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		CaseBlock sut = new CaseBlock();

		assertThat(new UnknownExpression(), equalTo(sut.getLabel()));
		assertThat(new ArrayList<IStatement>(), equalTo(sut.getBody()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		CaseBlock sut = new CaseBlock();
		sut.setLabel(this.label("a"));
		sut.setBody(Lists.newArrayList(new ReturnStatement()));

		assertThat(this.label("a"), equalTo(sut.getLabel()));
		assertThat(Lists.newArrayList(new ReturnStatement()), equalTo(sut.getBody()));
	}

	@Test
	public void testEqualityDefault() {
		CaseBlock a = new CaseBlock();
		CaseBlock b = new CaseBlock();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		CaseBlock a = new CaseBlock();
		CaseBlock b = new CaseBlock();
		a.setLabel(this.label("a"));
		a.setBody(Lists.newArrayList(new ReturnStatement()));
		b.setLabel(this.label("a"));
		b.setBody(Lists.newArrayList(new ReturnStatement()));

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentLabel() {
		CaseBlock a = new CaseBlock();
		CaseBlock b = new CaseBlock();
		a.setLabel(this.label("a"));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentBody() {
		CaseBlock a = new CaseBlock();
		CaseBlock b = new CaseBlock();
		a.setBody(Lists.newArrayList(new ReturnStatement()));

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}
}
