package cc.kave.episodes.postprocessor;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.commons.utils.io.Logger;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.evaluation.PatternsValidation;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.datastructures.Tuple;

public class SpecificPatternsTest {

	@Mock
	private PatternsValidation validation;
	@Mock
	private EventStreamIo eventsStream;

	private Map<Integer, Set<Tuple<Episode, Boolean>>> patterns;
	private List<Event> events;
	private EpisodeToGraphConverter graphConverter;

	@Before
	public void setup() {
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);

		patterns = Maps.newLinkedHashMap();
		Set<Tuple<Episode, Boolean>> episodes = Sets.newLinkedHashSet();
		episodes.add(Tuple.newTuple(createEpisode("1", "2", "1>2"), false));
		episodes.add(Tuple.newTuple(createEpisode("2", "3", "2>3"), true));
		episodes.add(Tuple.newTuple(createEpisode("4", "5"), true));
		patterns.put(2, episodes);

		episodes = Sets.newLinkedHashSet();
		episodes.add(Tuple.newTuple(createEpisode("1", "2", "3", "1>2"), false));
		episodes.add(Tuple.newTuple(createEpisode("2", "3", "4", "3>4"), true));
		episodes.add(Tuple.newTuple(createEpisode("3", "4", "5", "4>5"), false));
		patterns.put(3, episodes);

		episodes = Sets.newLinkedHashSet();
		episodes.add(Tuple.newTuple(createEpisode("1", "2", "3", "4"), false));
		episodes.add(Tuple.newTuple(createEpisode("2", "3", "4", "5"), false));
	}

	private Episode createEpisode(String... strings) {
		Episode episode = new Episode();
		episode.addStringsOfFacts(strings);
		return episode;
	}
}
