/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.model.naming.impl.v0.types;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyName;
import cc.kave.commons.model.naming.impl.v0.types.organization.NamespaceName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;
import cc.recommenders.exceptions.ValidationException;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

@RunWith(JUnitParamsRunner.class)
public class TypeNameTest {
	private final static String TestAssemblyIdentifier = "a, 1.0.0.0";

	private ITypeName sut;

	private static ITypeName T(String id) {
		return TypeUtils.createTypeName(id);
	}

	@Test
	public void DefaultValues() {
		sut = new TypeName();
		assertEquals("?", sut.getFullName());
		assertEquals("?", sut.getName());
		assertEquals(new NamespaceName(), sut.getNamespace());
		assertEquals(new AssemblyName(), sut.getAssembly());

		assertTrue(sut.isUnknown());
		assertFalse(sut.isHashed());

		assertFalse(sut.isArray());
		assertFalse(sut.isClassType());
		assertFalse(sut.isDelegateType());
		assertFalse(sut.isEnumType());
		assertFalse(sut.isInterfaceType());
		assertFalse(sut.isNestedType());
		assertFalse(sut.isNullableType());
		assertFalse(sut.isPredefined());
		assertFalse(sut.isReferenceType());
		assertFalse(sut.isSimpleType());
		assertFalse(sut.isStructType());
		assertFalse(sut.isTypeParameter());
		assertFalse(sut.isValueType());
		assertFalse(sut.isVoidType());

		assertNull(sut.getDeclaringType());
		assertFalse(sut.hasTypeParameters());
		assertEquals(Lists.newLinkedList(), sut.getTypeParameters());
	}

	@Test
	public void ShouldCacheFullName() {
		sut = new TypeName("T,P");
		assertSame(sut.getFullName(), sut.getFullName());
	}

	public String[][] provideValidTypes() {
		// use only ',P' Assemblies
		Set<String[]> ids = Sets.newHashSet();
		ids.add(new String[] { "i:n.T1`1[[T2 -> p:int]], P", "P", "n", "n.T1`1[[T2 -> p:int]]", "T1" });
		ids.add(new String[] { "T,P", "P", "", "T", "T" });
		ids.add(new String[] { "n.T,P", "P", "n", "n.T", "T" });
		ids.add(new String[] { "s:T,P", "P", "", "T", "T" });
		ids.add(new String[] { "s:n.T,P", "P", "n", "n.T", "T" });
		ids.add(new String[] { "e:T,P", "P", "", "T", "T" });
		ids.add(new String[] { "e:n.T,P", "P", "n", "n.T", "T" });
		ids.add(new String[] { "i:T,P", "P", "", "T", "T" });
		ids.add(new String[] { "i:n.T,P", "P", "n", "n.T", "T" });
		ids.add(new String[] { "n.T1`1[[T2]], P", "P", "n", "n.T1`1[[T2]]", "T1" });
		ids.add(new String[] { "n.T1+T2, P", "P", "n", "n.T1+T2", "T2" });
		ids.add(new String[] { "n.T1`1[[T2]]+T3`1[[T4]], P", "P", "n", "n.T1`1[[T2]]+T3`1[[T4]]", "T3" });
		ids.add(new String[] { "n.C+N`1[[T]],P", "P", "n", "n.C+N`1[[T]]", "N" });
		ids.add(new String[] { "n.C`1[[T]]+N,P", "P", "n", "n.C`1[[T]]+N", "N" });
		ids.add(new String[] { "n.C`1[[T]]+N`1[[T]],P", "P", "n", "n.C`1[[T]]+N`1[[T]]", "N" });
		return ids.toArray(new String[0][0]);
	}

	@Test
	public void ShouldHandleNullableType() {
		sut = T("s:System.Nullable`1[[T -> p:sbyte]], mscorlib, 4.0.0.0");

		assertEquals(new AssemblyName("mscorlib, 4.0.0.0"), sut.getAssembly());
		assertEquals(new NamespaceName("System"), sut.getNamespace());
		assertEquals("System.Nullable`1[[T -> p:sbyte]]", sut.getFullName());
		assertEquals("Nullable", sut.getName());
		assertTrue(sut.isNullableType());
	}

