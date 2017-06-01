package cc.kave.episodes.statistics;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.episodes.eventstream.EventStreamNotGenerated;
import cc.kave.episodes.eventstream.EventsFilter;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.EventStream;
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

public class StreamGenerator4 {

	private Directory contextsDir;
	private EventStreamIo eventStreamIo;

	private Map<ITypeName, Integer> reposType = Maps.newLinkedHashMap();
	private List<Event> eventsAll = Lists.newLinkedList();

	@Inject
	public StreamGenerator4(@Named("contexts") Directory directory,
			EventStreamIo streamIo) {
		this.contextsDir = directory;
		this.eventStreamIo = streamIo;
	}

	public void generate(int frequency, int foldNum) throws IOException {
		EventStreamNotGenerated generator = new EventStreamNotGenerated();
		for (String zip : findZips(contextsDir)) {
			Logger.log("Reading zip file %s", zip.toString());
			ReadingArchive ra = contextsDir.getReadingArchive(zip);

			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				if (ctx != null) {
					ITypeName type = ctx.getSST().getEnclosingType();
					if (reposType.containsKey(type)) {
						int counter = reposType.get(type);
						reposType.put(type, counter + 1);
					} else {
						reposType.put(type, 1);
					}
					generator.add(ctx);
				}
			}
			ra.close();
		}
		eventsAll = generator.getEventStream();

		System.out.printf("\n\nRepository information:\n");
		System.out.printf("typeDecls: %d (%d unique)\n", occurrences(reposType),
				reposType.keySet().size());
		System.out.printf("-------");

		System.out.printf("\nAfter filtering overlapping types and generated code:\n");
		createStats(eventsAll);

		List<Event> events = filterLocalsAndUnknown();
		System.out.printf("\nFinal filterings:\n");
		createStats(events);

		EventStream stream = EventsFilter.filterStream(eventsAll, frequency);
		eventStreamIo.write(stream, frequency, foldNum);
	}

	private List<Event> filterLocalsAndUnknown() {
		List<Event> results = Lists.newLinkedList();
		String element = "";
		for (Event e : eventsAll) {
			if (isUnknown(e)) {
				continue;
			}
			if (e.getKind() == EventKind.TYPE) {
				results.add(e);
				continue;
			}
			if (e.getKind() == EventKind.METHOD_DECLARATION) {
				element = e.getMethod().getIdentifier();
				results.add(e);
				continue;
			}
			if (isLocal(e, element)) {
				continue;
			}
			results.add(e);
		}
		return results;
	}

	private boolean isUnknown(Event e) {
		if (e.getKind() == EventKind.TYPE) {
			return e.getType().isUnknown();
		} else {
			return e.getMethod().isUnknown();
		}
	}

	private int occurrences(Map<ITypeName, Integer> cumulator) {
		int occurrences = 0;
		for (Map.Entry<ITypeName, Integer> entry : cumulator.entrySet()) {
			occurrences += entry.getValue();
		}
		return occurrences;
	}

	private void createStats(List<Event> events) {
		System.out.printf("finished at %s\n", new Date());

		Set<ITypeName> uniqueTypes = Sets.newHashSet();
		Set<IMethodName> uniqueFirstCtxs = Sets.newHashSet();
		Set<IMethodName> uniqueSuperCtxs = Sets.newHashSet();
		Set<IMethodName> uniqueCtxs = Sets.newHashSet();
		Set<IMethodName> uniqueInv = Sets.newHashSet();

		String element = "";

		int numType = 0;
		int numFirstCtx = 0;
		int numSuperCtx = 0;
		int numCtxs = 0;
		int numInv = 0;

		for (Event e : events) {
			switch (e.getKind()) {
			case TYPE:
				uniqueTypes.add(e.getType());
				numType++;
				break;
			case FIRST_DECLARATION:
				uniqueFirstCtxs.add(e.getMethod());
				numFirstCtx++;
				break;
			case SUPER_DECLARATION:
				uniqueSuperCtxs.add(e.getMethod());
				numSuperCtx++;
				break;
			case METHOD_DECLARATION:
				element = e.getMethod().getIdentifier();
				uniqueCtxs.add(e.getMethod());
				numCtxs++;
				break;
			case INVOCATION:
				uniqueInv.add(e.getMethod());
				numInv++;
				break;
			default:
				System.err.println("should not happen");
			}
			isLocal(e, element);
		}

		System.out.printf("typeDecls\t%d (%d unique)\n", numType,
				uniqueTypes.size());
		System.out.printf("ctxFirst\t%d (%d unique)\n", numFirstCtx,
				uniqueFirstCtxs.size());
		System.out.printf("ctxSuper\t%d (%d unique)\n", numSuperCtx,
				uniqueSuperCtxs.size());
		System.out.printf("ctxElem\t%d (%d unique)\n", numCtxs,
				uniqueCtxs.size());
		System.out
				.printf("invExpr\t%d (%d unique)\n", numInv, uniqueInv.size());
		System.out.println("-------");
		System.out.printf("full stream %d events (excl. types)\n", numFirstCtx
				+ numSuperCtx + numCtxs + numInv);
		System.out.printf("really finished at %s\n", new Date());
	}

	private boolean isLocal(Event e, String elementCtx) {
		// predefined types have always an unknown version, but come
		// from mscorlib, so they should be included
		boolean oldVal = false;
		boolean newVal = false;
		ITypeName type = null;
		if (e.getKind() == EventKind.TYPE) {
			type = e.getType();
		} else {
			type = e.getMethod().getDeclaringType();
		}
		IAssemblyName asm = type.getAssembly();
		if (!asm.getName().equals("mscorlib") && asm.getVersion().isUnknown()) {
			oldVal = true;
		}
		if (asm.isLocalProject()) {
			newVal = true;
		}
		if ((oldVal != newVal) && (!isUnknown(e))) {
			System.out.printf("element context: %s\n", elementCtx);
			System.out.printf("event kind: %s\n", e.getKind().toString());
			if (e.getKind() == EventKind.TYPE) {
				System.out.printf("different localness for: %s\n",
						type.getIdentifier());
			} else {
				System.out.printf("different localness for: %s\n", e
						.getMethod().getIdentifier());
			}
			System.out.printf("\n");
		}
		return newVal;
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
