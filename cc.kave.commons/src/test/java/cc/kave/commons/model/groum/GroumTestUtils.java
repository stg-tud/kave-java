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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import cc.kave.commons.model.pattexplore.PattExplorer;
import static cc.kave.commons.model.groum.GroumBuilder.*;

public class GroumTestUtils {

	public static Groum createGroum(String... nodeIds) {
		Node[] nodes = createNodes(nodeIds);
		GroumBuilder builder = buildGroum(nodes);
		for (int i = 0; i < nodes.length - 1; i++) {
			builder.withEdge(nodes[i], nodes[i+1]);
		}
		return builder.build();
	}

	public static Node[] createNodes(String... nodeIds) {
		Node[] nodes = new Node[nodeIds.length];
		for (int i = 0; i < nodeIds.length; i++) {
			nodes[i] = createNode(nodeIds[i]);
		}
		return nodes;
	}

	public static TestNode createNode(String id) {
		return new TestNode(id);
	}

	public static Set<IGroum> findPatternsWithMinFrequency(int threshold,
			Groum... groums) {
		PattExplorer uut = new PattExplorer(threshold);
		return uut.explore(Arrays.asList(groums));
	}

	public static SubGroum createSubGroum(Groum parent, Node... nodes) {
		return new SubGroum(parent, new HashSet<>(Arrays.asList(nodes)));
	}
}
