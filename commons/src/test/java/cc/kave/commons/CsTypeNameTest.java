/// **
// * Copyright 2015 Waldemar Graf
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */

package cc.kave.commons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsAssemblyName;
import cc.kave.commons.model.names.csharp.CsNamespaceName;
import cc.kave.commons.model.names.csharp.CsTypeName;

@Ignore
public class CsTypeNameTest {

	private final String TestAssemblyIdentifier = "a, 1.0.0.0";

	@Test
	public void shouldImplementIsUnknown() {
		assertTrue(CsTypeName.getUnknownName().isUnknown());
	}

	@Test
	public void shouldGetNamespaceFromTypeInGlobalNamespace() {
		TypeName name = CsTypeName.newTypeName("MyType, Assembly, 1.2.3.4");
		assertEquals(CsNamespaceName.getGlobalNamespace(), name.getNamespace());
	}

	@Test
	public void shouldBeVoidType() {
		TypeName name = CsTypeName.newTypeName("System.Void, mscorlib, 4.0.0.0");
		assertTrue(name.isVoidType());
	}

	private String[] shouldNotBeVoidTypeCases() {
		return new String[] { "System.Boolean, mscorlib, 4.0.0.0", "T -> System.Int32, mscorlib, 4.0.0.0" };
	}

	@Test
	public void shouldNotBeVoidType() {
		for (String identifier : shouldNotBeVoidTypeCases()) {
			TypeName name = CsTypeName.newTypeName(identifier);
			boolean voidType = name.isVoidType();
			assertFalse(voidType);
		}

	}

	private String[] shouldBeSimpleTypeCases() {
		return new String[] { "System.Boolean, mscorlib, 4.0.0.0", "System.Decimal, mscorlib, 4.0.0.0",
				"System.SByte, mscorlib, 4.0.0.0", "System.Int16, mscorlib, 4.0.0.0",
				"System.UInt16, mscorlib, 4.0.0.0", "System.Int32, mscorlib, 4.0.0.0",
				"System.UInt32, mscorlib, 4.0.0.0", "System.Int64,, mscorlib, 4.0.0.0",
				"System.UInt64, mscorlib, 4.0.0.0", "System.Char, mscorlib, 4.0.0.0",
				"System.Single, mscorlib, 4.0.0.0", "System.Double, mscorlib, 4.0.0.0",
				"T -> System.Int32, mscorlib, 4.0.0.0" };
	}

	@Test
	public void shouldBeSimpleType() {
		for (String identifer : shouldBeSimpleTypeCases()) {
			TypeName name = CsTypeName.newTypeName(identifer);
			assertTrue(name.isSimpleType());
		}
	}

	// TODO:
	// @Test
	// public void shouldBeSimpleType1() {
	// assertSimpleType("System.Boolean, mscorlib, 4.0.0.0");
	// assertSimpleType("System.Boolean, mscorlib, 4.0.0.0");
	// assertSimpleType("System.Boolean, mscorlib, 4.0.0.0");
	// assertSimpleType("System.Boolean, mscorlib, 4.0.0.0");
	// assertThat("System.Boolean, mscorlib, 4.0.0.0", t -> {
	// int i = 0;
	// // ...
	// return t.isSimpleType();
	// });
	// }
	//
	// public static void assertSimpleType(String identifer) {
	// assertThat(identifer, t -> t.isSimpleType());
	// }
	//
	// public static void assertThat(String identifer, Predicate<TypeName> pred)
	// {
	// TypeName name = CsTypeName.newTypeName(identifer);
	// assertTrue(pred.test(name));
	// }

	private String[] shouldNotBeSimpleTypeCases() {
		return new String[] { "System.Void, mscorlib, 4.0.0.0", "My.Custom.Type, A, 1.2.3.4", "?" };
	}

	@Test
	public void shouldNotBeSimpleType() {
		for (String identifier : shouldNotBeSimpleTypeCases()) {
			TypeName name = CsTypeName.newTypeName(identifier);
			assertFalse(name.isSimpleType());
		}
	}