	@Test
	@Parameters(method = "provideValidTypes")
	public void ShouldRecognizeRegularTypes(String typeId, String assemblyId, String namespaceId, String fullName,
			String name) {
		assertFalse(TypeUtils.isUnknownTypeIdentifier(typeId));
		assertFalse(ArrayTypeName.isArrayTypeNameIdentifier(typeId));
		assertFalse(DelegateTypeName.isDelegateTypeNameIdentifier(typeId));
		assertFalse(TypeParameterName.isTypeParameterNameIdentifier(typeId));
		assertFalse(PredefinedTypeName.IsPredefinedTypeNameIdentifier(typeId));
	}

	@Test
	@Parameters(method = "provideValidTypes")
	public void ShouldParseAssembly(String typeId, String assemblyId, String namespaceId, String fullName,
			String name) {
		String onlyTypeId = typeId.substring(0, typeId.lastIndexOf(','));
		for (String asmId : new String[] { "P", "A, 1.2.3.4" }) {
			typeId = String.format("%s, %s", onlyTypeId, asmId);
			assertEquals(new AssemblyName(asmId), T(typeId).getAssembly());
		}
	}

	@Test
	@Parameters(method = "provideValidTypes")
	public void ShouldParseNamespace(String typeId, String assemblyId, String namespaceId, String fullName,
			String name) {
		assertEquals(new NamespaceName(namespaceId), T(typeId).getNamespace());
	}

	@Test
	@Parameters(method = "provideValidTypes")
	public void ShouldParseFullName(String typeId, String assemblyId, String namespaceId, String fullName,
			String name) {
		sut = T(typeId);
		assertEquals(fullName, sut.getFullName());
	}

	@Test
	@Parameters(method = "provideValidTypes")
	public void ShouldParseName(String typeId, String assemblyId, String namespaceId, String fullName, String name) {
		assertEquals(name, T(typeId).getName());
	}

	@Test
	@Parameters(method = "provideValidTypes")
	public void ShouldParseAllChecker(String typeId, String assemblyId, String namespaceId, String fullName,
			String name) {
		boolean isClassType = !(typeId.startsWith("e:") || typeId.startsWith("i:") || typeId.startsWith("s:"));
		boolean isReferenceType = !(typeId.startsWith("e:") || typeId.startsWith("s:"));
		boolean isValueType = typeId.startsWith("e:") || typeId.startsWith("s:");

		sut = T(typeId);

		assertFalse(sut.isUnknown());
		assertFalse(sut.isHashed());

		assertFalse(sut.isArray());

		assertEquals(isClassType, sut.isClassType());
		assertFalse(sut.isDelegateType());
		assertEquals(typeId.startsWith("e:"), sut.isEnumType());
		assertEquals(typeId.startsWith("i:"), sut.isInterfaceType());
		assertEquals(typeId.contains("+"), sut.isNestedType());
		assertFalse(sut.isNullableType());
		assertEquals(isReferenceType, sut.isReferenceType());
		assertFalse(sut.isSimpleType());
		assertEquals(typeId.startsWith("s:"), sut.isStructType());
		assertFalse(sut.isTypeParameter());
		assertEquals(isValueType, sut.isValueType());
		assertFalse(sut.isVoidType());

		if (typeId.contains("+")) {
			assertNotNull(sut.getDeclaringType());
		} else {
			assertNull(sut.getDeclaringType());
		}
		boolean hasTypeParams = sut.getIdentifier().contains("`")
				& sut.getIdentifier().lastIndexOf('`') > sut.getIdentifier().lastIndexOf('+');
		assertEquals(hasTypeParams, sut.hasTypeParameters());
	}

	@Test
	public void ShouldImplementIsUnknown() {
		assertTrue(T(null).isUnknown());
		assertTrue(T("?").isUnknown());
		assertTrue(TypeUtils.isUnknownTypeIdentifier(T(null).getIdentifier()));
	}

