package cc.kave.episodes.mining.evaluation;

import javax.inject.Inject;

import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.postprocessor.EpisodesFilter;

public class Thresholds {

	private EpisodesParser episodeParser;
	private EpisodesFilter episodeFilter;
	
	@Inject
	public Thresholds(EpisodesParser parser, EpisodesFilter filter) {
		this.episodeParser = parser;
		this.episodeFilter = filter;
	}
}
