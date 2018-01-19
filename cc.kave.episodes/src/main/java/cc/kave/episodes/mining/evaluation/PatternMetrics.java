package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.patterns.PatternFilter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EnclosingMethods;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

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

		Map<Integer, Set<Episode>> patterns = filter.filter(type, episodes,
				freqThresh, entThresh);

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			for (Episode p : entry.getValue()) {
				EnclosingMethods ctxOccs = new EnclosingMethods(true);

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
