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

import com.google.common.collect.Lists;
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
		Map<IMethodName, List<Fact>> eventStream = streamToMap(stream);
		Map<String, List<Fact>> repoStream = getRepoStreams(repos, eventStream);

		List<Event> events = streamIo.readMapping(frequency);
		Map<Episode, Integer> patterns = getEvaluations(EpisodeType.GENERAL,
				threshFreq, threshEntr);

		Map<String, Set<Episode>> specPatterns = initSpecRepoPatterns();
		Map<String, Set<Episode>> repoPatterns = getRepoPatterns(repos, stream,
				patterns, specPatterns);
		Map<String, Set<ITypeName>> repoTypes = getRepoTypes(repoPatterns);
	}

	private Map<String, Set<ITypeName>> getRepoTypes(
			Map<String, Set<Episode>> repoPatterns) {
		// TODO Auto-generated method stub
		return null;
	}

	private Map<String, Set<Episode>> getRepoPatterns(
			Map<String, Set<IMethodName>> repos,
			List<Tuple<IMethodName, List<Fact>>> stream,
			Map<Episode, Integer> patterns,
			Map<String, Set<Episode>> specPatterns) {

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

	private Map<String, List<Fact>> getRepoStreams(
			Map<String, Set<IMethodName>> repos,
			Map<IMethodName, List<Fact>> eventStream) {
		Map<String, List<Fact>> result = Maps.newLinkedHashMap();

		for (Map.Entry<String, Set<IMethodName>> entry : repos.entrySet()) {
			List<Fact> stream = Lists.newLinkedList();

			for (IMethodName methodName : entry.getValue()) {
				stream.addAll(eventStream.get(methodName));
			}
			result.put(entry.getKey(), stream);
		}
		return result;
	}

	private Map<IMethodName, List<Fact>> streamToMap(
			List<Tuple<IMethodName, List<Fact>>> stream) {
		Map<IMethodName, List<Fact>> streamMap = Maps.newLinkedHashMap();

		for (Tuple<IMethodName, List<Fact>> tuple : stream) {
			streamMap.put(tuple.getFirst(), tuple.getSecond());
		}
		return streamMap;
	}
}
