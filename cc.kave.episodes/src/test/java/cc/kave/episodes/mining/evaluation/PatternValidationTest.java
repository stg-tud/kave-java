package cc.kave.episodes.mining.evaluation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

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
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.IndivReposParser;
import cc.kave.episodes.io.ValidationDataIO;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EpisodesFilter;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.exceptions.AssertionException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PatternValidationTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Rule
	public TemporaryFolder patternsFolder = new TemporaryFolder();

	@Mock
	private EventStreamIo eventStream;
	@Mock
	private EpisodesParser episodeParser;
	@Mock
	private EpisodesFilter episodeFilter;
	@Mock
	private ValidationDataIO validationDataIo;
	@Mock
	private TransClosedEpisodes transClosure;
	@Mock
	private EpisodeToGraphConverter episodeToGraph;
	@Mock
	private IndivReposParser reposParser;
//	@Mock
//	private OverlapingTypes overlaps;

	private EpisodeAsGraphWriter graphWriter;

	private List<Tuple<List<Fact>, Event>> streamMethods;
	private List<Event> trainEvents;
	private Map<String, Set<IMethodName>> repoMethods;

	private Map<Integer, Set<Episode>> patterns;

	private List<Event> valStream;

	private DirectedGraph<Fact, DefaultEdge> graph;

	private Set<ITypeName> typeOverlaps;

	private static final int FREQUENCY = 5;

	private PatternsValidation sut;

	@Before
	public void setup() throws ZipException, IOException {
		MockitoAnnotations.initMocks(this);

		graphWriter = new EpisodeAsGraphWriter();

		streamMethods = Lists.newLinkedList();
		streamMethods.add(Tuple.newTuple(Lists.newArrayList(new Fact(1),
				new Fact(2), new Fact(3), new Fact(4)), enclCtx(2)));
		streamMethods.add(Tuple.newTuple(Lists.newArrayList(new Fact(5),
				new Fact(6), new Fact(7), new Fact(3)), enclCtx(7)));
		streamMethods.add(Tuple.newTuple(
				Lists.newArrayList(new Fact(8), new Fact(4), new Fact(3)),
				enclCtx(8)));
		streamMethods.add(Tuple.newTuple(
				Lists.newArrayList(new Fact(5), new Fact(9), new Fact(3)),
				enclCtx(9)));

		trainEvents = Lists.newArrayList(dummy(), firstCtx(1), inv(2), inv(3),
				inv(4), firstCtx(5), superCtx(6), inv(6), inv(7), inv(5),
				inv(9));

		repoMethods = Maps.newLinkedHashMap();
		repoMethods
				.put("Repository1", Sets.newHashSet(enclCtx(2).getMethod(),
						enclCtx(9).getMethod()));
		repoMethods.put("Repository2", Sets.newHashSet(enclCtx(7).getMethod()));
		repoMethods.put("Repository3", Sets.newHashSet(enclCtx(8).getMethod()));

		patterns = Maps.newLinkedHashMap();
		Set<Episode> episodes = Sets.newLinkedHashSet();
		Episode ep = createEpisode(5, 1.0, "1");
		episodes.add(ep);

		ep = createEpisode(7, 1, "2");
		episodes.add(ep);
		patterns.put(1, episodes);

		ep = createEpisode(1, 1.0, "2", "3", "2>3");
		episodes = Sets.newHashSet(ep);
		patterns.put(2, episodes);

		valStream = Lists.newArrayList(firstCtx(1), enclCtx(3), inv(3), inv(4),
				firstCtx(5), superCtx(6), enclCtx(7), inv(3), firstCtx(0),
				enclCtx(4), inv(4), inv(3), firstCtx(5), enclCtx(9), inv(3),
				firstCtx(0), enclCtx(7), inv(2), inv(3), firstCtx(2),
				enclCtx2(4), inv(2), inv(3));

		typeOverlaps = Sets.newHashSet(enclCtx(7).getMethod()
				.getDeclaringType(), enclCtx(9).getMethod().getDeclaringType());

		graph = new DefaultDirectedGraph<Fact, DefaultEdge>(DefaultEdge.class);

		sut = new PatternsValidation(patternsFolder.getRoot(), episodeFilter,
				eventStream, episodeParser, transClosure, episodeToGraph,
				graphWriter, validationDataIo, reposParser);

		when(eventStream.parseEventStream(anyInt())).thenReturn(streamMethods);
		when(eventStream.readMapping(anyInt())).thenReturn(trainEvents);
		when(episodeParser.parse(anyInt(), any(EpisodeType.class))).thenReturn(
				patterns);
		when(episodeFilter.filter(any(Map.class), anyInt(), anyDouble()))
				.thenReturn(patterns);
		when(validationDataIo.read(anyInt())).thenReturn(valStream);
		when(transClosure.remTransClosure(any(Episode.class))).thenReturn(ep);
		when(episodeToGraph.convert(any(Episode.class), any(List.class)))
				.thenReturn(graph);
		when(reposParser.getRepoCtxMapper()).thenReturn(repoMethods);
//		when(overlaps.getOverlaps(anyInt())).thenReturn(typeOverlaps);
	}

	@Test
	public void cannotBeInitializedWithNonExistingPatternsFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Patterns folder does not exist");
		sut = new PatternsValidation(new File("does not exist"), episodeFilter,
				eventStream, episodeParser, transClosure, episodeToGraph,
				graphWriter, validationDataIo, reposParser);
	}

	@Test
	public void cannotBeInitializedWithPatternsFile() throws IOException {
		File patternsFile = patternsFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Patterns is not a folder, but a file");
		sut = new PatternsValidation(patternsFile, episodeFilter, eventStream,
				episodeParser, transClosure, episodeToGraph, graphWriter,
				validationDataIo, reposParser);
	}

	@Test
	public void mocksAreCalled() throws Exception {
		sut.validate(FREQUENCY, EpisodeType.GENERAL);
		;

		verify(eventStream).parseEventStream(anyInt());
		verify(eventStream).readMapping(anyInt());
		verify(episodeParser).parse(anyInt(), any(EpisodeType.class));
		verify(episodeFilter).filter(any(Map.class), anyInt(), anyDouble());
		verify(validationDataIo).read(anyInt());
		verify(transClosure).remTransClosure(any(Episode.class));
		verify(episodeToGraph).convert(any(Episode.class), any(List.class));
		verify(reposParser).getRepoCtxMapper();
//		verify(overlaps).getOverlaps(anyInt());
	}

	@Test
	public void fileIsCreated() throws Exception {
		File patternFile = getPatternFile(EpisodeType.SEQUENTIAL, 0);
		File evalFile = getEvalFile(EpisodeType.SEQUENTIAL);

		sut.validate(FREQUENCY, EpisodeType.SEQUENTIAL);

		assertTrue(patternFile.exists());
		assertTrue(evalFile.exists());
	}

	@Test
	public void checkFileContent() throws Exception {

		sut.validate(FREQUENCY, EpisodeType.SEQUENTIAL);

		String actuals = FileUtils
				.readFileToString(getEvalFile(EpisodeType.SEQUENTIAL));

		StringBuilder sb = new StringBuilder();
		sb.append("Patterns with 2-nodes:\n");
		sb.append("PatternId\tEvents\tFrequency\tEntropy\tNoRepos\tOccValidation\n");
		sb.append("0\t");
		sb.append("[2, 3, 2>3]\t");
		sb.append("1\t1.0\t1\t1\n\n");

		assertEquals(sb.toString(), actuals);
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

	private String getPath(EpisodeType episodeType) {
		String path = patternsFolder.getRoot().getAbsolutePath() + "/freq"
				+ FREQUENCY + "/" + episodeType.toString();
		return path;
	}

	private File getPatternFile(EpisodeType episodeType, int patternId) {
		File patternFile = new File(getPath(episodeType) + "/pattern"
				+ patternId + ".dot");
		return patternFile;
	}

	private File getEvalFile(EpisodeType episodeType) {
		File evalFile = new File(getPath(episodeType) + "/evaluations.txt");
		return evalFile;
	}
}
