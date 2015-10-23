package cc.kave.commons.model.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.ComponentAttributeProvider;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.commons.model.episodes.Fact;

public class EpisodeAsGraphOutput {

	public static void main(String[] args) throws IOException {
		write();
		System.out.println("Done!");
	}

	private static void write() throws IOException {

		DirectedGraph<Fact, DefaultEdge> graph = new DefaultDirectedGraph<Fact, DefaultEdge>(DefaultEdge.class);

		VertexNameProvider<Fact> vertexId = new VertexNameProvider<Fact>() {
			public String getVertexName(Fact fact) {
				return fact.getRawFact();
			}
		};

		VertexNameProvider<Fact> vertexName = new VertexNameProvider<Fact>() {
			public String getVertexName(Fact fact) {
				return fact.getRawFact() + "";
			}
		};

		ComponentAttributeProvider<Fact> legend = new ComponentAttributeProvider<Fact>() {
			public Map<String, String> getComponentAttributes(Fact fact) {
				Map<String, String> map = new HashMap<String, String>();
				map.put(fact.getRawFact(), "ec");
				return map;
			}
		};

		Fact f1 = new Fact("10");
		Fact f2 = new Fact("20");
		Fact f3 = new Fact("30");
		Fact f4 = new Fact("40");

		// add some sample data (graph manipulated via JGraphT)
		graph.addVertex(f1);
		graph.addVertex(f2);
		graph.addVertex(f3);
		graph.addVertex(f4);

		graph.addEdge(f1, f2);
		graph.addEdge(f1, f3);
		graph.addEdge(f1, f4);
		graph.addEdge(f2, f4);
		graph.addEdge(f3, f4);

		FileStructure graphWithLabels = new FileStructure();
		graphWithLabels.graph = graph;

		DOTExporter<Fact, DefaultEdge> exporter = new DOTExporter<Fact, DefaultEdge>(vertexName, null, null, legend,
				null);

		String targetDirectory = "/Users/ervinacergani/Documents/PhD_work/episode-miner/graphs/";
		new File(targetDirectory).mkdirs();
		exporter.export(new FileWriter(targetDirectory + "initial-graph.dot"), graph);
	}

	private static class FileStructure {
		private DirectedGraph<Fact, DefaultEdge> graph;
		private String labels;
	}
}
