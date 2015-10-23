package cc.kave.commons.mining.episodes;

import javax.swing.JFrame;

import org.jgraph.JGraph;
import org.jgrapht.DirectedGraph;
import org.jgrapht.demo.JGraphAdapterDemo;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultListenableGraph;
import org.jgrapht.graph.DirectedMultigraph;

import cc.kave.commons.model.episodes.Fact;

public class GraphTest {

	//
	private JGraphModelAdapter<Fact, DefaultEdge> jgAdapter;

	/**
	 * An alternative starting point for this demo, to also allow running this
	 * applet as an application.
	 *
	 * @param args
	 *            ignored.
	 */
	public static void main(String[] args) {
		JGraphAdapterDemo applet = new JGraphAdapterDemo();
		applet.init();

		JFrame frame = new JFrame();
		frame.getContentPane().add(applet);
		frame.setTitle("JGraphT Adapter to JGraph Demo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * {@inheritDoc}
	 */
	public void init() {
		// create a JGraphT graph
		DirectedGraph<Fact, DefaultEdge> g = new DefaultDirectedGraph<Fact, DefaultEdge>(DefaultEdge.class);

		// create a visualization using JGraph, via an adapter
		jgAdapter = new JGraphModelAdapter<Fact, DefaultEdge>(g);

		JGraph jgraph = new JGraph(jgAdapter);

		Fact f1 = new Fact("a");
		Fact f2 = new Fact("b");
		Fact f3 = new Fact("c");
		Fact f4 = new Fact("d");

		// add some sample data (graph manipulated via JGraphT)
		g.addVertex(f1);
		g.addVertex(f2);
		g.addVertex(f3);
		g.addVertex(f4);

		g.addEdge(f1, f2);
		g.addEdge(f1, f3);
		g.addEdge(f1, f4);
		g.addEdge(f2, f4);
		g.addEdge(f3, f4);

		// position vertices nicely within JGraph component
		// setVertexLabel(f1, "a", "Text.<intin>");
		// setVertexLabel(f2, "b", "Text.setText");
		// setVertexLabel(f3, "c", "Text.setLocation");
		// setVertexLabel(f4, "d", "Text.close");

		// that's all there is to it!...
	}

	/**
	 * a listenable directed multigraph that allows loops and parallel edges.
	 */
	private static class ListenableDirectedMultigraph<V, E> extends DefaultListenableGraph<V, E>
			implements DirectedGraph<V, E> {
		private static final long serialVersionUID = 1L;

		ListenableDirectedMultigraph(Class<E> edgeClass) {
			super(new DirectedMultigraph<V, E>(edgeClass));
		}
	}
}
