package cc.kave.episodes.mining.evaluation;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.episodes.io.EpisodeReader;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EpisodesFilter;
import cc.recommenders.io.Logger;

import com.google.common.collect.Sets;

public class PatternsStructure {

	private EventStreamIo eventStream;
	private EpisodeReader episodeParser;
	private EpisodesFilter episodeFilter;

	@Inject
	public PatternsStructure(EventStreamIo streamIo, EpisodeReader parser,
			EpisodesFilter filter) {
		this.eventStream = streamIo;
		this.episodeParser = parser;
		this.episodeFilter = filter;
	}

	public void analyzeTypes(EpisodeType type, int freqEpisode, int foldNum,
			int freqThresh, double entropy) {
		List<Event> events = eventStream.readMapping(freqEpisode);
		Map<Integer, Set<Episode>> episodes = episodeParser.read(freqEpisode);
		Map<Integer, Set<Episode>> patterns = episodeFilter.filter(type,
				episodes, freqThresh, entropy);

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			Logger.log("Analyzing patterns with %d-events ...", entry.getKey());
			int multTypes = 0;
			int multFrames = 0;
			int singleType = 0;
			int singleFrames = 0;

			for (Episode pattern : entry.getValue()) {
				Set<ITypeName> types = Sets.newLinkedHashSet();
				Set<IAssemblyName> asm = Sets.newLinkedHashSet();

				for (Fact fact : pattern.getEvents()) {
					int id = fact.getFactID();
					Event event = events.get(id);
				}
			}
		}
	}
}
