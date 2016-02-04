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

import java.util.Set;
import static cc.kave.commons.model.groum.GroumBuilder.*;

import org.junit.Test;

import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.hasItem;
import static cc.kave.commons.model.groum.GroumTestUtils.*;

public class SubGroumTest {

	@Test(expected = IllegalArgumentException.class)
	public void doesntAcceptForeignNode() {
		Groum groum = createGroum("A");

		createSubGroum(groum, new TestNode("B"));
	}

	@Test
	public void inheritsEdges() {
		Node[] nodes = createNodes("A", "B");
		Groum parent = buildGroum(nodes).withEdge(nodes[0], nodes[1]).build();

		IGroum uut = createSubGroum(parent, nodes[0], nodes[1]);

		Set<Node> successors = uut.getSuccessors(nodes[0]);
		assertThat(successors, hasItem(nodes[1]));
	}
}
