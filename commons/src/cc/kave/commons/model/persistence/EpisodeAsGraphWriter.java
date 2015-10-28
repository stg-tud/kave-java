package cc.kave.commons.model.persistence;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.VertexNameProvider;
import org.jgrapht.graph.DefaultEdge;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Fact;

public class EpisodeAsGraphWriter {

	private File rootFolder;

	// private EpisodeParser parser;
	// private MaximalFrequentEpisodes episodeLearned;
	// private EpisodeToGraphConverter graphConverter;
	// private EventMappingParser mappingParser;

	@Inject
	public EpisodeAsGraphWriter(@Named("graph") File directory) {
		assertTrue(directory.exists(), "Episode-miner folder does not exist");
		assertTrue(directory.isDirectory(), "Episode-miner folder is not a folder, but a file");
		this.rootFolder = directory;
	}

	// public void run() throws Exception {
	// Map<Integer, List<Episode>> allEpisodes = parser.parse();
	// Map<Integer, List<Episode>> maxEpisodes =
	// episodeLearned.getMaximalFrequentEpisodes(allEpisodes);
	// List<Event> eventMapping = mappingParser.parse(file)
	//
	// for (Map.Entry<Integer, List<Episode>> entry : maxEpisodes.entrySet()) {
	// for (Episode e : entry.getValue()) {
	// graphConverter.convert(e, eventMapping)
	// }
	// }
	// }

	public void write(DirectedGraph<Fact, DefaultEdge> graph, int fileIndex) throws IOException {

		VertexNameProvider<Fact> vertexName = new VertexNameProvider<Fact>() {
			public String getVertexName(Fact fact) {
				return fact.getRawFact();
			}
		};

		DOTExporter<Fact, DefaultEdge> exporter = new DOTExporter<Fact, DefaultEdge>(vertexName, null, null);

		exporter.export(new FileWriter(getFilePath(fileIndex)), graph);
	}

	private String getFilePath(int fileNumber) {
		String targetDirectory = rootFolder.getAbsolutePath() + "/graphs/";
		new File(targetDirectory).mkdirs();
		String fileName = targetDirectory + "graph" + fileNumber + ".dot";
		return fileName;
	}
}
