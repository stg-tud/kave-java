package cc.kave.episodes.postprocessor;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.RepositoriesParser;
import cc.kave.episodes.io.ValidationDataIO;
import cc.kave.episodes.mining.evaluation.PatternsValidation;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.Triplet;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class SpecificPatterns {

	private EventStreamIo eventStream;
	private EpisodesParser episodeParser;
	private EpisodesFilter episodeFilter;

	private RepositoriesParser repoParser;
	private ValidationDataIO validationIo;

	private PatternsValidation pattValidation;
	private EpisodeToGraphConverter graphConverter;

	@Inject
	public SpecificPatterns(EventStreamIo stream, EpisodesParser epParser,
			EpisodesFilter filter, RepositoriesParser reposParser,
			ValidationDataIO validationIo, PatternsValidation patValidate,
			EpisodeToGraphConverter graphs) {
		this.eventStream = stream;
		this.episodeParser = epParser;
		this.episodeFilter = filter;
		this.repoParser = reposParser;
		this.validationIo = validationIo;
		this.pattValidation = patValidate;
		this.graphConverter = graphs;
	}

	public void patternsInfo(EpisodeType type, int freqEpisode, int foldNum,
			int freqThresh, double entropy) throws Exception {
		Logger.log("Reading repositories - enclosing method declarations mapper!");
		repoParser.generateReposEvents();
		Map<String, Set<ITypeName>> repoCtxMapper = repoParser
				.getRepoTypesMapper();
		Logger.log("Reading events ...");
		List<Event> trainEvents = eventStream.readMapping(freqEpisode, foldNum);
		Logger.log("Reading training stream ...");
		List<Tuple<Event, List<Fact>>> streamContexts = eventStream
				.parseStream(freqEpisode, foldNum);
		Logger.log("Reading validation data ...");
		List<Event> valData = validationIo.read(freqEpisode, foldNum);
		Map<Event, Integer> eventsMap = mergeEventsToMap(trainEvents, valData);
		List<Event> eventsList = Lists.newArrayList(eventsMap.keySet());
		List<List<Fact>> valStream = validationIo.streamOfFacts(valData,
				eventsMap);

		Map<Integer, Set<Episode>> episodes = episodeParser.parse(type,
				freqEpisode, foldNum);
		Map<Integer, Set<Episode>> patterns = episodeFilter.filter(type,
				episodes, freqThresh, entropy);
		Map<Integer, Set<Triplet<Episode, Integer, Integer>>> validations = pattValidation
				.validate(patterns, streamContexts, repoCtxMapper, eventsList,
						valStream);

		Set<Fact> specPatterns = Sets.newLinkedHashSet();
		Set<Fact> genPatterns = Sets.newLinkedHashSet();

		for (Map.Entry<Integer, Set<Triplet<Episode, Integer, Integer>>> entry : validations
				.entrySet()) {
			Logger.log("\tEpisode size = %d", entry.getKey());
			Set<Triplet<Episode, Integer, Integer>> episodeSet = entry
					.getValue();

			for (Triplet<Episode, Integer, Integer> triplet : episodeSet) {
				Episode ep = triplet.getFirst();
				Set<Fact> epEvents = ep.getEvents();

				if ((triplet.getThird() == 0) && (triplet.getSecond() < 2)) {
					if (!specPatterns.containsAll(epEvents)) {
						Logger.log("\tEpisode: %s", ep.getFacts().toString());

						for (Fact fact : epEvents) {
							int id = fact.getFactID();
							Event e = eventsList.get(id);
							String label = id + ". ";
							label += graphConverter.toLabel(e.getMethod());
							Logger.log("\t%s", label);

							specPatterns.add(fact);
						}
						Logger.log("");
						occInfo(ep, streamContexts, repoCtxMapper);
					}
				} else {
					genPatterns.addAll(epEvents);
				}
			}
			Logger.log("");
			Logger.log("");
		}
		Set<Fact> specEvents = getSpecEvents(specPatterns, genPatterns);
		Logger.log("Printing patterns events ...");
		printEventsInfo(specPatterns, eventsList);
		Logger.log("Printing specific events ...");
		printEventsInfo(specEvents, eventsList);
	}

	public void getFrameworks(EpisodeType type, int frequency, int foldNum) {
		Map<Integer, Set<Episode>> episodes = episodeParser.parse(type,
				frequency, foldNum);
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
					graphConverter.toLabel(methodName));
		}
	}

	private void occInfo(Episode episode,
			List<Tuple<Event, List<Fact>>> streamContexts,
			Map<String, Set<ITypeName>> repoCtxMapper) {

		EnclosingMethods methodsOrderRelation = new EnclosingMethods(true);

		for (Tuple<Event, List<Fact>> tuple : streamContexts) {
			List<Fact> method = tuple.getSecond();
			if (method.size() < 2) {
				continue;
			}
			if (method.containsAll(episode.getEvents())) {
				methodsOrderRelation.addMethod(episode, method,
						tuple.getFirst());
			}
		}
		int trainOcc = methodsOrderRelation.getOccurrences();
		assertTrue(trainOcc >= episode.getFrequency(),
				"Episode is not found sufficient number of times in the Training Data!");

		Logger.log("Enclosing methods:");
		Set<ITypeName> methods = methodsOrderRelation.getTypeNames(trainOcc);
		Set<IMethodName> mns = methodsOrderRelation.getMethodNames(10);
		for (IMethodName methodName : mns) {
			String name = graphConverter.toLabel(methodName);
			Logger.log("%s", name);
		}
		Logger.log("");
		
		Set<String> repositories = Sets.newLinkedHashSet();
		for (Map.Entry<String, Set<ITypeName>> entry : repoCtxMapper.entrySet()) {
			for (ITypeName methodName : entry.getValue()) {
				if (methods.contains(methodName)) {
					repositories.add(entry.getKey());
					break;
				}
			}
		}
		Logger.log("Repository:");
		for (String repo : repositories) {
			Logger.log("%s", repo);
		}
	}

	private Map<Event, Integer> mergeEventsToMap(List<Event> lista,
			List<Event> listb) {
		Map<Event, Integer> events = Maps.newLinkedHashMap();
		int id = 0;

		for (Event event : lista) {
			events.put(event, id);
			id++;
		}
		for (Event event : listb) {
			if (!events.containsKey(event)) {
				events.put(event, id);
				id++;
			}
		}
		return events;
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
			label += graphConverter.toLabel(event.getMethod());
			Logger.log("%s", label);
		}
		Logger.log("");
	}
}
