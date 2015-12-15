package cc.kave.commons.model.ssts.impl.statements;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;

public class AssignmentTest {

	@Test
	public void testDefaultValues() {
		Assignment sut = new Assignment();

		assertThat(new UnknownReference(), equalTo(sut.getReference()));
		assertThat(new UnknownExpression(), equalTo(sut.getExpression()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		Assignment sut = new Assignment();
		VariableReference ref = new VariableReference();
		ref.setIdentifier("x");
		sut.setReference(ref);
		sut.setExpression(new ConstantValueExpression());

		assertThat(ref, equalTo(sut.getReference()));
		assertThat(new ConstantValueExpression(), equalTo(sut.getExpression()));
	}

	@Test
	public void testEqualityDefault() {
		Assignment a = new Assignment();
		Assignment b = new Assignment();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		Assignment a = (Assignment) SSTUtil.assignmentToLocal("x", new ConstantValueExpression());
		Assignment b = (Assignment) SSTUtil.assignmentToLocal("x", new ConstantValueExpression());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentReference() {
		Assignment a = (Assignment) SSTUtil.assignmentToLocal("a", new ConstantValueExpression());
		Assignment b = (Assignment) SSTUtil.assignmentToLocal("b", new ConstantValueExpression());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentExpression() {
		Assignment a = (Assignment) SSTUtil.assignmentToLocal("a", new ConstantValueExpression());
		Assignment b = (Assignment) SSTUtil.assignmentToLocal("a", new ComposedExpression());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		Assignment sut = new Assignment();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

}
