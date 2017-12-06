package cc.kave.episodes.statistics;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.FileReader;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.mining.patterns.PatternFilter;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EnclosingMethods;
import cc.kave.episodes.postprocessor.TransClosedEpisodes;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PatternsStatistics {

	private File patternsFile;

	private EventStreamIo streamIo;

	private FileReader reader;
	private EpisodeParser parser;
	private PatternFilter filter;

	private TransClosedEpisodes transClosure;
	private EpisodeToGraphConverter episodeGraphConverter;

	private EpisodeAsGraphWriter graphWriter;

	@Inject
	public PatternsStatistics(@Named("patterns") File folder,
			EventStreamIo eventsStreamIo, FileReader fileReader,
			EpisodeParser episodeReader, PatternFilter patternFilter,
			TransClosedEpisodes transClosure,
			EpisodeToGraphConverter graphConverter,
			EpisodeAsGraphWriter graphWriter) {
		assertTrue(folder.exists(), "Patterns folder does not exist");
		assertTrue(folder.isDirectory(), "Patterns is not a folder, but a file");
		this.patternsFile = folder;
		this.streamIo = eventsStreamIo;
		this.reader = fileReader;
		this.parser = episodeReader;
		this.filter = patternFilter;
		this.transClosure = transClosure;
		this.episodeGraphConverter = graphConverter;
		this.graphWriter = graphWriter;
	}

	public void numbAPIs(int frequency, int threshFreq, double threshEntr)
			throws Exception {
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);
		Map<Integer, Set<Episode>> patterns = filter.filter(
				EpisodeType.SEQUENTIAL, episodes, threshFreq, threshEntr);
		List<Event> events = streamIo.readMapping(frequency);

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			System.out.println(entry.getKey() + "-event patterns");
//			Logger.log("\t%d-event patterns", entry.getKey());
			Logger.log("\tNumber of API\tNumber of patterns");
			Map<Integer, Integer> numbAPIPatterns = Maps.newLinkedHashMap();

			for (Episode p : entry.getValue()) {
				Set<ITypeName> types = Sets.newHashSet();
				for (Fact fact : p.getEvents()) {
					int id = fact.getFactID();
					types.add(events.get(id).getMethod().getDeclaringType());
				}
				int numbTypes = types.size();
				if (numbAPIPatterns.containsKey(numbTypes)) {
					int counter = numbAPIPatterns.get(numbTypes);
					numbAPIPatterns.put(numbTypes, counter + 1);
				} else {
					numbAPIPatterns.put(numbTypes, 1);
				}
			}
			for (Map.Entry<Integer, Integer> count : numbAPIPatterns.entrySet()) {
				Logger.log("\t%d\t%d", count.getKey(), count.getValue());
			}
			Logger.log("");
		}
	}

	public void generalizability(int threshFreq, double threshEntr) {
		Map<Episode, Integer> generals = getEvaluations(EpisodeType.GENERAL,
				threshFreq, threshEntr);
		Map<Episode, Integer> sequentials = getEvaluations(
				EpisodeType.SEQUENTIAL, threshFreq, threshEntr);
		int numGens = 0;
		int numSpecs = 0;

		for (Map.Entry<Episode, Integer> entryPart : generals.entrySet()) {
			Episode gen = entryPart.getKey();
			boolean repr = false;
			for (Map.Entry<Episode, Integer> entSeq : sequentials.entrySet()) {
				if (gen.getEvents().equals(entSeq.getKey().getEvents())) {
					repr = true;
					break;
				}
			}
			if (!repr) {
				if (entryPart.getValue() > 1) {
					numGens++;
				} else {
					numSpecs++;
				}
			}
		}
		Logger.log("Number of unique partial-patterns that are general: %d",
				numGens);
		Logger.log("Number of unique partial patterns that are specific: %d",
				numSpecs);
	}

	public void patternSizes(int frequency, int threshFreq, double threshEntr)
			throws Exception {
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);
		Map<Integer, Set<Episode>> partials = filter.filter(
				EpisodeType.GENERAL, episodes, threshFreq, threshEntr);

		int numPatterns = 0;
		Logger.log("Analyzing partial-order patterns ...");
		Logger.log("Size-level\t#Patterns");
		for (Map.Entry<Integer, Set<Episode>> entry : partials.entrySet()) {
			Logger.log("%d\t%d", entry.getKey(), entry.getValue().size());
			numPatterns += entry.getValue().size();
		}
		Logger.log("Number of partial-order patterns: %d", numPatterns);
		Logger.log("");

		numPatterns = 0;
		Map<Integer, Set<Episode>> sequentials = filter.filter(
				EpisodeType.SEQUENTIAL, episodes, threshFreq, threshEntr);
		Logger.log("Analyzing sequential-order patterns ...");
		Logger.log("Size-level\t#Patterns");
		for (Map.Entry<Integer, Set<Episode>> entry : sequentials.entrySet()) {
			Logger.log("%d\t%d", entry.getKey(), entry.getValue().size());
			numPatterns += entry.getValue().size();
		}
		Logger.log("Number of sequential-order patterns: %d", numPatterns);
		Logger.log("");

		for (int idx = 5; idx <= 7; idx++) {
			Set<Episode> partLevel = partials.get(idx);
			Map<Set<Fact>, Set<Episode>> partEvents = getPatternEvents(partLevel);
			Logger.log("Number of uniques partials for %d level is %d", idx,
					partEvents.size());
			int partOrder = 0;
			int strictOrder = 0;
			for (Episode p : partLevel) {
				if (isPartial(p)) {
					partOrder++;
				} else {
					strictOrder++;
				}
			}
			Logger.log("Patterns with partial-order = %d", partOrder);
			Logger.log("Patterns with strict-order = %d", strictOrder);

			Set<Episode> seqLevel = sequentials.get(idx);
			Map<Set<Fact>, Set<Episode>> seqEvents = getPatternEvents(seqLevel);
			Logger.log("Number of sequentials for %d-level is %d", idx,
					seqEvents.size());

			int isContained = 0;
			for (Map.Entry<Set<Fact>, Set<Episode>> entry : partEvents
					.entrySet()) {
				if (seqEvents.containsKey(entry.getKey())) {
					isContained++;
				} else {
					Logger.log("Partials: %s", entry.getKey().toString());
				}
			}
			Logger.log("Number of equal event groups: %s", isContained);
			Logger.log("");
		}
	}

	public void numPatterns(int frequency, int threshFreq, double threshEnt)
			throws Exception {
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);

		Logger.log("\tPartial-order configuration:");
		Map<Integer, Set<Episode>> partPatterns = filter.filter(
				EpisodeType.GENERAL, episodes, threshFreq, threshEnt);
		outputStats(partPatterns);

		Logger.log("\tSequential-order configuration:");
		Map<Integer, Set<Episode>> seqPatterns = filter.filter(
				EpisodeType.SEQUENTIAL, episodes, threshFreq, threshEnt);
		outputStats(seqPatterns);

		Logger.log("\tNo-order configuration:");
		Map<Integer, Set<Episode>> paraPatterns = filter.filter(
				EpisodeType.PARALLEL, episodes, threshFreq, threshEnt);
		outputStats(paraPatterns);
	}

	public void partialSequentials(int frequency, int threshFreq,
			double threshEntr) throws Exception {
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);

		Map<Integer, Set<Episode>> sequentials = filter.filter(
				EpisodeType.SEQUENTIAL, episodes, threshFreq, threshEntr);
		int counterSeq = patternsCounter(sequentials);
		Map<Set<Fact>, Set<Episode>> groups = Maps.newLinkedHashMap();

		for (Map.Entry<Integer, Set<Episode>> entry : sequentials.entrySet()) {
			for (Episode pattern : entry.getValue()) {
				Set<Fact> events = pattern.getEvents();

				if (groups.containsKey(events)) {
					groups.get(events).add(pattern);
				} else {
					groups.put(events, Sets.newHashSet(pattern));
				}
			}
		}
		Map<Integer, Set<Episode>> partials = filter.filter(
				EpisodeType.GENERAL, episodes, threshFreq, threshEntr);
		int counterPart = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : partials.entrySet()) {
			for (Episode pattern : entry.getValue()) {
				Set<Fact> events = pattern.getEvents();

				if (groups.containsKey(events)) {
					if (isPartial(pattern)) {
						Logger.log("%s", pattern.getFacts().toString());
					} else {
						counterPart++;
					}
				}
			}
		}
		Logger.log("Sequential-order patterns: %d", counterSeq);
		Logger.log("Partial-order patterns with strict-order: %d", counterPart);
	}

	public void overlappingPatterns(int frequency, int threshFreq,
			double threshEnt) throws Exception {
		int equals = 0;
		int notEquals = 0;
		int covered = 0;
		int notCovered = 0;

		Set<Episode> covSeqs = Sets.newLinkedHashSet();
		Map<Integer, Set<Episode>> partials = getPatterns(EpisodeType.PARALLEL,
				frequency, threshFreq, threshEnt);
		Map<Integer, Set<Episode>> sequentials = getPatterns(
				EpisodeType.GENERAL, frequency, threshFreq, threshEnt);

		for (Map.Entry<Integer, Set<Episode>> entSeq : sequentials.entrySet()) {
			Map<Integer, Set<Episode>> tempPartials = Maps.newLinkedHashMap();
			for (Episode seqPat : entSeq.getValue()) {
				Set<Episode> partialPatt = partials.get(entSeq.getKey());
				boolean areEqual = false;
				for (Episode partPat : partialPatt) {
					areEqual = equalEpisodes(seqPat, partPat);
					if (areEqual) {
						equals++;
						break;
					}
				}
				if (!areEqual) {
					notEquals++;
					boolean isCovered = false;
					for (Episode partPat : partialPatt) {
						isCovered = doesCover(partPat, seqPat);
						if (isCovered) {
							int numRels = partPat.getRelations().size();
							if (tempPartials.containsKey(numRels)) {
								tempPartials.get(numRels).add(partPat);
							} else {
								tempPartials.put(numRels,
										Sets.newHashSet(partPat));
							}
							covered++;
							break;
						}
					}
					if (!isCovered) {
						notCovered++;
						Logger.log("Sequential: %s", seqPat.getFacts()
								.toString());
					}
				}
			}
			for (Map.Entry<Integer, Set<Episode>> tmpEntry : tempPartials
					.entrySet()) {
				covSeqs.addAll(tmpEntry.getValue());
			}
		}
		Logger.log("");
		Logger.log("Number of equal patterns: %d", equals);
		Logger.log("Number of not equal patterns: %d", notEquals);
		Logger.log("Partials covers %d from sequentials", covered);
		Logger.log("Partials do not cover %d from sequentials", notCovered);
		Logger.log(
				"Number of partial patterns for covering sequential patterns: %d",
				covSeqs.size());

		Set<Episode> adds = Sets.newLinkedHashSet();
		for (Map.Entry<Integer, Set<Episode>> entry : partials.entrySet()) {
			Map<Integer, Set<Episode>> tmpAdds = Maps.newLinkedHashMap();
			for (Episode pattern : entry.getValue()) {
				if (!covSeqs.contains(pattern) && isPartial(pattern)) {
					int numRels = pattern.getRelations().size();
					if (tmpAdds.containsKey(numRels)) {
						tmpAdds.get(numRels).add(pattern);
					} else {
						tmpAdds.put(numRels, Sets.newHashSet(pattern));
					}
				}
			}
			for (Map.Entry<Integer, Set<Episode>> entAdds : tmpAdds.entrySet()) {
				adds.addAll(entAdds.getValue());
			}
		}
		writePatterns(covSeqs, "coverSeqs", frequency, threshFreq, threshEnt);
		writePatterns(adds, "additionals", frequency, threshFreq, threshEnt);
	}

	public void patternsOrigin(int frequency, int threshFreq, double threshEnt) {
		Map<String, Set<IMethodName>> repos = streamIo.readRepoCtxs(frequency);
		List<Tuple<IMethodName, List<Fact>>> stream = streamIo
				.readStreamObject(frequency);
		Map<Episode, Integer> patterns = getEvaluations(EpisodeType.GENERAL,
				threshFreq, threshEnt);
		Map<Episode, Set<IMethodName>> specRepoPatterns = Maps
				.newLinkedHashMap();
		Map<Episode, Set<IMethodName>> specPatterns = Maps.newLinkedHashMap();
		Map<Episode, Set<IMethodName>> genPatterns = Maps.newLinkedHashMap();
		int patternId = 0;

		for (Map.Entry<Episode, Integer> entry : patterns.entrySet()) {
			if ((patternId % 100) == 0) {
				Logger.log("Processing patterns = %d", patternId);
			}
			Episode pattern = entry.getKey();

			EnclosingMethods ctxs = new EnclosingMethods(true);
			for (Tuple<IMethodName, List<Fact>> tuple : stream) {
				List<Fact> method = tuple.getSecond();
				if (method.size() < 2) {
					continue;
				}
				if (method.containsAll(pattern.getEvents())) {
					Event ctx = Events.newElementContext(tuple.getFirst());
					ctxs.addMethod(pattern, method, ctx);
				}
			}
			int numOccs = ctxs.getOccurrences();
			assertTrue(numOccs >= pattern.getFrequency(),
					"Found insufficient number of occurences!");

			Set<IMethodName> methodOccs = ctxs.getMethodNames(numOccs);

			boolean specRepo = false;
			for (IMethodName methodName : methodOccs) {
				if (repos.get("Contexts-170503/msgpack/msgpack-cli").contains(
						methodName)
						&& (entry.getValue() == 1)) {
					specRepo = true;
				}
			}
			if (specRepo) {
				specRepoPatterns.put(pattern, methodOccs);
			} else {
				if (entry.getValue() == 1) {
					specPatterns.put(pattern, methodOccs);
				} else {
					genPatterns.put(pattern, methodOccs);
				}
			}
			patternId++;
		}
		Logger.log("Patterns specific from msgpack/msgpack-cli repository");
		printCtxsInfo(specRepoPatterns);
		Logger.log("Other repository-specific patterns");
		printCtxsInfo(specPatterns);
		Logger.log("General patterns");
		printCtxsInfo(genPatterns);
	}

	public void specPatternsRepos(int frequency, int threshFreq,
			double threshEnt) {
		Map<String, Set<IMethodName>> repos = streamIo.readRepoCtxs(frequency);
		List<Tuple<IMethodName, List<Fact>>> stream = streamIo
				.readStreamObject(frequency);
		List<Event> events = streamIo.readMapping(frequency);
		Map<Episode, Integer> patterns = getEvaluations(EpisodeType.GENERAL,
				threshFreq, threshEnt);

		Map<String, Set<Episode>> specPatterns = Maps.newLinkedHashMap();
		specPatterns.put("Contexts-170503/msgpack/msgpack-cli",
				Sets.newHashSet());
		specPatterns.put("Contexts-170503/ServiceStack/ServiceStack",
				Sets.newHashSet());
		specPatterns.put("Contexts-170503/Glimpse/Glimpse", Sets.newHashSet());
		specPatterns.put("Contexts-170503/mono/mono", Sets.newHashSet());
		specPatterns.put("Contexts-170503/aumcode/nfx", Sets.newHashSet());
		specPatterns.put("Contexts-170503/devbridge/BetterCMS",
				Sets.newHashSet());
		specPatterns.put("Contexts-170503/octokit/octokit.net",
				Sets.newHashSet());
		specPatterns.put("Contexts-170503/nhibernate/nhibernate-core",
				Sets.newHashSet());
		specPatterns
				.put("Contexts-170503/OsmSharp/OsmSharp", Sets.newHashSet());
		specPatterns.put("Contexts-170503/ravendb/ravendb", Sets.newHashSet());
		specPatterns.put("Contexts-170503/aws/aws-sdk-net", Sets.newHashSet());

		int patternId = 0;
		for (Map.Entry<Episode, Integer> entry : patterns.entrySet()) {
			if ((patternId % 100) == 0) {
				Logger.log("Processing pattern = %d", patternId);
			}
			if (entry.getValue() > 1) {
				patternId++;
				continue;
			}
			Episode pattern = entry.getKey();

			EnclosingMethods ctxs = new EnclosingMethods(true);
			for (Tuple<IMethodName, List<Fact>> tuple : stream) {
				List<Fact> method = tuple.getSecond();
				if (method.size() < 2) {
					continue;
				}
				if (method.containsAll(pattern.getEvents())) {
					Event ctx = Events.newElementContext(tuple.getFirst());
					ctxs.addMethod(pattern, method, ctx);
				}
			}
			int numOccs = ctxs.getOccurrences();
			assertTrue(numOccs >= pattern.getFrequency(),
					"Found insufficient number of occurences!");

			Set<IMethodName> methodOccs = ctxs.getMethodNames(numOccs);
			for (IMethodName methodName : methodOccs) {
				for (Map.Entry<String, Set<Episode>> entryRepo : specPatterns
						.entrySet()) {
					String repoName = entryRepo.getKey();
					if (repos.get(repoName).contains(methodName)) {
						specPatterns.get(repoName).add(pattern);
						break;
					}
				}
				break;
			}
			patternId++;
		}
		patternId = 0;
		for (Map.Entry<String, Set<Episode>> entry : specPatterns.entrySet()) {
			Logger.log("\tPatterns from repository: %s", entry.getKey());
			for (Episode pattern : entry.getValue()) {
				String patternName = patternId + ". ";
				Set<Fact> facts = pattern.getEvents();
				for (Fact fact : facts) {
					Event event = events.get(fact.getFactID());
					IMethodName methodName = event.getMethod();
					patternName += episodeGraphConverter.toLabel(methodName)
							+ "\n";
				}
				Logger.log("\t%s", patternName);
				patternId++;
			}
			Logger.log("");
		}
	}

	public void repoClases(int frequency) {
		Map<String, Set<IMethodName>> repos = streamIo.readRepoCtxs(frequency);

		Logger.log("\tRepository\tTestClasses\tDevClasses");
		for (Map.Entry<String, Set<IMethodName>> entry : repos.entrySet()) {
			int numTests = 0;
			int numDevs = 0;
			for (IMethodName methodName : entry.getValue()) {
				if (methodName.getDeclaringType().getIdentifier()
						.contains("Test")) {
					numTests++;
				} else {
					numDevs++;
				}
			}
			Logger.log("\t%s\t%d\t%d", entry.getKey(), numTests, numDevs);
		}
	}

	private void printCtxsInfo(Map<Episode, Set<IMethodName>> patterns) {
		Logger.log("\tPattern\tTests\tDevs");
		int patternId = 0;
		for (Map.Entry<Episode, Set<IMethodName>> entry : patterns.entrySet()) {
			int numTests = 0;
			int numDevs = 0;
			for (IMethodName methodName : entry.getValue()) {
				if (methodName.getDeclaringType().getFullName()
						.contains("Test")) {
					numTests++;
				} else {
					numDevs++;
				}
			}
			Logger.log("\t%d\t%d\t%d", patternId, numTests, numDevs);
			patternId++;
		}
		Logger.log("");
	}

	public void specRepoPatterns(int frequency, int threshFreq, double threshEnt) {
		Map<String, Set<IMethodName>> repos = streamIo.readRepoCtxs(frequency);
		List<Tuple<IMethodName, List<Fact>>> stream = streamIo
				.readStreamObject(frequency);
		List<Event> events = streamIo.readMapping(frequency);
		Map<Episode, Integer> patterns = getEvaluations(EpisodeType.GENERAL,
				threshFreq, threshEnt);
		Set<Event> specRepoEvents = Sets.newHashSet();
		Set<Event> otherReposEvents = Sets.newHashSet();
		String specEvent = "System.Tuple.Create(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5, T6 item6, T7 item7) : Tuple";
		int patternId = 0;

		for (Map.Entry<Episode, Integer> entry : patterns.entrySet()) {
			Episode pattern = entry.getKey();
			if (entry.getValue() > 1) {
				patternId++;
				otherReposEvents.addAll(getEvents(pattern, events));
				continue;
			}
			EnclosingMethods ctxs = new EnclosingMethods(true);

			for (Tuple<IMethodName, List<Fact>> tuple : stream) {
				List<Fact> method = tuple.getSecond();
				if (method.size() < 2) {
					continue;
				}
				if (method.containsAll(pattern.getEvents())) {
					Event ctx = Events.newElementContext(tuple.getFirst());
					ctxs.addMethod(pattern, method, ctx);
				}
			}
			int numOccs = ctxs.getOccurrences();
			assertTrue(numOccs >= pattern.getFrequency(),
					"Found insufficient number of occurences!");

			Set<IMethodName> methodOccs = ctxs.getMethodNames(numOccs);

			boolean isContained = false;
			for (IMethodName methodName : methodOccs) {
				if (repos.get("Contexts-170503/msgpack/msgpack-cli").contains(
						methodName)) {
					isContained = true;
					specRepoEvents.addAll(getEvents(pattern, events));
				}
			}
			if (!isContained) {
				otherReposEvents.addAll(getEvents(pattern, events));
			} else {
				Logger.log("Pattern = %d", patternId);
				Logger.log("");
				for (Fact fact : pattern.getEvents()) {
					Event event = events.get(fact.getFactID());
					String name = episodeGraphConverter.toLabel(event
							.getMethod());
					if (name.equalsIgnoreCase(specEvent)) {
						break;
					}

				}
			}
			patternId++;
		}
		// Logger.log("Events only from (msgpack/msgpack-cli) specific-patterns");
		// for (Event event : specRepoEvents) {
		// if (!otherReposEvents.contains(event))
		// Logger.log("%s",
		// episodeGraphConverter.toLabel(event.getMethod()));
		// }
	}

	public void addsPartials(int frequency, double entropy) {
		Map<Episode, Integer> partials = getEvaluations(EpisodeType.GENERAL,
				frequency, entropy);
		Map<Episode, Integer> sequentials = getEvaluations(
				EpisodeType.SEQUENTIAL, frequency, entropy);

		Map<Set<Fact>, Set<Episode>> partGroups = groupEvents(partials);
		Map<Set<Fact>, Set<Episode>> seqGroups = groupEvents(sequentials);

		int numGens = 0;
		int numSpecs = 0;

		for (Map.Entry<Set<Fact>, Set<Episode>> entry : partGroups.entrySet()) {
			if (!seqGroups.containsKey(entry.getKey())) {
				for (Episode pattern : entry.getValue()) {
					if (partials.get(pattern) > 1) {
						numGens++;
					} else {
						numSpecs++;
					}
				}
			}
		}
		Logger.log("Number of additional general partial patterns: %d", numGens);
		Logger.log("Number of additional specific partial patterns: %d",
				numSpecs);
	}

	private Map<Set<Fact>, Set<Episode>> groupEvents(
			Map<Episode, Integer> patterns) {
		Map<Set<Fact>, Set<Episode>> result = Maps.newLinkedHashMap();
		for (Map.Entry<Episode, Integer> entry : patterns.entrySet()) {
			Episode p = entry.getKey();
			Set<Fact> events = p.getEvents();
			if (result.containsKey(events)) {
				result.get(events).add(p);
			} else {
				result.put(events, Sets.newHashSet(p));
			}
		}
		return result;
	}

	private Set<Event> getEvents(Episode pattern, List<Event> events) {
		Set<Event> patternEvents = Sets.newHashSet();
		for (Fact fact : pattern.getEvents()) {
			patternEvents.add(events.get(fact.getFactID()));
		}
		return patternEvents;
	}

	private Map<Episode, Integer> getEvaluations(EpisodeType type,
			int frequency, double entropy) {
		File evaluationFile = getEvalFile(type, frequency, entropy);
		List<String> evalText = reader.readFile(evaluationFile);

		Map<Episode, Integer> evaluations = Maps.newLinkedHashMap();

		for (String line : evalText) {
			if (line.isEmpty() || line.contains("PatternId")
					|| line.contains("Patterns")) {
				continue;
			}
			String[] elems = line.split("\t");
			String facts = elems[1];
			int freq = Integer.parseInt(elems[2]);
			double ent = Double.parseDouble(elems[3]);
			int gens = Integer.parseInt(elems[4]);

			Episode pattern = createPattern(facts, freq, ent);
			evaluations.put(pattern, gens);
		}
		return evaluations;
	}

	private Episode createPattern(String facts, int freq, double ent) {
		Episode pattern = new Episode();

		String[] indFacts = facts.split(",");
		for (String fact : indFacts) {
			if (fact.contains("]")) {
				String factId = fact.substring(1, fact.length() - 1);
				pattern.addFact(new Fact(factId));
				continue;
			}
			String factId = fact.substring(1);
			pattern.addFact(new Fact(factId));
		}
		pattern.setFrequency(freq);
		pattern.setEntropy(ent);

		return pattern;
	}

	private File getEvalFile(EpisodeType type, int frequeny, double entropy) {
		String path = patternsFile.getAbsolutePath() + "/freq" + frequeny
				+ "/entropy" + entropy + "/" + type + "/evaluations.txt";
		return new File(path);
	}

	private void writePatterns(Set<Episode> patterns, String folderName,
			int frequency, int threshFreq, double threshEnt) throws IOException {
		List<Event> events = streamIo.readMapping(frequency);
		int patternId = 0;
		for (Episode pattern : patterns) {
			Episode epGraph = transClosure.remTransClosure(pattern);
			DirectedGraph<Fact, DefaultEdge> graph = episodeGraphConverter
					.convert(epGraph, events);
			graphWriter.write(graph,
					getFilepath(threshFreq, threshEnt, folderName, patternId));
			patternId++;
		}
	}

	private String getFilepath(int frequency, double entropy,
			String folderName, int patternId) {
		File filePath = new File(patternsFile.getAbsolutePath() + "/freq"
				+ frequency + "/entropy" + entropy + "/" + folderName);
		if (!filePath.exists()) {
			filePath.mkdirs();
		}
		String fileName = filePath.getAbsolutePath() + "/pattern" + patternId
				+ ".dot";
		return fileName;
	}

	private Map<Integer, Set<Episode>> getPatterns(EpisodeType type,
			int frequency, int threshFreq, double threshEnt) throws Exception {
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);
		return filter.filter(type, episodes, threshFreq, threshEnt);
	}

	private boolean equalEpisodes(Episode episode1, Episode episode2) {
		Set<Fact> facts1 = episode1.getFacts();
		Set<Fact> facts2 = episode2.getFacts();
		if (facts1.size() != facts2.size()) {
			return false;
		}
		if (facts1.containsAll(facts2) && facts2.containsAll(facts1)) {
			return true;
		} else {
			return false;
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

	private boolean isPartial(Episode pattern) {
		int numRels = pattern.getRelations().size();
		if (numRels < maxRels(pattern.getNumEvents())) {
			return true;
		}
		return false;
	}

	private int maxRels(int numEvents) {
		if (numEvents < 3) {
			return 1;
		} else {
			return (numEvents - 1) + maxRels(numEvents - 1);
		}
	}

	private void outputStats(Map<Integer, Set<Episode>> patterns) {
		Logger.log("\tPatternSize\tNumPatterns");
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			Logger.log("\t%d-node\t%d", entry.getKey(), entry.getValue().size());
		}
		Logger.log("");
	}

	private int patternsCounter(Map<Integer, Set<Episode>> patterns) {
		int counter = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			counter += entry.getValue().size();
		}
		return counter;
	}

	private Map<Set<Fact>, Set<Episode>> getPatternEvents(Set<Episode> patterns) {
		Map<Set<Fact>, Set<Episode>> results = Maps.newLinkedHashMap();

		for (Episode p : patterns) {
			Set<Fact> facts = p.getEvents();
			if (results.containsKey(facts)) {
				results.get(facts).add(p);
			} else {
				results.put(facts, Sets.newHashSet(p));
			}
		}
		return results;
	}
}
