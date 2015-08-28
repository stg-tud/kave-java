package cc.kave.commons.model.groum;

import java.util.List;
import java.util.Set;

public interface IGroum extends Comparable<IGroum> {

	public boolean containsNode(INode node);

	public INode getNode(INode node);

	public boolean containsEdge(INode source, INode target);

	public void addEdge(INode source, INode target);

	public void addVertex(INode node);

	public Set<INode> getSuccessors(INode node);

	public int getVertexCount();

	public int getEdgeCount();

	public Set<INode> getAllNodes();

	public INode getRoot();

	public INode getLeaf();

	public Set<INode> getEqualNodes(INode reference);

	public List<ISubGroum> getSubgraphs(IGroum groum);

	public boolean containsEqualNode(INode node);

}
