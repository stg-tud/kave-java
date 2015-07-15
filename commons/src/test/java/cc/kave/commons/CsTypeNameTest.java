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

import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;

public class CsTypeNameTest {

	private TypeName name = CsTypeName.newTypeName("java.lang.String, java.lang, VersionPlaceholder");
	private TypeName genericName = CsTypeName
			.newTypeName("java.util.HashMap`2[[T -> String, java.lang.String, VersionPlaceholder], "
					+ "[T -> String, java.lang.String, VersionPlaceholder]], java.util, VersionPlaceholder");

	@Test
	public void isGenericEntity() {
		boolean actual = genericName.isGenericEntity();
		boolean expected = true;

		assertEquals(expected, actual);
	}

	@Test
	public void isGenericEntityFalse() {
		boolean actual = name.isGenericEntity();
		boolean expected = false;

		assertEquals(expected, actual);
	}

	@Test
	public void hasTypeParameters() {
		boolean actual = genericName.hasTypeParameters();
		boolean expected = true;

		assertEquals(expected, actual);
	}

	@Test
	public void hasTypeParametersFalse() {
		boolean actual = name.hasTypeParameters();
		boolean expected = false;

		assertEquals(expected, actual);
	}

	@Test
	public void getTypeParameters() {
		List<TypeName> typeParameters = genericName.getTypeParameters();
		String actual = typeParameters.get(0).getIdentifier();
		String expected = "String, java.lang.String, VersionPlaceholder";

		assertEquals(expected, actual);
	}

	@Test
	public void getFullName() {
		String actual = name.getFullName();
		String expected = "java.lang.String";

		assertEquals(expected, actual);
	}

	@Test
	public void getFullNameGeneric() {
		String actual = genericName.getFullName();
		String expected = "java.util.HashMap`2[[T -> String, java.lang.String, VersionPlaceholder], "
				+ "[T -> String, java.lang.String, VersionPlaceholder]]";

		assertEquals(expected, actual);
	}

	@Test
	public void getName() {
		String actual = name.getName();
		String expected = "String";

		assertEquals(expected, actual);
	}

	@Test
	public void getNameGeneric() {
		String actual = genericName.getName();
		String expected = "HashMap";

		assertEquals(expected, actual);
	}

	@Test
	public void getAssembly() {
		String actual = name.getAssembly().getIdentifier();
		String expected = "java.lang";

		assertEquals(expected, actual);
	}

	@Test
	public void getAssemblyGeneric() {
		String actual = genericName.getAssembly().getIdentifier();
		String expected = "java.util";

		assertEquals(expected, actual);
	}

	@Test
	public void getNamespace() {
		String actual = name.getNamespace().getIdentifier();
		String expected = "java.lang";

		assertEquals(expected, actual);
	}
}
