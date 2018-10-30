package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.episodes.data.ContextsParser;
import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.FileReader;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.mining.patterns.PatternFilter;
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
import com.google.inject.name.Named;

public class APIUsages {

	private File patternFolder;

	private FileReader reader;
	private EventStreamIo streamIo;

	private EpisodeParser parser;
	private PatternFilter filter;

	private ContextsParser ctxParser;
	private EpisodeToGraphConverter graphConverter;

	@Inject
	public APIUsages(@Named("patterns") File folder, FileReader fileReader,
			EventStreamIo streamIo, EpisodeParser episodeParser,
			PatternFilter patternFilter, ContextsParser ctxParser,
			EpisodeToGraphConverter graphConverter) {
		assertTrue(folder.exists(), "Patterns folder does not exist!");
		assertTrue(folder.isDirectory(),
				"Patterns is not a folder, but a file!");
		this.patternFolder = folder;
		this.reader = fileReader;
		this.streamIo = streamIo;
		this.parser = episodeParser;
		this.filter = patternFilter;
		this.ctxParser = ctxParser;
		this.graphConverter = graphConverter;
	}

	public void categorise(EpisodeType type, int frequeny, int threshFreq,
			double threshEnt) {
		File evaluationFile = getFilePath(type, threshFreq, threshEnt);
		List<String> evalText = reader.readFile(evaluationFile);
		List<Event> events = streamIo.readMapping(frequeny);

		Map<PatternClassifier, Integer> stats = Maps.newLinkedHashMap();

		for (String line : evalText) {
			if (line.isEmpty() || line.contains("PatternId")) {
				continue;
			}
			if (line.contains("Patterns")) {
				if (!stats.isEmpty()) {
					outputStats(stats);
					stats = Maps.newLinkedHashMap();
				}
				System.out.println(line);
				continue;
			}
			String[] elems = line.split("\t");
			String facts = elems[1];
			int freq = Integer.parseInt(elems[2]);
			double ent = Double.parseDouble(elems[3]);
			int gens = Integer.parseInt(elems[4]);

			Episode pattern = createPattern(facts, freq, ent);

			Set<String> apis = Sets.newHashSet();
			for (Fact e : pattern.getEvents()) {
				Event event = events.get(e.getFactID());
				ITypeName typeName = event.getMethod().getDeclaringType();
				apis.add(typeName.getNamespace().getIdentifier());
			}
			int maxRels = maxRels(pattern.getNumEvents());

			PatternClassifier classifier = new PatternClassifier();
			if (gens > 1) {
				classifier.setGeneral(true);
			}
			if (pattern.getRelations().size() < maxRels) {
				classifier.setPartial(true);
			}
			if (apis.size() > 1) {
				classifier.setMultApis(true);
			}

			if (stats.containsKey(classifier)) {
				int counter = stats.get(classifier);
				stats.put(classifier, counter + 1);
			} else {
				stats.put(classifier, 1);
			}
		}
		for (Map.Entry<PatternClassifier, Integer> entry : stats.entrySet()) {
			PatternClassifier pc = entry.getKey();
			Logger.log("\t%s%s%s\t%d", pc.isGeneral(), pc.isPartial(),
					pc.isMultApis(), entry.getValue());
		}
		// outputStats(stats);
	}

	public void getApisComp(int frequency, int threshFreq, double threshEnt) {
		List<Event> events = streamIo.readMapping(frequency);
		Map<Episode, Integer> patterns = getEvaluations(EpisodeType.GENERAL,
				threshFreq, threshEnt);
		int patternId = 0;

		for (Map.Entry<Episode, Integer> entry : patterns.entrySet()) {
			Set<Fact> facts = entry.getKey().getEvents();
			if (facts.size() == 2) {
				patternId++;
				continue;
			}
			Set<String> types = Sets.newHashSet();
			for (Fact f : facts) {
				int id = f.getFactID();
				Event e = events.get(id);
				String typeName = e.getMethod().getDeclaringType()
						.getFullName();
				types.add(typeName);
			}
			if ((types.size() > 1) && isPartial(entry.getKey())) {
				Logger.log("%d. %s", patternId, types.toString());
				Logger.log("");
			}
			patternId++;
		}
	}

