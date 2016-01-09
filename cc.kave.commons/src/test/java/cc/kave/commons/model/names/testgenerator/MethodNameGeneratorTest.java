package cc.kave.commons.model.names.testgenerator;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;

public class MethodNameGeneratorTest extends AbstractNameGeneratorTest {

	@Test
	@Ignore
	public void generateAllCombination() {
		for (List<ITypeName> typeParams : createTypeParameters()) {
			for (ITypeName returnType : getTypes()) {
				for (ITypeName declaringType : getTypes()) {
					for (String simpleMethodName : new String[] { "M", ".ctor", ".cctor" }) {

						String genericPart = createGenericPart(typeParams);

						IMethodName sut = m("[%s] [%s].%s%s()", returnType, declaringType, simpleMethodName,
								genericPart);
						Assert.assertEquals(createLog(sut), returnType, sut.getReturnType());
						assertReturnType(returnType, sut);
						Assert.assertEquals(declaringType, sut.getDeclaringType());
						assertSimpleMethodName(simpleMethodName, sut);

						if (isConstructor(simpleMethodName)) {
							assertIsConstructor(sut);
						}
						assertTypeParameters(typeParams, sut);

					}
				}
			}
		}
	}

	@Test
	public void testTypes() {
		int index = 0;
		for (ITypeName type : getTypes()) {
			index++;
		}
		System.out.println(index);
	}

	private void assertTypeParameters(List<ITypeName> typeParams, IMethodName sut) {
		Assert.assertEquals(!typeParams.isEmpty(), sut.isGenericEntity());
		Assert.assertEquals(!typeParams.isEmpty(), sut.hasTypeParameters());
		Assert.assertEquals(typeParams, sut.getTypeParameters());
	}

	private String createGenericPart(List<ITypeName> typeParams) {
		// TODO implement me!
		return "";
	}

	private Iterable<List<ITypeName>> createTypeParameters() {
		// TODO implement me!
		List<List<ITypeName>> params = Lists.newArrayList();
		params.add(Lists.newLinkedList());
		return params;
	}

	private boolean isConstructor(String simpleMethodName) {
		boolean isCtor = ".ctor".equals(simpleMethodName);
		boolean isCctor = ".cctor".equals(simpleMethodName);
		boolean isConstructor = isCtor || isCctor;
		return isConstructor;
	}

	private int num = 0;

	private IMethodName m(String id, Object... args) {
		IMethodName sut = MethodName.newMethodName(String.format(id, args));
		System.out.printf("(%d) testing '%s...'\n", (num++), sut.getIdentifier());
		return sut;
	}

	private void assertSimpleMethodName(String simpleMethodName, IMethodName sut) {
		Assert.assertEquals(simpleMethodName, sut.getName());
	}

	private void assertReturnType(ITypeName returnType, IMethodName sut) {
		// TODO Auto-generated method stub

	}

	private void assertIsConstructor(IMethodName sut) {
		// TODO Auto-generated method stub

	}

	private String createLog(IMethodName sut) {
		return sut.getIdentifier();
	}
}