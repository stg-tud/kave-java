package cc.kave.episodes.mining.evaluation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.Triplet;
import cc.recommenders.exceptions.AssertionException;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ThresholdsAnalyzerTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Rule
	public TemporaryFolder patternsFolder = new TemporaryFolder();

	@Mock
	private EpisodesParser episodeParser;
	@Mock
	private PatternsValidation patternsValidation;

	private Map<Integer, Set<Episode>> episodes;
	private Map<Integer, Set<Triplet<Episode, Integer, Integer>>> validation;

	private static final int FREQUENCY = 2;
	private static final double ENTROPY = 0.4;
	private static final int FOLDNUM = 0;

	private ThresholdsAnalyzer sut;

	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);

		sut = new ThresholdsAnalyzer(patternsFolder.getRoot(), episodeParser,
				patternsValidation);

		Episode episode2 = createEpisode(4, 0.5345, "1", "2", "1>2");

		Episode episode31 = createEpisode(3, 0.45, "1", "2", "3", "1>2", "1>3");
		Episode episode32 = createEpisode(4, 0.7, "1", "2", "4", "1>2", "1>4");
		Episode episode33 = createEpisode(2, 0.67894, "1", "3", "4");

		episodes = Maps.newLinkedHashMap();
		episodes.put(2, Sets.newHashSet(episode2));
		episodes.put(3, Sets.newHashSet(episode31, episode32, episode33));

		validation = Maps.newLinkedHashMap();
		Set<Triplet<Episode, Integer, Integer>> episodeSet = Sets
				.newLinkedHashSet();
		episodeSet.add(new Triplet<Episode, Integer, Integer>(episode2, 4, 2));
		validation.put(2, episodeSet);

		episodeSet = Sets.newLinkedHashSet();
		episodeSet.add(new Triplet<Episode, Integer, Integer>(episode31, 3, 0));
		episodeSet.add(new Triplet<Episode, Integer, Integer>(episode32, 1, 0));
		episodeSet.add(new Triplet<Episode, Integer, Integer>(episode33, 1, 2));
		validation.put(3, episodeSet);

		when(episodeParser.parse(any(EpisodeType.class), anyInt(), anyInt()))
				.thenReturn(episodes);
		when(patternsValidation.validate(any(Map.class), anyInt(), anyInt()))
				.thenReturn(validation);
	}

	@Test
	public void cannotBeInitializedWithNonExistingPatternsFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Patterns folder does not exist!");
		sut = new ThresholdsAnalyzer(new File("does not exist"), episodeParser,
				patternsValidation);
	}

	@Test
	public void cannotBeInitializedWithPatternsFile() throws IOException {
		File patternsFile = patternsFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Patterns is not a folder, but a file!");
		sut = new ThresholdsAnalyzer(patternsFile, episodeParser,
				patternsValidation);
	}

	@Test
	public void mocksAreCalled() throws Exception {
		sut.analyze(EpisodeType.GENERAL, FREQUENCY, FOLDNUM);

		verify(episodeParser).parse(any(EpisodeType.class), anyInt(), anyInt());
		verify(patternsValidation).validate(any(Map.class), anyInt(), anyInt());
	}

	@Test
	public void fileIsCreated() throws Exception {
		sut.analyze(EpisodeType.SEQUENTIAL, FREQUENCY, FOLDNUM);

		File file = getFilePath(EpisodeType.SEQUENTIAL);

		assertTrue(file.exists());
	}

	@Test
	public void fileContentGeneral() throws Exception {

		sut.analyze(EpisodeType.GENERAL, FREQUENCY, FOLDNUM);

		StringBuilder sb = new StringBuilder();

		sb.append("Frequency\tNumGens\tNumSpecs\tFraction\tNumPartials\n");
		sb.append("2\t3\t1\t0.75\t2\n");
		sb.append("3\t2\t1\t0.666\t2\n");
		sb.append("4\t1\t1\t0.5\t1\n\n");

		sb.append("Frequency\tEntropy\tNumGens\tNumSpecs\tFraction\tNumPartials\n");
		sb.append("2\t0.45\t3\t1\t0.75\t2\n");
		sb.append("2\t0.534\t2\t1\t0.666\t1\n");
		sb.append("2\t0.678\t1\t1\t0.5\t1\n");
		sb.append("2\t0.7\t0\t1\t0.0\t1\n");

		sb.append("3\t0.45\t2\t1\t0.666\t2\n");
		sb.append("3\t0.534\t1\t1\t0.5\t1\n");
		sb.append("3\t0.678\t0\t1\t0.0\t1\n");
		sb.append("3\t0.7\t0\t1\t0.0\t1\n");

		sb.append("4\t0.45\t1\t1\t0.5\t1\n");
		sb.append("4\t0.534\t1\t1\t0.5\t1\n");
		sb.append("4\t0.678\t0\t1\t0.0\t1\n");
		sb.append("4\t0.7\t0\t1\t0.0\t1\n");

		String actuals = FileUtils
				.readFileToString(getFilePath(EpisodeType.GENERAL));

		assertEquals(sb.toString(), actuals);
	}

	@Test
	public void fileContentSequential() throws Exception {

		sut.analyze(EpisodeType.SEQUENTIAL, FREQUENCY, FOLDNUM);

		StringBuilder sb = new StringBuilder();

		sb.append("Frequency\tNumGens\tNumSpecs\tFraction\n");
		sb.append("2\t3\t1\t0.75\n");
		sb.append("3\t2\t1\t0.666\n");
		sb.append("4\t1\t1\t0.5\n");

		String actuals = FileUtils
				.readFileToString(getFilePath(EpisodeType.SEQUENTIAL));

		assertEquals(sb.toString(), actuals);
	}

	private Episode createEpisode(int frequency, double entropy,
			String... facts) {
		Episode episode = new Episode();
		episode.setFrequency(frequency);
		episode.setEntropy(entropy);
		episode.addStringsOfFacts(facts);

		return episode;
	}

	private File getFilePath(EpisodeType type) {
		File fileName = new File(patternsFolder.getRoot().getAbsolutePath()
				+ "/freq" + FREQUENCY + "/" + type.toString()
				+ "/thresholds.txt");
		return fileName;
	}
}
