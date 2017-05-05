package cc.kave.episodes.statistics;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.episodes.eventstream.EventStreamGenerator;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class ReposStatistics {

	private Directory contextsDir;
	private File folder;

	private static StreamStatistics statistics = new StreamStatistics();

	@Inject
	public ReposStatistics(@Named("contexts") Directory directory,
			@Named("events") File root) {
		assertTrue(root.exists(), "Events folder does not exist");
		assertTrue(root.isDirectory(), "Events is not a folder, but a file");
		this.contextsDir = directory;
		this.folder = root;
	}

	public void generate(int frequency) throws Exception {
		List<Event> stream2Filters = reposParser();
		List<Event> stream3Filters = filterUnknownMethods(stream2Filters);
		stream2Filters.clear();
		List<Event> stream4Filters = filterLocalTypes(stream3Filters);
		stream3Filters.clear();
		List<Event> stream5Filters = filterElementContexts(stream4Filters);
		stream4Filters.clear();
		filterFrequency(stream5Filters, frequency);
	}

	public void ctxOverlaps() throws Exception {
		Map<ITypeName, Set<Context>> typeCtxs = Maps.newLinkedHashMap();
		// StringBuilder sb = new StringBuilder();
		String repoName = "";
		int dublicateCtx = 0;
		int dublicateType = 0;

		Logger.log("\tRepository\tNoTypes\tNoContext\tDublicateTypes\tDublicateCtxs");

		// sb.append("RepoName\tTypeName\tEquality\n");
		for (String zip : findZips(contextsDir)) {
			// Logger.log("Reading zip file %s", zip.toString());
			if ((repoName.equalsIgnoreCase("")) || (!zip.startsWith(repoName))) {
				// sb.append("\n" + repoName + "\t");
				int ctxCounter = getCtxCounter(typeCtxs);
				Logger.log("%s\t%d\t%d\t%d\t%d", repoName, typeCtxs.size(),
						ctxCounter, dublicateType, dublicateCtx);
				typeCtxs.clear();
				typeCtxs = Maps.newLinkedHashMap();
				repoName = getRepoName(zip);
				dublicateCtx = 0;
				dublicateType = 0;
			}
			ReadingArchive ra = contextsDir.getReadingArchive(zip);
			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				if (ctx != null) {
					ISST sst = ctx.getSST();
					for (IMethodDeclaration methodDecl : sst.getMethods()) {
						IMethodName name = methodDecl.getName();
					}
					ITypeName typeName = ctx.getSST().getEnclosingType();

					if (typeCtxs.containsKey(typeName)) {
						// sb.append(typeName.getFullName() + "\t");
						Set<Context> ctxList = typeCtxs.get(typeName);
						boolean equalCtxs = false;

						for (Context existCtx : ctxList) {
							if (ctx.equals(existCtx)) {
								equalCtxs = true;
								dublicateCtx++;
								break;
							}
						}
						if (!equalCtxs) {
							typeCtxs.get(typeName).add(ctx);
						}
						dublicateType++;
						// sb.append(equalCtxs + "\t");
					} else {
						typeCtxs.put(typeName, Sets.newHashSet(ctx));
					}
				}
			}
			ra.close();
		}
		// FileUtils.writeStringToFile(filePath(), sb.toString());
	}

	private int getCtxCounter(Map<ITypeName, Set<Context>> typeCtxs) {
		int counter = 0;

		for (Map.Entry<ITypeName, Set<Context>> entry : typeCtxs.entrySet()) {
			counter += entry.getValue().size();
		}
		return counter;
	}

	private File filePath() {
		File fileName = new File(folder.getAbsolutePath() + "/repoOverlaps.txt");
		return fileName;
	}

	private void filterFrequency(List<Event> events, int frequency) {
		Map<Event, Integer> occurrences = statistics.getFrequencies(events);
		List<Event> results = Lists.newLinkedList();

		for (Event event : events) {
			if (occurrences.get(event) >= frequency) {
				results.add(event);
			}
		}
		Logger.log("After filtering for frequency = %d ...", frequency);
		printEventStats(results);
	}

	private List<Event> filterElementContexts(List<Event> events) {
		List<Event> results = Lists.newLinkedList();

		for (Event event : events) {
			if (event.getKind() != EventKind.METHOD_DECLARATION) {
				results.add(event);
			}
		}
		Logger.log("After filtering element contexts ...");
		printEventStats(results);
		return results;
	}

	private List<Event> filterUnknownMethods(List<Event> events) {
		List<Event> results = Lists.newLinkedList();

		for (Event event : events) {
			if (!event.getMethod().isUnknown()) {
				results.add(event);
			}
		}
		Logger.log("After filtering unknown methods ...");
		printEventStats(results);
		return results;
	}

	private List<Event> filterLocalTypes(List<Event> events) {
		List<Event> results = Lists.newLinkedList();

		for (Event event : events) {
			if (isLocal(event)) {
				continue;
			}
			results.add(event);
		}
		Logger.log("After filtering local types ...");
		printEventStats(results);
		return results;
	}
	
	private boolean isLocal(Event e) {
	    // predefined types have always an unknown version, but come
	    // from mscorlib, so they should be included
	    boolean oldVal = false;
	    boolean newVal = false;
	    ITypeName type = e.getMethod().getDeclaringType();
	    IAssemblyName asm = type.getAssembly();
	    if (!asm.getName().equals("mscorlib") && asm.getVersion().isUnknown()) {
	        oldVal = true;
	    }
	    if (asm.isLocalProject()) {
	        newVal = true;
	    }
	    if (oldVal != newVal) {
	        System.out.printf("different localness for: %s\n", type);
	    }
	    return newVal;
	}

	private void filterGeneratedCode(EventStreamGenerator generator) {
		Logger.log("After filtering auto-generated code ...");
		printEventStats(generator.getEventStream());
	}

	private void printEventStats(List<Event> eventStream) {
		Map<Event, Integer> declarations = Maps.newLinkedHashMap();
		Map<Event, Integer> invocations = Maps.newLinkedHashMap();

		for (Event event : eventStream) {
			if (event.getKind() == EventKind.INVOCATION) {
				if (invocations.containsKey(event)) {
					int invOcc = invocations.get(event);
					invocations.put(event, invOcc + 1);
				} else {
					invocations.put(event, 1);
				}
			} else {
				if (declarations.containsKey(event)) {
					int declOcc = declarations.get(event);
					declarations.put(event, declOcc + 1);
				} else {
					declarations.put(event, 1);
				}
			}
		}
		int declsOccs = countOccurrences(declarations);
		int invOccs = countOccurrences(invocations);
		Logger.log("Number of unique method declarations: %d",
				declarations.size());
		Logger.log("Number of method declaration occurrences: %d", declsOccs);
		Logger.log("Number of unique method invocations: %d",
				invocations.size());
		Logger.log("Number of method invocation occurrences: %d", invOccs);
		Logger.log("");
	}

	private int countOccurrences(Map<Event, Integer> events) {
		int occurrences = 0;

		for (Map.Entry<Event, Integer> entry : events.entrySet()) {
			occurrences += entry.getValue();
		}
		return occurrences;
	}

	private List<Event> reposParser() throws Exception {
		// repository data
		Map<ITypeName, Integer> types = Maps.newLinkedHashMap();
		EventStreamGenerator streamGen0 = new EventStreamGenerator();
		// filter overlapping types
//		Set<ITypeName> reposTypes = Sets.newLinkedHashSet();
//		Set<ITypeName> currTypes = Sets.newLinkedHashSet();
		EventStreamGenerator streamGen1 = new EventStreamGenerator();
		// filter auto-generated code
		EventStreamGenerator streamGen2 = new EventStreamGenerator();

		String repoName = "";

		for (String zip : findZips(contextsDir)) {
			Logger.log("Reading zip file %s", zip.toString());
			if ((repoName.equalsIgnoreCase("")) || (!zip.startsWith(repoName))) {
//				reposTypes.addAll(currTypes);
//				currTypes = Sets.newLinkedHashSet();
				repoName = getRepoName(zip);
			}
			ReadingArchive ra = contextsDir.getReadingArchive(zip);
			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				if (ctx != null) {
					ITypeName typeName = ctx.getSST().getEnclosingType();

					if (types.containsKey(typeName)) {
						int counter = types.get(typeName);
						types.put(typeName, counter + 1);
					} else {
						types.put(typeName, 1);
					}
					streamGen0.addAny(ctx);

//					if (!reposTypes.contains(typeName)) {
//						currTypes.add(typeName);
						streamGen1.addAny(ctx);
						streamGen2.add(ctx);
//					}
				}
			}
			ra.close();
		}
