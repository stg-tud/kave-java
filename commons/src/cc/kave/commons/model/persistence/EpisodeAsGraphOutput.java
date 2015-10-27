package cc.kave.commons.model.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jgrapht.DirectedGraph;
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

		VertexNameProvider<Fact> vertexName = new VertexNameProvider<Fact>() {
			public String getVertexName(Fact fact) {
				return fact.getRawFact();
			}
		};

		Fact f1 = new Fact("10");
		Fact f2 = new Fact("20");
		Fact f3 = new Fact("30");
		Fact f4 = new Fact("40");

		Fact f5 = new Fact("50");
		Fact f6 = new Fact("60");
		Fact f7 = new Fact("70");
		Fact f8 = new Fact("80");

		// add some sample data (graph manipulated via JGraphT)
		graph.addVertex(f1);
		graph.addVertex(f2);
		graph.addVertex(f3);
		graph.addVertex(f4);
		
		graph.addVertex(f5);
		graph.addVertex(f6);
		graph.addVertex(f7);
		graph.addVertex(f8);

		graph.addEdge(f1, f2);
		graph.addEdge(f1, f3);
		graph.addEdge(f1, f4);
		graph.addEdge(f2, f4);
		graph.addEdge(f3, f4);
		
		graph.addEdge(f5, f6);
		graph.addEdge(f6, f7);
		graph.addEdge(f7, f8);
		

		DOTExporter<Fact, DefaultEdge> exporter = new DOTExporter<Fact, DefaultEdge>(vertexName, null, null);

		String targetDirectory = "/Users/ervinacergani/Documents/PhD_work/episode-miner/graphs/";
		new File(targetDirectory).mkdirs();
		exporter.export(new FileWriter(targetDirectory + "initial-graph.dot"), graph);

		// BufferedWriter bw = new BufferedWriter(new FileWriter(targetDirectory
		// + "initial-graph.dot", true));
		// bw.newLine();
		// bw.newLine();
		// bw.newLine();
		// bw.write("Show graph legend!");
		// bw.close();

		// PrintWriter writer = new PrintWriter(targetDirectory +
		// "initial-graph.dot");
		// writer.println("Show graph legend!");
		// writer.close();
	}
}
