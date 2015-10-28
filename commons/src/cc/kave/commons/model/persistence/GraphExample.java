package cc.kave.commons.model.persistence;

import java.io.FileWriter;
import java.io.IOException;

import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.commons.model.episodes.Fact;

public class GraphExample {

	public static void main(String[] args) throws IOException {
		Fact fact1 = new Fact("100");
		Fact fact2 = new Fact("200");
		Fact fact3 = new Fact("300");
		Fact fact4 = new Fact("400");

		Fact fact5 = new Fact("n100getText");
		Fact fact6 = new Fact("n200setText");
		Fact fact7 = new Fact("n300Visitor");
		Fact fact8 = new Fact("n400setLocation");

		DirectedGraph<Fact, DefaultEdge> graph = new DefaultDirectedGraph<Fact, DefaultEdge>(DefaultEdge.class);

		graph.addVertex(fact1);
		graph.addVertex(fact2);
		graph.addVertex(fact3);
		graph.addVertex(fact4);

		graph.addEdge(fact1, fact2);
		graph.addEdge(fact1, fact3);
		graph.addEdge(fact1, fact4);
		graph.addEdge(fact2, fact4);
		graph.addEdge(fact3, fact4);

		graph.addVertex(fact5);
		graph.addVertex(fact6);
		graph.addVertex(fact7);
		graph.addVertex(fact8);

		graph.addEdge(fact5, fact6);
		graph.addEdge(fact6, fact7);
		graph.addEdge(fact7, fact8);

		VertexNameProvider<Fact> vertexName = new VertexNameProvider<Fact>() {
			public String getVertexName(Fact fact) {
				return fact.getRawFact();
			}
		};

		DOTExporter<Fact, DefaultEdge> exporter = new DOTExporter<Fact, DefaultEdge>(vertexName, null, null);

		String fileIndex = "/Users/ervinacergani/Documents/PhD_work/episode-miner/graphs/testGraph.dot";

		exporter.export(new FileWriter(fileIndex), graph);

		System.out.println("Done!");
	}
}
