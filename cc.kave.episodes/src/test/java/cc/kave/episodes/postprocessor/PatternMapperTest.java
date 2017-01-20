package cc.kave.episodes.postprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PatternMapperTest {

	@Mock
	private EpisodesParser episodeParser;
	@Mock 
	private EpisodesFilter episodeFilter;

	private static final int FREQUENCY = 1;
	private static final double ENTROPY = 0.0;
	
	private MaximalEpisodes maxEpisodes;
	Map<Integer, Set<Episode>> episodes;
	
	private PatternsMapper sut;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		maxEpisodes = new MaximalEpisodes();
		
		episodes = Maps.newLinkedHashMap();
		Set<Episode> catEpisode = Sets.newHashSet();
		catEpisode.add(createEpisode(5, 1.0, "1"));
		catEpisode.add(createEpisode(7, 1.0, "2"));
		catEpisode.add(createEpisode(8, 1.0, "3"));
		catEpisode.add(createEpisode(6, 1.0, "4"));
		episodes.put(1, catEpisode);
		
		catEpisode = Sets.newLinkedHashSet();
		catEpisode.add(createEpisode(4, 1.0, "1", "2", "1>2"));
		catEpisode.add(createEpisode(5, 0.8, "1", "3", "3>1"));
		catEpisode.add(createEpisode(4, 1.0, "1", "4", "1>4"));
		episodes.put(2, catEpisode);
		
		catEpisode = Sets.newLinkedHashSet();
		catEpisode.add(createEpisode(3, 0.5, "1", "2", "4", "1>2", "1>4"));
		catEpisode.add(createEpisode(3, 0.7, "1", "2", "3", "1>2", "1>3"));
		episodes.put(3, catEpisode);
		
		catEpisode = Sets.newLinkedHashSet();
		catEpisode.add(createEpisode(2, 0.4, "1", "2", "3", "4", "1>2", "1>3", "1>4"));
		episodes.put(4, catEpisode);
		
		sut = new PatternsMapper(episodeParser, episodeFilter, maxEpisodes);
		
		when(episodeParser.parse(anyInt(), any(EpisodeType.class))).thenReturn(episodes);
		when(episodeFilter.filter(any(Map.class), anyInt(), anyDouble())).thenReturn(episodes);
	}
	
	@Test
	public void withMaximals() {
		List<Episode> expected = Lists.newLinkedList();
		expected.add(createEpisode(5, 0.8, "1", "3", "3>1"));
		expected.add(createEpisode(2, 0.4, "1", "2", "3", "4", "1>2", "1>3", "1>4"));
		
		List<Episode> actuals = sut.getMapper(FREQUENCY, EpisodeType.GENERAL, true);
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void noMaximals() {
		List<Episode> expected = Lists.newLinkedList();
		expected.add(createEpisode(5, 1.0, "1"));
		expected.add(createEpisode(7, 1.0, "2"));
		expected.add(createEpisode(8, 1.0, "3"));
		expected.add(createEpisode(6, 1.0, "4"));
		expected.add(createEpisode(4, 1.0, "1", "2", "1>2"));
		expected.add(createEpisode(5, 0.8, "1", "3", "3>1"));
		expected.add(createEpisode(4, 1.0, "1", "4", "1>4"));
		expected.add(createEpisode(3, 0.5, "1", "2", "4", "1>2", "1>4"));
		expected.add(createEpisode(3, 0.7, "1", "2", "3", "1>2", "1>3"));
		expected.add(createEpisode(2, 0.4, "1", "2", "3", "4", "1>2", "1>3", "1>4"));
		
		List<Episode> actuals = sut.getMapper(FREQUENCY, EpisodeType.GENERAL, false);
		
		assertLists(expected, actuals);
	}
	

	private void assertLists(List<Episode> expected, List<Episode> actuals) {
		if (expected.size() != actuals.size()) {
			fail();
		}
		for (Episode episode : expected) {
			if (!actuals.contains(episode)) {
				fail();
			} 
		}
		assertTrue(true);
	}

	private Episode createEpisode(int freq, double entropy, String...strings) {
		Episode episode = new Episode();
		episode.setFrequency(freq);
		episode.setEntropy(entropy);
		episode.addStringsOfFacts(strings);

		return episode;
	}
}