	private String[] shouldBeNullableTypeCases() {
		return new String[] { "System.Nullable`1[[T -> System.UInt64, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0",

				"T -> System.Nullable`1[[T -> System.UInt64, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0" };
	}

	@Test
	public void shouldBeNullableType() {
		for (String identifier : shouldBeNullableTypeCases()) {
			TypeName name = CsTypeName.newTypeName(identifier);
			assertTrue(name.isNullableType());
		}
	}

	private String[] shouldNotBeNullableTypeCases() {
		return new String[] { "System.UInt64, mscorlib, 4.0.0.0", "A.Type, B, 5.6.7.8", "?" };
	}

	@Test
	public void shouldNotBeNullableType() {
		for (String identifier : shouldNotBeNullableTypeCases()) {
			TypeName name = CsTypeName.newTypeName(identifier);
			assertFalse(name.isNullableType());
		}
	}

	private String[] shouldBeStructTypeCases() {
		return new String[] { "System.Void, mscorlib, 4.0.0.0", "System.Int32, mscorlib, 4.0.0.0",
				"System.Nullable`1[[T -> System.Int32, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0",
				"s:My.Struct, A, 1.0.0.0" };
	}

	@Test
	public void shouldBeStructType() {
		for (String identifier : shouldBeStructTypeCases()) {
			TypeName name = CsTypeName.newTypeName(identifier);
			assertTrue(name.isStructType());
		}
	}

	private String[] shouldNotBeStructTypeCases() {
		return new String[] { "My.Type, C, 9.0.1.2,", "?" };
	}

	@Test
	public void shouldNotBeStructType() {
		for (String identifier : shouldNotBeStructTypeCases()) {
			TypeName name = CsTypeName.newTypeName(identifier);
			assertFalse(name.isStructType());
		}
	}

	@Test
	public void shouldBeEnumType() {
		TypeName name = CsTypeName.newTypeName("e:My.EnumType, E, 3.9.5.6");
		assertTrue(name.isEnumType());
	}

	@Test
	public void shouldNotBeEnumType() {
		TypeName name = CsTypeName.newTypeName("Non.Enum.Type, E, 7.9.3.5");
		assertFalse(name.isEnumType());
	}

	private String[] shouldBeValueTypeCases() {
		return new String[] { "System.Int32, mscorlib, 4.0.0.0", "s:My.Struct, A, 1.0.0.0",
				"System.Void, mscorlib, 4.0.0.0", "e:My.Enumtype, A, 3.4.5.6",
				"T -> System.Boolean, mscorlib, 4.0.0.0" };
	}

	@Test
	public void shouldBeValueType() {
		for (String identifier : shouldBeValueTypeCases()) {
			TypeName name = CsTypeName.newTypeName(identifier);
			assertTrue(name.isValueType());
		}
	}

	private String[] shouldNotBeValueTypeCases() {
		return new String[] { "A.ReferenceType, G, 7.8.9.0", "?" };
	}

	@Test
	public void shouldNotBeValueType() {
		for (String identifier : shouldNotBeValueTypeCases()) {
			TypeName name = CsTypeName.newTypeName(identifier);
			assertFalse(name.isValueType());
		}
	}

	private String[] shouldBeInterfaceTypeCases() {
		return new String[] { "i:Some.Interface, I, 6.5.4.3",
				// TODO: reenable - "TI -> i:MyInterface, Is, 3.8.67.0"
		};
	}

	@Test
	public void shouldBeInterfaceType() {
		for (String identifier : shouldBeInterfaceTypeCases()) {
			TypeName name = CsTypeName.newTypeName(identifier);
			boolean interfaceType = name.isInterfaceType();
			assertTrue(name.isInterfaceType());
		}
	}

	@Test
	public void shouldNotBeInterfaceType() {
		TypeName name = CsTypeName.newTypeName("Some.Class, C, 3.2.1.0");
		assertFalse(name.isInterfaceType());
	}

	private String[] shouldBeUnknownTypeCases() {
		return new String[] { "", "?", "T -> ?", "TP" };
	}

