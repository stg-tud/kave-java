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
package cc.kave.commons.model.groum;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import static cc.kave.commons.model.groum.GroumBuilder.*;

public class GroumRootTest {

	@Test
	public void singleNodeIsRoot() {
		TestNode node = new TestNode("A");
		Groum groum = buildGroum(node).build();

		assertRoot(node, groum);
	}

	@Test
	public void nodeWithoutIncomingEdgeIsRoot() {
		TestNode node1 = new TestNode("A");
		TestNode node2 = new TestNode("B");
		TestNode node3 = new TestNode("C");
		Groum groum = buildGroum(node2, node1, node3).withEdge(node1, node2)
				.withEdge(node1, node3).build();

		assertRoot(node1, groum);
	}

	@Test(expected = IllegalStateException.class)
	public void multipleRootsFails() {
		Groum groum = buildGroum(new TestNode("A"), new TestNode("B")).build();

		groum.getRoot();
	}

	private void assertRoot(TestNode node, Groum groum) {
		Node root = groum.getRoot();
		assertEquals(node, root);
	}
}
