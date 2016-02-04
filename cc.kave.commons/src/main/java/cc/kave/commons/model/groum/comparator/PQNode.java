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

import cc.kave.commons.model.groum.Node;

public class PQNode implements Comparable<PQNode> {
	private final Node node;
	private final int fanIn;
	private final int fanOut;

	public PQNode(Node node, int fanIn, int fanOut) {
		this.node = node;
		this.fanIn = fanIn;
		this.fanOut = fanOut;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fanIn;
		result = prime * result + fanOut;
		result = prime * result + ((node == null) ? 0 : node.getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PQNode other = (PQNode) obj;
		return compareTo(other) == 0;
	}

	@Override
	public int compareTo(PQNode other) {
		int nodeCompare = node.compareTo(other.node);
		if (nodeCompare != 0) {
			return nodeCompare;
		}
		int fanInCompare = Integer.compare(fanIn, other.fanIn);
		if (fanInCompare != 0) {
			return fanInCompare;
		}
		int fanOutCompare = Integer.compare(fanOut, other.fanOut);
		if (fanOutCompare != 0) {
			return fanOutCompare;
		}
		return 0;
	}

	@Override
	public String toString() {
		return String.format("%s-%d-%d", node.getId(), fanIn, fanOut);
	}
}