	@Test
	public void shouldBeUnknownType() {
		for (String identifier : shouldBeUnknownTypeCases()) {
			TypeName name = CsTypeName.newTypeName(identifier);
			boolean unknownType = name.isUnknownType();
			assertTrue(name.isUnknownType());
		}
	}

	@Test
	public void shouldNotBeUnknownType() {
		TypeName name = CsTypeName.newTypeName("Some.Known.Type, A, 1.2.3.4");
		assertFalse(name.isUnknownType());
	}

	private String[] shouldBeClassTypeCases() {
		return new String[] { "System.Object, mscorlib, 4.0.0.0", "Some.Class, K, 0.9.8.7",
				"T -> Another.Class, F, 4.7.55.6" };
	}

	@Test
	public void shouldBeClassType() {
		for (String identifier : shouldBeClassTypeCases()) {
			TypeName name = CsTypeName.newTypeName(identifier);
			assertTrue(name.isClassType());
		}
	}

	private String[] shouldNotBeClassTypeCases() {
		return new String[] { "System.Boolean, mscorlib, 4.0.0.0", "i:My.TerfaceType, Is, 2.4.6.3", "Foo[], A, 5.3.6.7",
				"?" };
	}

	@Test
	public void shouldNotBeClassType() {
		for (String identifier : shouldNotBeClassTypeCases()) {
			TypeName name = CsTypeName.newTypeName(identifier);
			assertFalse(name.isClassType());
		}
	}

	private String[] shouldBeReferenceTypeCases() {
		return new String[] { "My.Namespace.TypeName, A, 3.5.7.9", "i:My.Nterface, I, 5.3.7.1", "Vt[], A, 5.2.7.8" };
	}

	@Test
	public void shouldBeReferenceType() {
		for (String identifier : shouldBeReferenceTypeCases()) {
			TypeName name = CsTypeName.newTypeName(identifier);
			assertTrue(name.isReferenceType());
		}
	}

	private String[] shouldNotBeReferenceTypeCases() {
		return new String[] { "System.Int64, mscorlib, 4.0.0.0", "?" };
	}

	@Test
	public void shouldNotBeReferenceType() {
		for (String identifier : shouldNotBeReferenceTypeCases()) {
			TypeName name = CsTypeName.newTypeName(identifier);
			assertFalse(name.isReferenceType());
		}
	}

	private String[][] shouldDetermineFullNameCases() {
		return new String[][] { new String[] { "System.UInt16, mscorlib, 4.0.0.0", "System.UInt16" },
				new String[] { "e:Full.Enum.Type, E, 1.2.3.4", "Full.Enum.Type" },
				new String[] { "d:Full.Delegate.Type, E, 1.2.3.4", "Full.Delegate.Type" },
				new String[] { "i:Full.Interface.Type, E, 1.2.3.4", "Full.Interface.Type" },
				new String[] { "s:Full.Struct.Type, E, 1.2.3.4", "Full.Struct.Type" },
				new String[] { "System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0",
						"System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]]" },
				new String[] { "T -> Some.Arbitrary.Type, Assembly, 5.6.4.7", "Some.Arbitrary.Type" },
				new String[] { "Outer.Type+InnerType, As, 1.2.3.4", "Outer.Type+InnerType" }, new String[] { "?", "?" },
				new String[] { "Task`1[[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0",
						"Task`1[[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]]" } };
	}

	@Test
	public void shouldDetermineFullName() {
		String[][] cases = shouldDetermineFullNameCases();
		for (int i = 0; i < cases.length; i++) {
			String identifier = cases[i][0];
			String expectedFullName = cases[i][1];

			TypeName name = CsTypeName.newTypeName(identifier);
			assertEquals(expectedFullName, name.getFullName());
		}
	}

