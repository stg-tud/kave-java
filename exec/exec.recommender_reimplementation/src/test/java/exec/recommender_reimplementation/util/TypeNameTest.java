package exec.recommender_reimplementation.util;
import org.junit.Assert;
import org.junit.Test;

import cc.kave.commons.model.naming.impl.v0.types.TypeUtils;
import cc.kave.commons.model.naming.types.ITypeName;
public class TypeNameTest {

	@Test
	public void enumNestedType() {
		shouldParseDeclaringTypes("e:n.T1+E, P", "n.T1, P");
	}

	@Test
	public void interfaceNestedType() {
		shouldParseDeclaringTypes("i:n.T1+I, P", "n.T1, P");
	}

	@Test
	public void structNestedType() {
		shouldParseDeclaringTypes("s:n.T1+S, P", "n.T1, P");
	}

	public void shouldParseDeclaringTypes(String typeId, String declTypeId)
    {
        ITypeName sut = t(typeId);
        Assert.assertTrue(sut.isNestedType());
		Assert.assertEquals(t(declTypeId), sut.getDeclaringType());
    }

	private static ITypeName t(String id)
    {
		return TypeUtils.createTypeName(id);
    }
}
