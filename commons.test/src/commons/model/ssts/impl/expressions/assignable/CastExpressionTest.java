package commons.model.ssts.impl.expressions.assignable;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CastExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import commons.model.ssts.impl.SSTBaseTest;
import commons.model.ssts.impl.SSTTestHelper;

public class CastExpressionTest extends SSTBaseTest {
	@Test
	public void testDefaultValues() {
		CastExpression sut = new CastExpression();
		Assert.assertEquals(new VariableReference(), sut.getReference());
		Assert.assertEquals(CsTypeName.UNKNOWN_NAME, sut.getTargetType());
		Assert.assertNotEquals(0, sut.hashCode());
		Assert.assertNotEquals(1, sut.hashCode());
	}

	@Test

	public void testSettingValues() {
		CastExpression sut = new CastExpression();
		sut.setTargetType(CsTypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0"));
		sut.setReference(someVarRef());

		Assert.assertEquals(CsTypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0"), sut.getTargetType());
		Assert.assertEquals(someVarRef(), sut.getReference());
	}

	@Test

	public void testEquality_Default() {
		CastExpression a = new CastExpression();
		CastExpression b = new CastExpression();
		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test

	public void testEquality_ReallyTheSame() {
		CastExpression a = new CastExpression();
		a.setTargetType(CsTypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0"));
		a.setReference(someVarRef());

		CastExpression b = new CastExpression();
		b.setTargetType(CsTypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0"));
		b.setReference(someVarRef());

		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test

	public void testEquality_DifferentTargetType() {
		CastExpression a = new CastExpression();
		a.setTargetType(CsTypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0"));
		a.setReference(someVarRef());

		CastExpression b = new CastExpression();
		a.setTargetType(CsTypeName.newTypeName("System.String, mscorlib, 4.0.0.0"));
		a.setReference(someVarRef());

		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test

	public void testEquality_DifferentVarRef() {
		CastExpression a = new CastExpression();
		a.setTargetType(CsTypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0"));
		a.setReference(someVarRef("i"));

		CastExpression b = new CastExpression();
		a.setTargetType(CsTypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0"));
		a.setReference(someVarRef("j"));

		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void testVisitorIsImplemented() {
		CastExpression sut = new CastExpression();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

}
