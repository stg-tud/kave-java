package commons.model.ssts.impl.expressions.assignable;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.expressions.assignable.ITypeCheckExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.TypeCheckExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import commons.model.ssts.impl.SSTBaseTest;
import commons.model.ssts.impl.SSTTestHelper;

public class TypeCheckExpressionTest extends SSTBaseTest {

	@Test
	public void testDefaultValues() {
		ITypeCheckExpression sut = new TypeCheckExpression();
		Assert.assertEquals(new VariableReference(), sut.getReference());
		Assert.assertEquals(CsTypeName.UNKNOWN_NAME, sut.getType());
		Assert.assertNotEquals(0, sut.hashCode());
		Assert.assertNotEquals(1, sut.hashCode());
	}

	@Test
	public void testSettingValues() {
		TypeCheckExpression sut = new TypeCheckExpression();
		sut.setReference(someVarRef());
		sut.setType(CsTypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0"));
		Assert.assertEquals(CsTypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0"), sut.getType());
		Assert.assertEquals(someVarRef(), sut.getReference());
	}

	@Test
	public void testEquality_Default() {
		TypeCheckExpression a = new TypeCheckExpression();
		TypeCheckExpression b = new TypeCheckExpression();
		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void testEquality_ReallyTheSame() {
		TypeCheckExpression a = new TypeCheckExpression();
		a.setType(CsTypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0"));
		a.setReference(someVarRef());

		TypeCheckExpression b = new TypeCheckExpression();
		b.setType(CsTypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0"));
		b.setReference(someVarRef());

		Assert.assertEquals(a, b);
		Assert.assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void testEquality_DifferentType() {
		TypeCheckExpression a = new TypeCheckExpression();
		a.setType(CsTypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0"));
		a.setReference(someVarRef());

		TypeCheckExpression b = new TypeCheckExpression();
		b.setType(CsTypeName.newTypeName("System.String, mscorlib, 4.0.0.0"));
		b.setReference(someVarRef());

		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void testEquality_DifferentVarRef() {
		TypeCheckExpression a = new TypeCheckExpression();
		a.setType(CsTypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0"));
		a.setReference(someVarRef("i"));

		TypeCheckExpression b = new TypeCheckExpression();
		b.setType(CsTypeName.newTypeName("System.Int32, mscorlib, 4.0.0.0"));
		b.setReference(someVarRef("j"));

		Assert.assertNotEquals(a, b);
		Assert.assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void testVisitorIsImplemented() {
		ITypeCheckExpression sut = new TypeCheckExpression();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

}
