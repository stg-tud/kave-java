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
}