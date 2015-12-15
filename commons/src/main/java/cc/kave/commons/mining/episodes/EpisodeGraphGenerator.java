package cc.kave.commons.mining.episodes;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.mining.reader.EpisodeParser;
import cc.kave.commons.mining.reader.EventMappingParser;
import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.persistence.EpisodeAsGraphWriter;
import cc.recommenders.io.Logger;

public class EpisodeGraphGenerator {

	private EpisodeParser episodeParser;
	private MaximalFrequentEpisodes episodeLearned;
	private EventMappingParser mappingParser;
	private EpisodeToGraphConverter episodeGraphConverter;
	private NoTransitivelyClosedEpisodes transitivityClosure;
	private EpisodeAsGraphWriter writer;

	private File rootFolder;

	@Inject
	public EpisodeGraphGenerator(@Named("graph") File directory, EpisodeParser episodeParser,
			MaximalFrequentEpisodes episodeLearned, EventMappingParser mappingParser,
			NoTransitivelyClosedEpisodes transitivityClosure, EpisodeAsGraphWriter writer,
			EpisodeToGraphConverter graphConverter) {

		assertTrue(directory.exists(), "Episode-miner folder does not exist");
		assertTrue(directory.isDirectory(), "Episode-miner folder is not a folder, but a file");

		this.rootFolder = directory;
		this.episodeParser = episodeParser;
		this.episodeLearned = episodeLearned;
		this.mappingParser = mappingParser;
		this.episodeGraphConverter = graphConverter;
		this.transitivityClosure = transitivityClosure;
		this.writer = writer;
	}

	public void generateGraphs(int frequencyThreshold, double bidirectionalThreshold) throws Exception {
		Map<Integer, List<Episode>> allEpisodes = episodeParser.parse();
		Map<Integer, List<Episode>> maxEpisodes = episodeLearned.getMaximalFrequentEpisodes(allEpisodes);
		Map<Integer, List<Episode>> learnedEpisodes = transitivityClosure.removeTransitivelyClosure(maxEpisodes);
		List<Event> eventMapping = mappingParser.parse();

		String directory = createDirectoryStructure(frequencyThreshold, bidirectionalThreshold);

		int graphIndex = 0;

		for (Map.Entry<Integer, List<Episode>> entry : learnedEpisodes.entrySet()) {
			Logger.log("Writting episodes with %d number of events.\n", entry.getKey());
			Logger.append("\n");
			for (Episode e : entry.getValue()) {
				if (e.getNumberOfFacts() == 1) {
					continue;
				}
				Logger.log("Writting episode number %s.\n", graphIndex);
				DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter.convert(e, eventMapping);
				List<String> types = getAPIType(e, eventMapping);
				for (String t : types) {
					writer.write(graph, getFilePath(directory, t, graphIndex));
				}
				graphIndex++;
			}
		}
	}

	private List<String> getAPIType(Episode frequentEpisode, List<Event> eventMapper) {
		List<String> apiTypes = new LinkedList<String>();
		for (Fact fact : frequentEpisode.getFacts()) {
			if (fact.getRawFact().contains(">")) {
				continue;
			}
			int index = Integer.parseInt(fact.getRawFact());
			String type = eventMapper.get(index).getMethod().getDeclaringType().getFullName().toString().replace(".",
					"/");
			if (!apiTypes.contains(type)) {
				apiTypes.add(type);
			}
		}
		return apiTypes;
	}

	private String createDirectoryStructure(int frequencyThreshold, double bidirectionalThreshold) {
		String targetDirectory = rootFolder.getAbsolutePath() + "/graphs/";
		if (!(new File(targetDirectory).isDirectory())) {
			new File(targetDirectory).mkdir();
		}

		String configurationFolder = targetDirectory + "/configurationF" + frequencyThreshold + "B"
				+ bidirectionalThreshold + "/";
		if (!(new File(configurationFolder).isDirectory())) {
			new File(configurationFolder).mkdir();
		}
		return configurationFolder;
	}

	private String getFilePath(String folderPath, String apiType, int fileNumber) throws Exception {
		String typeFolder = folderPath + "/" + apiType + "/";
		if (!(new File(typeFolder).isDirectory())) {
			new File(typeFolder).mkdirs();
		}
		String fileName = typeFolder + "/graph" + fileNumber + ".dot";

		return fileName;
	}
}