	private String[][] shouldDetermineNameCases() {
		return new String[][] { new String[] { "System.UInt16, mscorlib, 4.0.0.0", "UInt16" },
				new String[] { "e:Full.Enum.Type, E, 1.2.3.4", "Type" },
				new String[] { "d:Full.Delegate.Type, E, 1.2.3.4", "Type" },
				new String[] { "i:Full.Interface.Type, E, 1.2.3.4", "Type" },
				new String[] { "s:Full.Struct.Type, E, 1.2.3.4", "Type" },
				new String[] { "System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0", "Nullable" },
				new String[] { "T -> Some.Arbitrary.Type, Assembly, 5.6.4.7", "Type" },
				new String[] { "Outer.Type+InnerType, As, 1.2.3.4", "InnerType" }, new String[] { "?", "?" },
				new String[] { "Task`1[[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0",
						"Task" } };
	}

	@Test
	public void shouldDetermineName() {
		String[][] cases = shouldDetermineNameCases();
		for (int i = 0; i < cases.length; i++) {
			String identifier = cases[i][0];
			String expectedName = cases[i][1];

			TypeName name = CsTypeName.newTypeName(identifier);
			assertEquals(expectedName, name.getName());
		}

	}

	private String[][] shouldDetermineNamespaceCases() {
		return new String[][] { new String[] { "System.UInt16, mscorlib, 4.0.0.0", "System" },
				new String[] { "e:Full.Enum.Type, E, 1.2.3.4", "Full.Enum" },
				new String[] { "System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0", "System" },
				new String[] { "T -> Some.Arbitrary.Type, Assembly, 5.6.4.7", "Some.Arbitrary" },
				new String[] { "Outer.Type+InnerType, As, 1.2.3.4", "Outer" },
				new String[] { "GlobalType, A, 5.6.7.4", CsNamespaceName.getGlobalNamespace().getIdentifier() } };
	}

	@Test
	public void shouldDetermineNamespace() {
		String[][] cases = shouldDetermineNamespaceCases();
		for (int i = 0; i < cases.length; i++) {
			String identifier = cases[i][0];
			String expectedNamespace = cases[i][1];
			TypeName name = CsTypeName.newTypeName(identifier);
			assertEquals(expectedNamespace, name.getNamespace().getIdentifier());
		}
	}

	@Test
	public void unknownTypeShouldNotHaveNamespace() {
		TypeName name = CsTypeName.newTypeName("?");
		assertEquals(CsNamespaceName.getUnknownName(), name.getNamespace());
	}

	private String[][] shouldDetermineAssemblyCases() {
		return new String[][] { new String[] { "System.Object, mscorlib, 4.0.0.0", "mscorlib, 4.0.0.0" },
				new String[] { "i:Some.Interface, I, 1.2.3.4", "I, 1.2.3.4" },
				new String[] { "T -> Type.Parameter, A, 1.2.3.4", "A, 1.2.3.4" } };
	}

	@Test
	public void shouldDetermineAssembly() {
		String[][] cases = shouldDetermineAssemblyCases();
		for (int i = 0; i < cases.length; i++) {
			String identifier = cases[i][0];
			String expectedAssemblyIdentifier = cases[i][1];
			TypeName name = CsTypeName.newTypeName(identifier);
			assertEquals(expectedAssemblyIdentifier, name.getAssembly().getIdentifier());
		}
	}

	@Test
	public void unknownTypeShouldHaveUnknownAssembly() {
		TypeName name = CsTypeName.newTypeName("?");
		assertEquals(CsAssemblyName.getUnknownName(), name.getAssembly());
	}

	@Test
	public void shouldHaveTypeParameters() {
		final String stringIdentifier = "S -> System.String, mscore, 4.0.0.0";
		final String intIdentifier = "I -> System.Int32, mscore, 4.0.0.0";

		TypeName parameterizedTypeName = CsTypeName.newTypeName(
				"pack.age.MyType`2[[" + stringIdentifier + "],[" + intIdentifier + "]], " + TestAssemblyIdentifier);

		assertEquals(TestAssemblyIdentifier, parameterizedTypeName.getAssembly().getIdentifier());
		assertEquals("MyType", parameterizedTypeName.getName());
		assertTrue(parameterizedTypeName.isGenericEntity());
		assertTrue(parameterizedTypeName.hasTypeParameters());
		assertFalse(parameterizedTypeName.isArrayType());
		assertEquals(2, parameterizedTypeName.getTypeParameters().size());
		assertEquals(stringIdentifier, parameterizedTypeName.getTypeParameters().get(0).getIdentifier());
		assertEquals(intIdentifier, parameterizedTypeName.getTypeParameters().get(1).getIdentifier());
	}