	@Test
	public void ShouldGetNamespaceFromTypeInGlobalNamespace() {
		sut = T("MyType, Assembly, 1.2.3.4");
		assertEquals(new NamespaceName(""), sut.getNamespace());
	}

	public static String[] provideLegacyBuiltInTypes() {

		Set<String> ids = Sets.newHashSet();
		ids.add("System.Boolean, mscorlib, 4.0.0.0");
		ids.add("System.Decimal, mscorlib, 4.0.0.0");
		ids.add("System.SByte, mscorlib, 4.0.0.0");
		ids.add("System.Byte, mscorlib, 4.0.0.0");
		ids.add("System.Int16, mscorlib, 4.0.0.0");
		ids.add("System.UInt16, mscorlib, 4.0.0.0");
		ids.add("System.Int32, mscorlib, 4.0.0.0");
		ids.add("System.UInt32, mscorlib, 4.0.0.0");
		ids.add("System.Int64, mscorlib, 4.0.0.0");
		ids.add("System.UInt64, mscorlib, 4.0.0.0");
		ids.add("System.Char, mscorlib, 4.0.0.0");
		ids.add("System.Single, mscorlib, 4.0.0.0");
		ids.add("System.Double, mscorlib, 4.0.0.0");
		ids.add("System.String, mscorlib, 4.0.0.0");
		ids.add("System.Object, mscorlib, 4.0.0.0");
		ids.add("System.Void, mscorlib, 4.0.0.0");

		return ids.toArray(new String[0]);

	}

	@Test(expected = ValidationException.class)
	@Parameters(method = "provideLegacyBuiltInTypes")
	public void ShouldRejectBuiltInDataStructures(String identifer) {
		T(identifer);
	}

	@Test
	@Parameters({ "s:System.Nullable`1[[T]], mscorlib, 4.0.0.0",
			"s:System.Nullable`1[[T -> System.UInt64, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0" })
	public void ShouldBeNullableType(String id) {
		assertTrue(T(id).isNullableType());
	}

	@Test
	@Parameters({ "s:System.Nullable`1[[T -> System.Int32, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0",
			"s:My.Struct, A, 1.0.0.0" })
	public void ShouldRecognizeStructTypes(String identifier) {
		assertTrue(T(identifier).isStructType());
	}

	@Test
	@Parameters({ "s:My.Struct, A, 1.0.0.0", "e:My.Enumtype, A, 3.4.5.6" })
	public void ShouldBeValueType(String identifier) {
		sut = T(identifier);
		assertTrue(sut.isValueType());
	}

	@Test
	@Parameters({ "i:Some.Interface, I, 6.5.4.3" })
	public void ShouldBeInterfaceType(String identifier) {
		sut = T(identifier);
		assertTrue(sut.isInterfaceType());
	}

	@Test
	@Parameters({ "Some.Class, C, 3.2.1.0" })
	public void ShouldNotBeInterfaceType(String id) {
		sut = T(id);
		assertFalse(sut.isInterfaceType());
	}

	@Test
	@Parameters({ "", "?" })
	public void ShouldBeUnknownType(String identifier) {
		sut = T(identifier);
		assertTrue(sut.isUnknown());
	}

	@Test
	public void ShouldNotBeUnknownType() {
		sut = T("Some.Known.Type, A, 1.2.3.4");
		assertFalse(sut.isUnknown());
	}