//		reposTypes.addAll(currTypes);

		reposData(types, streamGen0);
//		sebStats(streamGen0);
		filterTypeOverlaps(types.keySet(), streamGen1);
		filterGeneratedCode(streamGen2);
		return streamGen2.getEventStream();
	}

	private void sebStats(EventStreamGenerator generator) {
		List<Event> stream = generator.getEventStream();
		List<List<Event>> parsedStream = parse(stream);
		List<Event> declarations = Lists.newLinkedList();
		Set<Event> invocations = Sets.newLinkedHashSet();
		int numbElements = 0;

		for (List<Event> method : parsedStream) {
			boolean decl = false;
			for (Event event : method) {
				if (!event.getMethod().isUnknown()) {
					if (!decl) {
						if ((event.getKind() == EventKind.FIRST_DECLARATION)
								|| (event.getKind() == EventKind.SUPER_DECLARATION)) {
							declarations.add(event);
							decl = true;
						}
					}
					if ((event.getKind() == EventKind.INVOCATION)
							&& !isLocal(event)) {
						invocations.add(event);
					}
				}
				if (event.getKind() == EventKind.METHOD_DECLARATION) {
					numbElements++;
				}
			}
		}
		Logger.log("Generating Sebastian's statistics ...");
		Logger.log("Number of methods in event stream: %d", parsedStream.size());
		Logger.log("Number of method elements: %d", numbElements);
		Logger.log("Occurrences of first or super method declarations: %d", declarations.size());
		Logger.log("Number of non-local method invocations: %d", invocations.size());
		Logger.log("");
	}

