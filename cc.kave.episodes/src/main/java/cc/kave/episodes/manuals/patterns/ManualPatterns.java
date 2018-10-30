package cc.kave.episodes.manuals.patterns;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.episodes.io.FileReader;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.patterns.PatternFilter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class ManualPatterns {

	private FileReader reader;
	private PatternFilter filter;

	private TransClosedEpisodes transClosure;
	private EpisodeAsGraphWriter graphWriter;

	@Inject
	public ManualPatterns(FileReader fileReader, PatternFilter patternFilter,
			TransClosedEpisodes closedEpisode,
			EpisodeAsGraphWriter episodeWriter) {
		this.reader = fileReader;
		this.filter = patternFilter;
		this.transClosure = closedEpisode;
		this.graphWriter = episodeWriter;
	}

	public void filter(int misuseId, int size) throws Exception {
		File fileName = new File(getPath() + "/episodes" + misuseId + ".txt");
		List<String> lines = reader.readFile(fileName);

		Map<Integer, Set<Episode>> episodes = parseEpisodes(lines);

		Map<Integer, Set<Episode>> patterns = filter.filter(episodes, 1, 0.0);
		File misusePath = new File(getPath() + "/misuse" + misuseId);
		if (!misusePath.exists()) {
			misusePath.mkdir();
		}
		int patternId = 0;

		for (Episode episode : patterns.get(size)) {
			String patternName = misusePath.getAbsolutePath() + "/p"
					+ patternId + ".dot";
			writePattern(episode, patternName);
			patternId++;
		}
	}

	private void writePattern(Episode episode, String patternName)
			throws IOException {
		Episode pattern = transClosure.remTransClosure(episode);

		DirectedGraph<Fact, DefaultEdge> graph = new DefaultDirectedGraph<Fact, DefaultEdge>(
				DefaultEdge.class);
		for (Fact fact : pattern.getFacts()) {
			if (!fact.isRelation()) {
				graph.addVertex(fact);
			}
		}
		for (Fact fact : pattern.getFacts()) {
			if (fact.isRelation()) {
				Tuple<Fact, Fact> existance = fact.getRelationFacts();
				graph.addEdge(existance.getFirst(), existance.getSecond());
			}
		}
		graphWriter.write(graph, patternName);
	}

	private Map<Integer, Set<Episode>> parseEpisodes(List<String> lines) {
		Map<Integer, Set<Episode>> episodeIndexed = Maps.newLinkedHashMap();
		Set<Episode> episodes = Sets.newLinkedHashSet();

		String[] rowValues;
		int numNodes = 0;

		for (String line : lines) {
			if (line.contains(":")) {
				rowValues = line.split(":");
				Episode episode = readEpisode(numNodes, rowValues);
				episodes.add(episode);
			} else {
				rowValues = line.split("\\s+");
				if (!episodes.isEmpty()) {
					episodeIndexed.put(numNodes, episodes);
				}
				if (Integer.parseInt(rowValues[3]) > 0) {
					String[] nodeString = rowValues[0].split("-");
					numNodes = Integer.parseInt(nodeString[0]);
					episodes = Sets.newLinkedHashSet();
				} else {
					break;
				}
			}
		}
		if (!episodeIndexed.containsKey(numNodes)) {
			episodeIndexed.put(numNodes, episodes);
		}
		return episodeIndexed;
	}

	private Episode readEpisode(int size, String[] rowValues) {
		Episode episode = new Episode();
		episode.setFrequency(Integer.parseInt(rowValues[1].trim()));
		episode.setEntropy(Double.parseDouble(rowValues[2].trim()));
		String[] events = rowValues[0].split("\\s+");
		for (int idx = 0; idx < size; idx++) {
			episode.addFact(events[idx]);
		}
		if (rowValues[3].contains(",")) {
			String[] relations = rowValues[3].substring(2).split(",");
			for (String relation : relations) {
				episode.addFact(relation);
			}
		}
		return episode;
	}

	private String getPath() {
		String path = "/Users/ervinacergani/Documents/CodeRecommender/manuals";
		return path;
	}
}