	public static String[][] provideSomeTypes() {
		Set<String[]> ids = Sets.newHashSet();
		// type id, fullName, shortName, namespace, assembly
		ids.add(new String[] { "System.X, mscorlib, 4.0.0.0", "System.X", "X" });
		ids.add(new String[] { "e:Full.Enum.Type, E, 1.2.3.4", "Full.Enum.Type", "Type" });
		ids.add(new String[] { "i:Full.Interface.Type, E, 1.2.3.4", "Full.Interface.Type", "Type" });
		ids.add(new String[] { "s:Full.Struct.Type, E, 1.2.3.4", "Full.Struct.Type", "Type" });
		ids.add(new String[] { "System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0",
				"System.Nullable`1[[System.Int32, mscorlib, 4.0.0.0]]", "Nullable" });
		ids.add(new String[] { "Outer.Type+InnerType, As, 1.2.3.4", "Outer.Type+InnerType", "InnerType" });
		ids.add(new String[] { "?", "?", "?" });
		ids.add(new String[] { "Task`1[[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]], mscorlib, 4.0.0.0",
				"Task`1[[TResult -> i:IList`1[[T -> T]], mscorlib, 4.0.0.0]]", "Task" });

		ids.add(new String[] { "a.b.TC`1[[T1]]+NC`1[[T2]], P", "a.b.TC`1[[T1]]+NC`1[[T2]]", "NC" });
		ids.add(new String[] { "e:n.E,P", "n.E", "E" });
		ids.add(new String[] { "n.T,P", "n.T", "T" });
		ids.add(new String[] { "n.T[],P", "n.T[]", "T[]" });
		ids.add(new String[] { "d:[?] [T+D,P].()", "T+D", "D" });
		ids.add(new String[] { "n.T`1[T2],P", "n.T`1[T2]", "T" });

		return ids.toArray(new String[0][0]);
	}

	@Test
	@Parameters(method = "provideSomeTypes")
	public void ShouldDetermineFullName(String id, String fullName, String shortName, String namespace,
			String assembly) {
		sut = T(id);
		assertEquals(fullName, sut.getFullName());
	}

	@Test
	@Parameters(method = "provideSomeTypes")
	public void ShouldDetermineName(String id, String fullName, String shortName, String namespace, String assembly) {
		sut = T(id);

		assertEquals(shortName, sut.getName());
	}

	@Test
	@Parameters(method = "provideSomeTypes")
	public void ShouldDetermineNamespace(String id, String fullName, String shortName, String namespace,
			String assembly) {
		sut = T(id);

		assertEquals(new NamespaceName(namespace), sut.getNamespace());
	}

	@Test
	@Parameters(method = "provideSomeTypes")
	public void ShouldDetermineAssembly(String id, String fullName, String shortName, String namespace,
			String assembly) {
		sut = T(id);

		assertEquals(new AssemblyName(assembly), sut.getAssembly());
	}

	@Test
	public void ShouldParseTypeParameters() {
		String StringIdentifier = "S -> System.X, mscore, 4.0.0.0";
		String intIdentifier = "I -> System.Y, mscore, 4.0.0.0";

		sut = T("pack.age.MyType`2[[" + StringIdentifier + "],[" + intIdentifier + "]], " + TestAssemblyIdentifier);

		assertEquals(TestAssemblyIdentifier, sut.getAssembly().getIdentifier());
		assertEquals("MyType", sut.getName());

		assertTrue(sut.hasTypeParameters());
		assertFalse(sut.isArray());
		assertEquals(2, sut.getTypeParameters().size());
		assertEquals(StringIdentifier, sut.getTypeParameters().get(0).getIdentifier());
		assertEquals(intIdentifier, sut.getTypeParameters().get(1).getIdentifier());
	}

	public static Object[][] provideHasTypeParameters() {
		Set<Object[]> cases = Sets.newHashSet();
		cases.add(new Object[] { "T", false });
		cases.add(new Object[] { "T,P", false });
		cases.add(new Object[] { "T`1[[T]], P", true });
		cases.add(new Object[] { "T`1[[T -> p:int]], P", true });
		cases.add(new Object[] { "T+N, P", false });
		cases.add(new Object[] { "T`1[[T]]+N, P", false });
		cases.add(new Object[] { "T+N`1[[T]], P", true });
		cases.add(new Object[] { "T`1[[T]]+N`1[[T]], P", true });

		cases.add(new Object[] { "SomeType`1[[T -> Foo, Bar, 1.2.3.4]], A, 1.2.3.4", false });
		cases.add(new Object[] { "n.T, A, 1.2.3.4", false });

		return cases.toArray(new Object[0][0]);
	}

