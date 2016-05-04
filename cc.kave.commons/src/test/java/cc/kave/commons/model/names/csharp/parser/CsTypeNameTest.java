/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.names.csharp.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.recommenders.exceptions.AssertionException;

public class CsTypeNameTest {

	private List<TypeNameTestCase> validTestCases;
	private List<String> invalidTestCases;

	@Before
	public void loadTestCases() {
		validTestCases = TypeTestLoader.validTypeNames();
		invalidTestCases = TypeTestLoader.invalidTypeNames();
		assertNotEquals(0, validTestCases.size());
		assertNotEquals(0, invalidTestCases.size());
	}

	@Test
	public void validTestCases() {
		for (TypeNameTestCase t : validTestCases) {
			ITypeName name = new CsTypeName(t.getIdentifier());
			assertEquals(t.getIdentifier(), name.getIdentifier());
			assertEquals(t.getIdentifier(), t.getNamespace(), name.getNamespace().getIdentifier());
			assertEquals(t.getIdentifier(), t.getAssembly(), name.getAssembly().getIdentifier());
		}
	}

	@Test
	public void invalidTestCases() {
		for (String t : invalidTestCases) {
			try {
				ITypeName name = new CsTypeName(t);
				fail("Invalid name validated:" + t);
			} catch (AssertionException e) {
			}
		}
	}
}
