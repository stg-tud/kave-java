package cc.kave.commons.model.groum;

import java.util.Set;

public interface IGroum {

	public int getNodeCount();

	public abstract Set<Node> getPredecessors(Node node);

	public abstract Set<Node> getSuccessors(Node node);

	public abstract Node getRoot();

}