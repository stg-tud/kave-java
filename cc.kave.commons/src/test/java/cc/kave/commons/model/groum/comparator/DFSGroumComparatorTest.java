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

import org.junit.Ignore;
import org.junit.Test;

import cc.kave.commons.model.groum.IGroum;

public class DFSGroumComparatorTest extends GroumComparatorConstractTest {

	@Override
	@Test
	@Ignore("fails this test nondeterministically (depends on the order of equal nodes in the underlying data structures)")
	public void equalNodesWithDifferentSuccessors() {
	}
	
	@Override
	@Test
	@Ignore("paths are equal -> indistinguishable")
	public void samePathsDifferentStructure() {
	}
	
	@Override
	@Test
	@Ignore("paths are equal -> indistinguishable")
	public void successorsOnDifferentEqualNodes2() {
	}

	@SuppressWarnings("deprecation")
	@Override
	protected Comparator<IGroum> createComparator() {
		return new DFSGroumComparator();
	}
}
