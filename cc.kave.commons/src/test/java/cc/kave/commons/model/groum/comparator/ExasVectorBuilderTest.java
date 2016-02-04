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

import static org.junit.Assert.*;

import cc.kave.commons.model.groum.Groum;
import cc.kave.commons.model.groum.Node;
import static cc.kave.commons.model.groum.GroumTestUtils.*;
import static cc.kave.commons.model.groum.GroumBuilder.*;

public class ExasVectorBuilderTest {

	@Test
	public void buildsVector() {
		Node[] nodes = createNodes("A", "B", "C");
		Groum groum = buildGroum(nodes).withEdge(nodes[0], nodes[1]).withEdge(nodes[1], nodes[2]).build();
		
		ExasVector vector = new ExasVectorBuilder().build(groum);
		
		assertFalse(vector.getNPaths().isEmpty());
		assertFalse(vector.getPQNodes().isEmpty());
	}
}
