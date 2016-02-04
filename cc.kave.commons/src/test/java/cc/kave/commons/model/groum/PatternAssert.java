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
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import cc.kave.commons.model.groum.comparator.DFSGroumComparator;

import static org.junit.Assert.assertEquals;

public class PatternAssert {
	
	static void assertContainsPatterns(Set<IGroum> actuals, IGroum... expecteds) {
		TreeSet<IGroum> actual = new TreeSet<IGroum>(new DFSGroumComparator());
		actual.addAll(actuals);
		TreeSet<IGroum> expected = new TreeSet<IGroum>(new DFSGroumComparator());
		expected.addAll(Arrays.asList(expecteds));
		assertEquals(expected, actual);
	}

	static Set<IGroum> filterBySize(Set<IGroum> actuals, int size) {
		return actuals.stream().filter(g -> (g.getNodeCount() == size)).collect(Collectors.toSet());
	}

}
