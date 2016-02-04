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

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

public class GroumBuilder {

	private Set<Node> nodes;
	private Set<Pair<Node, Node>> edges;
	private String methodSignature;
	
	public static GroumBuilder buildGroum(String methodSignature, Node... nodes) {
		return new GroumBuilder(methodSignature).withNodes(nodes);
	}

	public static GroumBuilder buildGroum(Node... nodes) {
		return new GroumBuilder("<unknown>").withNodes(nodes);
	}

	public GroumBuilder(String methodSignature) {
		this.methodSignature = methodSignature;
		this.nodes = new HashSet<>();
		this.edges = new HashSet<>();
	}

	public GroumBuilder withNode(Node node) {
		nodes.add(node);
		return this;
	}

	public GroumBuilder withNodes(Node... nodes) {
		for (Node node : nodes) {
			withNode(node);
		}
		return this;
	}

	public GroumBuilder withEdge(Node source, Node target) {
		edges.add(Pair.of(source, target));
		return this;
	}

	public Groum build() {
		return new Groum(methodSignature, nodes, edges);
	}

	public boolean containsNode(Node node) {
		return nodes.contains(node);
	}

	public Set<Node> getPredecessors(Node node) {
		Set<Node> predecessors = new HashSet<>();
		for (Pair<Node, Node> edge : edges) {
			if (edge.getRight() == node) {
				predecessors.add(edge.getLeft());
			}
		}
		return predecessors;
	}

	public void removeEdge(Node source, Node target) {
		edges.remove(Pair.of(source, target));
	}

	public void removeNode(Node node) {
		nodes.remove(node);
	}
}