package cc.kave.episodes.statistics;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.patterns.PatternFilter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.io.Logger;

import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class APITypes {

	private EventStreamIo streamIo;
	private EpisodeParser parser;
	private PatternFilter filter;

	@Inject
	public APITypes(EventStreamIo eventStream, EpisodeParser episodeParser, PatternFilter patternFilter) {
		this.streamIo = eventStream;
		this.parser = episodeParser;
		this.filter = patternFilter;
	}

	public void streamAPIs(int frequency) {
		List<Event> events = streamIo.readMapping(frequency);
		Set<String> libraries = Sets.newHashSet();

		for (Event event : events) {
			String libName = getLibraryName(event);
			libraries.add(libName);
		}
		Logger.log("\tLibraries analyzed in event stream");
		for (String lib : libraries) {
			Logger.log("\t%s", lib);
		}
	}

	public void patternAPI(int frequency, int freqThresh, double entThresh)
			throws Exception {
		List<Event> events = streamIo.readMapping(frequency);
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);
		Map<Integer, Set<Episode>> patterns = filter.filter(
				EpisodeType.PARALLEL, episodes, freqThresh, entThresh);
		Set<String> libraries = Sets.newHashSet();
		
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			for (Episode pattern : entry.getValue()) {
				for (Fact fact : pattern.getEvents()) {
					Event event = events.get(fact.getFactID());
					String libName = getLibraryName(event);
					libraries.add(libName);
				}
			}
		}
		Logger.log("\tLibraries for which we learn patterns");
		for (String lib : libraries) {
			Logger.log("\t%s", lib);
		}
	}
	
	private String getLibraryName(Event event) {
		String typeName = event.getMethod().getDeclaringType()
				.getFullName();
		if (typeName.contains(".")) {
			int idx = typeName.indexOf(".");
			String libName = typeName.substring(0, idx);
			return libName;
		}
		return "";
	}
}
