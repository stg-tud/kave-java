package cc.kave.episodes.statistics;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Named;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.FileReader;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EnclosingMethods;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

public class SpecificPatterns {

	private File patternsFile;

	private EventStreamIo streamIo;
	private FileReader reader;

	@Inject
	public SpecificPatterns(@Named("patterns") File folder,
			EventStreamIo eventStream, FileReader fileReader) {
		assertTrue(folder.exists(), "Patterns folder does not exist");
		assertTrue(folder.isDirectory(), "Patterns is not a folder, but a file");
		this.patternsFile = folder;
		this.streamIo = eventStream;
		this.reader = fileReader;
	}

	public void apiTypes(int frequency, int threshFreq, double threshEntr) {
		Map<String, Set<IMethodName>> repos = streamIo.readRepoCtxs(frequency);
		List<Tuple<IMethodName, List<Fact>>> stream = streamIo
				.readStreamObject(frequency);

		List<Event> events = streamIo.readMapping(frequency);
		Map<Episode, Integer> patterns = getEvaluations(EpisodeType.GENERAL,
				threshFreq, threshEntr);
		Set<ITypeName> generalTypes = getGenAPITypes(patterns, events);

		Map<String, Set<Episode>> repoSpecPatterns = getRepoSpecPatterns(repos,
				stream, patterns);

		printSpecPatterns(repoSpecPatterns, events);
		printGeneralTypes(generalTypes);
	}

	private void printGeneralTypes(Set<ITypeName> types) {
		Logger.log("\tAPI types occurring in general patterns:");
		for (ITypeName typeName : types) {
			Logger.log("\t%s", typeName.getIdentifier());
		}
	}

	private void printSpecPatterns(Map<String, Set<Episode>> repoSpecPatterns,
			List<Event> events) {
		int patternId = 0;
		for (Map.Entry<String, Set<Episode>> entry : repoSpecPatterns
				.entrySet()) {
			Logger.log("\tPatterns from repository: %s", entry.getKey());
			for (Episode pattern : entry.getValue()) {
				String patternName = patternId + ". ";
				Set<Fact> facts = pattern.getEvents();
				for (Fact fact : facts) {
					Event event = events.get(fact.getFactID());
					IMethodName methodName = event.getMethod();
					patternName += methodName.getDeclaringType().getIdentifier()
							+ "\n";
				}
				Logger.log("\t%s", patternName);
				patternId++;
			}
			Logger.log("");
		}
	}

	private Set<ITypeName> getGenAPITypes(Map<Episode, Integer> patterns,
			List<Event> events) {
		Set<ITypeName> types = Sets.newHashSet();

		for (Map.Entry<Episode, Integer> entry : patterns.entrySet()) {
			if (entry.getValue() == 1) {
				continue;
			}
			for (Fact fact : entry.getKey().getEvents()) {
				int id = fact.getFactID();
				types.add(events.get(id).getMethod().getDeclaringType());
			}
		}
		return types;
	}

	private Map<String, Set<Episode>> getRepoSpecPatterns(
			Map<String, Set<IMethodName>> repos,
			List<Tuple<IMethodName, List<Fact>>> stream,
			Map<Episode, Integer> patterns) {

		Map<String, Set<Episode>> specPatterns = initSpecRepoPatterns();
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
		return specPatterns;
	}

	private Map<String, Set<Episode>> initSpecRepoPatterns() {
		Map<String, Set<Episode>> result = Maps.newLinkedHashMap();
		result.put("Contexts-170503/msgpack/msgpack-cli", Sets.newHashSet());
		result.put("Contexts-170503/ServiceStack/ServiceStack",
				Sets.newHashSet());
		result.put("Contexts-170503/Glimpse/Glimpse", Sets.newHashSet());
		result.put("Contexts-170503/mono/mono", Sets.newHashSet());
		result.put("Contexts-170503/aumcode/nfx", Sets.newHashSet());
		result.put("Contexts-170503/devbridge/BetterCMS", Sets.newHashSet());
		result.put("Contexts-170503/octokit/octokit.net", Sets.newHashSet());
		result.put("Contexts-170503/nhibernate/nhibernate-core",
				Sets.newHashSet());
		result.put("Contexts-170503/OsmSharp/OsmSharp", Sets.newHashSet());
		result.put("Contexts-170503/ravendb/ravendb", Sets.newHashSet());
		result.put("Contexts-170503/aws/aws-sdk-net", Sets.newHashSet());

		return result;
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

	private File getEvalFile(EpisodeType type, int frequeny, double entropy) {
		String path = patternsFile.getAbsolutePath() + "/freq" + frequeny
				+ "/entropy" + entropy + "/" + type + "/evaluations.txt";
		return new File(path);
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
}
