package cc.kave.episodes.postprocessor;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.evaluation.PatternsValidation;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Sets;

public class SpecificPatterns {

	private PatternsValidation validation;
	private EventStreamIo eventStream;
	private EpisodeToGraphConverter graph;

	private EpisodesParser parser;

	@Inject
	public SpecificPatterns(PatternsValidation validate, EventStreamIo stream,
			EpisodeToGraphConverter graphConv, EpisodesParser parser) {
		this.validation = validate;
		this.eventStream = stream;
		this.graph = graphConv;
		this.parser = parser;
	}

	public void patternsInfo(EpisodeType type, int frequency, double entropy,
			int foldNum) throws Exception {
//		Map<Integer, Set<Tuple<Episode, Boolean>>> patterns = validation
//				.validate(type, frequency, entropy, foldNum);
//		List<Event> events = eventStream.readMapping(frequency, foldNum);
//
//		Set<Fact> specPatterns = Sets.newLinkedHashSet();
//		Set<Fact> genPatterns = Sets.newLinkedHashSet();
//
//		for (Map.Entry<Integer, Set<Tuple<Episode, Boolean>>> entry : patterns
//				.entrySet()) {
//			Logger.log("\tEpisode size = %d", entry.getKey());
//			Set<Tuple<Episode, Boolean>> episodes = entry.getValue();
//
//			for (Tuple<Episode, Boolean> tuple : episodes) {
//				Episode ep = tuple.getFirst();
//				Set<Fact> epEvents = ep.getEvents();
//
//				if (!tuple.getSecond()) {
//					if (!specPatterns.containsAll(epEvents)) {
//						Logger.log("\tEpisode: %s", ep.getFacts().toString());
//
//						for (Fact fact : epEvents) {
//							int id = fact.getFactID();
//							Event e = events.get(id);
//							String label = id + ". ";
//							label += graph.toLabel(e.getMethod());
//							Logger.log("\t%s", label);
//
//							specPatterns.add(fact);
//						}
//					}
//				} else {
//					for (Fact fact : epEvents) {
//						genPatterns.add(fact);
//					}
//				}
//			}
//			Logger.log("");
//		}
//		Set<Fact> specEvents = getSpecEvents(specPatterns, genPatterns);
//		Logger.log("Printing patterns events ...");
//		printEventsInfo(specPatterns, events);
//		Logger.log("Printing specific events ...");
//		printEventsInfo(specEvents, events);
	}

	public void patternEvents(EpisodeType type, int frequency, int foldNum) {
		Map<Integer, Set<Episode>> episodes = parser.parse(type, frequency,
				foldNum);
		List<Event> events = eventStream.readMapping(frequency, foldNum);
		Set<Episode> twoNodeEpisodes = episodes.get(2);
		Set<Fact> patternEvents = Sets.newLinkedHashSet();

		for (Episode twoNode : twoNodeEpisodes) {
			Set<Fact> facts = twoNode.getEvents();

			for (Fact f : facts) {
				patternEvents.add(f);
			}
		}
		for (Fact fact : patternEvents) {
			int id = fact.getFactID();
			IMethodName methodName = events.get(id).getMethod();
			IAssemblyName asm = methodName.getDeclaringType().getAssembly();
			Logger.log("\t%d.\t%s\t%s", id, asm.getName() + "."
					+ asm.getVersion().getIdentifier(),
					graph.toLabel(methodName));
		}
	}

	private Set<Fact> getSpecEvents(Set<Fact> specPatterns,
			Set<Fact> genPatterns) {
		Set<Fact> result = Sets.newLinkedHashSet();

		for (Fact fact : specPatterns) {
			if (!genPatterns.contains(fact)) {
				result.add(fact);
			}
		}
		return result;
	}

	private void printEventsInfo(Set<Fact> pattEvents, List<Event> events) {
		for (Fact fact : pattEvents) {
			int id = fact.getFactID();
			Event event = events.get(id);
			String label = id + ". ";
			label += graph.toLabel(event.getMethod());
			Logger.log("%s", label);
		}
		Logger.log("");
	}
}
