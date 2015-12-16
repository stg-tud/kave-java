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

package cc.kave.commons.model.names;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.model.names.IDelegateTypeName;
import cc.kave.commons.model.names.IEventName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.DelegateTypeName;
import cc.kave.commons.model.names.csharp.EventName;
import cc.kave.commons.model.names.csharp.LambdaName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.NamespaceName;
import cc.kave.commons.model.names.csharp.ParameterName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.names.csharp.TypeParameterName;

public class DelegateTypeNameTest {

	private static final IDelegateTypeName LEGACY_DELEGATE_NAME = DelegateTypeName
			.newDelegateTypeName("d:Some.DelegateType, A, 1.0.0.0");
	private static final IDelegateTypeName PARAMETERLESS_DELEGATE_NAME = DelegateTypeName
			.newDelegateTypeName("d:[R, A, 1.0.0.0] [Some.DelegateType, A, 1.0.0.0].()");
	private static final IDelegateTypeName PARAMETERIZED_DELEGATE_NAME = DelegateTypeName.newDelegateTypeName(
			"d:[R, A, 1.0.0.0] [Some.DelegateType, A, 1.0.0.0].([C, A, 1.2.3.4] p1, [D, A, 1.2.3.4] p2)");

	private static final IDelegateTypeName[] DELEGATE_TYPE_NAMES = { LEGACY_DELEGATE_NAME, PARAMETERLESS_DELEGATE_NAME,
			PARAMETERIZED_DELEGATE_NAME };

	// TODO:

	@Test
	public void parsesFullName() {
		assertEquals("Some.DelegateType", DelegateTypeNameTest.LEGACY_DELEGATE_NAME.getFullName());
		assertEquals("Some.DelegateType", DelegateTypeNameTest.PARAMETERLESS_DELEGATE_NAME.getFullName());
		assertEquals("Some.DelegateType", DelegateTypeNameTest.PARAMETERIZED_DELEGATE_NAME.getFullName());
	}

	@Test
	public void parsesName() {
		assertEquals("DelegateType", DelegateTypeNameTest.LEGACY_DELEGATE_NAME.getName());
		assertEquals("DelegateType", DelegateTypeNameTest.PARAMETERLESS_DELEGATE_NAME.getName());
		assertEquals("DelegateType", DelegateTypeNameTest.PARAMETERIZED_DELEGATE_NAME.getName());
	}

	@Test
	public void parsesNamespace() {
		assertEquals(NamespaceName.newNamespaceName("Some"),
				DelegateTypeNameTest.LEGACY_DELEGATE_NAME.getNamespace());
		assertEquals(NamespaceName.newNamespaceName("Some"),
				DelegateTypeNameTest.PARAMETERLESS_DELEGATE_NAME.getNamespace());
		assertEquals(NamespaceName.newNamespaceName("Some"),
				DelegateTypeNameTest.PARAMETERIZED_DELEGATE_NAME.getNamespace());
	}

	@Test
	public void isNested() {
		assertTrue(DelegateTypeName.newDelegateTypeName("d:[R, P] [O+D, P].()").isNestedType());
	}

	@Test
	public void isNotNested() {
		assertFalse(DelegateTypeName.newDelegateTypeName("d:[R, P] [D, P].()").isNestedType());
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
				new Object[] { ParameterName.newParameterName("[C, A, 1.2.3.4] p1"),
						ParameterName.newParameterName("[D, A, 1.2.3.4] p2") },
				PARAMETERIZED_DELEGATE_NAME.getParameters().toArray());
	}

	@Test
	public void otherTypeNameIsNoDelegateType() {
		ITypeName name = TypeName.newTypeName("My.NonDelegate.Type, ND, 6.6.6.6");

		assertFalse(name.isDelegateType());
	}

	@Test
	public void parsesDelegateTypeOfMethodParameter() {
		IMethodName methodName = MethodName.newMethodName("[R, A] [D, A].M([d:[DR, A] [DD, A].()] p)");
		IParameterName delegateParameter = methodName.getParameters().get(0);

		assertEquals(ParameterName.newParameterName("[d:[DR, A] [DD, A].()] p"), delegateParameter);
	}

	@Test
	public void parsesDelegateTypeOfLambdaParameter() {
		LambdaName lambdaName = LambdaName.newLambdaName("[R, P] ([d:[DR, A] [DD, A].()] p)");
		IParameterName delegateParameter = lambdaName.getParameters().get(0);

		assertEquals(ParameterName.newParameterName("[d:[DR, A] [DD, A].()] p"), delegateParameter);
	}

	@Test
	public void parsesDelegateTypeOfMemberValueType() {
		IEventName eventName = EventName.newEventName(
				"[d:[System.Void, mscorlib, 4.0.0.0] [C+Delegate, TestProject].([System.Object, mscorlib, 4.0.0.0] obj)] [C, TestProject].Event");
		assertEquals(
				TypeName.newTypeName(
						"d:[System.Void, mscorlib, 4.0.0.0] [C+Delegate, TestProject].([System.Object, mscorlib, 4.0.0.0] obj)"),
				eventName.getHandlerType());
	}

	@Test
	public void isDelegateTypeName() {
		ITypeName typeName = TypeName.newTypeName(
				"d:[System.Void, mscorlib, 4.0.0.0] [System.AppDomainInitializer, mscorlib, 4.0.0.0].([System.String[], mscorlib, 4.0.0.0] args)");
		assertTrue(typeName instanceof IDelegateTypeName);
	}

	@Test
	public void parsesTypeParameters() {
		ITypeName typeName = TypeName.newTypeName("d:[T] [DT`1[[T -> String, mscorlib]]].([T] p)");

		assertTrue(typeName.hasTypeParameters());
		assertEquals(new Object[] { TypeParameterName.newTypeParameterName("T -> String, mscorlib") },
				typeName.getTypeParameters().toArray());
	}

	@Test
	public void fixesLegacyDelegateTypeNameFormat() {
		assertEquals("d:[?] [Some.DelegateType, A, 1.0.0.0].()", LEGACY_DELEGATE_NAME.getIdentifier());
	}
}
