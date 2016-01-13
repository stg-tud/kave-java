package cc.kave.commons.model.groum;

public abstract class Node implements Comparable<Node> {

	public abstract String getId();

	@Override
	public int compareTo(Node o) {
		return this.getId().compareTo(o.getId());
	}

	@Override
	public String toString() {
		return getId();
	}
}
