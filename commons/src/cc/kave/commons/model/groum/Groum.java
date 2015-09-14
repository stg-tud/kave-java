package cc.kave.commons.model.groum;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.commons.model.groum.comparator.DFSGroumComparator;
import cc.kave.commons.model.groum.comparator.HashCodeComparator;

import com.google.common.collect.Multimap;
import com.google.common.collect.TreeMultimap;

public class Groum implements Comparable<Groum>, Cloneable {
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
			for (DefaultEdge edge : getAllEdges()) {
				clone.groum.addEdge(groum.getEdgeSource(edge), groum.getEdgeTarget(edge));
			}
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean equals(Object anotherGroum) {
		if (!(anotherGroum instanceof Groum))
			return false;
		else {
			return equals((Groum) anotherGroum);
		}
	}

	private boolean equals(Groum anotherGroum) {
		if (this.getAllNodes().size() != anotherGroum.getAllNodes().size())
			return false;

		List<Node> myNodes = new LinkedList<>();
		myNodes.addAll(this.getAllNodes());
		List<Node> otherNodes = new LinkedList<>();
		otherNodes.addAll(anotherGroum.getAllNodes());

		Collections.sort(myNodes);
		Collections.sort(otherNodes);

		for (int i = 0; i < myNodes.size(); i++) {
			if (!(myNodes.get(i).equals(otherNodes.get(i)))) {
				return false;
			}
			List<Node> mysuccessors = new LinkedList<>();
			List<Node> othersuccessors = new LinkedList<>();
			mysuccessors.addAll(getSuccessors(myNodes.get(i)));
			othersuccessors.addAll(anotherGroum.getSuccessors(otherNodes.get(i)));
			if (mysuccessors.size() != othersuccessors.size())
				return false;

			Collections.sort(mysuccessors);
			Collections.sort(othersuccessors);

			for (int x = 0; x < mysuccessors.size(); x++) {
				if (!(mysuccessors.get(x).equals(othersuccessors.get(x))))
					return false;
			}
		}
		return true;
	}

	public Set<DefaultEdge> getAllEdges() {
		return groum.edgeSet();
	}

	public boolean containsNode(Node node) {
		return groum.containsVertex(node);
	}

	public void addEdge(Node source, Node target) {
		groum.addEdge(source, target);
		dirty = true;
	}

	public void addNode(Node node) {
		groum.addVertex(node);
		if (root == null)
			root = node;
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

	public int getNodeCount() {
		return groum.vertexSet().size();
	}

	public int getEdgeCount() {
		return groum.edgeSet().size();
	}

	public Set<Node> getAllNodes() {
		return groum.vertexSet();
	}

	public int compareTo(Groum o) {
		if (o == null)
			return 1;
		else if (this.equals(o))
			return 0;
		else
			return toString().compareTo(o.toString());
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
