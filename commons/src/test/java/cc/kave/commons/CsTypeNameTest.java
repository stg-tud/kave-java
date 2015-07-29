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
//
// package cc.kave.commons;
//
// import static org.junit.Assert.assertEquals;
// import static org.junit.Assert.assertFalse;
// import static org.junit.Assert.assertNull;
// import static org.junit.Assert.assertTrue;
//
// import java.util.function.Predicate;
//
// import org.junit.Test;
//
// import cc.kave.commons.model.names.TypeName;
// import cc.kave.commons.model.names.csharp.CsAssemblyName;
// import cc.kave.commons.model.names.csharp.CsNamespaceName;
// import cc.kave.commons.model.names.csharp.CsTypeName;
// import junitparams.Parameters;
//
// public class CsTypeNameTest {
//
// private final String TestAssemblyIdentifier = "a, 1.0.0.0";
//
// @Test
// public void shouldImplementIsUnknown() {
// assertTrue(CsTypeName.getUnknownName().isUnknown());
// }
//
// @Test
// public void shouldGetNamespaceFromTypeInGlobalNamespace() {
// TypeName name = CsTypeName.newTypeName("MyType, Assembly, 1.2.3.4");
// assertEquals(CsNamespaceName.getGlobalNamespace(), name.getNamespace());
// }
//
// @Test
// public void shouldBeVoidType() {
// TypeName name = CsTypeName.newTypeName("System.Void, mscorlib, 4.0.0.0");
// assertTrue(name.isVoidType());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldNotBeVoidTypeCases() {
// return new Object[] { new Object[] { "System.Boolean, mscorlib, 4.0.0.0" },
// new Object[] { "T -> System.Int32, mscorlib, 4.0.0.0" } };
// }
//
// @Test
// @Parameters(method = "shouldNotBeVoidTypeCases")
// public void shouldNotBeVoidType(String identifier) {
// TypeName name = CsTypeName.newTypeName(identifier);
// boolean voidType = name.isVoidType();
// assertFalse(voidType);
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldBeSimpleTypeCases() {
// return new Object[] { new Object[] { "System.Boolean, mscorlib, 4.0.0.0" },
// new Object[] { "System.Decimal, mscorlib, 4.0.0.0" },
// new Object[] { "System.SByte, mscorlib, 4.0.0.0" }, new Object[] {
/// "System.Int16, mscorlib, 4.0.0.0" },
// new Object[] { "System.UInt16, mscorlib, 4.0.0.0" }, new Object[] {
/// "System.Int32, mscorlib, 4.0.0.0" },
// new Object[] { "System.UInt32, mscorlib, 4.0.0.0" },
// new Object[] { "System.Int64,, mscorlib, 4.0.0.0" },
// new Object[] { "System.UInt64, mscorlib, 4.0.0.0" }, new Object[] {
/// "System.Char, mscorlib, 4.0.0.0" },
// new Object[] { "System.Single, mscorlib, 4.0.0.0" },
// new Object[] { "System.Double, mscorlib, 4.0.0.0" },
// new Object[] { "T -> System.Int32, mscorlib, 4.0.0.0" } };
// }
//
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
// public static void assertThat(String identifer, Predicate<TypeName> pred) {
// TypeName name = CsTypeName.newTypeName(identifer);
// assertTrue(pred.test(name));
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldNotBeSimpleTypeCases() {
// return new Object[] { new Object[] { "System.Void, mscorlib, 4.0.0.0" },
// new Object[] { "My.Custom.Type, A, 1.2.3.4" }, new Object[] { "?" } };
// }
//
// @Test
// @Parameters(method = "shouldNotBeSimpleTypeCases")
// public void shouldNotBeSimpleType(String identifier) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertFalse(name.isSimpleType());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldBeNullableTypeCases() {
// return new Object[] {
// new Object[] { "System.Nullable`1[[T -> System.UInt64, mscorlib, 4.0.0.0]],
/// mscorlib, 4.0.0.0" },
// new Object[] {
// "T -> System.Nullable`1[[T -> System.UInt64, mscorlib, 4.0.0.0]], mscorlib,
/// 4.0.0.0" }, };
// }
//
// @Test
// @Parameters(method = "shouldNotBeSimpleTypeCases")
// public void shouldBeNullableType(String identifier) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertTrue(name.isNullableType());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldNotBeNullableTypeCases() {
// return new Object[] { new Object[] { "System.UInt64, mscorlib, 4.0.0.0" },
// new Object[] { "A.Type, B, 5.6.7.8" }, new Object[] { "?" } };
// }
//
// @Test
// @Parameters(method = "shouldNotBeNullableTypeCases")
// public void shouldNotBeNullableType(String identifier) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertFalse(name.isNullableType());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldBeStructTypeCases() {
// return new Object[] { new Object[] { "System.Void, mscorlib, 4.0.0.0" },
// new Object[] { "System.Int32, mscorlib, 4.0.0.0" },
// new Object[] { "System.Nullable`1[[T -> System.Int32, mscorlib, 4.0.0.0]],
/// mscorlib, 4.0.0.0" },
// new Object[] { "s:My.Struct, A, 1.0.0.0" } };
// }
//
// @Test
// @Parameters(method = "shouldBeStructTypeCases")
// public void shouldBeStructType(String identifier) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertTrue(name.isStructType());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldNotBeStructTypeCases() {
// return new Object[] { new Object[] { "System.UInt64, mscorlib, 4.0.0.0" },
// new Object[] { "A.Type, B, 5.6.7.8" }, new Object[] { "?" } };
// }
//
// @Test
// @Parameters(method = "shouldNotBeStructTypeCases")
// public void shouldNotBeStructType(String identifier) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertFalse(name.isStructType());
// }
//
// @Test
// public void shouldBeEnumType() {
// TypeName name = CsTypeName.newTypeName("e:My.EnumType, E, 3.9.5.6");
// assertTrue(name.isEnumType());
// }
//
// @Test
// public void shouldNotBeEnumType() {
// TypeName name = CsTypeName.newTypeName("Non.Enum.Type, E, 7.9.3.5");
// assertFalse(name.isEnumType());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldBeValueTypeCases() {
// return new Object[] { new Object[] { "System.Int32, mscorlib, 4.0.0.0" },
// new Object[] { "s:My.Struct, A, 1.0.0.0" }, new Object[] { "System.Void,
/// mscorlib, 4.0.0.0" },
// new Object[] { "e:My.Enumtype, A, 3.4.5.6" },
// new Object[] { "T -> System.Boolean, mscorlib, 4.0.0.0" } };
// }
//
// @Test
// @Parameters(method = "shouldBeValueTypeCases")
// public void shouldBeValueType(String identifier) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertTrue(name.isValueType());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldNotBeValueTypeCases() {
// return new Object[] { new Object[] { "A.ReferenceType, G, 7.8.9.0" }, new
/// Object[] { "?" } };
// }
//
// @Test
// @Parameters(method = "shouldNotBeValueTypeCases")
// public void shouldNotBeValueType(String identifier) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertFalse(name.isValueType());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldBeInterfaceTypeCases() {
// return new Object[] { new Object[] { "i:Some.Interface, I, 6.5.4.3" },
// new Object[] { "TI -> i:MyInterface, Is, 3.8.67.0" } };
// }
//
// @Test
// @Parameters(method = "shouldBeInterfaceTypeCases")
// public void shouldBeInterfaceType(String identifier) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertTrue(name.isInterfaceType());
// }
//
// @Test
// public void shouldNotBeInterfaceType() {
// TypeName name = CsTypeName.newTypeName("Some.Class, C, 3.2.1.0");
// assertFalse(name.isInterfaceType());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldBeUnknownTypeCases() {
// return new Object[] { new Object[] { "" }, new Object[] { "?" }, new Object[]
/// { "T -> ?" },
// new Object[] { "TP" } };
// }
//
// @Test
// @Parameters(method = "shouldBeUnknownTypeCases")
// public void shouldBeUnknownType(String identifier) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertTrue(name.isUnknownType());
// }
//
// @Test
// public void shouldNotBeUnknownType() {
// TypeName name = CsTypeName.newTypeName("Some.Known.Type, A, 1.2.3.4");
// assertFalse(name.isUnknownType());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldBeClassTypeCases() {
// return new Object[] { new Object[] { "System.Object, mscorlib, 4.0.0.0" },
// new Object[] { "Some.Class, K, 0.9.8.7" }, new Object[] { "T ->
/// Another.Class, F, 4.7.55.6" } };
// }
//
// @Test
// @Parameters(method = "shouldBeClassTypeCases")
// public void shouldBeClassType(String identifier) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertTrue(name.isClassType());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldNotBeClassTypeCases() {
// return new Object[] { new Object[] { "System.Boolean, mscorlib, 4.0.0.0" },
// new Object[] { "i:My.TerfaceType, Is, 2.4.6.3" }, new Object[] { "Foo[], A,
/// 5.3.6.7" },
// new Object[] { "?" } };
// }
//
// @Test
// @Parameters(method = "shouldNotBeClassTypeCases")
// public void shouldNotBeClassType(String identifier) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertFalse(name.isClassType());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldBeReferenceTypeCases() {
// return new Object[] { new Object[] { "My.Namespace.TypeName, A, 3.5.7.9" },
// new Object[] { "i:My.Nterface, I, 5.3.7.1" }, new Object[] { "Vt[], A,
/// 5.2.7.8" } };
// }
//
// @Test
// @Parameters(method = "shouldBeReferenceTypeCases")
// public void shouldBeReferenceType(String identifier) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertTrue(name.isReferenceType());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldNotBeReferenceTypeCases() {
// return new Object[] { new Object[] { "System.Int64, mscorlib, 4.0.0.0" }, new
/// Object[] { "?" } };
// }
//
// @Test
// @Parameters(method = "shouldNotBeReferenceTypeCases")
// public void shouldNotBeReferenceType(String identifier) {
// TypeName uut = CsTypeName.newTypeName(identifier);
// assertFalse(uut.isReferenceType());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldDetermineFullNameCases() {
// return new Object[] { new Object[] { "System.UInt16, mscorlib, 4.0.0.0",
/// "System.UInt16" },
// new Object[] { "e:Full.Enum.Type, E, 1.2.3.4", "Full.Enum.Type" },
// new Object[] { "d:Full.Delegate.Type, E, 1.2.3.4", "Full.Delegate.Type" },
// new Object[] { "i:Full.Interface.Type, E, 1.2.3.4", "Full.Interface.Type" },
// new Object[] { "s:Full.Struct.Type, E, 1.2.3.4", "Full.Struct.Type" },
// new Object[] { "System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]],
/// mscorlib, 4.0.0.0",
// "System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]]" },
// new Object[] { "T -> Some.Arbitrary.Type, Assembly, 5.6.4.7",
/// "Some.Arbitrary.Type" },
// new Object[] { "Outer.Type+InnerType, As, 1.2.3.4", "Outer.Type+InnerType" },
/// new Object[] { "?", "?" },
// new Object[] { "Task`1[[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]],
/// mscorlib, 4.0.0.0",
// "Task`1[[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]]" } };
// }
//
// @Test
// @Parameters(method = "shouldDetermineFullNameCases")
// public void shouldDetermineFullName(String identifier, String
/// expectedFullName) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertEquals(expectedFullName, name.getFullName());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldDetermineNameCases() {
// return new Object[] { new Object[] { "System.UInt16, mscorlib, 4.0.0.0",
/// "UInt16" },
// new Object[] { "e:Full.Enum.Type, E, 1.2.3.4", "Type" },
// new Object[] { "d:Full.Delegate.Type, E, 1.2.3.4", "Type" },
// new Object[] { "i:Full.Interface.Type, E, 1.2.3.4", "Type" },
// new Object[] { "s:Full.Struct.Type, E, 1.2.3.4", "Type" },
// new Object[] { "System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]],
/// mscorlib, 4.0.0.0", "Nullable" },
// new Object[] { "T -> Some.Arbitrary.Type, Assembly, 5.6.4.7", "Type" },
// new Object[] { "Outer.Type+InnerType, As, 1.2.3.4", "InnerType" }, new
/// Object[] { "?", "?" },
// new Object[] { "Task`1[[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]],
/// mscorlib, 4.0.0.0",
// "Task" } };
// }
//
// @Test
// @Parameters(method = "shouldDetermineNameCases")
// public void shouldDetermineName(String identifier, String expectedName) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertEquals(expectedName, name.getName());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldDetermineNamespaceCases() {
// return new Object[] { new Object[] { "System.UInt16, mscorlib, 4.0.0.0",
/// "System" },
// new Object[] { "e:Full.Enum.Type, E, 1.2.3.4", "Full.Enum" },
// new Object[] { "System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]],
/// mscorlib, 4.0.0.0", "System" },
// new Object[] { "T -> Some.Arbitrary.Type, Assembly, 5.6.4.7" },
// new Object[] { "Outer.Type+InnerType, As, 1.2.3.4", "Outer" },
// new Object[] { "GlobalType, A, 5.6.7.4", CsNamespaceName.getGlobalNamespace()
/// } };
// }
//
// @Test
// @Parameters(method = "shouldDetermineNameCases")
// public void shouldDetermineNamespace(String identifier, String
/// expectedNamespace) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertEquals(expectedNamespace, name.getNamespace().getIdentifier());
// }
//
// @Test
// public void unknownTypeShouldNotHaveNamespace() {
// TypeName name = CsTypeName.newTypeName("?");
// assertEquals(CsNamespaceName.getUnknownName(), name.getNamespace());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldDetermineAssemblyCases() {
// return new Object[] { new Object[] { "System.Object, mscorlib, 4.0.0.0",
/// "mscorlib, 4.0.0.0" },
// new Object[] { "i:Some.Interface, I, 1.2.3.4", "I, 1.2.3.4" },
// new Object[] { "T -> Type.Parameter, A, 1.2.3.4", "A, 1.2.3.4" } };
// }
//
// @Test
// @Parameters(method = "shouldDetermineAssemblyCases")
// public void shouldDetermineAssembly(String identifier, String
/// expectedAssemblyIdentifier) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertEquals(expectedAssemblyIdentifier, name.getAssembly().getIdentifier());
// }
//
// @Test
// public void unknownTypeShouldHaveUnknownAssembly() {
// TypeName uut = CsTypeName.newTypeName("?");
// assertEquals(CsAssemblyName.getUnknownName(), uut.getAssembly());
// }
//
// @Test
// public void shouldHaveTypeParameters() {
// final String stringIdentifier = "S -> System.String, mscore, 4.0.0.0";
// final String intIdentifier = "I -> System.Int32, mscore, 4.0.0.0";
//
// TypeName parameterizedTypeName = CsTypeName.newTypeName(
// "pack.age.MyType`2[[" + stringIdentifier + "],[" + intIdentifier + "]], " +
/// TestAssemblyIdentifier);
//
// assertEquals(TestAssemblyIdentifier,
/// parameterizedTypeName.getAssembly().getIdentifier());
// assertEquals("MyType", parameterizedTypeName.getName());
// assertTrue(parameterizedTypeName.isGenericEntity());
// assertTrue(parameterizedTypeName.hasTypeParameters());
// assertFalse(parameterizedTypeName.isArrayType());
// assertEquals(2, parameterizedTypeName.getTypeParameters().size());
// assertEquals(stringIdentifier,
/// parameterizedTypeName.getTypeParameters().get(0).getIdentifier());
// assertEquals(intIdentifier,
/// parameterizedTypeName.getTypeParameters().get(1).getIdentifier());
// }
//
// @Test
// public void shouldHaveUninstantiatedTypeParameters() {
// TypeName typeName = CsTypeName.newTypeName("OuterType`1+InnerType, Assembly,
/// 1.2.3.4");
//
// assertTrue(typeName.isGenericEntity());
// assertFalse(typeName.hasTypeParameters());
// assertEquals("OuterType`1+InnerType", typeName.getFullName());
// assertEquals("OuterType`1", typeName.getDeclaringType().getFullName());
// }
//
// @Test
// public void shouldBeTopLevelType() {
// TypeName typeName = CsTypeName.newTypeName("this.is.a.top.level.ValueType, "
/// + TestAssemblyIdentifier);
//
// assertFalse(typeName.isNestedType());
// assertNull(typeName.getDeclaringType());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldBeNestedTypeCases() {
// return new Object[] { new Object[] { "a.p.T+N", "a.p.T" }, new Object[] {
/// "N.O+M+I", "N.O+M" } };
// }
//
// @Test
// @Parameters(method = "shouldBeNestedTypeCases")
// public void shouldBeNestedType(String nestedTypeFullName, String
/// expectedDeclaringTypeFullName) {
// TypeName expected = CsTypeName.newTypeName(expectedDeclaringTypeFullName + ",
/// " + TestAssemblyIdentifier);
//
// TypeName nestedTypeName = CsTypeName.newTypeName(nestedTypeFullName + ", " +
/// TestAssemblyIdentifier);
// TypeName actual = nestedTypeName.getDeclaringType();
//
// assertTrue(nestedTypeName.isNestedType());
// assertEquals(expected, actual);
// }
//
// @Test
// public void shouldBeNestedTypeInParameterizedType() {
// final String paramIdentifier = "T -> p.OP, A, 1.0.0.0";
//
// TypeName nestedTypeName = CsTypeName
// .newTypeName("p.O`1+I[[" + paramIdentifier + "]], " +
/// TestAssemblyIdentifier);
//
// assertTrue(nestedTypeName.isNestedType());
// assertTrue(nestedTypeName.hasTypeParameters());
// assertEquals(1, nestedTypeName.getTypeParameters().size());
// assertEquals(paramIdentifier,
/// nestedTypeName.getTypeParameters().get(0).getIdentifier());
//
// TypeName declaringType = nestedTypeName.getDeclaringType();
// assertFalse(declaringType.isNestedType());
// assertTrue(declaringType.hasTypeParameters());
// assertEquals(1, declaringType.getTypeParameters().size());
// assertEquals(paramIdentifier,
/// declaringType.getTypeParameters().get(0).getIdentifier());
// }
//
// @Test
// public void shouldBeNestedParameterizedTypeInParameterizedType() {
// final String p1Identifier = "A -> OP, Z, 1.0.0.0";
// final String p2Identifier = "B -> IP1, A, 1.0.0.0";
// final String p3Identifier = "C -> IP2, B, 5.1.0.9";
//
// TypeName nestedTypeName = CsTypeName.newTypeName("p.O`1+I`2[[" + p1Identifier
/// + "],[" + p2Identifier + "],["
// + p3Identifier + "]], " + TestAssemblyIdentifier);
//
// assertTrue(nestedTypeName.isNestedType());
// assertTrue(nestedTypeName.hasTypeParameters());
// assertEquals(3, nestedTypeName.getTypeParameters().size());
// assertEquals(p1Identifier,
/// nestedTypeName.getTypeParameters().get(0).getIdentifier());
// assertEquals(p2Identifier,
/// nestedTypeName.getTypeParameters().get(1).getIdentifier());
// assertEquals(p3Identifier,
/// nestedTypeName.getTypeParameters().get(2).getIdentifier());
//
// TypeName declaringType = nestedTypeName.getDeclaringType();
// assertTrue(declaringType.hasTypeParameters());
// assertEquals(1, declaringType.getTypeParameters().size());
// assertEquals(p1Identifier,
/// declaringType.getTypeParameters().get(0).getIdentifier());
// }
//
// @Test
// public void shouldBeParameterizedTypeWithParameterizedTypeParameter() {
// final String paramParamIdentifier = "T -> yan.PTPT, Z, 1.0.0.0";
// final String paramIdentifier = "on.PT`1[[" + paramParamIdentifier + "]], Y,
/// 1.0.0.0";
//
// TypeName typeName = CsTypeName.newTypeName("n.OT`1[[" + paramIdentifier +
/// "]], " + TestAssemblyIdentifier);
//
// assertTrue(typeName.hasTypeParameters());
// assertEquals(1, typeName.getTypeParameters().size());
//
// TypeName typeParameter = typeName.getTypeParameters().get(0);
// assertEquals(paramIdentifier, typeParameter.getIdentifier());
// assertTrue(typeParameter.hasTypeParameters());
// assertEquals(1, typeParameter.getTypeParameters().size());
// assertEquals(paramParamIdentifier,
/// typeParameter.getTypeParameters().get(0).getIdentifier());
// }
//
// @Test
// public void shouldBeDeeplyNestedTypeWithLotsOfTypeParameters() {
// TypeName typeName = CsTypeName
// .newTypeName("p.O`1+M`1+I`1[[T -> p.P1, A, 1.0.0.0],[U -> p.P2, A,
/// 1.0.0.0],[V -> p.P3, A, 1.0.0.0]], "
// + TestAssemblyIdentifier);
// assertEquals("p.O`1+M`1[[T -> p.P1, A, 1.0.0.0],[U -> p.P2, A, 1.0.0.0]], " +
/// TestAssemblyIdentifier,
// typeName.getDeclaringType().getIdentifier());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldBeTypeParameterCases() {
// return new Object[] { new Object[] { "TR -> System.Int32, mscorelib, 4.0.0.0"
/// }, new Object[] { "R -> ?" },
// new Object[] { "TParam" } };
// }
//
// @Test
// @Parameters(method = "shouldBeTypeParameterCases")
// public void shouldBeTypeParameter(String identifier) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertTrue(name.isTypeParameter());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldExtractTypeParameterShortNameCases() {
// return new Object[] { new Object[] { "TR -> System.Int32, mscorelib,
/// 4.0.0.0", "TR" },
// new Object[] { "R -> ?", "R" }, new Object[] { "TParam", "TParam" } };
// }
//
// @Test
// @Parameters(method = "shouldExtractTypeParameterShortNameCases")
// public void shouldExtractTypeParameterShortName(String identifier, String
/// expectedShortName) {
// TypeName name = CsTypeName.newTypeName(identifier);
// assertEquals(expectedShortName, name.TypeParameterShortName());
// }
//
// @Test
// public void shouldNotHaveTypeParameterShortName() {
// TypeName name = CsTypeName.newTypeName("Non.Parameter.Type, As, 1.2.3.4");
// assertNull(name.TypeParameterShortName());
// }
//
// @Test
// public void shouldNotHaveTypeParameterType() {
// TypeName name = CsTypeName.newTypeName("Non.Parameter.Type, As, 1.2.3.4");
// assertNull(name.TypeParameterType());
// }
//
// @SuppressWarnings("unused")
// private Object[] shouldBeNotTypeParameterCases() {
// return new Object[] { new Object[] { "SomeType`1[[T -> Foo, Bar, 1.2.3.4]],
/// A, 1.2.3.4" },
// new Object[] { "System.Object, mscorlib, 4.0.0.0" } };
// }
//
// @Test
// @Parameters(method = "shouldBeNotTypeParameterCases")
// public void shouldBeNotTypeParameter(String identifier) {
// TypeName genericType = CsTypeName.newTypeName(identifier);
//
// assertFalse(genericType.isTypeParameter());
// }
// }
