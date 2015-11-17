package cc.kave.commons.mining.episodes;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.episodes.Episode;

public class EpisodeMappingTest {

	private Map<Integer, List<Episode>> learnedEpisodes;
	private Map<Integer, List<Episode>> emptyEpisodes;
	private EpisodeMapping sut;
	
	@Before
	public void setup() {
		sut = new EpisodeMapping();
		emptyEpisodes = new HashMap<Integer, List<Episode>>();

		learnedEpisodes = new HashMap<Integer, List<Episode>>();
		learnedEpisodes.put(1, newArrayList(newEpisode(3, 1, "1"), newEpisode(3, 1, "2"), newEpisode(3, 1, "3")));
		learnedEpisodes.put(2, newArrayList(newEpisode(3, 2, "4", "5", "4>5"), newEpisode(2, 2, "4", "6", "4>6")));
		learnedEpisodes.put(3, newArrayList(newEpisode(1, 3, "6", "7", "8", "7>8")));
	}
	
	@Test(expected=Exception.class)
	public void noLearnedEpisodes() throws Exception {
		sut.generateEpisodeIds(emptyEpisodes);
	}
	
	@Test
	public void testIds() throws Exception {
		Map<Episode, Integer> expected = new HashMap<Episode, Integer>();
		expected.put(newEpisode(3, 1, "1"), 0);
		expected.put(newEpisode(3, 1, "2"), 1);
		expected.put(newEpisode(3, 1, "3"), 2);
		expected.put(newEpisode(3, 2, "4", "5", "4>5"), 3);
		expected.put(newEpisode(2, 2, "4", "6", "4>6"), 4);
		expected.put(newEpisode(1, 3, "6", "7", "8", "7>8"), 5);
		
		Map<Episode, Integer> actuals = sut.generateEpisodeIds(learnedEpisodes);
		
		assertEquals(expected, actuals);
	}
	
	private Episode newEpisode(int frequency, int numberOfEvents, String... facts) {
		Episode episode = new Episode();
		episode.addStringsOfFacts(facts);
		episode.setFrequency(frequency);
		episode.setNumEvents(numberOfEvents);
		return episode;
	}
}
