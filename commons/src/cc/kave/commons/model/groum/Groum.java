package cc.kave.commons.model.groum;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class Groum implements Comparable<Groum>, Cloneable {
	DirectedGraph<INode, DefaultEdge> groum;
	INode root;
	Boolean dirty;

	public Groum() {
		groum = new DefaultDirectedGraph<INode, DefaultEdge>(DefaultEdge.class);
		dirty = true;
	}

	@Override
	public Object clone() {
		Groum clone;
		try {
			clone = (Groum) super.clone();
			clone.groum = new DefaultDirectedGraph<INode, DefaultEdge>(DefaultEdge.class);
			for (INode node : getAllNodes()) {
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

		List<INode> myNodes = new LinkedList<>();
		myNodes.addAll(this.getAllNodes());
		List<INode> otherNodes = new LinkedList<>();
		otherNodes.addAll(anotherGroum.getAllNodes());

		Collections.sort(myNodes);
		Collections.sort(otherNodes);

		for (int i = 0; i < myNodes.size(); i++) {
			if (!(myNodes.get(i).equals(otherNodes.get(i)))) {
				return false;
			}
			List<INode> mysuccessors = new LinkedList<>();
			List<INode> othersuccessors = new LinkedList<>();
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

	public INode getNode(INode node) {
		if (groum.containsVertex(node)) {
			for (INode aNode : groum.vertexSet()) {
				if (aNode == node)
					return node;
			}
		} else {
			return null;
		}
		return null;
	}

	public boolean containsEqualNode(INode node) {
		for (INode aNode : groum.vertexSet()) {
			if (node.equals(aNode))
				return true;
		}
		return false;
	}

	public boolean containsNode(INode node) {
		return groum.containsVertex(node);
	}

	public boolean containsEdge(INode source, INode target) {
		return groum.containsEdge(source, target);
	}

	public void addEdge(INode source, INode target) {
		groum.addEdge(source, target);
		dirty = true;
	}

	public void addVertex(INode node) {
		groum.addVertex(node);
		if (root == null)
			root = node;
	}

	public Set<INode> getSuccessors(INode node) {
		Set<INode> successors = new HashSet<>();
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

	public int getVertexCount() {
		return groum.vertexSet().size();
	}

	public int getEdgeCount() {
		return groum.edgeSet().size();
	}

	public Set<INode> getAllNodes() {
		return groum.vertexSet();
	}

	public Set<INode> getEqualNodes(INode reference) {

		Set<INode> equalNodes = new HashSet<>();
		for (INode node : groum.vertexSet()) {
			if (node.equals(reference))
				equalNodes.add(node);
		}
		if (equalNodes.size() == 0)
			return null;
		return equalNodes;
	}

	public int compareTo(Groum o) {
		if (o == null)
			return 1;
		else if (this.equals(o))
			return 0;
		else
			return toString().compareTo(o.toString());
	}

	/*
	 * Groums will have one root?
	 * 
	 * @see cc.kave.commons.model.groum.IGroum#getRoot()
	 */
	@Deprecated
	public INode getRoot() {
		if (dirty) {
			int count = 0;
			for (INode node : getAllNodes()) {
				if (groum.incomingEdgesOf(node).size() == 0) {
					count++;
					root = node;
				}
			}
			if (count != 1)
				throw new RuntimeException("Groum has more then one vertices with no incoming edges.");
		}
		return root;
	}

	/*
	 * There can be several leaves
	 * 
	 * @see cc.kave.commons.model.groum.IGroum#getLeaf()
	 */
	@Deprecated
	public INode getLeaf() {
		for (INode node : getAllNodes()) {
			if (groum.outDegreeOf(node) == 0)
				return node;
		}
		return null;
	}
}
