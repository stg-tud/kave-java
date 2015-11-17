package cc.kave.commons.mining.episodes;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.mining.reader.EpisodeParser;
import cc.kave.commons.mining.reader.EventMappingParser;
import cc.kave.commons.mining.reader.EventStreamParser;
import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.persistence.EpisodeAsGraphWriter;
import cc.recommenders.io.Logger;

public class EpisodeGraphGenerator {

	private EpisodeParser episodeParser;
	private MaximalFrequentEpisodes episodeLearned;
	private EventMappingParser mappingParser;
	private EpisodeToGraphConverter graphConverter;
	private NoTransitivelyClosedEpisodes transitivityClosure;
	private EpisodeAsGraphWriter writer;
	private EventStreamParser eventStreammer;

	private File rootFolder;

	@Inject
	public EpisodeGraphGenerator(@Named("graph") File directory, EpisodeParser episodeParser,
			MaximalFrequentEpisodes episodeLearned, EventMappingParser mappingParser,
			NoTransitivelyClosedEpisodes transitivityClosure, EpisodeAsGraphWriter writer,
			EpisodeToGraphConverter graphConverter, EventStreamParser eventStrammer) {

		assertTrue(directory.exists(), "Episode-miner folder does not exist");
		assertTrue(directory.isDirectory(), "Episode-miner folder is not a folder, but a file");

		this.rootFolder = directory;
		this.episodeParser = episodeParser;
		this.episodeLearned = episodeLearned;
		this.mappingParser = mappingParser;
		this.graphConverter = graphConverter;
		this.transitivityClosure = transitivityClosure;
		this.eventStreammer = eventStrammer;
		this.writer = writer;
	}

	public void generateGraphs() throws Exception {
		Map<Integer, List<Episode>> allEpisodes = episodeParser.parse();
		Map<Integer, List<Episode>> maxEpisodes = episodeLearned.getMaximalFrequentEpisodes(allEpisodes);
		Map<Integer, List<Episode>> learnedEpisodes = transitivityClosure.removeTransitivelyClosure(maxEpisodes);
		List<Event> eventMapping = mappingParser.parse();

		int graphIndex = 0;

		for (Map.Entry<Integer, List<Episode>> entry : learnedEpisodes.entrySet()) {
			Logger.log("Writting episodes with %d number of events.\n", entry.getKey());
			Logger.append("\n");
			for (Episode e : entry.getValue()) {
				Logger.log("Writting episode number %s.\n", graphIndex);
				DirectedGraph<Fact, DefaultEdge> graph = graphConverter.convert(e, eventMapping);
				writer.write(graph, getFilePath(graphIndex));
				graphIndex++;
			}
		}
	}

	private String getFilePath(int fileNumber) {
		String targetDirectory = rootFolder.getAbsolutePath() + "/graphs/";
		if (!(new File(targetDirectory).isDirectory())) {
			new File(targetDirectory).mkdirs();
		}
		String fileName = targetDirectory + "/graph" + fileNumber + ".dot";
		return fileName;
	}
}
