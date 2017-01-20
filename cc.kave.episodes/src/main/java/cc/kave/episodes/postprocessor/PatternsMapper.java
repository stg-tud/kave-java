package cc.kave.episodes.postprocessor;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;

import com.google.common.collect.Lists;

public class PatternsMapper {

	private static EpisodesParser episodeParser;
	private static EpisodesFilter episodeFilter;
	private static MaximalEpisodes maxEpisodes;
	
	private static final double ENTROPY = 0.0;
	
	@Inject
	public PatternsMapper(EpisodesParser parser, EpisodesFilter filter, MaximalEpisodes maxis) {
		this.episodeParser = parser;
		this.episodeFilter = filter;
		this.maxEpisodes = maxis;
	}
	
	public static List<Episode> getMapper(int frequency, EpisodeType episodeType, boolean maximals) {
		List<Episode> mapper = Lists.newLinkedList();
		
		Map<Integer, Set<Episode>> episodes = episodeParser.parse(frequency, episodeType);
		Map<Integer, Set<Episode>> filters = episodeFilter.filter(episodes, frequency, ENTROPY);
		Map<Integer, Set<Episode>> patterns;
		
		if (maximals) {
			patterns = maxEpisodes.getMaximalEpisodes(filters);
		} else {
			patterns = filters;
		}
		
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			Set<Episode> catEpisodes = entry.getValue();
			
			if (catEpisodes.size() == 0) {
				continue;
			} else {
				for (Episode ep : catEpisodes) {
					mapper.add(ep);
				}
			}
		}
		return mapper;
	}
}
