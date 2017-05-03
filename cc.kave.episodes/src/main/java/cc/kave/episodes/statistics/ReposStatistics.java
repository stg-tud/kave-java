package cc.kave.episodes.statistics;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
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

	private static StreamStatistics statistics = new StreamStatistics();
	
	@Inject
	public ReposStatistics(@Named("contexts") Directory directory) {
		this.contextsDir = directory;
	}

	public void generate(int frequency) throws Exception {
		Map<String, List<Context>> repos = reposParser();
		printReposStats(repos);

		filterTypeOverlaps(repos);
		List<Event> stream2Filters = filterGeneratedCode(repos);
		repos.clear();
		List<Event> stream3Filters = filterUnknownMethods(stream2Filters);
		stream2Filters.clear();
		List<Event> stream4Filters = filterLocalTypes(stream3Filters);
		stream3Filters.clear();
		List<Event> stream5Filters = filterElementContexts(stream4Filters);
		stream4Filters.clear();
		filterFrequency(stream5Filters, frequency);
	}
	
	public void ctxOverlaps() throws Exception {
		Map<String, List<Context>> repos = reposParser();
		
		for (Map.Entry<String, List<Context>> entry : repos.entrySet()) {
			Set<ITypeName> repoTypes = Sets.newLinkedHashSet();
			Logger.log("Repository: %s", entry.getKey());
			for (Context ctx : entry.getValue()) {
				ITypeName type = ctx.getSST().getEnclosingType();
				if (repoTypes.contains(type)) {
					Logger.log("%s", type.getFullName());
				} else {
					repoTypes.add(type);
				}
			}
			repoTypes = Sets.newLinkedHashSet();
			Logger.log("");
		}
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
			IAssemblyName asm = event.getMethod().getDeclaringType().getAssembly();
			if (!asm.getName().equals("mscorlib") && asm.getVersion().isUnknown()) {
				continue;
			}
			results.add(event);
		}
		Logger.log("After filtering local types ...");
		printEventStats(results);
		return results;
	}

	private List<Event> filterGeneratedCode(Map<String, List<Context>> repos) {
		EventStreamGenerator streamGenerator = new EventStreamGenerator();
		Set<ITypeName> reposTypes = Sets.newLinkedHashSet();
		Set<ITypeName> currTypes = Sets.newLinkedHashSet();

		for (Map.Entry<String, List<Context>> entry : repos.entrySet()) {
			for (Context ctx : entry.getValue()) {
				ITypeName type = ctx.getSST().getEnclosingType();
				if (!reposTypes.contains(type)) {
					streamGenerator.add(ctx);
					currTypes.add(type);
				}
			}
			reposTypes.addAll(currTypes);
			currTypes.clear();
			currTypes = Sets.newLinkedHashSet();
		}
		List<Event> events = streamGenerator.getEventStream();
		Logger.log("After filtering auto-generated code ...");
		printEventStats(events);
		
		return events;
	}

	private void filterTypeOverlaps(Map<String, List<Context>> repos) {
		EventStreamGenerator streamGenerator = new EventStreamGenerator();
		Set<ITypeName> reposTypes = Sets.newLinkedHashSet();
		Set<ITypeName> currTypes = Sets.newLinkedHashSet();

		for (Map.Entry<String, List<Context>> entry : repos.entrySet()) {
			for (Context ctx : entry.getValue()) {
				ITypeName type = ctx.getSST().getEnclosingType();
				if (!reposTypes.contains(type)) {
					streamGenerator.addAny(ctx);
					currTypes.add(type);
				}
			}
			reposTypes.addAll(currTypes);
			currTypes.clear();
			currTypes = Sets.newLinkedHashSet();
		}
		Logger.log("After filtering overlaping types between different repositories ...");
		printEventStats(streamGenerator.getEventStream());
	}

	private void printReposStats(Map<String, List<Context>> repos) {
		Map<ITypeName, Integer> ctxTypes = reposCtxTypes(repos);
		List<Event> eventStream = generateStream(repos);
		int typeOccs = 0;

		for (Map.Entry<ITypeName, Integer> entry : ctxTypes.entrySet()) {
			typeOccs += entry.getValue();
		}
		Logger.log("Repositories information ...");
		Logger.log("Number of unique context types: %d", ctxTypes.size());
		Logger.log("Number of context type occurrences: %d", typeOccs);
		printEventStats(eventStream);
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

	private List<Event> generateStream(Map<String, List<Context>> repos) {
		EventStreamGenerator streamGenerator = new EventStreamGenerator();

		for (Map.Entry<String, List<Context>> entry : repos.entrySet()) {
			for (Context ctx : entry.getValue()) {
				streamGenerator.addAny(ctx);
			}
		}
		return streamGenerator.getEventStream();
	}

	private Map<ITypeName, Integer> reposCtxTypes(
			Map<String, List<Context>> repos) {
		Map<ITypeName, Integer> ctxTypes = Maps.newLinkedHashMap();

		for (Map.Entry<String, List<Context>> repoEntry : repos.entrySet()) {
			for (Context ctx : repoEntry.getValue()) {
				ITypeName type = ctx.getSST().getEnclosingType();

				if (ctxTypes.containsKey(type)) {
					int counter = ctxTypes.get(type);
					ctxTypes.put(type, counter + 1);
				} else {
					ctxTypes.put(type, 1);
				}
			}
		}
		return ctxTypes;
	}

	private Map<String, List<Context>> reposParser() throws Exception {
		List<Context> contexts = Lists.newLinkedList();
		Map<String, List<Context>> results = Maps.newLinkedHashMap();
		String repoName = "";

		for (String zip : findZips(contextsDir)) {
			Logger.log("Reading zip file %s", zip.toString());
			if ((repoName.equalsIgnoreCase("")) || (!zip.startsWith(repoName))) {
				if (!contexts.isEmpty()) {
					results.put(repoName, contexts);
					contexts = Lists.newLinkedList();
				}
				repoName = getRepoName(zip);
			}
			ReadingArchive ra = contextsDir.getReadingArchive(zip);
			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				if (ctx != null) {
					contexts.add(ctx);
				}
			}
			ra.close();
		}
		if (!contexts.isEmpty()) {
			results.put(repoName, contexts);
		}
		return results;
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
