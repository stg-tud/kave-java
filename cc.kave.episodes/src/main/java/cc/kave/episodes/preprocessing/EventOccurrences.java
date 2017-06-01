//package cc.kave.episodes.preprocessing;
//
//import static cc.recommenders.assertions.Asserts.assertTrue;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//import javax.inject.Named;
//
//import org.apache.commons.io.FileUtils;
//
//import cc.kave.episodes.data.ContextsParser;
//import cc.kave.episodes.eventstream.EventStreamGeneratorPrev;
//import cc.kave.episodes.eventstream.EventStreamNotGenerated;
//import cc.kave.episodes.eventstream.EventsFilter;
//import cc.kave.episodes.io.RepositoriesParser;
//import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
//import cc.kave.episodes.model.EventStream;
//import cc.kave.episodes.model.events.Event;
//import cc.kave.episodes.model.events.EventKind;
//import cc.kave.episodes.model.events.Fact;
//import cc.recommenders.datastructures.Tuple;
//import cc.recommenders.io.Logger;
//
//import com.google.common.collect.Lists;
//import com.google.common.collect.Maps;
//import com.google.common.collect.Sets;
//import com.google.inject.Inject;
//
//public class EventOccurrences {
//
//	private File eventsDir;
//	private ContextsParser reposParser;
//	private EpisodeToGraphConverter graphConverter;
//
//	@Inject
//	public EventOccurrences(@Named("events") File folder,
//			ContextsParser repositories,
//			EpisodeToGraphConverter graphConverter) {
//		assertTrue(folder.exists(), "Events folder does not exist");
//		assertTrue(folder.isDirectory(), "Events is not a folder, but a file");
//		this.eventsDir = folder;
//		this.reposParser = repositories;
//		this.graphConverter = graphConverter;
//	}
//
//	public void generate(int frequency) throws Exception {
//
//		Map<String, EventStreamNotGenerated> repos = reposParser
//				.generateReposEvents();
//		List<Event> data = Lists.newLinkedList();
//
//		Logger.log("Generating event stream data for freq = %d ...", frequency);
//		for (Map.Entry<String, EventStreamGeneratorPrev> entry : repos.entrySet()) {
//			data.addAll(entry.getValue().getEventStream());
//		}
//		EventStream stream = EventsFilter.filterStream(data, frequency);
//		List<Tuple<Event, String>> streamData = stream.getStreamData();
//		List<Tuple<Event, List<Fact>>> parsedStream = parseStream(streamData);
//		List<Event> events = new ArrayList<Event>(stream.getMapping());
//		Logger.log("Event at position 0: %s", events.get(0).getMethod()
//				.getDeclaringType().getFullName());
//		Logger.log("Number of events: %d", events.size());
//
//		Map<Fact, Integer> declOccs = Maps.newLinkedHashMap();
//		Map<Fact, Integer> invOccs = Maps.newLinkedHashMap();
//
//		for (Tuple<Event, List<Fact>> method : parsedStream) {
//			for (Fact fact : method.getSecond()) {
//				int factId = fact.getFactID();
//				Event event = events.get(factId);
//
//				if (event.getKind() == EventKind.INVOCATION) {
//					if (invOccs.containsKey(fact)) {
//						int occurrence = invOccs.get(fact);
//						invOccs.put(fact, occurrence + 1);
//					} else {
//						invOccs.put(fact, 1);
//					}
//				} else {
//					if (declOccs.containsKey(fact)) {
//						int occurrence = declOccs.get(fact);
//						declOccs.put(fact, occurrence + 1);
//					} else {
//						declOccs.put(fact, 1);
//					}
//				}
//			}
//		}
//		Logger.log("Number of method declarations: %d", declOccs.size());
//		Logger.log("Number of method invocations: %d", invOccs.size());
//
//		storeOccurrences(declOccs, events, "declOccs");
//		storeOccurrences(invOccs, events, "invOccs");
//		
//		Map<Integer, Integer> declHistogram = getHistogram(declOccs);
//		storeHistogram(declHistogram, "declHistogram");
//		Map<Integer, Integer> invHistogram = getHistogram(invOccs);
//		storeHistogram(invHistogram, "invHistogram");
//	}
//
//	private void storeHistogram(Map<Integer, Integer> declHistogram,
//			String fileName) throws IOException {
//		StringBuilder sb = new StringBuilder();
//		sb.append("Occurrence\tNumber of events\n");
//		for (Map.Entry<Integer, Integer> entry : declHistogram.entrySet()) {
//			sb.append(entry.getKey() + "\t" + entry.getValue() + "\n");
//		}
//		FileUtils.writeStringToFile(getFilePath(fileName), sb.toString());
//		
//	}
//
//	private Map<Integer, Integer> getHistogram(Map<Fact, Integer> declOccs) {
//		Map<Integer, Integer> results = Maps.newLinkedHashMap();
//		Set<Integer> occurrences = Sets.newLinkedHashSet();
//		
//		for (Map.Entry<Fact, Integer> entry : declOccs.entrySet()) {
//			occurrences.add(entry.getValue());
//		}
//		for (Integer occ : occurrences) {
//			int numbEvents = 0;
//			for (Map.Entry<Fact, Integer> entry : declOccs.entrySet()) {
//				if (entry.getValue() >= occ) {
//					numbEvents++;
//				}
//			}
//			results.put(occ, numbEvents);
//		}
//		return results;
//	}
//
//	private void storeOccurrences(Map<Fact, Integer> occurrences,
//			List<Event> events, String fileName) throws IOException {
//		StringBuilder sb = new StringBuilder();
//		for (Map.Entry<Fact, Integer> entry : occurrences.entrySet()) {
//			Event event = events.get(entry.getKey().getFactID());
//			sb.append(graphConverter.toLabel(event.getMethod()));
//			sb.append("\t" + entry.getValue() + "\n");
//		}
//		FileUtils.writeStringToFile(getFilePath(fileName), sb.toString());
//	}
//
//	private File getFilePath(String fileName) {
//		File file = new File(eventsDir.getAbsolutePath() + "/" + fileName
//				+ ".txt");
//		return file;
//	}
//
//	private List<Tuple<Event, List<Fact>>> parseStream(
//			List<Tuple<Event, String>> streamData) {
//		List<Tuple<Event, List<Fact>>> results = Lists.newLinkedList();
//
//		for (Tuple<Event, String> methodTuple : streamData) {
//			List<Fact> methodFacts = Lists.newLinkedList();
//			String[] lines = methodTuple.getSecond().split("\n");
//
//			for (String line : lines) {
//				String[] eventTime = line.split(",");
//				int eventID = Integer.parseInt(eventTime[0]);
//				methodFacts.add(new Fact(eventID));
//			}
//			results.add(Tuple.newTuple(methodTuple.getFirst(), methodFacts));
//		}
//		return results;
//	}
//}
