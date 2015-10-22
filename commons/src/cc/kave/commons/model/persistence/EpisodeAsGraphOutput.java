package cc.kave.commons.model.persistence;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jgrapht.Graph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.graph.DefaultEdge;

public class EpisodeAsGraphOutput {

	public void write(Graph<Integer, DefaultEdge> graph) throws IOException {
		DOTExporter<Integer, DefaultEdge> exporter = new DOTExporter<Integer, DefaultEdge>();

		String targetDirectory = "/Users/ervinacergani/Documents/PhD_work/episode-miner/graphs/";
		new File(targetDirectory).mkdirs();
		exporter.export(new FileWriter(targetDirectory + "initial-graph.dot"), graph);

	}
}
