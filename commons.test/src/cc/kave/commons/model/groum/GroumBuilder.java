package cc.kave.commons.model.groum;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

public class GroumBuilder {

	private List<Node> nodes;
	private List<Pair<Node, Node>> edges;

	public static GroumBuilder buildGroum(Node... nodes) {
		return new GroumBuilder().withNodes(nodes);
	}

	public GroumBuilder() {
		this.nodes = new ArrayList<>();
		this.edges = new ArrayList<>();
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
		Groum groum = new Groum();
		for (Node node : nodes) {
			groum.addNode(node);
		}
		for (Pair<Node, Node> edge : edges) {
			groum.addEdge(edge.getLeft(), edge.getRight());
		}
		return groum;
	}
}