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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyName;
import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyVersion;
import cc.kave.commons.model.naming.impl.v0.types.organization.NamespaceName;
import cc.kave.commons.model.naming.types.IPredefinedTypeName;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.exceptions.ValidationException;

public class PredefinedTypeNameTest {
	private static String[][] predefinedTypeSource() {
		return new String[][] { //
				new String[] { "sbyte", "System.SByte", "p:sbyte" }, //
				new String[] { "byte", "System.Byte", "p:byte" }, //
				new String[] { "short", "System.Int16", "p:short" }, //
				new String[] { "ushort", "System.UInt16", "p:ushort" }, //
				new String[] { "int", "System.Int32", "p:int" }, //
				new String[] { "uint", "System.UInt32", "p:uint" }, //
				new String[] { "long", "System.Int64", "p:long" }, //
				new String[] { "ulong", "System.UInt64", "p:ulong" }, //
				new String[] { "char", "System.Char", "p:char" }, //
				new String[] { "float", "System.Single", "p:float" }, //
				new String[] { "double", "System.Double", "p:double" }, //
				new String[] { "bool", "System.Boolean", "p:bool" }, //
				new String[] { "decimal", "System.Decimal", "p:decimal" }, //
				new String[] { "void", "System.Void", "p:void" }, //
				new String[] { "object", "System.Object", "p:object" }, //
				new String[] { "string", "System.String", "p:string" } };
	}

	private PredefinedTypeName sut;

	public void ShouldParseShortName_Params() {
		for (String[] tc : predefinedTypeSource()) {
			ShouldParseShortName(tc[0], tc[1], tc[2]);
			ShouldParseFullName(tc[0], tc[1], tc[2]);
			ShouldParseNamespace(tc[0], tc[1], tc[2]);
			ShouldParseAssembly(tc[0], tc[1], tc[2]);
			ShouldRecognizeIdentifier(tc[0], tc[1], tc[2]);
			DefaultValues(tc[0], tc[1], tc[2]);
			ArrayHandling(tc[0], tc[1], tc[2]);
		}
	}

	// TODO NameUpdate: use parameter runner
	public void ShouldParseShortName(String shortName, String fullName, String id) {
		assertEquals(shortName, new PredefinedTypeName(id).getName());
	}

	// TODO NameUpdate: use parameter runner
	public void ShouldParseFullName(String shortName, String fullName, String id) {
		assertEquals(fullName, new PredefinedTypeName(id).getFullName());
	}

	// TODO NameUpdate: use parameter runner
	public void ShouldParseNamespace(String shortName, String fullName, String id) {
		assertEquals(new NamespaceName("System"), new PredefinedTypeName(id).getNamespace());
	}

	// TODO NameUpdate: use parameter runner
	public void ShouldParseAssembly(String shortName, String fullName, String id) {
		assertEquals(new AssemblyName(String.format("mscorlib, %s", new AssemblyVersion().getIdentifier())),
				new PredefinedTypeName(id).getAssembly());
	}

	// TODO NameUpdate: use parameter runner
	public void ShouldReturnFullType(String shortName, String fullName, String id) {
		sut = new PredefinedTypeName(id);

		String structPart = sut.isStructType() ? "s:" : "";
		assertEquals(new TypeName(
				String.format("%s%s, mscorlib, %s", structPart, fullName, new AssemblyVersion().getIdentifier())),
				sut.getFullType());
	}

	@Test(expected = AssertionException.class)
	public void ShouldCrashWhenFullTypeIsRequestedFromArray() {
		new PredefinedTypeName("p:int[]").getFullType();
	}