	public void orderApis(int frequency, int threshFreq, double threshEntropy)
			throws Exception {
		List<Event> events = streamIo.readMapping(frequency);
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);
		Map<Integer, Set<Episode>> patterns = filter.filter(episodes,
				threshFreq, threshEntropy);

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			TreeSet<String> apiStrict = Sets.newTreeSet();
			TreeSet<String> apiPartial = Sets.newTreeSet();

			for (Episode p : entry.getValue()) {
				Set<String> apis = Sets.newHashSet();
				for (Fact event : p.getEvents()) {
					int id = event.getFactID();
					IMethodName methodName = events.get(id).getMethod();
					ITypeName typeName = methodName.getDeclaringType();
					apis.add(typeName.getNamespace().getIdentifier());
				}
				int numRels = p.getRelations().size();
				if (numRels < maxRels(p.getNumEvents())) {
					apiPartial.addAll(apis);
				} else {
					apiStrict.addAll(apis);
				}
			}
			Logger.log("\tAPIs for %d-node patterns:", entry.getKey());
			Logger.log("\tAPIs only in strict-order patterns:");
			for (String api : apiStrict) {
				if (!apiPartial.contains(api)) {
					Logger.log("\t%s", api);
				}
			}
			Logger.log("");
			Logger.log("\tAPIs only in partial-order patterns:");
			for (String api : apiPartial) {
				if (!apiStrict.contains(api)) {
					Logger.log("\t%s", api);
				}
			}
			Logger.log("\n");
		}
	}

	public void specificPatterns(int frequency, int threshFreq,
			double threshEntr) {
		Set<String> defaults = Sets.newHashSet();
		defaults.add("System.InvalidOperationException");
		defaults.add("System.Math");
		defaults.add("System.Threading.Tasks.Task");
		defaults.add("System.Web.WebPages.WebPageBase");
		defaults.add("NUnit.Framework.Constraints.ConstraintExpression\tnunit.framework.2.6.0.12051");

		List<Event> events = streamIo.readMapping(frequency);
		Map<Episode, Integer> patterns = getEvaluations(EpisodeType.GENERAL,
				threshFreq, threshEntr);
		int numStricts = 0;
		for (Map.Entry<Episode, Integer> entry : patterns.entrySet()) {
			if (entry.getValue() == 1) {
				Set<String> types = Sets.newHashSet();
				Episode pattern = entry.getKey();

				for (Fact fact : pattern.getEvents()) {
					int id = fact.getFactID();
					ITypeName typeName = events.get(id).getMethod()
							.getDeclaringType();
					IAssemblyName asm = typeName.getAssembly();
					String name = typeName.getFullName() + "\t" + asm.getName()
							+ "." + asm.getVersion().getIdentifier();
					types.add(name);
				}
				if (!containedType(types, defaults)) {
					int strictRels = maxRels(pattern.getNumEvents());
					if (pattern.getRelations().size() == strictRels) {
						numStricts++;
						continue;
					}
					Logger.log("\tEvents involved:");
					Logger.log("\t%s", types.toString());
					Logger.log("\tPattern:");
					Logger.log("\t%s", pattern.getFacts().toString());
					Logger.log("");
				}
			}
		}
		Logger.log("\tNumber of strict-order, specific patterns: %d",
				numStricts);
	}

	public void freqGensApis(int frequency, int threshFreq, double threshEntropy)
			throws Exception {
		List<Event> events = streamIo.readMapping(frequency);
		Map<Episode, Integer> patterns = getEvaluations(EpisodeType.GENERAL,
				threshFreq, threshEntropy);

		int numNodes = 2;
		Map<String, Tuple<Integer, Integer>> types = Maps.newTreeMap();

		for (Map.Entry<Episode, Integer> entry : patterns.entrySet()) {
			Episode pattern = entry.getKey();
			if (pattern.getNumEvents() != numNodes) {
				Logger.log("\tAPIs for %d-node patterns:", numNodes);
				Logger.log("\t\t\tNumber of patterns:");
				Logger.log("\tAssembly\tTypes\tRepository-specific\tGeneral patterns");
				for (Map.Entry<String, Tuple<Integer, Integer>> apiEntry : types
						.entrySet()) {
					Tuple<Integer, Integer> tuple = apiEntry.getValue();
					Logger.log("\t%s\t%d\t%d", apiEntry.getKey(),
							tuple.getFirst(), tuple.getSecond());
				}
				Logger.log("");

				types = Maps.newTreeMap();
				numNodes = pattern.getNumEvents();
			}
			for (Fact event : pattern.getEvents()) {
				int id = event.getFactID();
				IMethodName methodName = events.get(id).getMethod();
				ITypeName typeName = methodName.getDeclaringType();
				IAssemblyName asm = typeName.getAssembly();
				String name = typeName.getFullName() + "\t" + asm.getName()
						+ "." + asm.getVersion().getIdentifier();

				if (types.containsKey(name)) {
					Tuple<Integer, Integer> tuple = types.get(name);
					if (entry.getValue() == 1) {
						types.put(
								name,
								Tuple.newTuple(tuple.getFirst() + 1,
										tuple.getSecond()));
					} else {
						types.put(
								name,
								Tuple.newTuple(tuple.getFirst(),
										tuple.getSecond() + 1));
					}
				} else {
					if (entry.getValue() == 1) {
						types.put(name, Tuple.newTuple(1, 0));
					} else {
						types.put(name, Tuple.newTuple(0, 1));
					}
				}
			}
		}
		Logger.log("\tAPIs for %d-node patterns:", numNodes);
		Logger.log("\t\t\tNumber of patterns:");
		Logger.log("\tAssembly\tTypes\tRepository-specific\tGeneral patterns");
		for (Map.Entry<String, Tuple<Integer, Integer>> apiEntry : types
				.entrySet()) {
			Tuple<Integer, Integer> tuple = apiEntry.getValue();
			Logger.log("\t%s\t%d\t%d", apiEntry.getKey(), tuple.getFirst(),
					tuple.getSecond());
		}
		Logger.log("");
	}

	public void freqOrderApis(int frequency, int threshFreq,
			double threshEntropy) throws Exception {
		List<Event> events = streamIo.readMapping(frequency);
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);
		Map<Integer, Set<Episode>> patterns = filter.filter(episodes,
				threshFreq, threshEntropy);

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			Map<String, Tuple<Integer, Integer>> types = Maps.newTreeMap();

			for (Episode p : entry.getValue()) {
				for (Fact event : p.getEvents()) {
					int id = event.getFactID();
					IMethodName methodName = events.get(id).getMethod();
					ITypeName typeName = methodName.getDeclaringType();
					IAssemblyName asm = typeName.getAssembly();
					String name = typeName.getFullName() + "\t" + asm.getName()
							+ "." + asm.getVersion().getIdentifier();

					if (types.containsKey(name)) {
						Tuple<Integer, Integer> tuple = types.get(name);
						if (isPartial(p)) {
							types.put(
									name,
									Tuple.newTuple(tuple.getFirst(),
											tuple.getSecond() + 1));
						} else {
							types.put(
									name,
									Tuple.newTuple(tuple.getFirst() + 1,
											tuple.getSecond()));
						}
					} else {
						if (isPartial(p)) {
							types.put(name, Tuple.newTuple(0, 1));
						} else {
							types.put(name, Tuple.newTuple(1, 0));
						}
					}
				}
			}
			Logger.log("\tAPIs for %d-node patterns:", entry.getKey());
			Logger.log("\t\t\tNumber of patterns:");
			Logger.log("\tAssembly\tTypes\tStrict-order\tPartial-order");
			for (Map.Entry<String, Tuple<Integer, Integer>> apiEntry : types
					.entrySet()) {
				Tuple<Integer, Integer> tuple = apiEntry.getValue();
				Logger.log("\t%s\t%d\t%d", apiEntry.getKey(), tuple.getFirst(),
						tuple.getSecond());
			}
			Logger.log("");
		}
	}

	public void apiUsages(EpisodeType type, int frequency, int threshFreq,
			double threshEntropy) {
		List<Event> events = streamIo.readMapping(frequency);
		Set<String> apiSpecific = Sets.newHashSet();
		Set<String> apiGeneral = Sets.newHashSet();

		Map<Episode, Integer> patterns = getEvaluations(type, threshFreq,
				threshEntropy);
		for (Map.Entry<Episode, Integer> entry : patterns.entrySet()) {
			Set<Fact> factEvent = entry.getKey().getEvents();
			if (factEvent.size() == 2) {
				if (entry.getValue() == 1) {
					for (Fact fact : factEvent) {
						int factId = fact.getFactID();
						ITypeName typeName = events.get(factId).getMethod()
								.getDeclaringType();
						apiSpecific
								.add(typeName.getNamespace().getIdentifier());
					}
				} else {
					for (Fact fact : factEvent) {
						int factId = fact.getFactID();
						ITypeName typeName = events.get(factId).getMethod()
								.getDeclaringType();
						apiGeneral.add(typeName.getNamespace().getIdentifier());
					}
				}
			}
		}
		Logger.log("APIs only in specific patterns:");
		for (String api : apiSpecific) {
			if (!apiGeneral.contains(api)) {
				Logger.log("%s", api);
			}
		}
		Logger.log("\nAPIs only in general patterns:");
		for (String api : apiGeneral) {
			if (!apiSpecific.contains(api)) {
				Logger.log("%s", api);
			}
		}
	}

	public void patternEvents(int frequency, int threshFreq,
			double threshEntropy) throws Exception {
		List<Event> events = streamIo.readMapping(frequency);
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);
		Map<Integer, Set<Episode>> patterns = filter.filter(episodes,
				threshFreq, threshEntropy);

		Map<ITypeName, Integer> types = Maps.newLinkedHashMap();
		int testPatterns = 0;

		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			for (Episode pattern : entry.getValue()) {
				for (Fact fact : pattern.getEvents()) {
					int id = fact.getFactID();
					Event e = events.get(id);
					IMethodName methodName = e.getMethod();
					ITypeName typeName = methodName.getDeclaringType();

					if (typeName.getFullName().contains("NUnit")
							&& !isPartial(pattern)) {
						testPatterns++;
						break;
					}

					if (types.containsKey(typeName)) {
						int counter = types.get(typeName);
						types.put(typeName, counter + 1);
					} else {
						types.put(typeName, 1);
					}
				}
			}
		}
		Logger.log("\tEvent types in partial-order patterns:");
		for (Map.Entry<ITypeName, Integer> entry : types.entrySet()) {
			Logger.log("\t%s\t%d", entry.getKey().getFullName(),
					entry.getValue());
		}
		Logger.log("Number of patterns learned from testing code: %d",
				testPatterns);
	}

	public void getRepoOccSpecPatt(int frequency, int threshFreq,
			double threshEntr) throws Exception {
		Logger.log("Reading event stream ...");
		List<Tuple<IMethodName, List<Fact>>> stream = streamIo
				.readStreamObject(frequency);
		Map<String, Set<IMethodName>> repos = streamIo.readRepoCtxs(frequency);

		Logger.log("Reading pattern evaluations ...");
		Map<Episode, Integer> patterns = getEvaluations(EpisodeType.GENERAL,
				threshFreq, threshEntr);

		Map<String, Integer> repoNoPatterns = Maps.newLinkedHashMap();

		int patternId = 0;

		Logger.log("Going thorugh the patterns ...");
		for (Map.Entry<Episode, Integer> entry : patterns.entrySet()) {
			if ((patternId % 100) == 0) {
				Logger.log("Analyzed %d patterns", patternId);
			}
			Episode pattern = entry.getKey();
			if (entry.getValue() == 1) {
				EnclosingMethods methodsOrderRelation = new EnclosingMethods(
						true);

				for (Tuple<IMethodName, List<Fact>> tuple : stream) {
					List<Fact> method = tuple.getSecond();
					if (method.size() < 2) {
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
				List<String> repositories = Lists.newLinkedList();

				for (Map.Entry<String, Set<IMethodName>> entryRepos : repos
						.entrySet()) {
					for (IMethodName methodName : entryRepos.getValue()) {
						if (methodOcc.contains(methodName)) {
							repositories.add(entryRepos.getKey());
							break;
						}
					}
				}
				if (repositories.size() > 1) {
					Logger.log("Repository-specific pattern occur in more than 1 repository!");
				} else {
					String repoName = repositories.get(0);
					if (repoNoPatterns.containsKey(repoName)) {
						int counter = repoNoPatterns.get(repoName);
						repoNoPatterns.put(repoName, counter + 1);
					} else {
						repoNoPatterns.put(repoName, 1);
					}
				}
			}
			patternId++;
		}
		Logger.log("\tRepository-specific patterns:");
		Logger.log("\tRepoName\tNoPatterns");
		for (Map.Entry<String, Integer> entry : repoNoPatterns.entrySet()) {
			Logger.log("\t%s\t%d", entry.getKey(), entry.getValue());
		}
	}

	public void repoEvents(int frequency) {
		List<Tuple<IMethodName, List<Fact>>> stream = streamIo
				.readStreamObject(frequency);
		Map<String, Set<IMethodName>> repoCtxs = streamIo
				.readRepoCtxs(frequency);
		Map<IMethodName, List<Fact>> streamMap = streamToMap(stream);

		Logger.log("\tRepository\tNumbEvents\tAvgEventOccs");
		for (Map.Entry<String, Set<IMethodName>> entry : repoCtxs.entrySet()) {
			Map<Fact, Integer> eventOccs = Maps.newLinkedHashMap();

			for (IMethodName methodName : entry.getValue()) {
				if (streamMap.containsKey(methodName)) {
					List<Fact> method = streamMap.get(methodName);

					for (Fact fact : method) {
						if (eventOccs.containsKey(fact)) {
							int counter = eventOccs.get(fact);
							eventOccs.put(fact, counter + 1);
						} else {
							eventOccs.put(fact, 1);
						}
					}
				}
			}
			Logger.log("\t%s\t%d\t%.3f", entry.getKey(), eventOccs.size(),
					avg(eventOccs));
		}
	}

	public void specRepo(int frequency, int threshFreq, double threshEnt) {
		List<Tuple<IMethodName, List<Fact>>> stream = streamIo
				.readStreamObject(frequency);
		Map<String, Set<IMethodName>> repos = streamIo.readRepoCtxs(frequency);
		Set<IMethodName> repo = repos
				.get("Contexts-170503/msgpack/msgpack-cli");

		Map<Episode, Integer> patterns = getEvaluations(EpisodeType.GENERAL,
				threshFreq, threshEnt);

		int patternId = 0;
		int specifics = 0;
		int generals = 0;

		Map<Integer, Set<Episode>> specPatterns = Maps.newLinkedHashMap();

		for (Map.Entry<Episode, Integer> entry : patterns.entrySet()) {
			if ((patternId % 100) == 0) {
				Logger.log("Analyzed %d patterns", patternId);
			}
			boolean isContained = false;
			Episode pattern = entry.getKey();
			EnclosingMethods methodsOrderRelation = new EnclosingMethods(true);

			for (Tuple<IMethodName, List<Fact>> tuple : stream) {
				List<Fact> method = tuple.getSecond();
				if (method.size() < 2) {
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
			Map<String, Set<IMethodName>> duplicates = Maps.newLinkedHashMap();
			for (IMethodName methodName : methodOcc) {
				if (repo.contains(methodName)) {
					isContained = true;
					String name = graphConverter.toLabel(methodName);
					if (duplicates.containsKey(name)) {
						duplicates.get(name).add(methodName);
					} else {
						duplicates.put(name, Sets.newHashSet(methodName));
					}
				}
			}
			if (isContained) {
				if (entry.getValue() == 1) {
					specifics++;
					int numEvents = entry.getKey().getNumEvents();
					if (specPatterns.containsKey(numEvents)) {
						specPatterns.get(numEvents).add(entry.getKey());
					} else {
						specPatterns.put(numEvents,
								Sets.newHashSet(entry.getKey()));
					}
				} else {
					generals++;
				}
			}
			for (Map.Entry<String, Set<IMethodName>> entryDupl : duplicates
					.entrySet()) {
				if (entryDupl.getValue().size() > 1) {
					for (IMethodName mn : entryDupl.getValue()) {
						Logger.log("%s", mn.toString());
					}
				}
			}
			patternId++;
			Logger.log("");
		}
		Logger.log("Number of specific patterns: %d", specifics);
		Logger.log("\tNumEvents\tNumPatterns");
		for (Map.Entry<Integer, Set<Episode>> entry : specPatterns.entrySet()) {
			Logger.log("\t%d\t%d", entry.getKey(), entry.getValue().size());
		}
		Logger.log("Number of general patterns: %d", generals);
	}

	public void defaultEvents(int frequency) {
		Map<String, Set<IMethodName>> repos = streamIo.readRepoCtxs(frequency);
		repos.remove("Contexts-170503/msgpack/msgpack-cli");
		Set<IMethodName> ctxs = getCtxs(repos);
		List<Tuple<IMethodName, List<Fact>>> stream = streamIo
				.readStreamObject(frequency);
		List<Event> events = streamIo.readMapping(frequency);

		Map<String, Boolean> defaultEvents = Maps.newLinkedHashMap();
		defaultEvents.put("System.Nullable`1[[T]].GetValueOrDefault() : T",
				false);
		// event only in this repository
		defaultEvents
				.put("System.Tuple.Create(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5, T6 item6, T7 item7) : Tuple",
						false);
		defaultEvents.put("System.IO.MemoryStream..ctor(byte[] buffer) : void",
				false);
		defaultEvents.put(
				"NUnit.Framework.Assert.Throws(TestDelegate code) : T", false);
		defaultEvents
				.put("NUnit.Framework.Constraints.ConstraintExpression.SameAs(object expected) : SameAsConstraint",
						false);
		defaultEvents
				.put("NUnit.Framework.Is.InstanceOf(Type expectedType) : InstanceOfTypeConstraint",
						false);

		for (Tuple<IMethodName, List<Fact>> tuple : stream) {
			if (ctxs.contains(tuple.getFirst())) {
				for (Fact fact : tuple.getSecond()) {
					Event event = events.get(fact.getFactID());
					String name = graphConverter.toLabel(event.getMethod());

					if (defaultEvents.containsKey(name)) {
						defaultEvents.put(name, true);
					}
				}
			}
		}
		for (Map.Entry<String, Boolean> entry : defaultEvents.entrySet()) {
			if (entry.getValue()) {
				Logger.log("%s", entry.getKey());
			}
		}
	}

	public void getRepoOccStrictPatt(int frequency, int threshFreq,
			double threshEntr) throws Exception {
		List<Event> events = streamIo.readMapping(frequency);
		Map<Event, Integer> eventsMap = mapConverter(events);

		Map<Episode, Integer> patterns = getEvaluations(EpisodeType.GENERAL,
				threshFreq, threshEntr);

		List<Tuple<Event, List<Event>>> stream = ctxParser.parse(frequency);
		Logger.log("Getting repos-ctx mapper ...");
		Map<String, Set<IMethodName>> repos = ctxParser.getRepoCtxMapper();
		Logger.log("Converting stream of events into stream of facts ...");
		List<Tuple<Event, List<Fact>>> streamOfFacts = convertStreamOfFacts(
				stream, eventsMap);
		stream.clear();

		Map<String, Integer> repoNoPatterns = Maps.newLinkedHashMap();
		int patternId = 0;

		for (Map.Entry<Episode, Integer> entry : patterns.entrySet()) {
			Logger.log("Analyzing pattern %d ...", patternId);
			Episode pattern = entry.getKey();
			if (isPartial(pattern)) {
				EnclosingMethods methodsOrderRelation = new EnclosingMethods(
						true);
				Logger.log("Iterating through the events of facts ...");
				for (Tuple<Event, List<Fact>> tuple : streamOfFacts) {
					List<Fact> method = tuple.getSecond();
					if (method.size() < 2) {
						continue;
					}
					if (method.containsAll(pattern.getEvents())) {
						Event ctx = tuple.getFirst();
						methodsOrderRelation.addMethod(pattern, method, ctx);
					}
				}
				int numOccs = methodsOrderRelation.getOccurrences();
				assertTrue(numOccs >= pattern.getFrequency(),
						"Found insufficient number of occurences!");

				Set<IMethodName> methodOcc = methodsOrderRelation
						.getMethodNames(numOccs);
				List<String> repositories = Lists.newLinkedList();

				Logger.log("Iterating through the repositories ...");
				for (Map.Entry<String, Set<IMethodName>> entryRepos : repos
						.entrySet()) {
					for (IMethodName methodName : entryRepos.getValue()) {
						if (methodOcc.contains(methodName)) {
							repositories.add(entryRepos.getKey());
							break;
						}
					}
				}
				Logger.log("Storing repos-number of patterns occurrences ...");
				for (String repoName : repositories) {
					if (repoNoPatterns.containsKey(repoName)) {
						int counter = repoNoPatterns.get(repoName);
						repoNoPatterns.put(repoName, counter + 1);
					} else {
						repoNoPatterns.put(repoName, 1);
					}
				}
			}
			patternId++;
		}
		Logger.log("\tPartial-order patterns:");
		Logger.log("\tRepoName\tNoPatterns");
		for (Map.Entry<String, Integer> entry : repoNoPatterns.entrySet()) {
			Logger.log("\t%s\t%d", entry.getKey(), entry.getValue());
		}
	}

	public void repoNumCtxs(int frequency) {
		Map<String, Set<IMethodName>> repoCtxs = streamIo
				.readRepoCtxs(frequency);
		List<Tuple<IMethodName, List<Fact>>> stream = streamIo
				.readStreamObject(frequency);
		Map<IMethodName, List<Fact>> streamMap = streamToMap(stream);

		Logger.log("\tRepository statistics:");
		Logger.log("\tRepository\tNum.Ctxs.\tstreamLength");
		for (Map.Entry<String, Set<IMethodName>> entry : repoCtxs.entrySet()) {
			int streamLength = 0;
			for (IMethodName methodName : entry.getValue()) {
				if (streamMap.containsKey(methodName)) {
					streamLength += streamMap.get(methodName).size();
				}
			}
			Logger.log("\t%s\t%d\t%d", entry.getKey(), entry.getValue().size(),
					streamLength);
		}
	}

	public void getOrderInfo(int frequency, int threshFreq, double threshEnt)
			throws Exception {
		Map<Integer, Set<Episode>> episodes = parser.parser(frequency);
		Map<Integer, Set<Episode>> patterns = filter.filter(episodes,
				threshFreq, threshEnt);

		Logger.log("\tPatternSize\tNumStrict\tNumPartials");
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			int numStrict = 0;
			int numPart = 0;

			for (Episode p : entry.getValue()) {
				if (isPartial(p)) {
					numPart++;
				} else {
					numStrict++;
				}
			}
			Logger.log("\t%d-node\t%d\t%d", entry.getKey(), numStrict, numPart);
		}
	}

	private double avg(Map<Fact, Integer> eventOccs) {
		if (eventOccs.isEmpty()) {
			return 0.0;
		}
		int sum = 0;

		for (Map.Entry<Fact, Integer> entry : eventOccs.entrySet()) {
			sum += entry.getValue();
		}
		return (sum / eventOccs.size());
	}

	private Map<IMethodName, List<Fact>> streamToMap(
			List<Tuple<IMethodName, List<Fact>>> stream) {
		Map<IMethodName, List<Fact>> streamMap = Maps.newLinkedHashMap();

		for (Tuple<IMethodName, List<Fact>> tuple : stream) {
			streamMap.put(tuple.getFirst(), tuple.getSecond());
		}
		return streamMap;
	}

	private List<Tuple<Event, List<Fact>>> convertStreamOfFacts(
			List<Tuple<Event, List<Event>>> stream, Map<Event, Integer> events) {
		List<Tuple<Event, List<Fact>>> results = Lists.newLinkedList();

		for (Tuple<Event, List<Event>> tuple : stream) {
			List<Fact> facts = Lists.newLinkedList();

			for (Event event : tuple.getSecond()) {
				int id = events.get(event);
				facts.add(new Fact(id));
			}
			results.add(Tuple.newTuple(tuple.getFirst(), facts));
		}
		return results;
	}

	private Map<Event, Integer> mapConverter(List<Event> events) {
		Map<Event, Integer> map = Maps.newLinkedHashMap();
		int id = 0;

		for (Event event : events) {
			map.put(event, id);
			id++;
		}
		return map;
	}

	private boolean isPartial(Episode pattern) {
		int numRels = pattern.getRelations().size();
		if (numRels < maxRels(pattern.getNumEvents())) {
			return true;
		}
		return false;
	}

	private void outputStats(Map<PatternClassifier, Integer> stats) {
		StringBuilder sb = new StringBuilder();
		sb.append("\t\tOne API\tMultiple APIs\n");
		sb.append("Specific\tStrict");

		PatternClassifier classifier = new PatternClassifier();
		sb.append(getValue(classifier, stats));
		classifier.setMultApis(true);
		sb.append(getValue(classifier, stats));
		sb.append("\n");

		sb.append("\tPartial");
		classifier.setPartial(true);
		classifier.setMultApis(false);
		sb.append(getValue(classifier, stats));
		classifier.setMultApis(true);
		sb.append(getValue(classifier, stats));
		sb.append("\n");

		sb.append("General\tStrict");
		classifier.setGeneral(true);
		classifier.setPartial(false);
		classifier.setMultApis(false);
		sb.append(getValue(classifier, stats));
		classifier.setMultApis(true);
		sb.append(getValue(classifier, stats));
		sb.append("\n");

		sb.append("\tPartial");
		classifier.setPartial(true);
		classifier.setMultApis(false);
		sb.append(getValue(classifier, stats));
		classifier.setMultApis(true);
		sb.append(getValue(classifier, stats));
		sb.append("\n");

		System.out.println(sb.toString());
	}

	private String getValue(PatternClassifier classifier,
			Map<PatternClassifier, Integer> stats) {
		String text = "";
		if (stats.containsKey(classifier)) {
			text += "\t" + stats.get(classifier).toString();
		} else {
			text += "\t0";
		}
		return text;
	}

	private Map<Episode, Integer> getEvaluations(EpisodeType type,
			int frequency, double entropy) {
		File evaluationFile = getFilePath(type, frequency, entropy);
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

	private boolean containedType(Set<String> types, Set<String> defaults) {
		for (String t : types) {
			for (String d : defaults) {
				if (t.startsWith(d)) {
					return true;
				}
			}
		}
		return false;
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

	private int maxRels(int numEvents) {
		if (numEvents < 3) {
			return 1;
		} else {
			return (numEvents - 1) + maxRels(numEvents - 1);
		}
	}

	private Set<IMethodName> getCtxs(Map<String, Set<IMethodName>> repos) {
		Set<IMethodName> ctxs = Sets.newHashSet();
		for (Map.Entry<String, Set<IMethodName>> entry : repos.entrySet()) {
			ctxs.addAll(entry.getValue());
		}
		return ctxs;
	}

	private File getFilePath(EpisodeType type, int frequeny, double entropy) {
		String path = patternFolder.getAbsolutePath() + "/freq" + frequeny
				+ "/entropy" + entropy + "/" + type + "/evaluations.txt";
		return new File(path);
	}
}
