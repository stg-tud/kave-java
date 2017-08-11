package cc.kave.episodes.mining.evaluation;


public class SimilarityMetrics {

//	private File eventsFolder;
//
//	private EventStreamIo eventStreamIo;
//	private EpisodesFilter episodeProcessor;
//	private MaximalEpisodes maxEpisodes;
//	private QueryGeneration generator;
//	private ProposalsSorter sorter;
//
//	private TransClosedEpisodes transClosure;
//	private EpisodeToGraphConverter episodeGraphConverter;
//	private EpisodeAsGraphWriter graphWriter;
//
//	@Inject
//	public SimilarityMetrics(@Named("events") File folder,
//			EpisodesFilter postprocess, MaximalEpisodes maxEp,
//			QueryGeneration qGenerator, ProposalsSorter propSorter,
//			TransClosedEpisodes closedEp, EpisodeToGraphConverter epToGraph,
//			EpisodeAsGraphWriter graphWriter) {
//		assertTrue(folder.exists(), "Events folder does not exist!");
//		assertTrue(folder.isDirectory(), "Events is not a folder, but a file!");
//		this.eventsFolder = folder;
//		this.episodeProcessor = postprocess;
//		this.maxEpisodes = maxEp;
//		this.generator = qGenerator;
//		this.sorter = propSorter;
//		this.transClosure = closedEp;
//		this.episodeGraphConverter = epToGraph;
//		this.graphWriter = graphWriter;
//	}
//
//	public void validate(int frequency) throws Exception {
//		List<Event> mapping = eventStreamIo.readMapping(frequency);
//
//		Map<Integer, Set<Episode>> episodes = episodeProcessor.filter(
//				EpisodeType.GENERAL, Maps.newHashMap(), 500, 0.6);
//		Map<Integer, Set<Episode>> patterns = maxEpisodes
//				.getMaximalEpisodes(episodes);
//		Set<Episode> patternsSet = getAllPatterns(patterns);
//		Logger.log("Number of patterns is %d", patternsSet.size());
//
//		int queryCounter = 0;
//
//		for (Map.Entry<Integer, Set<Episode>> pEntry : patterns.entrySet()) {
//			if (pEntry.getKey() > 1) {
//				for (Episode pattern : pEntry.getValue()) {
//					Set<Episode> queries = generator.generate(pattern);
//					for (Episode query : queries) {
//						Logger.log("Generating results for query %d...",
//								queryCounter);
//
//						// Episode closedEpisodes =
//						// transClosure.remTransClosure(episode);
//						//
//						// File filePath = getPath(numbRepos, frequency,
//						// entropy);
//						//
//						// DirectedGraph<Fact, DefaultEdge> graph =
//						// episodeGraphConverter.convert(
//						// closedEpisodes, events);
//						// graphWriter.write(graph, getGraphPaths(filePath,
//						// pId));
//
//						Episode closedQuery = transClosure
//								.remTransClosure(query);
//						DirectedGraph<Fact, DefaultEdge> queryGraph = episodeGraphConverter
//								.convert(closedQuery, mapping);
//						graphWriter.write(queryGraph,
//								getPropPath(queryCounter, "query"));
//
//						Episode closedPattern = transClosure
//								.remTransClosure(pattern);
//						DirectedGraph<Fact, DefaultEdge> patternGraph = episodeGraphConverter
//								.convert(closedPattern, mapping);
//						graphWriter.write(patternGraph,
//								getPropPath(queryCounter, "pattern"));
//
//						// FileUtils.writeStringToFile(
//						// new File(getPropPath(queryCounter, "query")),
//						// query.toString());
//
//						Set<Tuple<Episode, Double>> proposals = sorter.sort(
//								query, patternsSet, Metrics.F1_EVENTS);
//						storeProposals(proposals, queryCounter, "f1Events",
//								mapping);
//
//						proposals = sorter.sort(query, patternsSet,
//								Metrics.F1_FACTS);
//						storeProposals(proposals, queryCounter, "f1Facts",
//								mapping);
//
//						proposals = sorter.sort(query, patternsSet,
//								Metrics.MAPO);
//						storeProposals(proposals, queryCounter, "Mapo", mapping);
//						queryCounter++;
//					}
//				}
//			}
//		}
//	}
//
//	private void storeProposals(Set<Tuple<Episode, Double>> proposals,
//			int queryCounter, String metric, List<Event> mapping)
//			throws IOException {
//		StringBuilder sb = new StringBuilder();
//		int propCounter = 0;
//
//		// Episode closedEpisodes = transClosure.remTransClosure(episode);
//		//
//		// File filePath = getPath(numbRepos, frequency, entropy);
//		//
//		// DirectedGraph<Fact, DefaultEdge> graph =
//		// episodeGraphConverter.convert(
//		// closedEpisodes, events);
//		// graphWriter.write(graph, getGraphPaths(filePath, pId));
//
//		for (Tuple<Episode, Double> tuple : proposals) {
//			if (tuple.getSecond() > 0.0) {
//				Episode closedEpisode = transClosure.remTransClosure(tuple
//						.getFirst());
//				DirectedGraph<Fact, DefaultEdge> queryGraph = episodeGraphConverter
//						.convert(closedEpisode, mapping);
//				graphWriter
//						.write(queryGraph,
//								getPropPath(queryCounter, metric + "Prop"
//										+ propCounter));
//				propCounter++;
//				// sb.append(tuple.getFirst() + "\nMetric = " +
//				// tuple.getSecond());
//			}
//			// sb.append("\n");
//		}
//		// FileUtils.writeStringToFile(
//		// new File(getPropPath(queryCounter, metric)), sb.toString());
//	}
//
//	private File getPath(int queryCounter) {
//		File path = new File(eventsFolder.getAbsolutePath()
//				+ "/200Repos/simMetrics/query" + queryCounter);
//		if (!path.isDirectory()) {
//			path.mkdir();
//		}
//		return path;
//	}
//
//	private String getPropPath(int queryCounter, String metric) {
//		File filePath = getPath(queryCounter);
//		String fileName = filePath.getAbsolutePath() + "/" + metric + ".dot";
//		return fileName;
//	}
//
//	private Set<Episode> getAllPatterns(Map<Integer, Set<Episode>> patterns) {
//		Set<Episode> results = Sets.newHashSet();
//
//		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
//			if (entry.getKey() > 1) {
//				results.addAll(entry.getValue());
//			}
//		}
//		return results;
//	}
}
