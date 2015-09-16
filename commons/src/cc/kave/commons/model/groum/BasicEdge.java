package cc.kave.commons.model.groum;

import org.jgrapht.graph.DefaultEdge;

public class BasicEdge extends DefaultEdge {
	private static final long serialVersionUID = 2878169640473163752L;

	String label = "";

	public BasicEdge() {
	}

	public BasicEdge(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}
}
