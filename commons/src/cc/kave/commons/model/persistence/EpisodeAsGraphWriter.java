package cc.kave.commons.model.persistence;

import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.ComponentAttributeProvider;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.commons.model.episodes.Fact;

public class EpisodeAsGraphWriter {

	// private File rootFolder;
	//
	// @Inject
	// public EpisodeAsGraphWriter(@Named("graph") File directory) {
	// assertTrue(directory.exists(), "Episode-miner folder does not exist");
	// assertTrue(directory.isDirectory(), "Episode-miner folder is not a
	// folder, but a file");
	// this.rootFolder = directory;
	// }

	public void write(DirectedGraph<Fact, DefaultEdge> graph, String fileName) throws IOException {

		VertexNameProvider<Fact> vertexId = new VertexNameProvider<Fact>() {
			public String getVertexName(Fact fact) {
				if (fact.getRawFact().contains("\\l")) {
					return "<LabelNode>";
				} else {
					return fact.getRawFact();
				}
			}
		};

		VertexNameProvider<Fact> vertexName = new VertexNameProvider<Fact>() {
			public String getVertexName(Fact fact) {
				return fact.getRawFact();
			}
		};

		ComponentAttributeProvider<Fact> vertexFormat = new ComponentAttributeProvider<Fact>() {
			public Map<String, String> getComponentAttributes(Fact fact) {
				Map<String, String> map = new LinkedHashMap<String, String>();
				if (fact.getRawFact().contains("\\l")) {
					map.put("shape", "rectangular");
				}
				return map;
			}

		};

		DOTExporter<Fact, DefaultEdge> exporter = new DOTExporter<Fact, DefaultEdge>(vertexId, vertexName, null,
				vertexFormat, null);

		exporter.export(new FileWriter(fileName), graph);
	}
}
