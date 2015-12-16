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

import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.csharp.PropertyName;

public class PropertyNameTest {

	@Test
	public void shouldImplementIsUnknown() {
		assertTrue(PropertyName.UNKNOWN_NAME.isUnknown());
	}

	@Test
	public void shouldBeSimpleProperty() {
		final String valueTypeIdentifier = "A, B, 1.0.0.0";
		final String declaringTypeIdentifier = "C, D, 0.9.8.7";
		IPropertyName propertyName = PropertyName
				.newPropertyName("[" + valueTypeIdentifier + "] [" + declaringTypeIdentifier + "].Property");

		assertEquals("Property", propertyName.getName());
		assertEquals(valueTypeIdentifier, propertyName.getValueType().getIdentifier());
		assertEquals(declaringTypeIdentifier, propertyName.getDeclaringType().getIdentifier());
		assertFalse(propertyName.isStatic());
	}

	@Test
	public void shoudBePropertyWithGetter() {
		IPropertyName propertyName = PropertyName.newPropertyName("get [Z, Y, 0.5.6.1] [X, W, 0.3.4.2].Prop");

		assertTrue(propertyName.hasGetter());
	}

	@Test
	public void shoudBePropertyWithSetter() {
		IPropertyName propertyName = PropertyName.newPropertyName("set [Z, Y, 0.5.6.1] [X, W, 0.3.4.2].Prop");

		assertTrue(propertyName.hasSetter());
	}

	@Test
	public void shouldBeStaticProperty() {
		IPropertyName propertyName = PropertyName.newPropertyName("static [A, B, 1.2.3.4] [C, D, 5.6.7.8].E");

		assertTrue(propertyName.isStatic());
	}

	@Test
	public void handlesDelegateValueType() {
		IPropertyName propertyName = PropertyName.newPropertyName("[d:[R,A] [D,A].()] [D,B].P");

		assertEquals("P", propertyName.getName());
	}
}
