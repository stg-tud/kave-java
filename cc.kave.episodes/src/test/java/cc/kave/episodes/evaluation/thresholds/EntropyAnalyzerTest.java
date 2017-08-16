package cc.kave.episodes.evaluation.thresholds;

import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cc.kave.episodes.evaluations.thresholds.EntropyAnalyzer;
import cc.kave.episodes.mining.patterns.PatternFilter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import static cc.recommenders.io.LoggerUtils.assertLogContains;

public class EntropyAnalyzerTest {

	@Mock
	private PatternFilter filter;

	private static final int FREQUENCY = 200;

	private Map<Integer, Set<Episode>> episodes;

	private EntropyAnalyzer sut;

	@Before
	public void setup() throws Exception {
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);

		episodes = Maps.newLinkedHashMap();

		Set<Episode> twoNodes = Sets.newLinkedHashSet();
		twoNodes.add(createEpisode(200, 1.0, "1", "2", "1>2"));
		twoNodes.add(createEpisode(220, 1.0, "1", "2", "2>1"));
		twoNodes.add(createEpisode(250, 0.5, "1", "2"));
		episodes.put(2, twoNodes);

		Set<Episode> threNodes = Sets.newLinkedHashSet();
		threNodes.add(createEpisode(200, 0.5, "7", "8", "34", "34>7", "34>8"));
		threNodes.add(createEpisode(220, 0.7, "7", "8", "34", "7>8"));
		threNodes.add(createEpisode(250, 0.5, "7", "8", "34", "7>8", "34>8"));
		threNodes.add(createEpisode(200, 0.7, "7", "8", "34", "7>8", "7>34"));
		threNodes.add(createEpisode(220, 0.5, "7", "8", "34", "7>8", "7>34",
				"8>34"));
		threNodes.add(createEpisode(250, 0.7, "7", "8", "34", "7>34", "7>8",
				"34>8"));
		threNodes.add(createEpisode(200, 0.7, "7", "8", "34", "34>7", "34>8",
				"7>8"));
		threNodes.add(createEpisode(220, 0.5, "7", "8", "34", "8>7", "34>7"));
		threNodes.add(createEpisode(250, 0.5, "7", "8", "34", "34>8", "34>7",
				"8>7"));
		episodes.put(3, threNodes);

		sut = new EntropyAnalyzer(filter);

		when(
				filter.filter(eq(EpisodeType.GENERAL), anyInt(), anyInt(),
						anyDouble())).thenReturn(episodes);
	}
	
	@After
	public void teardown() {
		Logger.reset();
	}
	
	@Test
	public void logger() throws Exception {
		sut.threshold(FREQUENCY);
		
		assertLogContains(0, "\tEntropy threshold analyses!");
		assertLogContains(1, "\tFrequency\tEntropy\t#Patterns");
		assertLogContains(2, "\t200\t0.00\t12");
		assertLogContains(3, "\t200\t0.70\t6");
		assertLogContains(4, "\t200\t1.00\t2");
		
		assertLogContains(5, "\t220\t0.00\t8");
		assertLogContains(6, "\t220\t0.70\t3");
		assertLogContains(7, "\t220\t1.00\t1");
		
		assertLogContains(8, "\t250\t0.00\t4");
		assertLogContains(9, "\t250\t0.70\t1");
		assertLogContains(10, "\t250\t1.00\t0");
		
		verify(filter).filter(eq(EpisodeType.GENERAL), anyInt(), anyInt(), anyDouble());
	}

	private Episode createEpisode(int freq, double bdmeas, String... strings) {
		Episode episode = new Episode();
		episode.setFrequency(freq);
		episode.setEntropy(bdmeas);
		for (String fact : strings) {
			episode.addFact(fact);
		}
		return episode;
	}
}
