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

import static cc.kave.commons.model.groum.GroumTestUtils.*;
import static cc.kave.commons.model.groum.GroumBuilder.*;

import org.junit.Test;

import com.google.common.collect.Multiset;

import static org.junit.Assert.*;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.Node;

public class ExasVectorBuilderNPathTest {

	private static Node[] nodes = createNodes("A", "B", "B", "C", "D");
	private static Groum groum = buildGroum(nodes)
			.withEdge(nodes[0], nodes[1])
			.withEdge(nodes[0], nodes[2])
			.withEdge(nodes[0], nodes[3])
			.withEdge(nodes[1], nodes[4]).build();
	
	@Test
	public void onePath() {
		Multiset<Path> nPaths = buildNPaths(groum);
		
		assertThat(nPaths, hasItem(nPath("A")));
	}
	
	@Test
	public void twoPaths() {
		Multiset<Path> nPaths = buildNPaths(groum);
		
		assertThat(nPaths, hasItems(nPath("A", "B"), nPath("A", "C")));
	}
	
	@Test
	public void duplicatedPath() {
		Multiset<Path> nPaths = buildNPaths(groum);
		
		assertEquals(2, nPaths.count(nPath("A", "B")));
	}
	
	@Test
	public void tailPaths(){
		Multiset<Path> nPaths = buildNPaths(groum);
		
		assertThat(nPaths, hasItem(nPath("B", "D")));
	}
	
	private Path nPath(String... nodeIds) {
		Node[] nodes = new Node[nodeIds.length];
		for (int i = 0; i < nodeIds.length; i++) {
			nodes[i] = createNode(nodeIds[i]);
		}
		return new Path(nodes);
	}

	private Multiset<Path> buildNPaths(IGroum groum) {
		return new ExasVectorBuilder().build(groum).getNPaths();
	}
}
