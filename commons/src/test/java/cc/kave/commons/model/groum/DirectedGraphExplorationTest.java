package cc.kave.commons.model.groum;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DirectedGraphExplorationTest {

	@Test
	public void acceptsTwoEqualNodes() {
		DirectedGraph<Node, DefaultEdge> g = new DefaultDirectedGraph<Node, DefaultEdge>(DefaultEdge.class);
		g.addVertex(new TestNode("3"));
		g.addVertex(new TestNode("3"));
		assertEquals(2, g.vertexSet().size());
	}
}
