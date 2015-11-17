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

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.commons.mining.episodes.EpisodeMapping;
import cc.kave.commons.mining.episodes.EpisodeRecommender;
import cc.kave.commons.mining.episodes.EpisodeToGraphConverter;
import cc.kave.commons.mining.episodes.MaximalFrequentEpisodes;
import cc.kave.commons.mining.episodes.QueryGenerator;
import cc.kave.commons.mining.reader.EpisodeParser;
import cc.kave.commons.mining.reader.EventMappingParser;
import cc.kave.commons.model.episodes.Episode;
import cc.recommenders.datastructures.Tuple;

public class Suggestions {

	private EpisodeParser episodeParser;
	private MaximalFrequentEpisodes episodeLearned;
	private QueryGenerator queryGenerator;
	private EpisodeMapping episodeMapping;
	private EventMappingParser eventMapping;
	private EpisodeRecommender recommender;
	private EpisodeToGraphConverter graphConverter;
	//
	// @Inject
	// public Suggestions(MaxFreqEpisodes episodeLearned, QueryGenerator
	// queryGenerator, EpisodeRecommender proposals) {
	// this.episodeLearned = episodeLearned;
	// this.queryGenerator = queryGenerator;
	// this.proposals = proposals;
	// }
	 
	@Inject
	public Suggestions(EpisodeParser episodeParser, MaximalFrequentEpisodes episodeLearned, QueryGenerator queryGenerator, 
			EpisodeMapping episodeMapping, EventMappingParser eventMapping, EpisodeRecommender recommender, EpisodeToGraphConverter graphConverter) {
		this.episodeParser = episodeParser;
		this.episodeLearned = episodeLearned;
		this.queryGenerator = queryGenerator;
		this.episodeMapping = episodeMapping;
		this.eventMapping = eventMapping;
		this.recommender = recommender;
		this.graphConverter = graphConverter;
	}
	
	public void run() throws Exception {
		Map<Integer, List<Episode>> allEpisodes = episodeParser.parse();
		Map<Integer, List<Episode>> learnedEpisodes = episodeLearned.getMaximalFrequentEpisodes(allEpisodes);
		Map<Episode, Integer> episodeIds = episodeMapping.generateEpisodeIds(learnedEpisodes);
		List<Episode> listOfQueries = queryGenerator.parse();
		for (Episode query : listOfQueries) {
			Set<Tuple<Episode, Double>> proposals = recommender.getProposals(query, learnedEpisodes, 3); 
		}
		
//		Set<Tuple<Episode, Double>> recommendations = proposals.getProposals(query.getFirst(), maxEpisodes);
//		
//		Logger.log("Initial episode: " +  query.getSecond().toString());
//		Logger.log("Generated query: " + query.getFirst().toString());
//		Logger.log("List of proposals: ");
//		
//		Iterator<Tuple<Episode, Double>> iterator = recommendations.iterator();
//		while (iterator.hasNext()) {
//			Tuple<Episode, Double> proposal = iterator.next();
//			Logger.log(proposal.getFirst().toString() + ", " + proposal.getSecond().toString());
//		}
	}
}
