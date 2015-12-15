/**
 * Copyright 2015 Waldemar Graf
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cc.kave.commons;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsTypeName;

public class CsMethodNameTest {

	@Test
	public void shouldImplementIsUnknown() {
		assertTrue(CsMethodName.getUnknownName().isUnknown());
	}

	@Test
	public void shouldBeSimpleMethod() {
		MethodName methodName = CsMethodName
				.newMethodName("[System.Void, mscore, 4.0.0.0] [T, P, 1.2.3.4].MethodName()");

		assertEquals("T, P, 1.2.3.4", methodName.getDeclaringType().getIdentifier());
		assertEquals("System.Void, mscore, 4.0.0.0", methodName.getReturnType().getIdentifier());
		assertEquals("MethodName", methodName.getName());
		assertTrue(methodName.getTypeParameters().isEmpty());
		assertFalse(methodName.hasParameters());
		assertTrue(methodName.getParameters().isEmpty());
	}

	@Test
	public void shouldBeMethodWithOneParameter() {
		final String declaringTypeIdentifier = "A, B, 9.9.9.9";
		final String returnTypeIdentifier = "R, C, 7.6.5.4";
		final String parameterIdentifier = "[P, D, 3.4.3.2] n";

		MethodName methodName = CsMethodName.newMethodName(
				"[" + returnTypeIdentifier + "] [" + declaringTypeIdentifier + "].M(" + parameterIdentifier + ")");

		assertEquals(declaringTypeIdentifier, methodName.getDeclaringType().getIdentifier());
		assertEquals(returnTypeIdentifier, methodName.getReturnType().getIdentifier());
		assertEquals("M", methodName.getName());
		assertFalse(methodName.hasTypeParameters());
		assertEquals(1, methodName.getParameters().size());
		assertEquals(parameterIdentifier, methodName.getParameters().get(0).getIdentifier());
	}

	@Test
	public void shouldBeMethodWithMultipleParameters() {
		final String declaringTypeIdentifier = "A, B, 9.9.9.9";
		final String returnTypeIdentifier = "R, C, 7.6.5.4";
		final String param1Identifier = "[P, D, 3.4.3.2] n";
		final String param2Identifier = "[Q, E, 9.1.8.2] o";
		final String param3Identifier = "[R, F, 6.5.7.4] p";

		MethodName methodName = CsMethodName.newMethodName("[" + returnTypeIdentifier + "] [" + declaringTypeIdentifier
				+ "].DoIt(" + param1Identifier + ", " + param2Identifier + ", " + param3Identifier + ")");

		assertEquals(declaringTypeIdentifier, methodName.getDeclaringType().getIdentifier());
		assertEquals(returnTypeIdentifier, methodName.getReturnType().getIdentifier());
		assertEquals("DoIt", methodName.getName());
		assertEquals(3, methodName.getParameters().size());
		assertEquals(param1Identifier, methodName.getParameters().get(0).getIdentifier());
		assertEquals(param2Identifier, methodName.getParameters().get(1).getIdentifier());
		assertEquals(param3Identifier, methodName.getParameters().get(2).getIdentifier());
	}

	@Test
	public void shouldIncludeMethodNameInSignature() {
		MethodName methodName = CsMethodName.newMethodName("[Value, A, 0.0.0.1] [Declarator, A, 0.0.0.1].Method()");

		assertEquals("Method()", methodName.getSignature());
	}

	@Test
	public void shouldIncludeParametersInSignature() {
		MethodName methodName = CsMethodName
				.newMethodName("[Value, A, 1.0.0.0] [Decl, B, 2.0.0.0].A(out [A, A, 1.0.0.0] p1, [B, B, 1.0.0.0] p2)");

		assertEquals("A(out [A, A, 1.0.0.0] p1, [B, B, 1.0.0.0] p2)", methodName.getSignature());
	}

	@Test
	public void shouldIncludeTypeParametersInSignature() {
		MethodName methodName = CsMethodName.newMethodName("[R, R, 1.2.3.4] [D, D, 5.6.7.8].M`1[[T]]([T] p)");

		assertEquals("M`1[[T]]([T] p)", methodName.getSignature());
	}

	@Test
	public void shouldHaveNoTypeParameters() {
		MethodName methodName = CsMethodName.newMethodName("[Value, A, 0.0.0.1] [Declarator, A, 0.0.0.1].Method()");

		assertFalse(methodName.hasTypeParameters());
		assertEquals(0, methodName.getTypeParameters().size());
	}

	@Test
	public void shouldHaveTypeParameter() {
		MethodName methodName = CsMethodName
				.newMethodName("[Value, A, 0.0.0.1] [Declarator, A, 0.0.0.1].Method`1[[T]]()");

		assertTrue(methodName.hasTypeParameters());
	}

	@Test
	public void shouldHaveTwoUnboundTypeParameters() {
		MethodName methodName = CsMethodName.newMethodName("[T] [D, D, 4.5.6.7].M`2[[T],[O]]([O] p)");

		Object[] expected = new Object[] { CsTypeName.newTypeName("T"), CsTypeName.newTypeName("O") };
		assertArrayEquals(expected, methodName.getTypeParameters().toArray());
	}

	@Test
	public void shouldHaveBoundTypeParameter() {
		MethodName methodName = CsMethodName
				.newMethodName("[A] [D, D, 1.2.3.4].M`1[[A -> System.Int32, mscorlib, 4.0.0.0]]()");

		Object[] expected = new Object[] { CsTypeName.newTypeName("A -> System.Int32, mscorlib, 4.0.0.0") };
		assertArrayEquals(expected, methodName.getTypeParameters().toArray());
	}

	@Test
	public void shouldNotConfuseGenericParameterTypesWithTypeParameters1() {
		MethodName methodName = CsMethodName
				.newMethodName("[R, R, 1.2.3.4] [D, D, 5.6.7.8].M([F`1[[T -> G, G, 5.3.2.1]]] p)");

		assertFalse(methodName.hasTypeParameters());
		assertEquals(0, methodName.getTypeParameters().size());
	}

	@Test
	public void shouldNotConfuseGenericParameterTypesWithTypeParameters2() {
		MethodName method = CsMethodName
				.newMethodName("[T, A, 1.0.0.0] [T, A, 1.0.0.0].M`1[[T]]([F`1[[U]], A, 1.0.0.0] p)");

		Object[] expected = new Object[] { CsTypeName.newTypeName("T") };
		assertArrayEquals(expected, method.getTypeParameters().toArray());
	}

	@Test
	public void shouldExcludeTypeParametersFromName() {
		MethodName methodName = CsMethodName.newMethodName("[R] [D, D, 9.8.7.6].M`1[[T]]()");

		assertEquals("M", methodName.getName());
	}

	@Test
	public void shouldNotBeConstructor() {
		MethodName methodName = CsMethodName.newMethodName("[Value, A, 0.0.0.1] [Declarator, A, 0.0.0.1].Method()");

		assertFalse(methodName.isConstructor());
	}

	@Test
	public void shouldBeConstructor() {
		MethodName methodName = CsMethodName.newMethodName("[MyType, A, 0.0.0.1] [MyType, A, 0.0.0.1]..ctor()");

		assertTrue(methodName.isConstructor());
	}

	@Test
	public void shouldBeInstanceMethod() {
		MethodName methodName = CsMethodName.newMethodName("[I, A, 1.0.2.0] [K, K, 0.1.0.2].m()");

		assertFalse(methodName.isStatic());
	}

	@Test
	public void shouldBeStaticMethod() {
		MethodName methodName = CsMethodName.newMethodName("static [I, A, 1.0.2.0] [K, K, 0.1.0.2].m()");

		assertTrue(methodName.isStatic());
	}

	@Test
	public void shouldBeUnknownMethod() {
		assertEquals(CsTypeName.UNKNOWN_NAME, CsMethodName.UNKNOWN_NAME.getReturnType());
		assertEquals(CsTypeName.UNKNOWN_NAME, CsMethodName.UNKNOWN_NAME.getDeclaringType());
		assertEquals("???", CsMethodName.UNKNOWN_NAME.getName());
		assertFalse(CsMethodName.UNKNOWN_NAME.hasParameters());
	}

	@Test
	public void shouldHandleDelegateReturnType() {
		MethodName methodName = CsMethodName.newMethodName("[d:[R,A] [D,A].()] [D,B].M([P,B] p)");

		assertEquals("M([P,B] p)", methodName.getSignature());
		assertEquals("M", methodName.getName());
	}

	@Test
	public void shouldHandleDelegateParameterType() {
		MethodName methodName = CsMethodName.newMethodName("[R,B] [D,B].M([d:[R,A] [D,A].()] p)");

		assertEquals("M([d:[R,A] [D,A].()] p)", methodName.getSignature());
		assertEquals("M", methodName.getName());
	}

	@Test
	public void shouldFindParametersOfMethodNamesEvenWithDelegateNames() {
		MethodName methodName = CsMethodName.newMethodName("[A,P] [d:[B,P] [C,P].([D,P] args)].M([E,P] p)");

		List<ParameterName> ps = methodName.getParameters();
	}

	@Test
	public void shouldBeExtensionMethod() {
		String id = "static [T,P] [T,P].M(this [T,P] o)";
		CsMethodName sut = (CsMethodName) CsMethodName.newMethodName(id);
		assertTrue(sut.isExtensionMethod());
	}

	@Test
	public void ShouldBeExtensionMethod_NotIfNotStatic() {
		String id = "[T,P] [T,P].M(this [T,P] o)";
		CsMethodName sut = (CsMethodName) CsMethodName.newMethodName(id);
		assertFalse(sut.isExtensionMethod());
	}

	@Test
	public void shouldBeExntesionMethod_NotWithoutParameters() {
		String id = "static [T,P] [T,P].M()";
		CsMethodName sut = (CsMethodName) CsMethodName.newMethodName(id);
		assertFalse(sut.isExtensionMethod());
	}

	@Test
	public void shouldBeExtensionMethod_NotWithoutThisMarker() {
		String id = "static [T,P] [T,P].M([T,P] o)";
		CsMethodName sut = (CsMethodName) CsMethodName.newMethodName(id);
		assertFalse(sut.isExtensionMethod());
	}

}