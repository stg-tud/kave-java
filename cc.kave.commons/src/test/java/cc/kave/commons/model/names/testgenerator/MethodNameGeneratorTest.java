/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
	public void generateAllCombination() {
		for (List<ITypeName> typeParams : createTypeParameters()) {
			for (String simpleMethodName : new String[] { "M", ".ctor", ".cctor" }) {
				String genericPart = createGenericPart(typeParams);
				IMethodName sut = m("[?] [?].%s%s()", simpleMethodName, "");
				assertSimpleMethodName(simpleMethodName, sut);
				if (isConstructor(simpleMethodName)) {
					assertIsConstructor(sut);
				}
				//assertTypeParameters(typeParams, sut);
			}
		}

		for (ITypeName declaringType : getTypes()) {
			IMethodName sut = m("[?] [%s].M()", declaringType);
			Assert.assertEquals(declaringType, sut.getDeclaringType());
		}

		for (ITypeName returnType : getTypes()) {
			IMethodName sut = m("[%s] [?].M()", returnType);
			assertReturnType(returnType, sut);
		}
	}

	private void assertTypeParameters(List<ITypeName> typeParams, IMethodName sut) {
		Assert.assertEquals(!typeParams.isEmpty(), sut.isGenericEntity());
		Assert.assertEquals(!typeParams.isEmpty(), sut.hasTypeParameters());
		Assert.assertEquals(typeParams, sut.getTypeParameters());
	}

	private String createGenericPart(List<ITypeName> typeParams) {
		if(typeParams.size() == 1){
			return String.format("´1[[%s]]", typeParams.get(0));
		}else if(typeParams.size() == 2){
			return String.format("´2[[%s],[%s]]", typeParams.get(0), typeParams.get(1));
		}
		return "";
	}

	private Iterable<List<ITypeName>> createTypeParameters() {
		List<List<ITypeName>> params = Lists.newArrayList();
		params.add(Lists.newLinkedList());
		ITypeName lastType = null;
		for(ITypeName t : getTypeParameters()){
			params.add(Lists.newArrayList(t));
			if(lastType != null){
				params.add(Lists.newArrayList(lastType, t));
			}
			lastType = t;
		}
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
		String identifier = String.format(id, args);
		System.out.printf("(%d) building '%s...'\n", (num), identifier);
		IMethodName sut = MethodName.newMethodName(identifier);
		System.out.printf("(%d) testing '%s...'\n", (num++), sut.getIdentifier());
		return sut;
	}

	private void assertSimpleMethodName(String simpleMethodName, IMethodName sut) {
		Assert.assertEquals(simpleMethodName, sut.getName());
	}

	private void assertReturnType(ITypeName returnType, IMethodName sut) {
		Assert.assertEquals(returnType, sut.getReturnType());
	}

	private void assertIsConstructor(IMethodName sut) {
		Assert.assertTrue(sut.isConstructor());
	}

	private String createLog(IMethodName sut) {
		return sut.getIdentifier();
	}
}