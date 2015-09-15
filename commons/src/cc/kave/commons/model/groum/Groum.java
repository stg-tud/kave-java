package cc.kave.commons.model.groum;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.commons.model.groum.comparator.DFSGroumComparator;
import cc.kave.commons.model.groum.comparator.HashCodeComparator;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

public class Groum implements IGroum {
	DirectedGraph<Node, DefaultEdge> groum;
	Node root;
	Boolean dirty;

	public Groum(Set<Node> nodes, Set<Pair<Node, Node>> edges) {
		groum = new DefaultDirectedGraph<Node, DefaultEdge>(DefaultEdge.class);
		nodes.forEach(node -> groum.addVertex(node));
		edges.forEach(edge -> groum.addEdge(edge.getLeft(), edge.getRight()));
	}

	public boolean containsNode(Node node) {
		return groum.containsVertex(node);
	}

	public int getNodeCount() {
		return groum.vertexSet().size();
	}

	@Override
	public Set<Node> getAllNodes() {
		return groum.vertexSet();
	}

	public int getEdgeCount() {
		return groum.edgeSet().size();
	}

	@Override
	public Set<Node> getSuccessors(Node node) {
		Set<Node> successors = new HashSet<>();
		Set<DefaultEdge> outgoingEdges = groum.outgoingEdgesOf(node);

		for (DefaultEdge edge : outgoingEdges) {
			successors.add(groum.getEdgeTarget(edge));
		}
		return successors;
	}

	@Override
	public String toString() {
		return groum.toString();
	}

	@Override
	public Node getRoot() {
		// TODO this is potentially used very frequently. The result should be
		// cached and invalidated when the Groum is changed.
		Node root = null;
		for (Node node : getAllNodes()) {
			if (groum.inDegreeOf(node) == 0) {
				if (root != null) {
					throw new IllegalStateException("groum has multiple roots: " + toString());
				}
				root = node;
			}
		}
		if (root == null) {
			throw new IllegalStateException("groum has no root: " + toString());
		}
		return root;
	}

	public Multimap<SubGroum, SubGroum> getAtomicSubGroums() {
		TreeMultimap<SubGroum, SubGroum> atomics = TreeMultimap.create(new DFSGroumComparator(),
				new HashCodeComparator());
		for (Node node : getAllNodes()) {
			Set<Node> nodes = Collections.singleton(node);
			SubGroum subGroum = new SubGroum(this, nodes);
			atomics.put(subGroum, subGroum);
		}
		return atomics;
	}
}
