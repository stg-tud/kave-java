package cc.kave.episodes.mining.evaluation;


public class PatternsEvents {

//	private EventStreamIo eventsStream;
//	private EpisodeReader episodeParser;
//	private EpisodesFilter episodeFilter;
//
//	@Inject
//	public PatternsEvents(EventStreamIo eventsIo, EpisodeReader parser,
//			EpisodesFilter filter) {
//		this.eventsStream = eventsIo;
//		this.episodeParser = parser;
//		this.episodeFilter = filter;
//	}
//
//	public void getEventsType(EpisodeType type, int frequency, double entropy,
//			int foldNum) {
//		List<Event> events = eventsStream.readMapping(frequency);
//		Map<Integer, Set<Episode>> episodes = episodeParser.read(frequency);
//		Map<Integer, Set<Episode>> patterns = episodeFilter.filter(type,
//				episodes, frequency, entropy);
//
//		int numPatterns = 0;
//		Map<EventKind, Set<Event>> eventKinds = Maps.newLinkedHashMap();
//
//		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
//			for (Episode ep : entry.getValue()) {
//				numPatterns++;
//				for (Fact fact : ep.getEvents()) {
//					int factId = fact.getFactID();
//					Event event = events.get(factId);
//					EventKind kind = event.getKind();
//
//					if (eventKinds.containsKey(kind)) {
//						eventKinds.get(kind).add(event);
//					} else {
//						eventKinds.put(kind, Sets.newHashSet(event));
//					}
//				}
//			}
//		}
//		Logger.log("Configuration %s learns %d patterns", type.toString(),
//				numPatterns);
//		Logger.log("Patterns contains the following event kinds:");
//		for (Map.Entry<EventKind, Set<Event>> entry : eventKinds.entrySet()) {
//			Logger.log("Kind %s: %d events", entry.getKey().toString(), entry
//					.getValue().size());
//		}
//	}
}