	@Test
	public void shouldHaveUninstantiatedTypeParameters() {
		TypeName typeName = CsTypeName.newTypeName("OuterType`1+InnerType, Assembly, 1.2.3.4");

		assertTrue(typeName.isGenericEntity());
		assertFalse(typeName.hasTypeParameters());
		assertEquals("OuterType`1+InnerType", typeName.getFullName());
		assertEquals("OuterType`1", typeName.getDeclaringType().getFullName());
	}

	@Test
	public void shouldBeTopLevelType() {
		TypeName typeName = CsTypeName.newTypeName("this.is.a.top.level.ValueType, " + TestAssemblyIdentifier);

		assertFalse(typeName.isNestedType());
		assertNull(typeName.getDeclaringType());
	}

	private String[][] shouldBeNestedTypeCases() {
		return new String[][] { new String[] { "a.p.T+N", "a.p.T" }, new String[] { "N.O+M+I", "N.O+M" } };
	}

	@Test
	public void shouldBeNestedType() {
		String[][] cases = shouldBeNestedTypeCases();
		for (int i = 0; i < cases.length; i++) {
			String nestedTypeFullName = cases[i][0];
			String expectedDeclaringTypeFullName = cases[i][1];
			TypeName expected = CsTypeName.newTypeName(expectedDeclaringTypeFullName + "," + TestAssemblyIdentifier);

			TypeName nestedTypeName = CsTypeName.newTypeName(nestedTypeFullName + ", " + TestAssemblyIdentifier);
			TypeName actual = nestedTypeName.getDeclaringType();

			assertTrue(nestedTypeName.isNestedType());
			assertEquals(expected, actual);
		}
	}

	@Test
	public void shouldBeNestedTypeInParameterizedType() {
		final String paramIdentifier = "T -> p.OP, A, 1.0.0.0";

		TypeName nestedTypeName = CsTypeName
				.newTypeName("p.O`1+I[[" + paramIdentifier + "]], " + TestAssemblyIdentifier);

		assertTrue(nestedTypeName.isNestedType());
		assertTrue(nestedTypeName.hasTypeParameters());
		assertEquals(1, nestedTypeName.getTypeParameters().size());
		assertEquals(paramIdentifier, nestedTypeName.getTypeParameters().get(0).getIdentifier());

		TypeName declaringType = nestedTypeName.getDeclaringType();
		assertFalse(declaringType.isNestedType());
		assertTrue(declaringType.hasTypeParameters());
		assertEquals(1, declaringType.getTypeParameters().size());
		assertEquals(paramIdentifier, declaringType.getTypeParameters().get(0).getIdentifier());
	}

	@Test
	public void shouldBeNestedParameterizedTypeInParameterizedType() {
		final String p1Identifier = "A -> OP, Z, 1.0.0.0";
		final String p2Identifier = "B -> IP1, A, 1.0.0.0";
		final String p3Identifier = "C -> IP2, B, 5.1.0.9";

		TypeName nestedTypeName = CsTypeName.newTypeName("p.O`1+I`2[[" + p1Identifier + "],[" + p2Identifier + "],["
				+ p3Identifier + "]], " + TestAssemblyIdentifier);

		assertTrue(nestedTypeName.isNestedType());
		assertTrue(nestedTypeName.hasTypeParameters());
		assertEquals(3, nestedTypeName.getTypeParameters().size());
		assertEquals(p1Identifier, nestedTypeName.getTypeParameters().get(0).getIdentifier());
		assertEquals(p2Identifier, nestedTypeName.getTypeParameters().get(1).getIdentifier());
		assertEquals(p3Identifier, nestedTypeName.getTypeParameters().get(2).getIdentifier());

		TypeName declaringType = nestedTypeName.getDeclaringType();
		assertTrue(declaringType.hasTypeParameters());
		assertEquals(1, declaringType.getTypeParameters().size());
		assertEquals(p1Identifier, declaringType.getTypeParameters().get(0).getIdentifier());
	}

