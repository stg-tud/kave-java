package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.io.LoggerUtils.assertLogContains;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;

public class ThresholdsAnalyzerTest {

	@Mock
	private PatternsValidation patternsValidation;

	private Map<Episode, Boolean> validation;

	private static final int FREQUENCY = 2;
	private static final double ENTROPY = 0.4;
	private static final int FOLDNUM = 0;

	private ThresholdsAnalyzer sut;

	@Before
	public void setup() throws Exception {
		Logger.reset();
		Logger.setCapturing(true);
		
		MockitoAnnotations.initMocks(this);

		sut = new ThresholdsAnalyzer(patternsValidation);

		validation = Maps.newLinkedHashMap();
		validation.put(createEpisode(4, 0.5, "1", "2", "1>2"), true);
		validation
				.put(createEpisode(3, 0.4, "1", "2", "3", "1>2", "1>3"), true);
		validation.put(createEpisode(4, 0.7, "1", "2", "4", "1>2", "1>4"),
				false);
		validation
				.put(createEpisode(2, 0.6, "1", "3", "4", "1>3", "1>4", "3>4"),
						true);

		when(
				patternsValidation.validate(any(EpisodeType.class), anyInt(),
						anyDouble(), anyInt())).thenReturn(validation);
	}
	
	@After
	public void teardown() {
		Logger.reset();
	}

	@Test
	public void mocksAreCalled() throws Exception {
		sut.analyze(EpisodeType.GENERAL, FREQUENCY, ENTROPY, FOLDNUM);

		verify(patternsValidation).validate(any(EpisodeType.class), anyInt(),
				anyDouble(), anyInt());
	}

	@Test
	public void generalPattherns() throws Exception {
		Logger.clearLog();
		
		sut.analyze(EpisodeType.GENERAL, FREQUENCY, ENTROPY, FOLDNUM);

		assertLogContains(0,
				"Frequency\tEntropy\tNumGens\tNumSpecs\tFraction\n");

	}

	private Episode createEpisode(int frequency, double entropy,
			String... facts) {
		Episode episode = new Episode();
		episode.setFrequency(frequency);
		episode.setEntropy(entropy);
		episode.addStringsOfFacts(facts);

		return episode;
	}
}
