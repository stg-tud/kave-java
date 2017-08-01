/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ervina Cergani - initial API and implementation
 */
package exec.episodes;

import java.io.File;

import javax.inject.Inject;

import cc.kave.episodes.io.EpisodesReader;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.evaluation.EpisodeRecommender;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.postprocessor.MaximalEpisodes;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;

import com.google.inject.name.Named;

public class Suggestions {

	private EpisodesReader episodeParser;
	private MaximalEpisodes maximalEpisodes;
	private TransClosedEpisodes transitivityClosure;
	private EventStreamIo streamIo;
	private EpisodeRecommender recommender;
	private EpisodeToGraphConverter graphConverter;
	private EpisodeAsGraphWriter graphWriter;

	private File rootFolder;

	@Inject
	public Suggestions(@Named("graph") File directory, EpisodesReader episodeParser, MaximalEpisodes maximalEpisodes,
			TransClosedEpisodes transitivityClosure, EventStreamIo streamIo,
			EpisodeRecommender recommender, EpisodeToGraphConverter graphConverter, EpisodeAsGraphWriter graphWriter) {
		this.rootFolder = directory;
		this.episodeParser = episodeParser;
		this.maximalEpisodes = maximalEpisodes;
		this.transitivityClosure = transitivityClosure;
		this.streamIo = streamIo;
		this.recommender = recommender;
		this.graphConverter = graphConverter;
		this.graphWriter = graphWriter;
	}

	// public void run() throws Exception {
	// Map<Integer, List<Episode>> allEpisodes = episodeParser.parse();
	// Map<Integer, List<Episode>> maxEpisodes =
	// maximalEpisodes.getMaximalFrequentEpisodes(allEpisodes);
	// Map<Integer, List<Episode>> learnedEpisodes =
	// transitivityClosure.removeTransitivelyClosure(maxEpisodes);
	// List<Event> eventMapper = eventMappingParser.parse();
	// List<Method> listOfQueries = queryGenerator.parse();
	//
	// int queryIndex = 0;
	//
	// for (Episode query : listOfQueries) {
	// if (queryIndex == 1489) {
	// queryIndex++;
	// continue;
	// }
	// int proposalIndex = 0;
	// Set<Tuple<Episode, Double>> proposals = recommender.getProposals(query,
	// learnedEpisodes, 3);
	// if (proposals.size() > 0) {
	// continue;
	// }
	// DirectedGraph<Fact, DefaultEdge> queryGraph =
	// graphConverter.convert(query, eventMapper);
	// graphWriter.write(queryGraph, getFilePath("query", queryIndex,
	// proposalIndex));
	// Iterator<Tuple<Episode, Double>> iterator = proposals.iterator();
	// while (iterator.hasNext()) {
	// Tuple<Episode, Double> p = iterator.next();
	// DirectedGraph<Fact, DefaultEdge> proposalGraph =
	// graphConverter.convert(p.getFirst(), eventMapper);
	// graphWriter.write(proposalGraph, getFilePath("proposal", queryIndex,
	// proposalIndex));
	// proposalIndex++;
	// }
	// Logger.log("Finished writting query%d", queryIndex);
	// queryIndex++;
	// }
	// }

	private String getFilePath(String fileName, int queryIndex, int proposalIndex) {
		String directory = rootFolder.getAbsolutePath() + "/queries/";
		if (!(new File(directory).isDirectory())) {
			new File(directory).mkdirs();
		}
		String targetDirectory = directory + "/query" + queryIndex + "/";
		if (fileName.equalsIgnoreCase("query")) {
			if (!(new File(targetDirectory).isDirectory())) {
				new File(targetDirectory).mkdirs();
			}
			String filePath = targetDirectory + "/query.dot";
			return filePath;
		} else {
			String filePath = targetDirectory + "/proposal" + proposalIndex + ".dot";
			return filePath;
		}
	}
}
