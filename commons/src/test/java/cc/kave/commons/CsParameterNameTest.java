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

import org.junit.Test;

import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.csharp.CsParameterName;

public class CsParameterNameTest {

	private ParameterName name = CsParameterName
			.newParameterName("[org.eclipse.jdt.core.IPackageFragment, org.eclipse.jdt.core, VersionPlaceholder] packages");

	@Test
	public void getValueType() {
		String actual = name.getValueType().getIdentifier();
		String expected = "org.eclipse.jdt.core.IPackageFragment, org.eclipse.jdt.core, VersionPlaceholder";

		assertEquals(expected, actual);
	}

	@Test
	public void getName() {
		String actual = name.getName();
		String expected = "packages";

		assertEquals(expected, actual);
	}
}
