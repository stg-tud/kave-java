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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.names.csharp.CsPropertyName;

public class CsPropertyNameTest {

	@Test
	public void shouldImplementIsUnknown() {
		assertTrue(CsPropertyName.UNKNOWN_NAME.isUnknown());
	}

	@Test
	public void shouldBeSimpleProperty() {
		final String valueTypeIdentifier = "A, B, 1.0.0.0";
		final String declaringTypeIdentifier = "C, D, 0.9.8.7";
		PropertyName propertyName = CsPropertyName
				.newPropertyName("[" + valueTypeIdentifier + "] [" + declaringTypeIdentifier + "].Property");

		assertEquals("Property", propertyName.getName());
		assertEquals(valueTypeIdentifier, propertyName.getValueType().getIdentifier());
		assertEquals(declaringTypeIdentifier, propertyName.getDeclaringType().getIdentifier());
		assertFalse(propertyName.isStatic());
	}

	@Test
	public void shoudBePropertyWithGetter() {
		PropertyName propertyName = CsPropertyName.newPropertyName("get [Z, Y, 0.5.6.1] [X, W, 0.3.4.2].Prop");

		assertTrue(propertyName.hasGetter());
	}

	@Test
	public void shoudBePropertyWithSetter() {
		PropertyName propertyName = CsPropertyName.newPropertyName("set [Z, Y, 0.5.6.1] [X, W, 0.3.4.2].Prop");

		assertTrue(propertyName.hasSetter());
	}

	@Test
	public void shouldBeStaticProperty() {
		PropertyName propertyName = CsPropertyName.newPropertyName("static [A, B, 1.2.3.4] [C, D, 5.6.7.8].E");

		assertTrue(propertyName.isStatic());
	}

	@Test
	public void handlesDelegateValueType() {
		PropertyName propertyName = CsPropertyName.newPropertyName("[d:[R,A] [D,A].()] [D,B].P");

		assertEquals("P", propertyName.getName());
	}
}
