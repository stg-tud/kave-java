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

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;

import static org.junit.Assert.assertThat;

import static cc.kave.commons.model.groum.GroumBuilder.*;

public class GroumBuilderTest {

	@Test
	public void singleNode() {
		TestNode node = new TestNode("A");

		Groum groum = buildGroum(node).build();

		assertThat(groum.getAllNodes(), hasItem(node));
	}

	@Test
	public void multipleNodes() {
		TestNode nodeA = new TestNode("A");
		TestNode nodeB = new TestNode("B");

		Groum groum = buildGroum(nodeA, nodeB).build();

		assertThat(groum.getAllNodes(), hasItems(nodeA, nodeB));
	}

	@Test
	public void singleEdge() {
		TestNode nodeA = new TestNode("A");
		TestNode nodeB = new TestNode("B");

		Groum groum = buildGroum(nodeA, nodeB).withEdge(nodeA, nodeB).build();

		assertThat(groum.getSuccessors(nodeA), hasItem(nodeB));
	}

	@Test
	public void multipleEdges() {
		TestNode nodeA = new TestNode("A");
		TestNode nodeB = new TestNode("B");

		Groum groum = buildGroum(nodeA, nodeB).withEdge(nodeA, nodeB)
				.withEdge(nodeB, nodeA).build();

		assertThat(groum.getSuccessors(nodeA), hasItem(nodeB));
		assertThat(groum.getSuccessors(nodeB), hasItem(nodeA));
	}
}
