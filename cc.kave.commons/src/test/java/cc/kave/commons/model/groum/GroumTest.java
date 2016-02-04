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
import java.util.Collections;
import java.util.TreeSet;

import org.junit.Test;

import cc.kave.commons.model.groum.comparator.DFSGroumComparator;

import static org.junit.Assert.assertEquals;

import static cc.kave.commons.model.groum.GroumTestUtils.*;
import static cc.kave.commons.model.groum.GroumBuilder.*;

public class GroumTest {

	@Test
	public void producesSubGroumsOfSizeOne() {
		Node[] nodes = createNodes("A", "B");
		Groum groum = buildGroum(nodes).build();

		SubGroumMultiSet atomicSubGroums = SubGroum.getAtomicSubGroums(groum);

		assertContainsAll(atomicSubGroums, createSubGroum(groum, nodes[0]),
				createSubGroum(groum, nodes[1]));
	}

	@Test
	public void producesMultipleSubGroumsForEqualNodes() {
		Node[] nodes = createNodes("A", "A");
		Groum groum = buildGroum(nodes).build();

		SubGroumMultiSet atomicSubGroums = SubGroum.getAtomicSubGroums(groum);

		assertContainsAll(atomicSubGroums, createSubGroum(groum, nodes[0]),
				createSubGroum(groum, nodes[1]));
	}

	private SubGroum createSubGroum(Groum groum, Node node) {
		return new SubGroum(groum, Collections.singleton(node));
	}

	static void assertContainsAll(SubGroumMultiSet actuals, SubGroum... expecteds) {
		TreeSet<SubGroum> actual = new TreeSet<SubGroum>(new DFSGroumComparator());
		actual.addAll(actuals.getAllInstances());
		TreeSet<SubGroum> expected = new TreeSet<SubGroum>(new DFSGroumComparator());
		expected.addAll(Arrays.asList(expecteds));
		assertEquals(expected, actual);
	}
}
