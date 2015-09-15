package cc.kave.commons.model.groum;

import java.util.Set;

public interface IGroum {

	public abstract Set<Node> getAllNodes();

	public int getNodeCount();

	public abstract Set<Node> getSuccessors(Node node);

	public abstract Node getRoot();

}