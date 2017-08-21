package cc.kave.episodes.mining.evaluation;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.data.ContextsParser;
import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.mining.patterns.PatternFilter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.Triplet;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class GeneralizabilityTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Mock
	private ContextsParser ctxParser;
	@Mock
	private EventStreamIo streamIo;
	@Mock
	private EpisodeParser episodeParser;
	@Mock
	private PatternFilter patternFilter;
	@Mock
	private TransClosedEpisodes transClosure;
	@Mock
	private EpisodeToGraphConverter graphConverter;
	
	private Map<Integer, Set<Episode>> patterns;
	private List<Tuple<Event, List<Fact>>> streamMethods;
	private Map<String, Set<IMethodName>> repoMethods;
	private List<Event> events;

	private DirectedGraph<Fact, DefaultEdge> graph;
	private EpisodeAsGraphWriter graphWriter;

	private static final int FREQUENCY = 2;
	private static final double ENTROPY = 0.3;

	private Generalizability sut;
	
	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);

		graphWriter = new EpisodeAsGraphWriter();
		graph = new DefaultDirectedGraph<Fact, DefaultEdge>(DefaultEdge.class);

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
		repoMethods.put("Repository2",
				Sets.newHashSet(enclCtx2(5).getMethod()));

		events = Lists.newArrayList(dummy(), firstCtx(1),
				inv(3), inv(4), inv(6), inv(7), inv(8));

//		valStream = Lists.newArrayList(firstCtx(10), enclCtx(5), inv(3),
//				inv(4), firstCtx(11), superCtx(12), enclCtx(9), inv(4),
//				firstCtx(13), enclCtx(5), inv(3), inv(4), firstCtx(11),
//				enclCtx(14), inv(4), firstCtx(11), enclCtx(9), inv(3), inv(4),
//				firstCtx(13), enclCtx2(15), inv(3), inv(4));
//
//		valFactStream = Lists.newLinkedList();
//		List<Fact> method = Lists.newArrayList(new Fact(10), new Fact(5),
//				new Fact(3), new Fact(4));
//		valFactStream.add(method);
//		method = Lists.newArrayList(new Fact(11), new Fact(12), new Fact(9),
//				new Fact(4));
//		valFactStream.add(method);
//		method = Lists.newArrayList(new Fact(13), new Fact(5), new Fact(3),
//				new Fact(4));
//		valFactStream.add(method);
//		method = Lists.newArrayList(new Fact(11), new Fact(14), new Fact(4));
//		valFactStream.add(method);
//		method = Lists.newArrayList(new Fact(11), new Fact(9), new Fact(3),
//				new Fact(4));
//		valFactStream.add(method);
//		method = Lists.newArrayList(new Fact(13), new Fact(15), new Fact(3),
//				new Fact(4));
//		valFactStream.add(method);
//
//		valResults = Maps.newLinkedHashMap();
//		Set<Triplet<Episode, Integer, Integer>> valSet = Sets
//				.newLinkedHashSet();
//		valSet.add(new Triplet<Episode, Integer, Integer>(createEpisode(4, 1.0,
//				"3", "4", "3>4"), 1, 4));
//		valSet.add(new Triplet<Episode, Integer, Integer>(createEpisode(3, 0.5,
//				"6", "7", "6>7"), 2, 0));
//		valResults.put(2, valSet);
//
//		valSet = Sets.newLinkedHashSet();
//		valSet.add(new Triplet<Episode, Integer, Integer>(createEpisode(2, 0.3,
//				"1", "3", "4"), 1, 0));
//		valSet.add(new Triplet<Episode, Integer, Integer>(createEpisode(1, 0.7,
//				"3", "4", "7", "3>4", "3>7"), 1, 0));
//		valResults.put(3, valSet);
//
//		sut = new Evaluations(rootFolder.getRoot(), reposParser, eventStream,
//				episodeParser, episodeFilter, patternsValidation, validationIo,
//				transClosure, graphConverter, graphWriter);
//
//		when(eventStream.parseStream(FREQUENCY, FOLDNUM)).thenReturn(
//				streamMethods);
//		when(eventStream.readMapping(FREQUENCY, FOLDNUM)).thenReturn(
//				trainEvents);
//		when(episodeParser.parse(any(EpisodeType.class), anyInt(), anyInt()))
//				.thenReturn(patterns);
//		when(
//				episodeFilter.filter(any(EpisodeType.class), any(Map.class),
//						anyInt(), anyDouble())).thenReturn(patterns);
//		when(reposParser.getRepoMethodsMapper()).thenReturn(repoMethods);
//		when(validationIo.read(anyInt(), anyInt())).thenReturn(valStream);
//		when(validationIo.streamOfFacts(any(List.class), any(Map.class)))
//				.thenReturn(valFactStream);
//		when(
//				patternsValidation.validate(any(Map.class), any(List.class),
//						any(Map.class), any(List.class), any(List.class)))
//				.thenReturn(valResults);
//		when(transClosure.remTransClosure(any(Episode.class))).thenReturn(ep);
//		when(graphConverter.convert(any(Episode.class), any(List.class)))
//				.thenReturn(graph);
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

	private String getResultsPath(EpisodeType episodeType) {
		String path = rootFolder.getRoot().getAbsolutePath() + "/freq"
				+ FREQUENCY + "/" + episodeType.toString();
		return path;
	}

	private String getPatternsPath(EpisodeType type) {
		String path = getResultsPath(type) + "/allPatterns";
		return path;
	}

	private File getPatternFile(EpisodeType episodeType, int patternId) {
		File patternFile = new File(getPatternsPath(episodeType) + "/pattern"
				+ patternId + ".dot");
		return patternFile;
	}

	private File getEvalFile(EpisodeType episodeType) {
		File evalFile = new File(getResultsPath(episodeType)
				+ "/evaluations.txt");
		return evalFile;
	}
}
