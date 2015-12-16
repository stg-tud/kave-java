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

import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.TypeName;

public class FieldNameTest {

	private final String identifier = "[b.ValueType, B, 1.3.3.7] [a.ValueType, A, 4.2.2.3].fieldName";

	@Test
	public void shouldImplementIsUnknown() {
		assertTrue(FieldName.UNKNOWN_NAME.isUnknown());
	}

	@Test
	public void shouldBeInstanceFieldName() {
		IFieldName fieldName = FieldName.newFieldName(identifier);

		assertEquals(identifier, fieldName.getIdentifier());
		assertEquals("fieldName", fieldName.getName());
		assertEquals("a.ValueType, A, 4.2.2.3", fieldName.getDeclaringType().getIdentifier());
		assertEquals("b.ValueType, B, 1.3.3.7", fieldName.getValueType().getIdentifier());
		assertFalse(fieldName.isStatic());
	}

	@Test
	public void shouldBeStaticFieldName() {
		IFieldName fieldName = FieldName.newFieldName("static " + identifier);

		assertTrue(fieldName.isStatic());
	}

	@Test
	public void shouldBeFieldWithTypeParameters() {
		final String valueTypeIdentifier = "T`1[[A, B, 1.0.0.0]], A, 9.1.8.2";
		final String declaringTypeIdentifier = "U`2[[B, C, 6.7.5.8],[C, D, 8.3.7.4]], Z, 0.0.0.0";
		IFieldName fieldName = FieldName
				.newFieldName("[" + valueTypeIdentifier + "] [" + declaringTypeIdentifier + "].bar");

		assertEquals("bar", fieldName.getName());
		assertEquals(valueTypeIdentifier, fieldName.getValueType().getIdentifier());
		assertTrue(fieldName.getValueType().hasTypeParameters());
		assertEquals(declaringTypeIdentifier, fieldName.getDeclaringType().getIdentifier());
		assertTrue(fieldName.getDeclaringType().hasTypeParameters());
	}

	@Test
	public void shouldBeUnknownField() {
		assertEquals(TypeName.UNKNOWN_NAME, FieldName.UNKNOWN_NAME.getValueType());
		assertEquals(TypeName.UNKNOWN_NAME, FieldName.UNKNOWN_NAME.getDeclaringType());
		assertEquals("???", FieldName.UNKNOWN_NAME.getName());
	}

	@Test
	public void handlesDelegateValueType() {
		IFieldName fieldName = FieldName.newFieldName("[d:[V,A] [D,A].()] [D,A].fieldName");

		assertEquals("fieldName", fieldName.getName());
		assertEquals("d:[V,A] [D,A].()", fieldName.getValueType().getIdentifier());
	}

}
