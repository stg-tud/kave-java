package exec.recommender_reimplementation.java_printer;

import static exec.recommender_reimplementation.java_printer.PhantomClassGeneratorUtil.removesQualifier;
import static exec.recommender_reimplementation.java_printer.PhantomClassGeneratorUtil.transformNestedType;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;

public class PhantomClassGeneratorUtilTest {

	@Test
	public void transformsNestedType() {
		ITypeName nestedType = Names.newType("T+U,P");
		ITypeName actual = transformNestedType(nestedType);
		ITypeName expected = Names.newType("T_U,P");
		assertEquals(expected, actual);
	}

	@Test
	public void doesNotDestoryNonNestedTypes() {
		ITypeName nestedType = Names.newType("T,P");
		ITypeName actual = transformNestedType(nestedType);
		ITypeName expected = Names.newType("T,P");
		assertEquals(expected, actual);
	}


	@Test
	public void removesEnumQualfier() {
		ITypeName enumType = Names.newType("e:T,P");
		assertRemoveQualifier(enumType);
	}

	@Test
	public void removesInterfaceQualfier() {
		ITypeName interfaceType = Names.newType("i:T,P");
		assertRemoveQualifier(interfaceType);
	}

	@Test
	public void removesStructQualfier() {
		ITypeName structType = Names.newType("s:T,P");
		assertRemoveQualifier(structType);
	}

	@Test
	public void removesQualifierDoesNotDestoryTypes() {
		ITypeName type = Names.newType("T,P");
		assertRemoveQualifier(type);
	}

	private void assertRemoveQualifier(ITypeName type) {
		ITypeName actual = removesQualifier(type);
		ITypeName expected = Names.newType("T,P");
		assertEquals(expected, actual);
	}
}
