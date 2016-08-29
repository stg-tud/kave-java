package exec.recommender_reimplementation.java_printer;

import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;

public class PhantomClassGeneratorUtilTest {

	@Test
	public void transformsNestedType() {
		ITypeName nestedType = Names.newType("T+U,P");
		ITypeName actual = PhantomClassGeneratorUtil.transformNestedType(nestedType);
		ITypeName expected = Names.newType("T,P");
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void doesNotDestoryNonNestedTypes() {
		ITypeName nestedType = Names.newType("T,P");
		ITypeName actual = PhantomClassGeneratorUtil.transformNestedType(nestedType);
		ITypeName expected = Names.newType("T,P");
		Assert.assertEquals(expected, actual);
	}
}
