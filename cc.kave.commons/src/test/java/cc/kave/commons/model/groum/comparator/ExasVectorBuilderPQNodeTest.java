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
package cc.kave.commons.model.groum.comparator;


import org.junit.Test;

import com.google.common.collect.Multiset;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;

import static org.junit.Assert.*;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.Node;
import static cc.kave.commons.model.groum.GroumBuilder.*;
import static cc.kave.commons.model.groum.GroumTestUtils.*;

public class ExasVectorBuilderPQNodeTest {

	@Test
	public void singleNode() {
		Node[] nodes = createNodes("A");
		Groum groum = buildGroum(nodes).build();
		
		Multiset<PQNode> pqNodes = buildPQNodes(groum);
		
		assertThat(pqNodes, hasItem(pqNode("A", 0, 0)));
	}
	
	@Test
	public void withSuccessor() {
		Node[] nodes = createNodes("A", "B");
		Groum groum = buildGroum(nodes)
				.withEdge(nodes[0], nodes[1]).build();

		Multiset<PQNode> pqNodes = buildPQNodes(groum);
		
		assertThat(pqNodes, hasItems(
				pqNode("A", 0, 1),
				pqNode("B", 1, 0)));
	}
	
	@Test
	public void withSuccessors() {
		Node[] nodes = createNodes("A", "B", "C");
		Groum groum = buildGroum(nodes)
				.withEdge(nodes[0], nodes[1])
				.withEdge(nodes[0], nodes[2]).build();

		Multiset<PQNode> pqNodes = buildPQNodes(groum);
		
		assertThat(pqNodes, hasItems(
				pqNode("A", 0, 2),
				pqNode("B", 1, 0),
				pqNode("C", 1, 0)));
	}
	
	@Test
	public void withMergePoint() {
		Node[] nodes = createNodes("A", "B", "C", "D");
		Groum groum = buildGroum(nodes)
				.withEdge(nodes[0], nodes[1])
				.withEdge(nodes[0], nodes[2])
				.withEdge(nodes[1], nodes[3])
				.withEdge(nodes[2], nodes[3]).build();

		Multiset<PQNode> pqNodes = buildPQNodes(groum);

		assertThat(pqNodes, hasItems(
				pqNode("A", 0, 2),
				pqNode("B", 1, 1),
				pqNode("C", 1, 1),
				pqNode("D", 2, 0)));
	}
	
	@Test
	public void duplicate() {
		Node[] nodes = createNodes("A", "B", "B");
		Groum groum = buildGroum(nodes)
				.withEdge(nodes[0], nodes[1])
				.withEdge(nodes[0], nodes[2]).build();
		
		Multiset<PQNode> pqNodes = buildPQNodes(groum);
		
		assertEquals(2, pqNodes.count(pqNode("B", 1, 0)));
	}
	
	private PQNode pqNode(String nodeId, int fanIn, int fanOut) {
		return new PQNode(createNode(nodeId), fanIn, fanOut);
	}

	private Multiset<PQNode> buildPQNodes(IGroum groum) {
		return new ExasVectorBuilder().build(groum).getPQNodes();
	}
}
