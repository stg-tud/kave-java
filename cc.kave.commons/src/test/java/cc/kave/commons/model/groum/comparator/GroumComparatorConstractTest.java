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

import java.util.Comparator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.Node;
import static cc.kave.commons.model.groum.GroumTestUtils.*;
import static cc.kave.commons.model.groum.GroumBuilder.*;
public abstract class GroumComparatorConstractTest {

	@Test
	public void singleEqualNode() {
		assertEqualGroums(createGroum("A"), createGroum("A"));
	}

	@Test
	public void singleUnequalNode() {
		assertDifferentGroums(createGroum("A"), createGroum("B"));
	}
	
	@Test
	public void threeNodesEqual() {
		Node[] nodes1 = createNodes("A", "B", "C");
		Groum groum1 = buildGroum(nodes1)
				.withEdge(nodes1[0], nodes1[1])
				.withEdge(nodes1[0], nodes1[2]).build();

		Node[] nodes2 = createNodes("A", "B", "C");
		Groum groum2 = buildGroum(nodes2)
				.withEdge(nodes2[0], nodes2[1])
				.withEdge(nodes2[0], nodes2[2]).build();
		
		assertEqualGroums(groum1, groum2);
	}
	
	/*
	 *  A                       A
	 *  | \  is different from  | \
	 *  B  C                    B  D
	 *  
	 *  The first Groum is considered smaller, since node C is smaller than
	 *  node D.
	 */
	@Test
	public void threeNodesDifferentNodes() {
		Node[] nodes1 = createNodes("A", "B", "C");
		Groum groum1 = buildGroum(nodes1)
				.withEdge(nodes1[0], nodes1[1])
				.withEdge(nodes1[0], nodes1[2]).build();

		Node[] nodes2 = createNodes("A", "B", "D");
		Groum groum2 = buildGroum(nodes2)
				.withEdge(nodes2[0], nodes2[1])
				.withEdge(nodes2[0], nodes2[2]).build();
		
		assertDifferentGroums(groum1, groum2);
	}
	
	/*
	 *   A
	 *   | \  is different from  A -> B -> C
	 *   B  C
	 *   
	 *   The first Groum is considered smaller, since it's node B has less
	 *   successors.
	 */
	@Test
	public void threeNodesDifferentEdges() {
		Node[] nodes1 = createNodes("A", "B", "C");
		Groum groum1 = buildGroum(nodes1)
				.withEdge(nodes1[0], nodes1[1])
				.withEdge(nodes1[0], nodes1[2]).build();

		Node[] nodes2 = createNodes("A", "B", "C");
		Groum groum2 = buildGroum(nodes2)
				.withEdge(nodes2[0], nodes2[1])
				.withEdge(nodes2[1], nodes2[2]).build();
		
		assertDifferentGroums(groum1, groum2);
	}
	
	/*
	 *   A                                A -> C
	 *  |   \  is indistinguishable from  |
	 *  B -> C                            B -> C
	 */
	@Test
	public void samePathsDifferentStructure() {
		Node[] nodes1 = createNodes("A", "B", "C");
		Groum groum1 = buildGroum(nodes1)
				.withEdge(nodes1[0], nodes1[1])
				.withEdge(nodes1[0], nodes1[2])
				.withEdge(nodes1[1], nodes1[2]).build();

		Node[] nodes2 = createNodes("A", "B", "C", "C");
		Groum groum2 = buildGroum(nodes2)
				.withEdge(nodes2[0], nodes2[1])
				.withEdge(nodes2[0], nodes2[2])
				.withEdge(nodes2[1], nodes2[3]).build();
		
		assertDifferentGroums(groum2, groum1);
	}
	
	/*
	 * A -> B -> C               A -> B
	 * |            is equal to  |
	 * B                         B -> C
	 * 
	 * but the comparator currently fail to detect that sometimes, if the "B"
	 * nodes' order is flipped.
	 */
	@Test
	public void equalNodesWithDifferentSuccessors() {
		Node[] nodes1 = createNodes("A", "B", "B", "C");
		Groum groum1 = buildGroum(nodes1)
				.withEdge(nodes1[0], nodes1[1])
				.withEdge(nodes1[0], nodes1[2])
				.withEdge(nodes1[1], nodes1[3])
				.build();

		createNodes("A", "B", "B", "C");
		Groum groum2 = buildGroum(nodes1)
				.withEdge(nodes1[0], nodes1[1])
				.withEdge(nodes1[0], nodes1[2])
				.withEdge(nodes1[2], nodes1[3])
				.build();

		assertEqualGroums(groum1, groum2);
	}
	
	/*
	 * A -> B -> C                   A -> B -> C
	 * |			is not equal to  |    |
	 * B -> D                        B    D
	 */
	@Test
	public void successorsOnDifferentEqualNodes() {
		Node[] nodes1 = createNodes("A", "B", "B", "C", "D");
		Groum groum1 = buildGroum(nodes1)
				.withEdge(nodes1[0], nodes1[1])
				.withEdge(nodes1[0], nodes1[2])
				.withEdge(nodes1[1], nodes1[3])
				.withEdge(nodes1[2], nodes1[4]).build();
		

		Node[] nodes2 = createNodes("A", "B", "B", "C", "D");
		Groum groum2 = buildGroum(nodes2)
				.withEdge(nodes2[0], nodes2[1])
				.withEdge(nodes2[0], nodes2[2])
				.withEdge(nodes2[1], nodes2[3])
				.withEdge(nodes2[1], nodes2[4]).build();
		
		assertDifferentGroums(groum1, groum2);
	}
	
	/*
	 * A -> B -> C                   A -> B -> C
	 * |			is not equal to  |    |
	 * B -> D                        B    D
	 * |                             |
	 * E                             E
	 */
	@Test
	public void successorsOnDifferentEqualNodes2() {
		Node[] nodes1 = createNodes("A", "B", "B", "C", "D", "E");
		Groum groum1 = buildGroum(nodes1)
				.withEdge(nodes1[0], nodes1[1])
				.withEdge(nodes1[0], nodes1[2])
				.withEdge(nodes1[1], nodes1[3])
				.withEdge(nodes1[2], nodes1[4])
				.withEdge(nodes1[2], nodes1[5]).build();
		

		Node[] nodes2 = createNodes("A", "B", "B", "C", "D", "E");
		Groum groum2 = buildGroum(nodes2)
				.withEdge(nodes2[0], nodes2[1])
				.withEdge(nodes2[0], nodes2[2])
				.withEdge(nodes2[1], nodes2[3])
				.withEdge(nodes2[1], nodes2[4])
				.withEdge(nodes2[2], nodes2[5]).build();
		
		assertDifferentGroums(groum1, groum2);
	}

	private void assertDifferentGroums(Groum groum1, Groum groum2) {
		int comparison = createComparator().compare(groum1, groum2);
		assertNotEquals(0, comparison);
	}

	private void assertEqualGroums(Groum groum1, Groum groum2) {
		int comparison = createComparator().compare(groum1, groum2);
		assertEquals(0, comparison);
	}

	protected abstract Comparator<IGroum> createComparator();
}