	@Test
	@Parameters(method = "provideHasTypeParameters")
	public void ShouldHaveTypeParameters(String id, boolean hasTypeParameters) {
		sut = T(id);
		assertEquals(hasTypeParameters, sut.hasTypeParameters());
	}

	@Test

	public void ShouldBeTopLevelType() {
		sut = T("this.is.a.top.level.ValueType, " + TestAssemblyIdentifier);
		assertFalse(sut.isNestedType());
		assertNull(sut.getDeclaringType());
	}

	@Test

	public void ShouldBeParameterizedTypeWithParameterizedTypeParameter() {
		String paramParamIdentifier = "T -> x.A, P1";
		String paramIdentifier = "y.B`1[[" + paramParamIdentifier + "]], P2";

		sut = T("z.C`1[[" + paramIdentifier + "]], P3");

		assertTrue(sut.hasTypeParameters());
		assertEquals(1, sut.getTypeParameters().size());
		ITypeParameterName actual = sut.getTypeParameters().get(0);

		assertEquals(new TypeParameterName(paramIdentifier), actual);
	}

	public static String[][] provideTypeParameters() {
		Set<String[]> cases = Sets.newHashSet();
		// id, shortName, targetType
		cases.add(new String[] { "TP -> System.X, mscorlib, 4.0.0.0", "TR", "System.X, mscorlib, 4.0.0.0" });
		cases.add(new String[] { "TP -> ?", "TP", "?" });
		cases.add(new String[] { "TP", "TP", "?" });

		return cases.toArray(new String[0][0]);
	}

	@Test
	@Parameters(method = "provideTypeParameters")
	public void ShouldBeTypeParameter(String id, String shortName, String targetType) {
		sut = T(id);
		assertTrue(sut.isTypeParameter());
	}

	@Test
	@Parameters(method = "provideTypeParameters")
	public void ShouldExtractTypeParameterShortName(String id, String shortName, String targetType) {
		sut = T(id);

		assertEquals(shortName, sut.asTypeParameterName().getTypeParameterShortName());
	}

	@Test
	@Parameters(method = "provideTypeParameters")
	public void ShouldExtractShortAndFullName(String id, String shortName, String targetType) {
		sut = T(id);

		assertEquals(shortName, sut.getName());
		assertEquals(shortName, sut.getFullName());
	}

	@Test
	@Parameters(method = "provideTypeParameters")
	public void ShouldExtractTargetType(String id, String shortName, String targetType) {
		sut = T(id);

		assertEquals(T(targetType), sut.asTypeParameterName().getTypeParameterType());
	}

	@Test
	public void TypeParameterParsingIsCached() {
		sut = T("T,P");
		assertSame(sut.getTypeParameters(), sut.getTypeParameters());
	}

	@Test(expected = ValidationException.class)
	@Parameters({ "T`1[[G1]]", "T`1[[G1 -> T,P]]" })
	public void TypeNameNeedsComma(String invalidTypeName) {
		// ReSharper disable once ObjectCreationAsStatement
		new TypeName(invalidTypeName);
	}

	@Test
	@Parameters(method = "provideNestedTypes")
	public void ShouldParseDeclaringTypes(String typeId, String declTypeId) {
		sut = T(typeId);
		assertTrue(sut.isNestedType());
		assertEquals(T(declTypeId), sut.getDeclaringType());
	}

	public static String[][] provideNestedTypes() {
		return new String[][] { new String[] { "n.T1+T2, P", "n.T1, P" },
				new String[] { "d:[?] [n.T1+T2, P].()", "n.T1, P" }, new String[] { "e:n.T1+E, P", "n.T1, P" },
				new String[] { "i:n.T1+I, P", "n.T1, P" }, new String[] { "s:n.T1+S, P", "n.T1, P" },
				new String[] { "n.T1`1[[G1]]+T2`1[[G2]], P", "n.T1`1[[G1]], P" },
				new String[] { "a.p.T+N, P", "a.p.T, P" }, new String[] { "N.O+M+I, P", "N.O+M, P" },
				new String[] { "n.T+A`1[[T1 -> e:n.T+B, P]], P", "n.T, P" } };
	}
}