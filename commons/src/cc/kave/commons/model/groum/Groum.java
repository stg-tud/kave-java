package cc.kave.commons.model.groum;

import java.util.HashSet;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.commons.model.groum.comparator.DFSGroumComparator;
import cc.kave.commons.model.groum.comparator.HashCodeComparator;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

public class Groum implements Cloneable {
	DirectedGraph<Node, DefaultEdge> groum;
	Node root;
	Boolean dirty;

	public Groum() {
		groum = new DefaultDirectedGraph<Node, DefaultEdge>(DefaultEdge.class);
		dirty = true;
	}

	@Override
	public Object clone() {
		Groum clone;
		try {
			clone = (Groum) super.clone();
			clone.groum = new DefaultDirectedGraph<Node, DefaultEdge>(DefaultEdge.class);
			for (Node node : getAllNodes()) {
				clone.groum.addVertex(node);
			}
			for (DefaultEdge edge : groum.edgeSet()) {
				clone.groum.addEdge(groum.getEdgeSource(edge), groum.getEdgeTarget(edge));
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	public void addNode(Node node) {
		groum.addVertex(node);
		if (root == null)
			root = node;
	}

	public boolean containsNode(Node node) {
		return groum.containsVertex(node);
	}

	public int getNodeCount() {
		return groum.vertexSet().size();
	}

	public Set<Node> getAllNodes() {
		return groum.vertexSet();
	}

	public void addEdge(Node source, Node target) {
		groum.addEdge(source, target);
		dirty = true;
	}

	public int getEdgeCount() {
		return groum.edgeSet().size();
	}

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
			SubGroum subGroum = new SubGroum(this);
			subGroum.addNode(node);
			atomics.put(subGroum, subGroum);
		}
		return atomics;
	}
}
