package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.io.LoggerUtils.assertLogContains;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.RepositoriesParser;
import cc.kave.episodes.io.ValidationDataIO;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.Triplet;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EpisodesFilter;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;
import cc.recommenders.utils.LocaleUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ThresholdsAnalyzerTest {

	@Mock
	private EventStreamIo eventStream;
	@Mock
	private EpisodesParser episodeParser;
	@Mock
	private EpisodesFilter episodeFilter;
	@Mock
	private RepositoriesParser reposParser;
	@Mock
	private ValidationDataIO validatioIo;
	@Mock
	private PatternsValidation patternsValidation;

	private Map<Integer, Set<Episode>> patterns;
	private List<Tuple<Event, List<Fact>>> streamMethods;
	private Map<String, Set<IMethodName>> repoMethods;
	private List<Event> trainEvents;
	private List<Event> valStream;
	private List<List<Fact>> valFactStream;
	private Map<Integer, Set<Triplet<Episode, Integer, Integer>>> valResults;

	private static final int FREQUENCY = 2;
	private static final int FOLDNUM = 0;

	private ThresholdsAnalyzer sut;

	@Before
	public void setup() throws Exception {
		LocaleUtils.setDefaultLocale();
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);

		patterns = Maps.newLinkedHashMap();
		Set<Episode> episodes = Sets.newLinkedHashSet();

		Episode ep = createEpisode(4, 1.0, "3", "4", "3>4");
		episodes.add(ep);
		ep = createEpisode(3, 0.5, "6", "7", "6>7");
		episodes.add(ep);
		patterns.put(2, episodes);

		episodes = Sets.newLinkedHashSet();
		ep = createEpisode(2, 0.3, "1", "3", "4");
		episodes.add(ep);
		ep = createEpisode(1, 0.7, "3", "4", "7", "3>4", "3>7");
		episodes.add(ep);
		patterns.put(3, episodes);

		streamMethods = Lists.newLinkedList();
		streamMethods.add(Tuple.newTuple(enclCtx(2), Lists
				.newArrayList(new Fact(1), new Fact(3), new Fact(3),
						new Fact(4), new Fact(4))));
		streamMethods.add(Tuple.newTuple(enclCtx2(5), Lists.newArrayList(
				new Fact(6), new Fact(6), new Fact(7), new Fact(7))));
		streamMethods.add(Tuple.newTuple(enclCtx(2), Lists.newArrayList(
				new Fact(1), new Fact(3), new Fact(8), new Fact(4))));
		streamMethods.add(Tuple.newTuple(enclCtx(9), Lists.newArrayList(
				new Fact(6), new Fact(3), new Fact(7), new Fact(4))));

		repoMethods = Maps.newLinkedHashMap();
		repoMethods
				.put("Repository1", Sets.newHashSet(enclCtx(2).getMethod(),
						enclCtx(9).getMethod()));
		repoMethods
				.put("Repository2", Sets.newHashSet(enclCtx2(5).getMethod()));

		trainEvents = Lists.newArrayList(dummy(), firstCtx(1), enclCtx(2),
				inv(3), inv(4), enclCtx(5), inv(6), inv(7), inv(8), enclCtx(9));

		valStream = Lists.newArrayList(firstCtx(10), enclCtx(5), inv(3),
				inv(4), firstCtx(11), superCtx(12), enclCtx(9), inv(4),
				firstCtx(13), enclCtx(5), inv(3), inv(4), firstCtx(11),
				enclCtx(14), inv(4), firstCtx(11), enclCtx(9), inv(3), inv(4),
				firstCtx(13), enclCtx2(15), inv(3), inv(4));

		valFactStream = Lists.newLinkedList();
		List<Fact> method = Lists.newArrayList(new Fact(10), new Fact(5),
				new Fact(3), new Fact(4));
		valFactStream.add(method);
		method = Lists.newArrayList(new Fact(11), new Fact(12), new Fact(9),
				new Fact(4));
		valFactStream.add(method);
		method = Lists.newArrayList(new Fact(13), new Fact(5), new Fact(3),
				new Fact(4));
		valFactStream.add(method);
		method = Lists.newArrayList(new Fact(11), new Fact(14), new Fact(4));
		valFactStream.add(method);
		method = Lists.newArrayList(new Fact(11), new Fact(9), new Fact(3),
				new Fact(4));
		valFactStream.add(method);
		method = Lists.newArrayList(new Fact(13), new Fact(15), new Fact(3),
				new Fact(4));
		valFactStream.add(method);

		valResults = Maps.newLinkedHashMap();
		Set<Triplet<Episode, Integer, Integer>> valSet = Sets
				.newLinkedHashSet();
		valSet.add(new Triplet<Episode, Integer, Integer>(createEpisode(4, 1.0,
				"3", "4", "3>4"), 1, 4));
		valSet.add(new Triplet<Episode, Integer, Integer>(createEpisode(3, 0.5,
				"6", "7", "6>7"), 2, 0));
		valResults.put(2, valSet);

		valSet = Sets.newLinkedHashSet();
		valSet.add(new Triplet<Episode, Integer, Integer>(createEpisode(2, 0.3,
				"1", "3", "4"), 1, 0));
		valSet.add(new Triplet<Episode, Integer, Integer>(createEpisode(1, 0.7,
				"3", "4", "7", "3>4", "3>7"), 1, 0));
		valResults.put(3, valSet);

		sut = new ThresholdsAnalyzer(eventStream, episodeParser, episodeFilter,
				reposParser, validatioIo, patternsValidation);

		when(eventStream.parseStream(FREQUENCY, FOLDNUM)).thenReturn(
				streamMethods);
		when(eventStream.readMapping(FREQUENCY, FOLDNUM)).thenReturn(
				trainEvents);
		when(episodeParser.parse(any(EpisodeType.class), anyInt(), anyInt()))
				.thenReturn(patterns);
		when(
				episodeFilter.filter(any(EpisodeType.class), any(Map.class),
						anyInt(), anyDouble())).thenReturn(patterns);
		when(reposParser.getRepoMethodsMapper()).thenReturn(repoMethods);
		when(validatioIo.read(anyInt(), anyInt())).thenReturn(valStream);
		when(validatioIo.streamOfFacts(any(List.class), any(Map.class)))
				.thenReturn(valFactStream);
		when(
				patternsValidation.validate(any(Map.class), any(List.class),
						any(Map.class), any(List.class), any(List.class)))
				.thenReturn(valResults);
	}

	@After
	public void teardown() {
		Logger.reset();
	}

	@Test
	public void mocksAreCalled() throws Exception {
		sut.analyze(EpisodeType.GENERAL, FREQUENCY, FOLDNUM, 0, 0.2);

		verify(eventStream).parseStream(anyInt(), anyInt());
		verify(eventStream).readMapping(anyInt(), anyInt());
		verify(episodeParser).parse(any(EpisodeType.class), anyInt(), anyInt());
		verify(episodeFilter, times(4)).filter(any(EpisodeType.class),
				any(Map.class), anyInt(), anyDouble());
		verify(reposParser).getRepoMethodsMapper();
		verify(validatioIo).read(anyInt(), anyInt());
		verify(validatioIo).streamOfFacts(any(List.class), any(Map.class));
		verify(patternsValidation, times(4)).validate(any(Map.class),
				any(List.class), any(Map.class), any(List.class),
				any(List.class));
	}

	@Test
	public void fileContentGeneral() throws Exception {
		Logger.clearLog();

		sut.analyze(EpisodeType.GENERAL, FREQUENCY, FOLDNUM, 0, 0.2);

		assertLogContains(0,
				"Reading repositories - enclosing method declarations mapper!");
		assertLogContains(1, "Reading events ...");
		assertLogContains(2, "Reading training stream ...");
		assertLogContains(3, "Reading validation data ...");
		assertLogContains(4,
				"\tFrequency\tEntropy\tNumGens\tNumSpecs\tFraction\tNumPartials");
		assertLogContains(5, "Number of frequency thresholds: 4");
		assertLogContains(6, "\t1\t0.2\t2\t2\t0.5\t1");
		assertLogContains(7, "\t2\t0.2\t2\t2\t0.5\t1");
		assertLogContains(8, "\t3\t0.2\t2\t2\t0.5\t1");
		assertLogContains(9, "\t4\t0.2\t2\t2\t0.5\t1");
	}

	@Test
	public void fileContentSequential() throws Exception {
		Logger.clearLog();

		sut.analyze(EpisodeType.SEQUENTIAL, FREQUENCY, FOLDNUM, FREQUENCY, 0.0);

		assertLogContains(0,
				"Reading repositories - enclosing method declarations mapper!");
		assertLogContains(1, "Reading events ...");
		assertLogContains(2, "Reading training stream ...");
		assertLogContains(3, "Reading validation data ...");
		assertLogContains(4,
				"\tFrequency\tEntropy\tNumGens\tNumSpecs\tFraction");
		// assertLogContains(5, "Number of entropy thresholds: 3");
		assertLogContains(5, "\t2\t0.0\t2\t2\t0.5");
		assertLogContains(6, "\t2\t0.1\t2\t2\t0.5");
		assertLogContains(7, "\t2\t0.2\t2\t2\t0.5");
		assertLogContains(8, "\t2\t0.3\t2\t2\t0.5");
		assertLogContains(9, "\t2\t0.4\t2\t2\t0.5");
		assertLogContains(10, "\t2\t0.5\t2\t2\t0.5");
		assertLogContains(11, "\t2\t0.6\t2\t2\t0.5");
		assertLogContains(12, "\t2\t0.7\t2\t2\t0.5");
		assertLogContains(13, "\t2\t0.8\t2\t2\t0.5");
		assertLogContains(14, "\t2\t0.9\t2\t2\t0.5");
		assertLogContains(15, "\t2\t1.0\t2\t2\t0.5");
	}

	private Episode createEpisode(int frequency, double entropy,
			String... strings) {
		Episode episode = new Episode();
		episode.addStringsOfFacts(strings);
		episode.setFrequency(frequency);
		episode.setEntropy(entropy);
		return episode;
	}

	private static Event inv(int i) {
		return Events.newInvocation(m(i));
	}

	private static Event firstCtx(int i) {
		return Events.newFirstContext(m(i));
	}

	private static Event superCtx(int i) {
		return Events.newSuperContext(m(i));
	}

	private static Event enclCtx(int i) {
		return Events.newContext(m(i));
	}

	private static Event enclCtx2(int i) {
		return Events.newContext(m2(i));
	}

	private static Event dummy() {
		return Events.newDummyEvent();
	}

	private static IMethodName m(int i) {
		if (i == 0) {
			return Names.getUnknownMethod();
		} else {
			return Names.newMethod("[T,P] [T,P].m" + i + "()");
		}
	}

	private static IMethodName m2(int i) {
		if (i == 0) {
			return Names.getUnknownMethod();
		} else {
			return Names.newMethod("[T2,P2] [T2,P2].m" + i + "()");
		}
	}
}
