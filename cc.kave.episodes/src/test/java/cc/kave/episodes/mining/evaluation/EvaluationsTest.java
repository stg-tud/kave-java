//package cc.kave.episodes.mining.evaluation;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyDouble;
//import static org.mockito.Matchers.anyInt;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import org.apache.commons.io.FileUtils;
//import org.jgrapht.DirectedGraph;
//import org.jgrapht.graph.DefaultDirectedGraph;
//import org.jgrapht.graph.DefaultEdge;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.ExpectedException;
//import org.junit.rules.TemporaryFolder;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import cc.kave.commons.model.naming.Names;
//import cc.kave.commons.model.naming.codeelements.IMethodName;
//import cc.kave.episodes.data.ContextsParser;
//import cc.kave.episodes.io.EpisodesParser;
//import cc.kave.episodes.io.EventStreamIo;
//import cc.kave.episodes.io.ValidationDataIO;
//import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
//import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
//import cc.kave.episodes.model.Episode;
//import cc.kave.episodes.model.EpisodeType;
//import cc.kave.episodes.model.Triplet;
//import cc.kave.episodes.model.events.Event;
//import cc.kave.episodes.model.events.Events;
//import cc.kave.episodes.model.events.Fact;
//import cc.kave.episodes.postprocessor.EpisodesFilter;
//import cc.kave.episodes.postprocessor.TransClosedEpisodes;
//import cc.recommenders.datastructures.Tuple;
//import cc.recommenders.exceptions.AssertionException;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.google.common.collect.Sets;
//
//public class EvaluationsTest {
//
//	@Rule
//	public TemporaryFolder rootFolder = new TemporaryFolder();
//	@Rule
//	public ExpectedException thrown = ExpectedException.none();
//
//	@Mock
//	private ContextsParser reposParser;
//	@Mock
//	private EventStreamIo eventStream;
//	@Mock
//	private EpisodesParser episodeParser;
//	@Mock
//	private EpisodesFilter episodeFilter;
//	@Mock
//	private ValidationDataIO validationIo;
//	@Mock
//	private PatternsValidation patternsValidation;
//	@Mock
//	private TransClosedEpisodes transClosure;
//	@Mock
//	private EpisodeToGraphConverter graphConverter;
//
//	private EpisodeAsGraphWriter graphWriter;
//
//	private Map<Integer, Set<Episode>> patterns;
//	private List<Tuple<Event, List<Fact>>> streamMethods;
//	private Map<String, Set<IMethodName>> repoMethods;
//	private List<Event> trainEvents;
//	private List<Event> valStream;
//	private List<List<Fact>> valFactStream;
//	private Map<Integer, Set<Triplet<Episode, Integer, Integer>>> valResults;
//	private DirectedGraph<Fact, DefaultEdge> graph;
//
//	private static final int FREQUENCY = 2;
//	private static final int FOLDNUM = 0;
//	private static final double ENTROPY = 0.3;
//
//	private Evaluations sut;
//
//	@Before
//	public void setup() throws Exception {
//		MockitoAnnotations.initMocks(this);
//
//		graphWriter = new EpisodeAsGraphWriter();
//		graph = new DefaultDirectedGraph<Fact, DefaultEdge>(DefaultEdge.class);
//
//		patterns = Maps.newLinkedHashMap();
//		Set<Episode> episodes = Sets.newLinkedHashSet();
//
//		Episode ep = createEpisode(4, 1.0, "3", "4", "3>4");
//		episodes.add(ep);
//		ep = createEpisode(3, 0.5, "6", "7", "6>7");
//		episodes.add(ep);
//		patterns.put(2, episodes);
//
//		episodes = Sets.newLinkedHashSet();
//		ep = createEpisode(2, 0.3, "1", "3", "4");
//		episodes.add(ep);
//		ep = createEpisode(1, 0.7, "3", "4", "7", "3>4", "3>7");
//		episodes.add(ep);
//		patterns.put(3, episodes);
//
//		streamMethods = Lists.newLinkedList();
//		streamMethods.add(Tuple.newTuple(enclCtx(2), Lists
//				.newArrayList(new Fact(1), new Fact(3), new Fact(3),
//						new Fact(4), new Fact(4))));
//		streamMethods.add(Tuple.newTuple(enclCtx2(5), Lists.newArrayList(
//				new Fact(6), new Fact(6), new Fact(7), new Fact(7))));
//		streamMethods.add(Tuple.newTuple(enclCtx(2), Lists.newArrayList(
//				new Fact(1), new Fact(3), new Fact(8), new Fact(4))));
//		streamMethods.add(Tuple.newTuple(enclCtx(9), Lists.newArrayList(
//				new Fact(6), new Fact(3), new Fact(7), new Fact(4))));
//
//		repoMethods = Maps.newLinkedHashMap();
//		repoMethods
//				.put("Repository1", Sets.newHashSet(enclCtx(2).getMethod(),
//						enclCtx(9).getMethod()));
//		repoMethods.put("Repository2",
//				Sets.newHashSet(enclCtx2(5).getMethod()));
//
//		trainEvents = Lists.newArrayList(dummy(), firstCtx(1), enclCtx(2),
//				inv(3), inv(4), enclCtx(5), inv(6), inv(7), inv(8), enclCtx(9));
//
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
//	}
//
//	@Test
//	public void cannotBeInitializedWithNonExistingPatternsFolder() {
//		thrown.expect(AssertionException.class);
//		thrown.expectMessage("Patterns folder does not exist");
//		sut = new Evaluations(new File("does not exists"), reposParser,
//				eventStream, episodeParser, episodeFilter, patternsValidation,
//				validationIo, transClosure, graphConverter, graphWriter);
//	}
//
//	@Test
//	public void cannotBeInitializedWithPatternsFile() throws IOException {
//		File patternsFile = rootFolder.newFile("a");
//		thrown.expect(AssertionException.class);
//		thrown.expectMessage("Patterns is not a folder, but a file");
//		sut = new Evaluations(patternsFile, reposParser, eventStream,
//				episodeParser, episodeFilter, patternsValidation, validationIo,
//				transClosure, graphConverter, graphWriter);
//	}
//
//	@Test
//	public void mocksAreCalled() throws Exception {
//		sut.patternsOutput(EpisodeType.GENERAL, FREQUENCY, FOLDNUM, FREQUENCY,
//				ENTROPY);
//
//		verify(eventStream).parseStream(anyInt(), anyInt());
//		verify(eventStream).readMapping(anyInt(), anyInt());
//		verify(episodeParser).parse(any(EpisodeType.class), anyInt(), anyInt());
//		verify(episodeFilter).filter(any(EpisodeType.class), any(Map.class),
//				anyInt(), anyDouble());
//		verify(reposParser).getRepoMethodsMapper();
//		verify(validationIo).read(anyInt(), anyInt());
//		verify(validationIo).streamOfFacts(any(List.class), any(Map.class));
//		verify(patternsValidation).validate(any(Map.class), any(List.class),
//				any(Map.class), any(List.class), any(List.class));
//		verify(transClosure, times(4)).remTransClosure(any(Episode.class));
//		verify(graphConverter, times(4)).convert(any(Episode.class),
//				any(List.class));
//	}
//
//	@Test
//	public void fileIsCreated() throws Exception {
//		File patternFile1 = getPatternFile(EpisodeType.GENERAL, 0);
//		File patternFile2 = getPatternFile(EpisodeType.GENERAL, 1);
//		File patternFile3 = getPatternFile(EpisodeType.GENERAL, 2);
//		File patternFile4 = getPatternFile(EpisodeType.GENERAL, 3);
//		File evalFile = getEvalFile(EpisodeType.GENERAL);
//
//		sut.patternsOutput(EpisodeType.GENERAL, FREQUENCY, FOLDNUM, FREQUENCY,
//				ENTROPY);
//
//		assertTrue(patternFile1.exists());
//		assertTrue(patternFile2.exists());
//		assertTrue(patternFile3.exists());
//		assertTrue(patternFile4.exists());
//		assertTrue(evalFile.exists());
//	}
//
//	@Test
//	public void checkFileContent() throws Exception {
//
//		sut.patternsOutput(EpisodeType.GENERAL, FREQUENCY, FOLDNUM, FREQUENCY,
//				ENTROPY);
//
//		String actuals = FileUtils
//				.readFileToString(getEvalFile(EpisodeType.GENERAL));
//
//		StringBuilder sb = new StringBuilder();
//		sb.append("Patterns with 2-nodes:\n");
//		sb.append("PatternId\tFacts\tFrequency\tEntropy\tNoRepos\tOccValidation\n");
//		sb.append("0\t");
//		sb.append("[3, 4, 3>4]\t");
//		sb.append("4\t1.0\t1\t4\n");
//
//		sb.append("1\t");
//		sb.append("[6, 7, 6>7]\t");
//		sb.append("3\t0.5\t2\t0\n\n");
//
//		sb.append("Patterns with 3-nodes:\n");
//		sb.append("PatternId\tFacts\tFrequency\tEntropy\tNoRepos\tOccValidation\n");
//		sb.append("2\t[1, 3, 4]\t");
//		sb.append("2\t0.3\t1\t0\n");
//
//		sb.append("3\t[3, 4, 7, 3>4, 3>7]\t");
//		sb.append("1\t0.7\t1\t0\n\n");
//
//		assertEquals(sb.toString(), actuals);
//	}
//
//	private Episode createEpisode(int frequency, double entropy,
//			String... strings) {
//		Episode episode = new Episode();
//		episode.addStringsOfFacts(strings);
//		episode.setFrequency(frequency);
//		episode.setEntropy(entropy);
//		return episode;
//	}
//
//	private static Event inv(int i) {
//		return Events.newInvocation(m(i));
//	}
//
//	private static Event firstCtx(int i) {
//		return Events.newFirstContext(m(i));
//	}
//
//	private static Event superCtx(int i) {
//		return Events.newSuperContext(m(i));
//	}
//
//	private static Event enclCtx(int i) {
//		return Events.newContext(m(i));
//	}
//
//	private static Event enclCtx2(int i) {
//		return Events.newContext(m2(i));
//	}
//
//	private static Event dummy() {
//		return Events.newDummyEvent();
//	}
//
//	private static IMethodName m(int i) {
//		if (i == 0) {
//			return Names.getUnknownMethod();
//		} else {
//			return Names.newMethod("[T,P] [T,P].m" + i + "()");
//		}
//	}
//
//	private static IMethodName m2(int i) {
//		if (i == 0) {
//			return Names.getUnknownMethod();
//		} else {
//			return Names.newMethod("[T2,P2] [T2,P2].m" + i + "()");
//		}
//	}
//
//	private String getResultsPath(EpisodeType episodeType) {
//		String path = rootFolder.getRoot().getAbsolutePath() + "/freq"
//				+ FREQUENCY + "/" + episodeType.toString();
//		return path;
//	}
//
//	private String getPatternsPath(EpisodeType type) {
//		String path = getResultsPath(type) + "/allPatterns";
//		return path;
//	}
//
//	private File getPatternFile(EpisodeType episodeType, int patternId) {
//		File patternFile = new File(getPatternsPath(episodeType) + "/pattern"
//				+ patternId + ".dot");
//		return patternFile;
//	}
//
//	private File getEvalFile(EpisodeType episodeType) {
//		File evalFile = new File(getResultsPath(episodeType)
//				+ "/evaluations.txt");
//		return evalFile;
//	}
//}
