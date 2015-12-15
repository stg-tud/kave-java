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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.model.names.NamespaceName;
import cc.kave.commons.model.names.csharp.CsNamespaceName;

public class CsGlobalNameSpaceTest {

	private static final NamespaceName GLOBAL_NAMESPACENAME = CsNamespaceName.getGlobalNamespace();

	@Test
	public void shouldHaveEmptyName() {
		assertTrue(GLOBAL_NAMESPACENAME.getName().equals(""));
	}

	@Test
	public void shouldBeGlobalNamespace() {
		assertTrue(GLOBAL_NAMESPACENAME.isGlobalNamespace());
	}

	@Test
	public void ShouldHaveEmptyIdentifier() {
		assertTrue(GLOBAL_NAMESPACENAME.getIdentifier().equals(""));
	}

	@Test
	public void shouldHaveNoParentNamespace() {
		assertNull(GLOBAL_NAMESPACENAME.getParentNamespace());
	}
}
