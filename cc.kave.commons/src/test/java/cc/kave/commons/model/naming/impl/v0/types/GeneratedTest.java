// ##############################################################################
// Attention: This file was auto-generated, do not modify its contents manually!!
// (generated at: 8/11/2016 3:20:03 PM)
// ##############################################################################
/**
* Copyright 2016 Technische Universit„t Darmstadt
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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyName;
import cc.kave.commons.model.naming.impl.v0.types.organization.NamespaceName;
import cc.kave.commons.model.naming.types.IArrayTypeName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.IPredefinedTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;
import cc.recommenders.exceptions.AssertionException;

public class GeneratedTest {
	/*
	 * default value tests
	 */
	@Test
	public void DefaultValues_TypeName() {
		String id = "?";
		assertEquals(true, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof TypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(true, sut.isUnknown());
		assertEquals(new NamespaceName("???"), sut.getNamespace());
		assertEquals(new AssemblyName("???"), sut.getAssembly());
		assertEquals("?", sut.getFullName());
		assertEquals("?", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(false, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(false, sut.isArray());
		hasThrown = false;
		try {
			sut.asArrayTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void DefaultValues_DelegateTypeName() {
		String id = "d:[?] [?].()";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(true, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof DelegateTypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(true, sut.isUnknown());
		assertEquals(new NamespaceName("???"), sut.getNamespace());
		assertEquals(new AssemblyName("???"), sut.getAssembly());
		assertEquals("?", sut.getFullName());
		assertEquals("?", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(false, sut.isArray());
		hasThrown = false;
		try {
			sut.asArrayTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// delegates
		assertEquals(true, sut.isDelegateType());
		IDelegateTypeName sutD = sut.asDelegateTypeName();
		assertEquals(new TypeName("?"), sutD.getDelegateType());
		assertEquals(false, sutD.hasParameters());
		assertEquals(false, sutD.isRecursive());
		assertEquals(Lists.newArrayList(), sutD.getParameters());
		assertEquals(new TypeName("?"), sutD.getReturnType());
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void DeriveArrayTest_0() {
		String baseId = "?";
		String arr1Id = "?[]";
		String arr2Id = "?[,]";
		ITypeName base = TypeUtils.createTypeName(baseId);
		ITypeName arr1 = ArrayTypeName.from(base, 1);
		assertTrue(arr1 instanceof ArrayTypeName);
		assertEquals(arr1Id, arr1.getIdentifier());
		ITypeName arr2 = ArrayTypeName.from(base, 2);
		assertTrue(arr2 instanceof ArrayTypeName);
		assertEquals(arr2Id, arr2.getIdentifier());
	}

	@Test
	public void DeriveArrayTest_1() {
		String baseId = "T,P";
		String arr1Id = "T[],P";
		String arr2Id = "T[,],P";
		ITypeName base = TypeUtils.createTypeName(baseId);
		ITypeName arr1 = ArrayTypeName.from(base, 1);
		assertTrue(arr1 instanceof ArrayTypeName);
		assertEquals(arr1Id, arr1.getIdentifier());
		ITypeName arr2 = ArrayTypeName.from(base, 2);
		assertTrue(arr2 instanceof ArrayTypeName);
		assertEquals(arr2Id, arr2.getIdentifier());
	}

	@Test
	public void DeriveArrayTest_2() {
		String baseId = "n.T`1[[G]],P";
		String arr1Id = "n.T`1[][[G]],P";
		String arr2Id = "n.T`1[,][[G]],P";
		ITypeName base = TypeUtils.createTypeName(baseId);
		ITypeName arr1 = ArrayTypeName.from(base, 1);
		assertTrue(arr1 instanceof ArrayTypeName);
		assertEquals(arr1Id, arr1.getIdentifier());
		ITypeName arr2 = ArrayTypeName.from(base, 2);
		assertTrue(arr2 instanceof ArrayTypeName);
		assertEquals(arr2Id, arr2.getIdentifier());
	}

	@Test
	public void DeriveArrayTest_3() {
		String baseId = "n.T`1[[G -> p:byte]],P";
		String arr1Id = "n.T`1[][[G -> p:byte]],P";
		String arr2Id = "n.T`1[,][[G -> p:byte]],P";
		ITypeName base = TypeUtils.createTypeName(baseId);
		ITypeName arr1 = ArrayTypeName.from(base, 1);
		assertTrue(arr1 instanceof ArrayTypeName);
		assertEquals(arr1Id, arr1.getIdentifier());
		ITypeName arr2 = ArrayTypeName.from(base, 2);
		assertTrue(arr2 instanceof ArrayTypeName);
		assertEquals(arr2Id, arr2.getIdentifier());
	}

	@Test
	public void DeriveArrayTest_4() {
		String baseId = "T";
		String arr1Id = "T[]";
		String arr2Id = "T[,]";
		ITypeName base = TypeUtils.createTypeName(baseId);
		ITypeName arr1 = ArrayTypeName.from(base, 1);
		assertTrue(arr1 instanceof TypeParameterName);
		assertEquals(arr1Id, arr1.getIdentifier());
		ITypeName arr2 = ArrayTypeName.from(base, 2);
		assertTrue(arr2 instanceof TypeParameterName);
		assertEquals(arr2Id, arr2.getIdentifier());
	}

	@Test
	public void DeriveArrayTest_5() {
		String baseId = "p:int";
		String arr1Id = "p:int[]";
		String arr2Id = "p:int[,]";
		ITypeName base = TypeUtils.createTypeName(baseId);
		ITypeName arr1 = ArrayTypeName.from(base, 1);
		assertTrue(arr1 instanceof PredefinedTypeName);
		assertEquals(arr1Id, arr1.getIdentifier());
		ITypeName arr2 = ArrayTypeName.from(base, 2);
		assertTrue(arr2 instanceof PredefinedTypeName);
		assertEquals(arr2Id, arr2.getIdentifier());
	}

	@Test
	public void DeriveArrayTest_6() {
		String baseId = "A, B, 1.2.3.4";
		String arr1Id = "A[], B, 1.2.3.4";
		String arr2Id = "A[,], B, 1.2.3.4";
		ITypeName base = TypeUtils.createTypeName(baseId);
		ITypeName arr1 = ArrayTypeName.from(base, 1);
		assertTrue(arr1 instanceof ArrayTypeName);
		assertEquals(arr1Id, arr1.getIdentifier());
		ITypeName arr2 = ArrayTypeName.from(base, 2);
		assertTrue(arr2 instanceof ArrayTypeName);
		assertEquals(arr2Id, arr2.getIdentifier());
	}

	@Test
	public void TypeTest_0() {
		String id = "?";
		assertEquals(true, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof TypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(true, sut.isUnknown());
		assertEquals(new NamespaceName("???"), sut.getNamespace());
		assertEquals(new AssemblyName("???"), sut.getAssembly());
		assertEquals("?", sut.getFullName());
		assertEquals("?", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(false, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(false, sut.isArray());
		hasThrown = false;
		try {
			sut.asArrayTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_1() {
		String id = "?[]";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof ArrayTypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName("???"), sut.getNamespace());
		assertEquals(new AssemblyName("???"), sut.getAssembly());
		assertEquals("?[]", sut.getFullName());
		assertEquals("?[]", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(true, sut.isArray());
		IArrayTypeName sutArr = sut.asArrayTypeName();
		assertEquals(1, sutArr.getRank());
		assertEquals(new TypeName("?"), sutArr.getArrayBaseType());
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_2() {
		String id = "?[,]";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof ArrayTypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName("???"), sut.getNamespace());
		assertEquals(new AssemblyName("???"), sut.getAssembly());
		assertEquals("?[,]", sut.getFullName());
		assertEquals("?[,]", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(true, sut.isArray());
		IArrayTypeName sutArr = sut.asArrayTypeName();
		assertEquals(2, sutArr.getRank());
		assertEquals(new TypeName("?"), sutArr.getArrayBaseType());
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_3() {
		String id = "T,P";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(true, TypeName.isTypeNameIdentifier(id));
		assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof TypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName(""), sut.getNamespace());
		assertEquals(new AssemblyName("P"), sut.getAssembly());
		assertEquals("T", sut.getFullName());
		assertEquals("T", sut.getName());
		assertEquals(true, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(false, sut.isArray());
		hasThrown = false;
		try {
			sut.asArrayTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_4() {
		String id = "T[],P";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof ArrayTypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName(""), sut.getNamespace());
		assertEquals(new AssemblyName("P"), sut.getAssembly());
		assertEquals("T[]", sut.getFullName());
		assertEquals("T[]", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(true, sut.isArray());
		IArrayTypeName sutArr = sut.asArrayTypeName();
		assertEquals(1, sutArr.getRank());
		assertEquals(new TypeName("T,P"), sutArr.getArrayBaseType());
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_5() {
		String id = "T[,],P";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof ArrayTypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName(""), sut.getNamespace());
		assertEquals(new AssemblyName("P"), sut.getAssembly());
		assertEquals("T[,]", sut.getFullName());
		assertEquals("T[,]", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(true, sut.isArray());
		IArrayTypeName sutArr = sut.asArrayTypeName();
		assertEquals(2, sutArr.getRank());
		assertEquals(new TypeName("T,P"), sutArr.getArrayBaseType());
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_6() {
		String id = "n.T`1[[G]],P";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(true, TypeName.isTypeNameIdentifier(id));
		assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof TypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName("n"), sut.getNamespace());
		assertEquals(new AssemblyName("P"), sut.getAssembly());
		assertEquals("n.T`1[[G]]", sut.getFullName());
		assertEquals("T", sut.getName());
		assertEquals(true, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(true, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(new TypeParameterName("G")), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(false, sut.isArray());
		hasThrown = false;
		try {
			sut.asArrayTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_7() {
		String id = "n.T`1[][[G]],P";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof ArrayTypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName("n"), sut.getNamespace());
		assertEquals(new AssemblyName("P"), sut.getAssembly());
		assertEquals("n.T`1[[G]][]", sut.getFullName());
		assertEquals("T[]", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(true, sut.isArray());
		IArrayTypeName sutArr = sut.asArrayTypeName();
		assertEquals(1, sutArr.getRank());
		assertEquals(new TypeName("n.T`1[[G]],P"), sutArr.getArrayBaseType());
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_8() {
		String id = "n.T`1[,][[G]],P";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof ArrayTypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName("n"), sut.getNamespace());
		assertEquals(new AssemblyName("P"), sut.getAssembly());
		assertEquals("n.T`1[[G]][,]", sut.getFullName());
		assertEquals("T[,]", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(true, sut.isArray());
		IArrayTypeName sutArr = sut.asArrayTypeName();
		assertEquals(2, sutArr.getRank());
		assertEquals(new TypeName("n.T`1[[G]],P"), sutArr.getArrayBaseType());
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_9() {
		String id = "n.T`1[[G -> p:byte]],P";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(true, TypeName.isTypeNameIdentifier(id));
		assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof TypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName("n"), sut.getNamespace());
		assertEquals(new AssemblyName("P"), sut.getAssembly());
		assertEquals("n.T`1[[G -> p:byte]]", sut.getFullName());
		assertEquals("T", sut.getName());
		assertEquals(true, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(true, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(new TypeParameterName("G -> p:byte")), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(false, sut.isArray());
		hasThrown = false;
		try {
			sut.asArrayTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_10() {
		String id = "n.T`1[][[G -> p:byte]],P";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof ArrayTypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName("n"), sut.getNamespace());
		assertEquals(new AssemblyName("P"), sut.getAssembly());
		assertEquals("n.T`1[[G -> p:byte]][]", sut.getFullName());
		assertEquals("T[]", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(true, sut.isArray());
		IArrayTypeName sutArr = sut.asArrayTypeName();
		assertEquals(1, sutArr.getRank());
		assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sutArr.getArrayBaseType());
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_11() {
		String id = "n.T`1[,][[G -> p:byte]],P";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof ArrayTypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName("n"), sut.getNamespace());
		assertEquals(new AssemblyName("P"), sut.getAssembly());
		assertEquals("n.T`1[[G -> p:byte]][,]", sut.getFullName());
		assertEquals("T[,]", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(true, sut.isArray());
		IArrayTypeName sutArr = sut.asArrayTypeName();
		assertEquals(2, sutArr.getRank());
		assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sutArr.getArrayBaseType());
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_12() {
		String id = "T";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof TypeParameterName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName("???"), sut.getNamespace());
		assertEquals(new AssemblyName("???"), sut.getAssembly());
		assertEquals("T", sut.getFullName());
		assertEquals("T", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(false, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(true, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(false, sut.isArray());
		hasThrown = false;
		try {
			sut.asArrayTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(true, sut.isTypeParameter());
		ITypeParameterName sutT = sut.asTypeParameterName();
		assertEquals(false, sutT.isBound());
		assertEquals("T", sutT.getTypeParameterShortName());
		assertEquals(new TypeName("?"), sutT.getTypeParameterType());
	}

	@Test
	public void TypeTest_13() {
		String id = "T[]";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof TypeParameterName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName("???"), sut.getNamespace());
		assertEquals(new AssemblyName("???"), sut.getAssembly());
		assertEquals("T[]", sut.getFullName());
		assertEquals("T[]", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(true, sut.isArray());
		IArrayTypeName sutArr = sut.asArrayTypeName();
		assertEquals(1, sutArr.getRank());
		assertEquals(new TypeParameterName("T"), sutArr.getArrayBaseType());
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_14() {
		String id = "T[,]";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(true, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof TypeParameterName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName("???"), sut.getNamespace());
		assertEquals(new AssemblyName("???"), sut.getAssembly());
		assertEquals("T[,]", sut.getFullName());
		assertEquals("T[,]", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(true, sut.isArray());
		IArrayTypeName sutArr = sut.asArrayTypeName();
		assertEquals(2, sutArr.getRank());
		assertEquals(new TypeParameterName("T"), sutArr.getArrayBaseType());
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_15() {
		String id = "p:int";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(true, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof PredefinedTypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName("System"), sut.getNamespace());
		assertEquals(new AssemblyName("mscorlib, ???"), sut.getAssembly());
		assertEquals("System.Int32", sut.getFullName());
		assertEquals("int", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(true, sut.isPredefined());
		assertEquals(false, sut.isReferenceType());
		assertEquals(true, sut.isSimpleType());
		assertEquals(true, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(true, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(false, sut.isArray());
		hasThrown = false;
		try {
			sut.asArrayTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(true, sut.isPredefined());
		IPredefinedTypeName sutP = sut.asPredefinedTypeName();
		assertEquals(new TypeName("s:System.Int32, mscorlib, ???"), sutP.getFullType());
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_16() {
		String id = "p:int[]";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(true, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof PredefinedTypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName("System"), sut.getNamespace());
		assertEquals(new AssemblyName("mscorlib, ???"), sut.getAssembly());
		assertEquals("System.Int32[]", sut.getFullName());
		assertEquals("int[]", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(true, sut.isArray());
		IArrayTypeName sutArr = sut.asArrayTypeName();
		assertEquals(1, sutArr.getRank());
		assertEquals(new PredefinedTypeName("p:int"), sutArr.getArrayBaseType());
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_17() {
		String id = "p:int[,]";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(true, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof PredefinedTypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName("System"), sut.getNamespace());
		assertEquals(new AssemblyName("mscorlib, ???"), sut.getAssembly());
		assertEquals("System.Int32[,]", sut.getFullName());
		assertEquals("int[,]", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(true, sut.isArray());
		IArrayTypeName sutArr = sut.asArrayTypeName();
		assertEquals(2, sutArr.getRank());
		assertEquals(new PredefinedTypeName("p:int"), sutArr.getArrayBaseType());
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_18() {
		String id = "A, B, 1.2.3.4";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(true, TypeName.isTypeNameIdentifier(id));
		assertEquals(false, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof TypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName(""), sut.getNamespace());
		assertEquals(new AssemblyName("B, 1.2.3.4"), sut.getAssembly());
		assertEquals("A", sut.getFullName());
		assertEquals("A", sut.getName());
		assertEquals(true, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(false, sut.isArray());
		hasThrown = false;
		try {
			sut.asArrayTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_19() {
		String id = "A[], B, 1.2.3.4";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof ArrayTypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName(""), sut.getNamespace());
		assertEquals(new AssemblyName("B, 1.2.3.4"), sut.getAssembly());
		assertEquals("A[]", sut.getFullName());
		assertEquals("A[]", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(true, sut.isArray());
		IArrayTypeName sutArr = sut.asArrayTypeName();
		assertEquals(1, sutArr.getRank());
		assertEquals(new TypeName("A, B, 1.2.3.4"), sutArr.getArrayBaseType());
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}

	@Test
	public void TypeTest_20() {
		String id = "A[,], B, 1.2.3.4";
		assertEquals(false, TypeUtils.isUnknownTypeIdentifier(id));
		assertEquals(false, TypeName.isTypeNameIdentifier(id));
		assertEquals(true, ArrayTypeName.isArrayTypeNameIdentifier(id));
		assertEquals(false, TypeParameterName.isTypeParameterNameIdentifier(id));
		assertEquals(false, DelegateTypeName.isDelegateTypeNameIdentifier(id));
		assertEquals(false, PredefinedTypeName.isPredefinedTypeNameIdentifier(id));
		ITypeName sut = TypeUtils.createTypeName(id);
		assertTrue(sut instanceof ArrayTypeName);
		assertEquals(false, sut.isHashed());
		assertEquals(false, sut.isUnknown());
		assertEquals(new NamespaceName(""), sut.getNamespace());
		assertEquals(new AssemblyName("B, 1.2.3.4"), sut.getAssembly());
		assertEquals("A[,]", sut.getFullName());
		assertEquals("A[,]", sut.getName());
		assertEquals(false, sut.isClassType());
		assertEquals(false, sut.isEnumType());
		assertEquals(false, sut.isInterfaceType());
		assertEquals(false, sut.isNullableType());
		assertEquals(false, sut.isPredefined());
		assertEquals(true, sut.isReferenceType());
		assertEquals(false, sut.isSimpleType());
		assertEquals(false, sut.isStructType());
		assertEquals(false, sut.isTypeParameter());
		assertEquals(false, sut.isValueType());
		assertEquals(false, sut.isVoidType());
		assertEquals(false, sut.isNestedType());
		assertEquals(null, sut.getDeclaringType());
		assertEquals(false, sut.hasTypeParameters());
		assertEquals(Lists.newArrayList(), sut.getTypeParameters());
		boolean hasThrown;
		// array
		assertEquals(true, sut.isArray());
		IArrayTypeName sutArr = sut.asArrayTypeName();
		assertEquals(2, sutArr.getRank());
		assertEquals(new TypeName("A, B, 1.2.3.4"), sutArr.getArrayBaseType());
		// delegates
		assertEquals(false, sut.isDelegateType());
		hasThrown = false;
		try {
			sut.asDelegateTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// predefined
		assertEquals(false, sut.isPredefined());
		hasThrown = false;
		try {
			sut.asPredefinedTypeName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
		// type parameters
		assertEquals(false, sut.isTypeParameter());
		hasThrown = false;
		try {
			sut.asTypeParameterName();
		} catch (AssertionException e) {
			hasThrown = true;
		}
		assertTrue(hasThrown);
	}
}