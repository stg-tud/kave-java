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
import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.EpisodeKind;
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
	private DirectoryStructure folderStructure = new DirectoryStructure();

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

		createDirectoryStructure(frequencyThreshold, bidirectionalThreshold);

		int graphIndex = 0;

		for (Map.Entry<Integer, List<Episode>> entry : learnedEpisodes.entrySet()) {
			Logger.log("Writting episodes with %d number of events.\n", entry.getKey());
			Logger.append("\n");
			for (Episode e : entry.getValue()) {
				Logger.log("Writting episode number %s.\n", graphIndex);
				DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter.convert(e, eventMapping);
				writer.write(graph, getFilePath(getEpisodeKind(e, eventMapping), graphIndex));
				graphIndex++;
			}
		}
	}

	private EpisodeKind getEpisodeKind(Episode frequentEpisode, List<Event> eventMapper) {
		for (Fact fact : frequentEpisode.getFacts()) {
			if (fact.getRawFact().contains(">")) {
				continue;
			}
			int index = Integer.parseInt(fact.getRawFact()) - 1;
			if (!eventMapper.get(index).getMethod().getDeclaringType().toString().startsWith("System")) {
				return EpisodeKind.PROJECT_SPECIFIC;
			}
		}
		return EpisodeKind.GENERAL;
	}

	private void createDirectoryStructure(int frequencyThreshold, double bidirectionalThreshold) {
		String targetDirectory = rootFolder.getAbsolutePath() + "/graphs/";
		if (!(new File(targetDirectory).isDirectory())) {
			new File(targetDirectory).mkdirs();
		}

		String configurationFolder = targetDirectory + "/configurationF" + frequencyThreshold + "B"
				+ bidirectionalThreshold + "/";
		if (!(new File(configurationFolder).isDirectory())) {
			new File(configurationFolder).mkdirs();
		}

		String generalAPIDirectory = configurationFolder + "/generalAPI/";
		if (!(new File(generalAPIDirectory).isDirectory())) {
			new File(generalAPIDirectory).mkdirs();
		}
		folderStructure.generalAPIStructure = generalAPIDirectory;

		String specificAPIDirectory = configurationFolder + "/specificAPI/";
		if (!(new File(specificAPIDirectory).isDirectory())) {
			new File(specificAPIDirectory).mkdirs();
		}
		folderStructure.specificAPIStructure = specificAPIDirectory;
	}

	private class DirectoryStructure {
		private String generalAPIStructure;
		private String specificAPIStructure;
	}

	private String getFilePath(EpisodeKind episodeKind, int fileNumber) throws Exception {
		String fileName = "";
		if (episodeKind.equals(EpisodeKind.GENERAL)) {
			fileName = folderStructure.generalAPIStructure + "/graph" + fileNumber + ".dot";
		} else if (episodeKind.equals(EpisodeKind.PROJECT_SPECIFIC)) {
			fileName = folderStructure.specificAPIStructure + "/graph" + fileNumber + ".dot";
		} else {
			throw new Exception("Invalid folder structure for graph generation!");
		}
		return fileName;
	}
}
