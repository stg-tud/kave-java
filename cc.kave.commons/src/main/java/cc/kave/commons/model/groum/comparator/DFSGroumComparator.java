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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cc.kave.commons.model.groum.IGroum;
import cc.kave.commons.model.groum.Node;

@Deprecated
public class DFSGroumComparator implements Comparator<IGroum> {

	@Override
	public int compare(IGroum groum1, IGroum groum2) {
		Node root1 = groum1.getRoot();
		Node root2 = groum2.getRoot();
		return compare(root1, groum1, root2, groum2);
	}

	private int compare(Node node1, IGroum groum1, Node node2, IGroum groum2) {
		int equalNodes = node1.compareTo(node2);
		if (equalNodes != 0) {
			return equalNodes;
		}
		List<Node> successors1 = new ArrayList<Node>(groum1.getSuccessors(node1));
		List<Node> successors2 = new ArrayList<Node>(groum2.getSuccessors(node2));
		if (successors1.size() != successors2.size()) {
			return (int) Math.signum(successors2.size() - successors1.size());
		}

		Collections.sort(successors1);
		Collections.sort(successors2);

		for (int i = 0; i < successors1.size(); i++) {
			int compare = compare(successors1.get(i), groum1, successors2.get(i), groum2);
			if (compare != 0) {
				return compare;
			}
		}
		return 0;
	}

}