	// TODO NameUpdate: use parameter runner
	public void ShouldRecognizeIdentifier(String shortName, String fullName, String id) {
		assertTrue(PredefinedTypeName.IsPredefinedTypeNameIdentifier(id));
		assertFalse(ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertFalse(TypeUtils.isUnknownTypeIdentifier(id));
		assertFalse(TypeParameterName.isTypeParameterNameIdentifier(id));
		assertFalse(DelegateTypeName.isDelegateTypeNameIdentifier(id));
	}

	@Test(expected = ValidationException.class)
	public void ShouldRejectUnknownIds() {
		String invalidId = "p:...";
		assertFalse(PredefinedTypeName.IsPredefinedTypeNameIdentifier(invalidId));
		new PredefinedTypeName(invalidId);
	}

	// TODO NameUpdate: use parameter runner
	public void DefaultValues(String shortName, String fullName, String id) {
		IPredefinedTypeName sut = new PredefinedTypeName(id);

		assertFalse(sut.isUnknown());
		assertFalse(sut.isHashed());

		assertTrue(sut.isPredefined());

		assertFalse(sut.isArray());
		assertFalse(sut.isDelegateType());
		assertFalse(sut.isEnumType());
		assertFalse(sut.isInterfaceType());
		assertFalse(sut.isNestedType());
		assertNull(sut.getDeclaringType());
		assertFalse(sut.isNullableType());
		assertFalse(sut.isTypeParameter());
		assertFalse(sut.hasTypeParameters());

		AssertIsTrueIf(sut.isClassType(), shortName, "object", "string");
		AssertIsTrueIf(sut.isReferenceType(), shortName, "object", "string");
		AssertIsFalseIf(sut.isSimpleType(), shortName, "object", "string", "void");
		AssertIsFalseIf(sut.isStructType(), shortName, "object", "string");
		AssertIsFalseIf(sut.isValueType(), shortName, "object", "string");
		AssertIsTrueIf(sut.isVoidType(), shortName, "void");
	}

	// TODO NameUpdate: use parameter runner
	public void ArrayHandling(String shortName, String fullName, String id) {
		if ("void".equals(shortName)) {
			return;
		}
		// ran kand basetype are tested in ArrayTypeNameTest
		for (String arrSuffix : new String[] { "[]", "[,]" }) {
			sut = new PredefinedTypeName(id + arrSuffix);

			assertFalse(sut.isUnknown());
			assertFalse(sut.isHashed());

			assertTrue(sut.isArray());
			assertFalse(sut.isClassType());
			assertFalse(sut.isDelegateType());
			assertFalse(sut.isEnumType());
			assertFalse(sut.isInterfaceType());
			assertFalse(sut.isNestedType());
			assertNull(sut.getDeclaringType());
			assertFalse(sut.isNullableType());
			assertFalse(sut.isPredefined());
			assertTrue(sut.isReferenceType());
			assertFalse(sut.isSimpleType());
			assertFalse(sut.isStructType());
			assertFalse(sut.isTypeParameter());
			assertFalse(sut.isValueType());
			assertFalse(sut.isVoidType());
			assertFalse(sut.hasTypeParameters());
		}
	}

	@Test
	public void ShouldReturnItselfOnConversion_Array() {
		sut = new PredefinedTypeName("p:int[]");
		assertSame(sut, sut.asArrayTypeName());
	}

	@Test
	public void ShouldReturnItselfOnConversion_Predef() {
		sut = new PredefinedTypeName("p:int");
		assertSame(sut, sut.asPredefinedTypeName());
	}

	private static void AssertIsFalseIf(boolean actual, String shortName, String... isFalseSet) {
		boolean expected = !contains(isFalseSet, shortName);
		assertEquals(expected, actual);
	}

	private static void AssertIsTrueIf(boolean actual, String shortName, String... isTrueSet) {
		boolean expected = contains(isTrueSet, shortName);
		assertEquals(expected, actual);
	}

	private static boolean contains(String[] hay, String needle) {
		for (String s : hay) {
			if (needle.equals(s)) {
				return true;
			}
		}
		return false;
	}

	@Test
	public void ShouldDifferentiatePredefinedTypeAndArray() {
		assertFalse(new PredefinedTypeName("p:int").isArray());
		assertTrue(new PredefinedTypeName("p:int[]").isArray());
		assertTrue(new PredefinedTypeName("p:int").isPredefined());
		assertFalse(new PredefinedTypeName("p:int[]").isPredefined());
	}

	@Test(expected = AssertionException.class)
	public void ShouldCrashIfConversionIsNotAppropriate_Array() {
		new PredefinedTypeName("p:int[]").asPredefinedTypeName();
	}

	@Test(expected = AssertionException.class)
	public void ShouldCrashIfConversionIsNotAppropriate_NonArray() {
		new PredefinedTypeName("p:int").asArrayTypeName();
	}

	@Test(expected = AssertionException.class)
	public void ShouldCrashIfConversionIsNotAppropriate_TypeParameter() {
		new PredefinedTypeName("p:int").asTypeParameterName();
	}

	@Test(expected = AssertionException.class)
	public void ShouldCrashIfConversionIsNotAppropriate_Delegate() {
		new PredefinedTypeName("p:int").asDelegateTypeName();
	}

	@Test(expected = AssertionException.class)
	public void ShouldCrashIfConversionIsNotAppropriate_FullType() {
		new PredefinedTypeName("p:int[]").getFullType();
	}

	@Test(expected = ValidationException.class)
	public void ShouldRejectVoidArrays() {
		// ReSharper disable once ObjectCreationAsStatement
		new PredefinedTypeName("p:void[]");
	}

	// TODO NameUpdate: use parameter runner
	@Test
	public void ShouldRejectInvalidNames() {
		for (String invalidId : new String[] { "p:int[", "p:int]", "p:int][" }) {
			boolean wasThrown = false;
			try {
				new PredefinedTypeName(invalidId);
			} catch (ValidationException e) {
				wasThrown = true;
			}
			assertTrue(wasThrown);
		}
	}
}