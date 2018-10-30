package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.patterns.PatternFilter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Events;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EnclosingMethods;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PatternMetrics {

	private EventStreamIo streamIo;

	private EpisodeParser parser;
	private PatternFilter filter;

	@Inject
	public PatternMetrics(EventStreamIo eventsStreamIo,
			EpisodeParser episodeParser, PatternFilter patternFilter) {
		this.streamIo = eventsStreamIo;
		this.parser = episodeParser;
		this.filter = patternFilter;
	}

	public void interestingness(int frequency, int freqThresh, double entThresh)
			throws Exception {
		Logger.log("Reading the event stream file ...");
		List<Tuple<IMethodName, List<Fact>>> stream = streamIo
				.readStreamObject(frequency);
		Logger.log("Reading the episode results file ...");
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);

		Logger.log("Getting results for partial-order ...");
		Map<Episode, Tuple<Integer, Integer>> partials = getOccs(
				EpisodeType.GENERAL, episodes, stream, freqThresh, entThresh);
		printOccs(partials);

		Logger.log("Getting results for sequential-order ...");
		Map<Episode, Tuple<Integer, Integer>> sequentials = getOccs(
				EpisodeType.SEQUENTIAL, episodes, stream, freqThresh, entThresh);
		printOccs(sequentials);

		Logger.log("Getting results for no-order ...");
		Map<Episode, Tuple<Integer, Integer>> unordered = getOccs(
				EpisodeType.PARALLEL, episodes, stream, freqThresh, entThresh);
		printOccs(unordered);
	}

	public void importance(int frequency, int freqThresh, double entThresh)
			throws Exception {
		Logger.log("Reading the event stream file ...");
		List<Tuple<IMethodName, List<Fact>>> stream = streamIo
				.readStreamObject(frequency);
		Logger.log("Reading the episode results file ...");
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);

		Logger.log("Getting results for partial-order ...");
		Map<Episode, Tuple<Integer, Integer>> partials = getOccs(
				EpisodeType.GENERAL, episodes, stream, freqThresh, entThresh);
		Logger.log("Getting results for no-order ...");
		Map<Episode, Tuple<Integer, Integer>> unordered = getOccs(
				EpisodeType.PARALLEL, episodes, stream, freqThresh, entThresh);
		Logger.log("\tOrder importance in partial-order patterns\n");
		orderInfo(partials, unordered);

		Logger.log("Getting results for sequential-order ...");
		Map<Episode, Tuple<Integer, Integer>> sequentials = getOccs(
				EpisodeType.SEQUENTIAL, episodes, stream, freqThresh, entThresh);
		Logger.log("\tOrder importance in sequential-order patterns\n");
		orderInfo(sequentials, unordered);
	}

	public void genDecl(int frequency, int freqThresh, double entThresh)
			throws Exception {
		Logger.log("Reading input files ...");
		List<Tuple<IMethodName, List<Fact>>> stream = streamIo
				.readStreamObject(frequency);
		List<Event> events = streamIo.readMapping(frequency);
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);

		Logger.log("\tGeneralizability for partial-order:");
		Map<Integer, Set<Episode>> partials = filter.filter(episodes,
				freqThresh, entThresh);
		methodDecl(partials, stream, events);

		Logger.log("\tGeneralizability for no-order:");
		Map<Integer, Set<Episode>> unordered = filter.filter(episodes,
				freqThresh, entThresh);
		methodDecl(unordered, stream, events);

		Logger.log("\tGeneralizability for sequential-order:");
		Map<Integer, Set<Episode>> sequentials = filter.filter(episodes,
				freqThresh, entThresh);
		methodDecl(sequentials, stream, events);
	}

	private void methodDecl(Map<Integer, Set<Episode>> patterns,
			List<Tuple<IMethodName, List<Fact>>> stream, List<Event> events) {

		int patternId = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			Logger.log("\tPatterns with %d-nodes:", entry.getKey());
			Logger.log("\tPatternId\tFacts\tFrequency\tEntropy\t#Decls");

			for (Episode pattern : entry.getValue()) {
				int numDecls = getDeclOccs(pattern, stream, events);
				Logger.log("\t%d\t%s\t%d\t%.2f\t%d", patternId, pattern
						.getFacts().toString(), pattern.getFrequency(), pattern
						.getEntropy(), numDecls);
				patternId++;
			}
			Logger.log("");
		}
	}

	private int getDeclOccs(Episode pattern,
			List<Tuple<IMethodName, List<Fact>>> stream, List<Event> events) {
		Map<IMethodName, List<Fact>> streamMap = getMapStream(stream);
		Set<IMethodName> decls = Sets.newHashSet();

		EnclosingMethods methodsOrderRelation = new EnclosingMethods(true);
		for (Tuple<IMethodName, List<Fact>> tuple : stream) {
			List<Fact> method = tuple.getSecond();
			if (method.size() < pattern.getNumEvents()) {
				continue;
			}
			if (method.containsAll(pattern.getEvents())) {
				Event ctx = Events.newElementContext(tuple.getFirst());
				methodsOrderRelation.addMethod(pattern, method, ctx);
			}
		}
		int numOccs = methodsOrderRelation.getOccurrences();
		assertTrue(numOccs >= pattern.getFrequency(),
				"Found insufficient number of occurences!");

		Set<IMethodName> methodOcc = methodsOrderRelation
				.getMethodNames(numOccs);
		for (IMethodName enclMethod : methodOcc) {
			IMethodName mn = getDeclName(streamMap.get(enclMethod), events);
			if (mn.isUnknown()) {
				decls.add(enclMethod);
			} else {
				decls.add(mn);
			}
		}
		return decls.size();
	}

	private IMethodName getDeclName(List<Fact> method, List<Event> events) {

		for (Fact fact : method) {
			Event event = events.get(fact.getFactID());
			if (event.getKind() == EventKind.FIRST_DECLARATION) {
				return event.getMethod();
			}
		}
		for (Fact fact : method) {
			Event event = events.get(fact.getFactID());
			if (event.getKind() == EventKind.SUPER_DECLARATION) {
				return event.getMethod();
			}
		}
		return Names.getUnknownMethod();
	}

	private Map<IMethodName, List<Fact>> getMapStream(
			List<Tuple<IMethodName, List<Fact>>> stream) {
		Map<IMethodName, List<Fact>> result = Maps.newLinkedHashMap();
		for (Tuple<IMethodName, List<Fact>> tuple : stream) {
			result.put(tuple.getFirst(), tuple.getSecond());
		}
		assertTrue(stream.size() == result.size(), "Stream conversion error");
		return result;
	}

	public void importancePartials(int frequency, int freqThresh,
			double entThresh) throws Exception {
		Logger.log("Reading the event stream file ...");
		List<Tuple<IMethodName, List<Fact>>> stream = streamIo
				.readStreamObject(frequency);
		Logger.log("Reading the episode results file ...");
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);

		Logger.log("Getting results for partial-order ...");
		Map<Episode, Tuple<Integer, Integer>> partials = getOccs(
				EpisodeType.GENERAL, episodes, stream, freqThresh, entThresh);
		Logger.log("Getting results for sequential-order ...");
		Map<Episode, Tuple<Integer, Integer>> sequentials = getOccs(
				EpisodeType.SEQUENTIAL, episodes, stream, freqThresh, entThresh);
		Logger.log("\tOrder importance in sequential-order patterns\n");

		Map<Set<Fact>, Set<Episode>> partialGroups = groupPartials(partials);
		int patternId = 0;

		Logger.log("\tOrder importance of sequential-order over partial-order");
		Logger.log("\tID\tFacts\tFrequency\tEntropy\tSequential\tPartial");
		for (Map.Entry<Episode, Tuple<Integer, Integer>> entry : sequentials
				.entrySet()) {
			Episode seqPattern = entry.getKey();
			for (Episode partPattern : partialGroups
					.get(seqPattern.getEvents())) {
				if (doesCover(partPattern, seqPattern)) {
					String facts = seqPattern.getFacts().toString();
					int seqOccs = sequentials.get(seqPattern).getSecond();
					int partOccs = partials.get(partPattern).getSecond();

					Logger.log("\t%d\t%s\t%d\t%.2f\t%d\t%d", patternId, facts,
							seqPattern.getFrequency(), seqPattern.getEntropy(),
							seqOccs, partOccs);
					patternId++;
					break;
				}
			}
		}
	}

	private boolean doesCover(Episode partial, Episode sequential) {
		Set<Fact> partEvents = partial.getEvents();
		Set<Fact> seqEvents = sequential.getEvents();

		if (!partEvents.containsAll(seqEvents)) {
			return false;
		}
		Set<Fact> seqRels = sequential.getRelations();
		Set<Fact> partRels = partial.getRelations();
		for (Fact relation : seqRels) {
			Tuple<Fact, Fact> relFacts = relation.getRelationFacts();
			String factString = relFacts.getSecond().getFactID() + ">"
					+ relFacts.getFirst().getFactID();
			Fact fact = new Fact(factString);
			if (partRels.contains(fact)) {
				return false;
			}
		}
		return true;
	}

	private Map<Set<Fact>, Set<Episode>> groupPartials(
			Map<Episode, Tuple<Integer, Integer>> partials) {
		Map<Set<Fact>, Set<Episode>> results = Maps.newLinkedHashMap();

		for (Map.Entry<Episode, Tuple<Integer, Integer>> entry : partials
				.entrySet()) {
			Episode pattern = entry.getKey();
			Set<Fact> events = pattern.getEvents();

			if (results.containsKey(events)) {
				results.get(events).add(pattern);
			} else {
				results.put(events, Sets.newHashSet(pattern));
			}
		}
		return results;
	}

	private void orderInfo(Map<Episode, Tuple<Integer, Integer>> ordered,
			Map<Episode, Tuple<Integer, Integer>> unordered) {
		int patternId = 0;

		Map<Set<Fact>, Integer> unordOccs = getUnorderOccs(unordered);

		Logger.log("\tID\tFacts\tFrequency\tEntropy\tOrder\tUnordered\n");
		for (Map.Entry<Episode, Tuple<Integer, Integer>> entry : ordered
				.entrySet()) {
			Episode op = entry.getKey();
			int oo = entry.getValue().getSecond();
			int uo = unordOccs.get(op.getEvents());
			Logger.log("\t%d\t%s\t%d\t%.2f\t%d\t%d", patternId, op.getFacts()
					.toString(), op.getFrequency(), op.getEntropy(), oo, uo);
			patternId++;
		}
		Logger.log("\n");
	}

	private Map<Set<Fact>, Integer> getUnorderOccs(
			Map<Episode, Tuple<Integer, Integer>> unordered) {
		Map<Set<Fact>, Integer> results = Maps.newLinkedHashMap();

		for (Map.Entry<Episode, Tuple<Integer, Integer>> entry : unordered
				.entrySet()) {
			Set<Fact> events = entry.getKey().getEvents();
			int occs = entry.getValue().getSecond();
			results.put(events, occs);
		}
		return results;
	}

	private void printOccs(Map<Episode, Tuple<Integer, Integer>> occurrences) {
		int patternId = 0;
		StringBuilder sb = new StringBuilder();
		sb.append("\tID\tFacts\tFrequency\tNumbDistinctMethods\tTotalOccurrence\n");

		for (Map.Entry<Episode, Tuple<Integer, Integer>> entry : occurrences
				.entrySet()) {
			Episode pattern = entry.getKey();
			Tuple<Integer, Integer> occs = entry.getValue();

			sb.append("\t" + patternId);
			sb.append("\t" + pattern.getFacts().toString());
			sb.append("\t" + pattern.getFrequency());
			sb.append("\t" + occs.getFirst());
			sb.append("\t" + occs.getSecond());
			sb.append("\n");
			patternId++;
		}
		Logger.log("%s", sb.toString());
		Logger.log("\n\n");
	}

	private Map<Episode, Tuple<Integer, Integer>> getOccs(EpisodeType type,
			Map<Integer, Set<Episode>> episodes,
			List<Tuple<IMethodName, List<Fact>>> stream, int freqThresh,
			double entThresh) throws Exception {
		Map<Episode, Tuple<Integer, Integer>> results = Maps.newLinkedHashMap();

		Map<Integer, Set<Episode>> patterns = filter.filter(episodes,
				freqThresh, entThresh);

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			for (Episode p : entry.getValue()) {
				EnclosingMethods ctxOccs = new EnclosingMethods(true);
				if (type == EpisodeType.PARALLEL) {
					ctxOccs = new EnclosingMethods(false);
				}

				for (Tuple<IMethodName, List<Fact>> tuple : stream) {
					List<Fact> method = tuple.getSecond();
					if (method.size() < 2) {
						continue;
					}
					if (method.containsAll(p.getEvents())) {
						Event ctx = Events.newElementContext(tuple.getFirst());
						ctxOccs.addMethod(p, method, ctx);
					}
				}
				int numOccs = ctxOccs.getOccurrences();
				int frequency = p.getFrequency();
				assertTrue(numOccs >= frequency,
						"Found insufficient number of occurences!");
				Set<IMethodName> methodOccs = ctxOccs.getMethodNames(numOccs);

				results.put(p, Tuple.newTuple(methodOccs.size(), numOccs));
			}
		}
		return results;
	}
}
