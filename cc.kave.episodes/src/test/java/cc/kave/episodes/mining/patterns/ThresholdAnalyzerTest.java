package cc.kave.episodes.mining.patterns;

import static cc.recommenders.io.LoggerUtils.assertLogContains;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
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
import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ThresholdAnalyzerTest {

	@Mock
	private EpisodeParser parser;
	@Mock
	private PatternFilter filter;
	@Mock
	private EventStreamIo streamIo;

	private static final int FREQUENCY = 2;
	private static final double ENTROPY = 0.5;

	private Map<Integer, Set<Episode>> episodes;
	private List<Tuple<IMethodName, List<Fact>>> stream;
	private Map<String, Set<IMethodName>> repoCtxs;

	private ThresholdAnalyzer sut;

	@Before
	public void setup() throws Exception {
		Logger.reset();
		Logger.setCapturing(true);

		MockitoAnnotations.initMocks(this);

		episodes = Maps.newLinkedHashMap();

		Set<Episode> twoNodes = Sets.newLinkedHashSet();
		twoNodes.add(createEpisode(2, 1.0, "1", "2", "1>2"));
		twoNodes.add(createEpisode(3, 1.0, "1", "2", "2>1"));
		twoNodes.add(createEpisode(4, 0.5, "1", "2"));
		episodes.put(2, twoNodes);

		Set<Episode> threeNodes = Sets.newLinkedHashSet();
		threeNodes.add(createEpisode(2, 0.5, "7", "8", "34", "34>7", "34>8"));
		threeNodes.add(createEpisode(3, 0.7, "7", "8", "34", "7>8"));
		threeNodes.add(createEpisode(3, 0.5, "7", "8", "34", "7>8", "34>8"));
		threeNodes.add(createEpisode(2, 0.7, "7", "8", "34", "7>8", "7>34"));
		threeNodes.add(createEpisode(3, 0.5, "7", "8", "34", "7>8", "7>34",
				"8>34"));
		threeNodes.add(createEpisode(3, 0.7, "7", "8", "34", "7>34", "7>8",
				"34>8"));
		threeNodes.add(createEpisode(2, 0.7, "7", "8", "34", "34>7", "34>8",
				"7>8"));
		threeNodes.add(createEpisode(3, 0.5, "7", "8", "34", "8>7", "34>7"));
		threeNodes.add(createEpisode(3, 0.5, "7", "8", "34", "34>8", "34>7",
				"8>7"));
		episodes.put(3, threeNodes);

		stream = Lists.newLinkedList();
		stream.add(Tuple.newTuple(m(2), Lists.newArrayList(new Fact(1),
				new Fact(2), new Fact(1), new Fact(2), new Fact(3),
				new Fact(1), new Fact(3))));
		stream.add(Tuple.newTuple(m(34), Lists.newArrayList(new Fact(7),
				new Fact(8), new Fact(34), new Fact(8), new Fact(34), new Fact(
						7), new Fact(8), new Fact(7))));
		stream.add(Tuple.newTuple(m(3), Lists.newArrayList(new Fact(1),
				new Fact(2), new Fact(6), new Fact(1), new Fact(3))));
		stream.add(Tuple.newTuple(m(9), Lists.newArrayList(new Fact(7),
				new Fact(34), new Fact(1), new Fact(2), new Fact(8),
				new Fact(7), new Fact(34), new Fact(8), new Fact(34))));

		repoCtxs = Maps.newLinkedHashMap();
		repoCtxs.put("Repository1", Sets.newHashSet(m(2), m(9)));
		repoCtxs.put("Repository2", Sets.newHashSet(m(3), m(5)));

		sut = new ThresholdAnalyzer(streamIo, parser, filter);

		when(parser.parser(anyInt())).thenReturn(episodes);
		when(filter.filter(anyMap(), anyInt(), anyDouble())).thenReturn(
				episodes);
		when(streamIo.readStreamObject(FREQUENCY)).thenReturn(stream);
		when(streamIo.readRepoCtxs(FREQUENCY)).thenReturn(repoCtxs);
	}

	@After
	public void teardown() {
		Logger.reset();
	}

	@Test
	public void oneDim() throws Exception {
		sut.EntDim(FREQUENCY);

		assertLogContains(0, "\tEntropy threshold analyses!");
		assertLogContains(1, "\tFrequency\tEntropy\t#Patterns");
		assertLogContains(2, "\t2\t0.00\t12");

		verify(parser).parser(anyInt());
		verify(filter, times(1010)).filter(eq(episodes), anyInt(), anyDouble());
	}

	@Test
	public void twoDim() throws Exception {
		sut.EntFreqDim(FREQUENCY);

		assertLogContains(0, "\tFrequency-entropy analyzes!");
		assertLogContains(1, "\tFrequency\tEntropy\t#Patterns");
		assertLogContains(2, "\t2\t0.00\t12");

		verify(parser, times(2)).parser(anyInt());
		verify(filter, times(101)).filter(eq(episodes), anyInt(), anyDouble());
	}

	@Test
	public void histogram() throws Exception {
		sut.createHistogram(EpisodeType.GENERAL, FREQUENCY, ENTROPY);

		assertLogContains(0, "\tHistogram for GENERAL-configuration:");
		assertLogContains(1, "\tEntropy threshold = 0.5");
		assertLogContains(2, "\tFrequency\t#Patterns");
		assertLogContains(3, "\t2\t12");

		verify(parser).parser(anyInt());
		verify(filter, times(3)).filter(eq(episodes), anyInt(), anyDouble());
	}

	@Test
	public void generalizability() throws Exception {
		sut.generalizability(FREQUENCY);

		verify(streamIo).readStreamObject(anyInt());
		verify(streamIo).readRepoCtxs(anyInt());
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

	private static IMethodName m(int i) {
		if (i == 0) {
			return Names.getUnknownMethod();
		} else {
			return Names.newMethod("[T,P] [T,P].m" + i + "()");
		}
	}
}
