package cc.kave.episodes.mining.patterns;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.recommenders.exceptions.AssertionException;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PatternFilterTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private PartialPatterns partials;
	@Mock
	private SequentialPatterns sequentials;
	@Mock
	private ParallelPatterns parallels;

	private static final int FREQTHRESH = 5;
	private static final double ENTROPY = 0.5;
	private static final double THRESHSUB = 1.0;

	private Map<Integer, Set<Episode>> episodes;

	private PatternFilter sut;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		episodes = Maps.newLinkedHashMap();

		sut = new PatternFilter(partials, sequentials, parallels);
	}

	@Test
	public void exception() throws Exception {
		thrown.expect(Exception.class);
		thrown.expectMessage("Not valid episode type!");

		sut.filter(EpisodeType.OTHER, episodes, FREQTHRESH, ENTROPY);
	}

	@Test
	public void partials() throws Exception {
		when(partials.filter(anyMap(), anyInt(), anyDouble())).thenReturn(
				episodes);

		sut.filter(EpisodeType.GENERAL, episodes, FREQTHRESH, ENTROPY);

		verify(partials).filter(anyMap(), anyInt(), anyDouble());
	}

	@Test
	public void sequentials() throws Exception {
		when(sequentials.filter(anyMap(), anyInt())).thenReturn(episodes);

		sut.filter(EpisodeType.SEQUENTIAL, episodes, FREQTHRESH, ENTROPY);

		verify(sequentials).filter(anyMap(), anyInt());
	}

	@Test
	public void parallel() throws Exception {
		when(parallels.filter(anyMap(), anyInt())).thenReturn(episodes);

		sut.filter(EpisodeType.PARALLEL, episodes, FREQTHRESH, ENTROPY);

		verify(parallels).filter(anyMap(), anyInt());
	}
	
	@Test
	public void emptyEpisodes() throws Exception {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("The list of episodes is empty!");
		
		sut.subEpisodes(episodes, THRESHSUB);
	}
	
	@Test
	public void subepisodes() throws Exception {
		episodes.put(1, Sets.newHashSet(createEpisode(2, 1.0, "a")));
		
		Set<Episode> epSet = Sets.newLinkedHashSet();
		epSet.add(createEpisode(5, 1.0, "a", "b", "a>b"));
		epSet.add(createEpisode(6,  0.5, "c", "d"));
		epSet.add(createEpisode(5, 1.0, "e", "f", "e>f"));
		episodes.put(2, epSet);
		
		epSet = Sets.newLinkedHashSet();
		epSet.add(createEpisode(4, 0.5, "a", "b", "c", "a>b", "a>c"));
		epSet.add(createEpisode(6, 0.5, "c", "d", "e", "c>d", "c>e"));
		epSet.add(createEpisode(2, 0.8, "b", "d", "f"));
		episodes.put(3, epSet);
		
		Map<Integer, Set<Episode>> expected = Maps.newLinkedHashMap();
		epSet = Sets.newLinkedHashSet();
		epSet.add(createEpisode(5, 1.0, "e", "f", "e>f"));
		expected.put(2, epSet);
		
		epSet = Sets.newLinkedHashSet();
		epSet.add(createEpisode(4, 0.5, "a", "b", "c", "a>b", "a>c"));
		epSet.add(createEpisode(6, 0.5, "c", "d", "e", "c>d", "c>e"));
		epSet.add(createEpisode(2, 0.8, "b", "d", "f"));
		expected.put(3, epSet);
		
		Map<Integer, Set<Episode>> actuals = sut.subEpisodes(episodes, THRESHSUB);
		
		assertEquals(expected, actuals);
	}

	private Episode createEpisode(int frequency, double entropy, String...strings) {
		Episode episode = new Episode();
		episode.setFrequency(frequency);
		episode.setEntropy(entropy);
		
		for (String string :  strings) {
			episode.addFact(string);
		}
		return episode;
	}
}