	@Test
	public void shouldBeParameterizedTypeWithParameterizedTypeParameter() {
		final String paramParamIdentifier = "T -> yan.PTPT, Z, 1.0.0.0";
		final String paramIdentifier = "on.PT`1[[" + paramParamIdentifier + "]], Y, 1.0.0.0";

		TypeName typeName = CsTypeName.newTypeName("n.OT`1[[" + paramIdentifier + "]], " + TestAssemblyIdentifier);

		assertTrue(typeName.hasTypeParameters());
		assertEquals(1, typeName.getTypeParameters().size());

		TypeName typeParameter = typeName.getTypeParameters().get(0);
		assertEquals(paramIdentifier, typeParameter.getIdentifier());
		assertTrue(typeParameter.hasTypeParameters());
		assertEquals(1, typeParameter.getTypeParameters().size());
		assertEquals(paramParamIdentifier, typeParameter.getTypeParameters().get(0).getIdentifier());
	}

	@Test
	public void shouldBeDeeplyNestedTypeWithLotsOfTypeParameters() {
		TypeName typeName = CsTypeName
				.newTypeName("p.O`1+M`1+I`1[[T -> p.P1, A, 1.0.0.0],[U -> p.P2, A, 1.0.0.0],[V -> p.P3, A, 1.0.0.0]], "
						+ TestAssemblyIdentifier);
		assertEquals("p.O`1+M`1[[T -> p.P1, A, 1.0.0.0],[U -> p.P2, A, 1.0.0.0]], " + TestAssemblyIdentifier,
				typeName.getDeclaringType().getIdentifier());
	}

	private String[] shouldBeTypeParameterCases() {
		return new String[] { "TR -> System.Int32, mscorelib, 4.0.0.0", "R -> ?", "TParam" };
	}

	@Test
	public void shouldBeTypeParameter() {
		for (String identifier : shouldBeTypeParameterCases()) {
			TypeName name = CsTypeName.newTypeName(identifier);
			assertTrue(name.isTypeParameter());
		}
	}

	private String[][] shouldExtractTypeParameterShortNameCases() {
		return new String[][] { new String[] { "TR -> System.Int32, mscorelib, 4.0.0.0", "TR" },
				new String[] { "R -> ?", "R" }, new String[] { "TParam", "TParam" } };
	}

	@Test
	public void shouldExtractTypeParameterShortName() {
		String[][] cases = shouldExtractTypeParameterShortNameCases();
		for (int i = 0; i < cases.length; i++) {
			String identifier = cases[i][0];
			String expectedShortName = cases[i][1];
			TypeName name = CsTypeName.newTypeName(identifier);
			assertEquals(expectedShortName, name.getTypeParameterShortName());
		}
	}

	@Test
	public void shouldNotHaveTypeParameterShortName() {
		TypeName name = CsTypeName.newTypeName("Non.Parameter.Type, As, 1.2.3.4");
		assertNull(name.getTypeParameterShortName());
	}

	@Test
	public void shouldNotHaveTypeParameterType() {
		TypeName name = CsTypeName.newTypeName("Non.Parameter.Type, As, 1.2.3.4");
		assertNull(name.getTypeParameterType());
	}

	private String[] shouldBeNotTypeParameterCases() {
		return new String[] { "SomeType`1[[T -> Foo, Bar, 1.2.3.4]], A, 1.2.3.4", "System.Object, mscorlib, 4.0.0.0" };
	}

	@Test
	public void shouldBeNotTypeParameter() {
		for (String identifier : shouldBeNotTypeParameterCases()) {
			TypeName genericType = CsTypeName.newTypeName(identifier);

			assertFalse(genericType.isTypeParameter());
		}
	}
}
