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
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.model.names.BundleName;
import cc.kave.commons.model.names.csharp.CsAssemblyName;
import cc.kave.commons.model.names.csharp.CsAssemblyVersion;

public class CsAssemblyNameTest {

	@Test
	public void shouldImplementIsUnknown() {
		assertTrue(CsAssemblyName.UNKNOWN_NAME.isUnknown());
	}

	@Test
	public void shouldBeMSCorLibAssembly() {
		final String identifier = "mscorlib, 4.0.0.0";
		BundleName mscoreAssembly = CsAssemblyName.newAssemblyName(identifier);

		assertEquals("mscorlib", mscoreAssembly.getName());
		assertEquals("4.0.0.0", mscoreAssembly.getVersion().getIdentifier());
		assertEquals(identifier, mscoreAssembly.getIdentifier());
	}

	@Test
	public void shouldBeVersionlessAssembly() {
		final String identifier = "assembly";
		BundleName assemblyName = CsAssemblyName.newAssemblyName(identifier);

		assertEquals("assembly", assemblyName.getName());
		assertEquals(CsAssemblyVersion.UNKNOWN_NAME, assemblyName.getVersion());
		assertEquals(identifier, assemblyName.getIdentifier());
	}

	@Test
	public void shouldHaveUnknownVersionIfUnknown() {
		BundleName uut = CsAssemblyName.UNKNOWN_NAME;

		assertEquals("???", uut.getName());
		assertEquals(CsAssemblyVersion.UNKNOWN_NAME, uut.getVersion());
	}
}
