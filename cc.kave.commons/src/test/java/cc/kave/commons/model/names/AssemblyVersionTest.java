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

import org.junit.Test;

import cc.kave.commons.model.names.IAssemblyVersion;
import cc.kave.commons.model.names.csharp.AssemblyVersion;

public class AssemblyVersionTest {

	IAssemblyVersion version = AssemblyVersion.newAssemblyVersion("12.30.17.42");

	@Test
	public void getMajor() {
		int actual = version.getMajor();
		int expected = 12;

		assertEquals(expected, actual);
	}

	@Test
	public void getMinor() {
		int actual = version.getMinor();
		int expected = 30;

		assertEquals(expected, actual);
	}

	@Test
	public void getBuild() {
		int actual = version.getBuild();
		int expected = 17;

		assertEquals(expected, actual);
	}

	@Test
	public void getRevision() {
		int actual = version.getRevision();
		int expected = 42;

		assertEquals(expected, actual);
	}
}
