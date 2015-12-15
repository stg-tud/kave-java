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

import org.junit.Test;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsArrayTypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.names.csharp.CsTypeParameterName;

public class CsArrayTypeNameTest {

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
			TypeName arrayTypeName = CsArrayTypeName.from(CsTypeName.newTypeName(derivesFromCases[i][0]), 1);

			assertEquals(CsTypeName.newTypeName(derivesFromCases[i][1]).getIdentifier(), arrayTypeName.getIdentifier());
			assertEquals(CsTypeName.newTypeName(derivesFromCases[i][1]), arrayTypeName);
		}
	}

	@Test
	public void derivesMultiDimensionalArray() {
		TypeName arrayTypeName = CsArrayTypeName.from(CsTypeName.newTypeName("SomeType, Assembly, 1.2.3.4"), 2);

		assertEquals(CsTypeName.newTypeName("SomeType[,], Assembly, 1.2.3.4").getIdentifier(), arrayTypeName.getIdentifier());
		assertEquals(CsTypeName.newTypeName("SomeType[,], Assembly, 1.2.3.4"), arrayTypeName);
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
			TypeName arrayTypeName = CsTypeName.newTypeName(identifier);

			assertTrue(arrayTypeName.isArrayType());
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
			TypeName arrayTypeName = CsTypeName.newTypeName(s[0]);

			assertEquals(s[1], arrayTypeName.getArrayBaseType().getIdentifier());
		}
	}

	@Test
	public void shouldIdentifyRank() {
		CsArrayTypeName arrayTypeName1 = (CsArrayTypeName) CsTypeName.newTypeName("ValueType[,,], As, 9.8.7.6");
		assertEquals(3, CsArrayTypeName.getArrayRank(arrayTypeName1));

		CsArrayTypeName arrayTypeName2 = (CsArrayTypeName) CsTypeName
				.newTypeName("T`1[][[T -> d:[TR] [T2, P2].([T] arg)]], P");
		assertEquals(1, CsArrayTypeName.getArrayRank(arrayTypeName2));
	}

	@Test
	public void arrayOfNullablesShouldNotBeNullable() {
		TypeName actual = CsTypeName
				.newTypeName("System.Nullable`1[][[System.Int32, mscorlib, 1.2.3.4]], mscorlib, 1.2.3.4");

		assertFalse(actual.isNullableType());
	}

	@Test
	public void arrayOfSimpleTypesShouldNotBeSimpleType() {
		TypeName actual = CsTypeName.newTypeName("System.Int64[], mscorlib, 1.2.3.4");

		assertFalse(actual.isSimpleType());
	}

	@Test
	public void arrayOfCustomStructsShouldNotBeStruct() {
		TypeName actual = CsTypeName.newTypeName("s:My.Custom.Struct[], A, 1.2.3.4");

		assertFalse(actual.isStructType());
	}

	@Test
	public void shouldHaveArrayBracesInName() {
		TypeName uut = CsTypeName.newTypeName("ValueType[,,], As, 9.8.7.6");

		assertEquals("ValueType[,,]", uut.getName());
	}

	@Test
	public void shouldNotBeArrayType() {
		TypeName uut = CsTypeName.newTypeName("ValueType, As, 2.5.1.6");

		assertFalse(uut.isArrayType());
	}

	@Test
	public void handlesDelegateBaseType() {
		TypeName uut = CsTypeName.newTypeName("d:[RT, A] [N.O+DT, AA, 1.2.3.4].()[]");
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
		TypeName uut = CsTypeName.newTypeName("d:[T] [DT`1[[T -> String, mscorlib]]].([T] p)[]");

		assertTrue(uut.hasTypeParameters());
		assertArrayEquals(new TypeName[] { CsTypeParameterName.newTypeParameterName("T -> String, mscorlib") },
				uut.getTypeParameters().toArray());
	}
}
