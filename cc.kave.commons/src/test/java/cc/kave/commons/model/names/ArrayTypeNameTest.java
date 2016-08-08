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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.ArrayTypeName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.names.csharp.TypeParameterName;

public class ArrayTypeNameTest {

	private String[][] derivesFromCases() {
		return new String[][] { new String[] { "T, P", "T[], P" }, new String[] { "A, B, 1.2.3.4", "A[], B, 1.2.3.4" },
				new String[] { "GT`1[[T -> PT, A]], A", "GT`1[][[T -> PT, A]], A" }, new String[] { "T", "T[]" },
				new String[] { "s:S, P", "s:S[], P" },
				new String[] { "d:[RT, A] [DT, A].()", "d:[RT, A] [DT, A].()[]" },
				new String[] { "d:[RT[], A] [DT, A].([PT[], A] p)", "d:[RT[], A] [DT, A].([PT[], A] p)[]" },
				new String[] { "A[], B", "A[,], B" }, new String[] { "A[,,], B", "A[,,,], B" }, new String[] {
						"T`1[[T -> d:[TR] [T2, P2].([T] arg)]], P", "T`1[][[T -> d:[TR] [T2, P2].([T] arg)]], P" } };
	}

	@Test
	public void derivesFrom() {
		String[][] derivesFromCases = derivesFromCases();

		for (int i = 0; i < derivesFromCases.length; i++) {
			ITypeName arrayTypeName = ArrayTypeName.from(TypeName.newTypeName(derivesFromCases[i][0]), 1);

			assertEquals(TypeName.newTypeName(derivesFromCases[i][1]).getIdentifier(), arrayTypeName.getIdentifier());
			assertEquals(TypeName.newTypeName(derivesFromCases[i][1]), arrayTypeName);
		}
	}

	@Test
	public void derivesMultiDimensionalArray() {
		ITypeName arrayTypeName = ArrayTypeName.from(TypeName.newTypeName("SomeType, Assembly, 1.2.3.4"), 2);

		assertEquals(TypeName.newTypeName("SomeType[,], Assembly, 1.2.3.4").getIdentifier(), arrayTypeName.getIdentifier());
		assertEquals(TypeName.newTypeName("SomeType[,], Assembly, 1.2.3.4"), arrayTypeName);
	}

	private String[] shouldBeArrayTypeCases() {
		return new String[] { "ValueType[,,], As, 9.8.7.6", "ValueType[], As, 5.4.3.2",
				"a.Foo`1[][[T -> int, mscore, 1.0.0.0]], Y, 4.3.6.1", "A[]", "T -> System.String[], mscorlib, 4.0.0.0",
				"System.Int32[], mscorlib, 4.0.0.0", "d:[RT, A] [DT, A].()[]",
				"T`1[][[T -> d:[TR] [T2, P2].([T] arg)]], P" };
	}

	@Test
	public void shouldBeArrayType() {
		for (String identifier : shouldBeArrayTypeCases()) {
			ITypeName arrayTypeName = TypeName.newTypeName(identifier);

			assertTrue(arrayTypeName.isArray());
		}
	}

	private String[][] shouldGetArrayBaseTypeCases() {
		return new String[][] { new String[] { "ValueType[,,], As, 9.8.7.6", "ValueType, As, 9.8.7.6" },
				new String[] { "ValueType[], As, 5.4.3.2", "ValueType, As, 5.4.3.2" },
				new String[] { "a.Foo`1[][[T -> int, mscore, 1.0.0.0]], A, 1.2.3.4",
						"a.Foo`1[[T -> int, mscore, 1.0.0.0]], A, 1.2.3.4" },
				new String[] { "A[]", "A" },
				new String[] { "T -> System.String[], mscorlib, 4.0.0.0", "System.String, mscorlib, 4.0.0.0" },
				new String[] { "System.Int32[], mscorlib, 4.0.0.0", "System.Int32, mscorlib, 4.0.0.0" },
				new String[] { "d:[RT, A] [DT, A].()[]", "d:[RT, A] [DT, A].()" },
				new String[] { "d:[RT[], A] [DT, A].([PT[], A] p)[]", "d:[RT[], A] [DT, A].([PT[], A] p)" },
				new String[] { "T`1[][[T -> d:[TR] [T2, P2].([T] arg)]], P",
						"T`1[[T -> d:[TR] [T2, P2].([T] arg)]], P" } };
	}

	@Test
	public void shouldGetArrayBaseType() {
		for (String[] s : shouldGetArrayBaseTypeCases()) {
			ITypeName arrayTypeName = TypeName.newTypeName(s[0]);

			assertEquals(s[1], arrayTypeName.getArrayBaseType().getIdentifier());
		}
	}

	@Test
	public void shouldIdentifyRank() {
		ArrayTypeName arrayTypeName1 = (ArrayTypeName) TypeName.newTypeName("ValueType[,,], As, 9.8.7.6");
		assertEquals(3, ArrayTypeName.getArrayRank(arrayTypeName1));

		ArrayTypeName arrayTypeName2 = (ArrayTypeName) TypeName
				.newTypeName("T`1[][[T -> d:[TR] [T2, P2].([T] arg)]], P");
		assertEquals(1, ArrayTypeName.getArrayRank(arrayTypeName2));
	}

	@Test
	public void arrayOfNullablesShouldNotBeNullable() {
		ITypeName actual = TypeName
				.newTypeName("System.Nullable`1[][[System.Int32, mscorlib, 1.2.3.4]], mscorlib, 1.2.3.4");

		assertFalse(actual.isNullableType());
	}

	@Test
	public void arrayOfSimpleTypesShouldNotBeSimpleType() {
		ITypeName actual = TypeName.newTypeName("System.Int64[], mscorlib, 1.2.3.4");

		assertFalse(actual.isSimpleType());
	}

	@Test
	public void arrayOfCustomStructsShouldNotBeStruct() {
		ITypeName actual = TypeName.newTypeName("s:My.Custom.Struct[], A, 1.2.3.4");

		assertFalse(actual.isStructType());
	}

	@Test
	public void shouldHaveArrayBracesInName() {
		ITypeName uut = TypeName.newTypeName("ValueType[,,], As, 9.8.7.6");

		assertEquals("ValueType[,,]", uut.getName());
	}

	@Test
	public void shouldNotBeArrayType() {
		ITypeName uut = TypeName.newTypeName("ValueType, As, 2.5.1.6");

		assertFalse(uut.isArray());
	}

	@Test
	public void handlesDelegateBaseType() {
		ITypeName uut = TypeName.newTypeName("d:[RT, A] [N.O+DT, AA, 1.2.3.4].()[]");
		String fullName = uut.getFullName();
		String fullName2 = uut.getArrayBaseType().getFullName();

		assertEquals("N.O+DT[]", uut.getFullName());
		assertEquals("N", uut.getNamespace().getIdentifier());
		assertEquals("DT[]", uut.getName());
		assertEquals("AA, 1.2.3.4", uut.getAssembly().getIdentifier());
		assertEquals("N.O, AA, 1.2.3.4", uut.getDeclaringType().getIdentifier());
	}

	@Test
	public void handlesGenericDelegateBaseType() {
		ITypeName uut = TypeName.newTypeName("d:[T] [DT`1[[T -> String, mscorlib]]].([T] p)[]");

		assertTrue(uut.hasTypeParameters());
		assertArrayEquals(new ITypeName[] { TypeParameterName.newTypeParameterName("T -> String, mscorlib") },
				uut.getTypeParameters().toArray());
	}
}