//	private boolean isLocal(Event event) {
//		IAssemblyName asm = event.getMethod().getDeclaringType().getAssembly();
//		if (!asm.getName().equals("mscorlib") && asm.getVersion().isUnknown()) {
//			return true;
//		}
//		return false;
//	}

	private List<List<Event>> parse(List<Event> stream) {
		List<List<Event>> results = Lists.newLinkedList();
		List<Event> method = Lists.newLinkedList();

		for (Event event : stream) {
			if (event.getKind() == EventKind.FIRST_DECLARATION) {
				if (!method.isEmpty()) {
					results.add(method);
					method = Lists.newLinkedList();
					method.add(event);
				}
			} else {
				method.add(event);
			}
		}
		if (!method.isEmpty()) {
			results.add(method);
		}
		return results;
	}

	private void reposData(Map<ITypeName, Integer> types,
			EventStreamGenerator streamGen) {
		int typeCounter = 0;

		for (Map.Entry<ITypeName, Integer> entry : types.entrySet()) {
			typeCounter += entry.getValue();
		}
		Logger.log("Repositories information ...");
		Logger.log("Number of unique context types: %d", types.size());
		Logger.log("Number of context type occurrences: %d", typeCounter);
		printEventStats(streamGen.getEventStream());
	}

	private void filterTypeOverlaps(Set<ITypeName> types,
			EventStreamGenerator generator) {
		Logger.log("After filtering overlaping types between different repositories ...");
		Logger.log("Number of unique context types: %d", types.size());
		printEventStats(generator.getEventStream());
	}

	private String getRepoName(String zipName) {
		int index = zipName.indexOf("/",
				zipName.indexOf("/", zipName.indexOf("/") + 1) + 1);
		String startPrefix = zipName.substring(0, index);

		return startPrefix;
	}

	private Set<String> findZips(Directory contextsDir) {
		Set<String> zips = contextsDir.findFiles(new Predicate<String>() {

			@Override
			public boolean apply(String arg0) {
				return arg0.endsWith(".zip");
			}
		});
		return zips;
	}
}
