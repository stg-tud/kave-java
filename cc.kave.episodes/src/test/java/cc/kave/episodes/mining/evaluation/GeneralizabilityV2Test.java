package cc.kave.episodes.mining.evaluation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
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
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.exceptions.AssertionException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class GeneralizabilityV2Test {

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
	private List<Tuple<Event, List<Event>>> streamMethods;
	private Map<String, Set<IMethodName>> repoMethods;
	private List<Event> events;

	private DirectedGraph<Fact, DefaultEdge> graph;
	private EpisodeAsGraphWriter graphWriter;

	private static final int FREQUENCY = 2;
	private static final double ENTROPY = 0.3;

	private GeneralizabilityV2 sut;

	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);

		patterns = Maps.newLinkedHashMap();
		Set<Episode> episodes = Sets.newLinkedHashSet();

		Episode episode1 = createEpisode(4, 1.0, "2", "3", "2>3");
		episodes.add(episode1);
		Episode episode2 = createEpisode(3, 0.5, "4", "5", "4>5");
		episodes.add(episode2);
		patterns.put(2, episodes);

		episodes = Sets.newLinkedHashSet();
		Episode episode3 = createEpisode(2, 0.3, "1", "2", "3");
		episodes.add(episode3);
		Episode episode4 = createEpisode(1, 0.7, "2", "3", "5", "2>3", "2>5");
		episodes.add(episode4);
		patterns.put(3, episodes);

		streamMethods = Lists.newLinkedList();
		streamMethods
				.add(Tuple.newTuple(enclCtx(2), Lists.newArrayList(firstCtx(1),
						inv(2), inv(2), inv(3), inv(3))));
		streamMethods.add(Tuple.newTuple(enclCtx2(5),
				Lists.newArrayList(inv(4), inv(4), inv(5), inv(5))));
		streamMethods.add(Tuple.newTuple(enclCtx(3),
				Lists.newArrayList(firstCtx(1), inv(2), inv(6), inv(3))));
		streamMethods.add(Tuple.newTuple(enclCtx(9),
				Lists.newArrayList(inv(4), inv(2), inv(5), inv(3))));

		repoMethods = Maps.newLinkedHashMap();
		repoMethods
				.put("Repository1", Sets.newHashSet(enclCtx(2).getMethod(),
						enclCtx(9).getMethod()));
		repoMethods.put("Repository2", Sets.newHashSet(enclCtx(3).getMethod(),
				enclCtx2(5).getMethod()));

		events = Lists.newArrayList(dummy(), firstCtx(1), inv(2), inv(3),
				inv(4), inv(5), inv(6));

		graph = new DefaultDirectedGraph<Fact, DefaultEdge>(DefaultEdge.class);
		graphWriter = new EpisodeAsGraphWriter();

		sut = new GeneralizabilityV2(rootFolder.getRoot(), ctxParser,
				episodeParser, patternFilter, streamIo, transClosure,
				graphConverter, graphWriter);

		when(streamIo.readMapping(FREQUENCY)).thenReturn(events);
		when(episodeParser.parser(anyInt())).thenReturn(patterns);
		when(
				patternFilter.filter(any(EpisodeType.class), any(Map.class),
						anyInt(), anyDouble())).thenReturn(patterns);
		when(ctxParser.parse(anyInt())).thenReturn(streamMethods);
		when(ctxParser.getRepoCtxMapper()).thenReturn(repoMethods);

		when(transClosure.remTransClosure(eq(episode1))).thenReturn(episode1);
		when(transClosure.remTransClosure(eq(episode2))).thenReturn(episode2);
		when(transClosure.remTransClosure(eq(episode3))).thenReturn(episode3);
		when(transClosure.remTransClosure(eq(episode4))).thenReturn(episode4);

		when(graphConverter.convert(any(Episode.class), any(List.class)))
				.thenReturn(graph);
	}

	@Test
	public void cannotBeInitializedWithNonExistingPatternsFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Patterns folder does not exist!");
		sut = new GeneralizabilityV2(new File("does not exists"), ctxParser,
				episodeParser, patternFilter, streamIo, transClosure,
				graphConverter, graphWriter);
	}

	@Test
	public void cannotBeInitializedWithPatternsFile() throws IOException {
		File patternsFile = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Patterns is not a folder, but a file!");
		sut = new GeneralizabilityV2(patternsFile, ctxParser, episodeParser,
				patternFilter, streamIo, transClosure, graphConverter,
				graphWriter);
	}

	@Test
	public void mocksAreCalled() throws Exception {
		sut.validate(FREQUENCY, FREQUENCY, ENTROPY);

		verify(streamIo).readMapping(anyInt());
		verify(episodeParser).parser(anyInt());
		verify(patternFilter, times(3)).filter(any(EpisodeType.class), any(Map.class),
				anyInt(), anyDouble());
		verify(ctxParser).parse(anyInt());
		verify(ctxParser).getRepoCtxMapper();
		verify(transClosure, times(12)).remTransClosure(any(Episode.class));
		verify(graphConverter, times(12)).convert(any(Episode.class),
				any(List.class));
	}
	
	@Test
	public void filesAreCreated() throws Exception {
		File patternFile1 = getPatternFile(EpisodeType.SEQUENTIAL, 0);
		File patternFile2 = getPatternFile(EpisodeType.SEQUENTIAL, 1);
		File patternFile3 = getPatternFile(EpisodeType.SEQUENTIAL, 2);
		File patternFile4 = getPatternFile(EpisodeType.SEQUENTIAL, 3);
		File evalFile1 = getEvalFile(EpisodeType.SEQUENTIAL);
		
		File patternFile5 = getPatternFile(EpisodeType.PARALLEL, 0);
		File patternFile6 = getPatternFile(EpisodeType.PARALLEL, 1);
		File patternFile7 = getPatternFile(EpisodeType.PARALLEL, 2);
		File patternFile8 = getPatternFile(EpisodeType.PARALLEL, 3);
		File evalFile2 = getEvalFile(EpisodeType.PARALLEL);
		
		File patternFile9 = getPatternFile(EpisodeType.GENERAL, 0);
		File patternFile10 = getPatternFile(EpisodeType.GENERAL, 1);
		File patternFile11 = getPatternFile(EpisodeType.GENERAL, 2);
		File patternFile12 = getPatternFile(EpisodeType.GENERAL, 3);
		File evalFile3 = getEvalFile(EpisodeType.GENERAL);

		sut.validate(FREQUENCY, FREQUENCY, ENTROPY);

		assertTrue(patternFile1.exists());
		assertTrue(patternFile2.exists());
		assertTrue(patternFile3.exists());
		assertTrue(patternFile4.exists());
		assertTrue(evalFile1.exists());
		
		assertTrue(patternFile5.exists());
		assertTrue(patternFile6.exists());
		assertTrue(patternFile7.exists());
		assertTrue(patternFile8.exists());
		assertTrue(evalFile2.exists());
		
		assertTrue(patternFile9.exists());
		assertTrue(patternFile10.exists());
		assertTrue(patternFile11.exists());
		assertTrue(patternFile12.exists());
		assertTrue(evalFile3.exists());
	}
	
	@Test
	public void checkFileContent() throws Exception {

		sut.validate(FREQUENCY, FREQUENCY, ENTROPY);

		String actuals1 = FileUtils
				.readFileToString(getEvalFile(EpisodeType.SEQUENTIAL));
		String actuals2 = FileUtils
				.readFileToString(getEvalFile(EpisodeType.PARALLEL));
		String actuals3 = FileUtils
				.readFileToString(getEvalFile(EpisodeType.GENERAL));

		StringBuilder sb = new StringBuilder();
		sb.append("PatternId\tFacts\tFrequency\tEntropy\t#Repos\n");
		sb.append("0\t");
		sb.append("[2, 3, 2>3]\t");
		sb.append("4\t1.0\t2\n");

		sb.append("1\t");
		sb.append("[4, 5, 4>5]\t");
		sb.append("3\t0.5\t2\n");

		sb.append("2\t[1, 2, 3]\t");
		sb.append("2\t0.3\t2\n");

		sb.append("3\t[2, 3, 5, 2>3, 2>5]\t");
		sb.append("1\t0.7\t1\n\n");

		assertEquals(sb.toString(), actuals1);
		assertEquals(sb.toString(), actuals2);
		assertEquals(sb.toString(), actuals3);
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
				+ FREQUENCY + "/entropy" + ENTROPY + "/" + episodeType.toString();
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
