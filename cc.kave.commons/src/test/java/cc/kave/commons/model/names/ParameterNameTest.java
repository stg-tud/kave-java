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

import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.csharp.ParameterName;
import cc.kave.commons.model.names.csharp.TypeName;

public class ParameterNameTest {

	@Test
	public void shouldImplementIsUnknown() {
		assertTrue(ParameterName.getUnknownName().isUnknown());
	}

	@Test
	public void shouldBeSimpleParameter() {
		IParameterName parameterName = ParameterName.newParameterName("[ValueType, Assembly, 1.2.3.4] ParameterName");

		assertEquals("ValueType, Assembly, 1.2.3.4", parameterName.getValueType().getIdentifier());
		assertEquals("ParameterName", parameterName.getName());
		assertFalse(parameterName.isOptional());
		assertFalse(parameterName.isOutput());
		assertFalse(parameterName.isParameterArray());
		assertFalse(parameterName.isExtensionMethodParameter());
		assertTrue(parameterName.isPassedByReference());
	}

	@Test
	public void shouldBeOutputParameter() {
		IParameterName parameterName = ParameterName.newParameterName("out [VT, A, 1.0.0.0] PName");

		assertEquals("VT, A, 1.0.0.0", parameterName.getValueType().getIdentifier());
		assertEquals("PName", parameterName.getName());
		assertTrue(parameterName.isOutput());
	}

	@Test
	public void shouldBeValueParameter() {
		IParameterName parameterName = ParameterName.newParameterName("[System.Single, mscore, 4.0.0.0] i");

		assertFalse(parameterName.isPassedByReference());
	}

	@Test
	public void shouldBeReferenceParameter() {
		IParameterName parameterName = ParameterName.newParameterName("ref [System.Single, mscore, 4.0.0.0] i");

		assertTrue(parameterName.isPassedByReference());
	}

	@Test
	public void shouldBeParameterArray() {
		IParameterName parameterName = ParameterName.newParameterName("params [T, P, 1.3.2.4] name");

		assertTrue(parameterName.isParameterArray());
	}

	@Test
	public void shouldNoBeParameterArray() {
		IParameterName parameterName = ParameterName.newParameterName("[T[], P, 1.3.2.4] name");

		assertTrue(parameterName.getValueType().isArray());
		assertFalse(parameterName.isParameterArray());
	}

	@Test
	public void shouldHaveDefaultValue() {
		IParameterName parameterName = ParameterName.newParameterName("opt [T, A, 4.3.2.1] p");

		assertTrue(parameterName.isOptional());
	}

	@Test
	public void shouldBeExtensionMethodParameter() {
		IParameterName parameterName = ParameterName.newParameterName("this [T, A, 4.3.2.1] p");
		assertTrue(parameterName.isExtensionMethodParameter());
	}

	@Test
	public void shouldBeOptionalReferenceParameter() {
		IParameterName parameterName = ParameterName
				.newParameterName("opt ref [System.Double, mscore, 4.0.0.0] param");

		assertTrue(parameterName.isOptional());
		assertTrue(parameterName.isPassedByReference());
		assertFalse(parameterName.isOutput());
		assertFalse(parameterName.isParameterArray());
	}

	public void shouldBeUnknownParameter() {
		assertEquals(TypeName.getUnknownName(), ParameterName.getUnknownName().getValueType());
		assertEquals("???", ParameterName.getUnknownName().getName());
	}

}
