package cc.kave.commons.model.groum;

import org.jgrapht.graph.DefaultEdge;

public class LabelledEdge extends DefaultEdge {
	private static final long serialVersionUID = 2878169640473163752L;

	String label = "";

	/**
	 * For internal use only
	 */
	@Deprecated
	public LabelledEdge() {
	}

	public LabelledEdge(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}
}
