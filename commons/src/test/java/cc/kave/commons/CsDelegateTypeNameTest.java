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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.model.names.DelegateTypeName;
import cc.kave.commons.model.names.EventName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsDelegateTypeName;
import cc.kave.commons.model.names.csharp.CsEventName;
import cc.kave.commons.model.names.csharp.CsLambdaName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsParameterName;
import cc.kave.commons.model.names.csharp.CsTypeName;

public class CsDelegateTypeNameTest {

	private static final DelegateTypeName LEGACY_DELEGATE_NAME = CsDelegateTypeName
			.newDelegateTypeName("d:Some.DelegateType, A, 1.0.0.0");
	private static final DelegateTypeName PARAMETERLESS_DELEGATE_NAME = CsDelegateTypeName
			.newDelegateTypeName("d:[R, A, 1.0.0.0] [Some.DelegateType, A, 1.0.0.0].()");
	private static final DelegateTypeName PARAMETERIZED_DELEGATE_NAME = CsDelegateTypeName.newDelegateTypeName(
			"d:[R, A, 1.0.0.0] [Some.DelegateType, A, 1.0.0.0].([C, A, 1.2.3.4] p1, [D, A, 1.2.3.4] p2)");

	private static final DelegateTypeName[] DELEGATE_TYPE_NAMES = { LEGACY_DELEGATE_NAME, PARAMETERLESS_DELEGATE_NAME,
			PARAMETERIZED_DELEGATE_NAME };

	// TODO:
	/*
	 * [TestCaseSource("DelegateTypeNames")] public void
	 * ParsesFullName(IDelegateTypeName delegateType) {
	 * Assert.AreEqual("Some.DelegateType", delegateType.FullName); }
	 * 
	 * [TestCaseSource("DelegateTypeNames")] public void
	 * ParsesName(IDelegateTypeName delegateType) {
	 * Assert.AreEqual("DelegateType", delegateType.Name); }
	 * 
	 * [TestCaseSource("DelegateTypeNames")] public void
	 * ParsesNamespace(IDelegateTypeName delegateType) {
	 * Assert.AreEqual(NamespaceName.Get("Some"), delegateType.Namespace); }
	 */

	@Test
	public void ParsesFullName() {
		assertTrue(false);
	}

	@Test
	public void isNested() {
		assertTrue(CsDelegateTypeName.newDelegateTypeName("d:[R, P] [O+D, P].()").isNestedType());
	}

	@Test
	public void isNotNested() {
		assertFalse(CsDelegateTypeName.newDelegateTypeName("d:[R, P] [D, P].()").isNestedType());
	}

	@Test
	public void parsesSignature() {
		assertEquals("DelegateType()", LEGACY_DELEGATE_NAME.getSignature());
		assertEquals("DelegateType()", PARAMETERLESS_DELEGATE_NAME.getSignature());
		assertEquals("DelegateType([C, A, 1.2.3.4] p1, [D, A, 1.2.3.4] p2)",
				PARAMETERIZED_DELEGATE_NAME.getSignature());
	}

	@Test
	public void hasNoParameters() {
		assertFalse(LEGACY_DELEGATE_NAME.hasParameters());
		assertFalse(PARAMETERLESS_DELEGATE_NAME.hasParameters());
	}

	@Test
	public void hasParameters() {
		assertTrue(PARAMETERIZED_DELEGATE_NAME.hasParameters());
	}

	@Test
	public void parsesParameters() {
		assertTrue(LEGACY_DELEGATE_NAME.getParameters().isEmpty());
		assertTrue(PARAMETERLESS_DELEGATE_NAME.getParameters().isEmpty());
		assertEquals(
				new Object[] { CsParameterName.newParameterName("[C, A, 1.2.3.4] p1"),
						CsParameterName.newParameterName("[D, A, 1.2.3.4] p2") },
				PARAMETERIZED_DELEGATE_NAME.getParameters().toArray());
	}

	@Test
	public void otherTypeNameIsNoDelegateType() {
		TypeName name = CsTypeName.newTypeName("My.NonDelegate.Type, ND, 6.6.6.6");

		assertFalse(name.isDelegateType());
	}

	@Test
	public void parsesDelegateTypeOfMethodParameter() {
		MethodName methodName = CsMethodName.newMethodName("[R, A] [D, A].M([d:[DR, A] [DD, A].()] p)");
		ParameterName delegateParameter = methodName.getParameters().get(0);

		assertEquals(CsParameterName.newParameterName("[d:[DR, A] [DD, A].()] p"), delegateParameter);
	}

	@Test
	public void parsesDelegateTypeOfLambdaParameter() {
		CsLambdaName lambdaName = CsLambdaName.newLambdaName("[R, P] ([d:[DR, A] [DD, A].()] p)");
		ParameterName delegateParameter = lambdaName.getParameters().get(0);

		assertEquals(CsParameterName.newParameterName("[d:[DR, A] [DD, A].()] p"), delegateParameter);
	}

	@Test
	public void parsesDelegateTypeOfMemberValueType() {
		EventName eventName = CsEventName.newEventName(
				"[d:[System.Void, mscorlib, 4.0.0.0] [C+Delegate, TestProject].([System.Object, mscorlib, 4.0.0.0] obj)] [C, TestProject].Event");
		assertEquals(
				CsTypeName.newTypeName(
						"d:[System.Void, mscorlib, 4.0.0.0] [C+Delegate, TestProject].([System.Object, mscorlib, 4.0.0.0] obj)"),
				eventName.getHandlerType());
	}

	@Test
	public void isDelegateTypeName() {
		TypeName typeName = CsTypeName.newTypeName(
				"d:[System.Void, mscorlib, 4.0.0.0] [System.AppDomainInitializer, mscorlib, 4.0.0.0].([System.String[], mscorlib, 4.0.0.0] args)");
		assertTrue(typeName instanceof DelegateTypeName);
	}

	@Test
	public void parsesTypeParameters() {
		TypeName typeName = CsTypeName.newTypeName("d:[T] [DT`1[[T -> String, mscorlib]]].([T] p)");

		assertTrue(typeName.hasTypeParameters());
		// assertEquals(new Object[] { CsTypeParameterName.Get("T -> String,
		// mscorlib") },
		// typeName.getTypeParameters().toArray());
		assertTrue(false);
	}

	@Test
	public void fixesLegacyDelegateTypeNameFormat() {
		assertEquals("d:[?] [Some.DelegateType, A, 1.0.0.0].()", LEGACY_DELEGATE_NAME.getIdentifier());
	}
}
