package cc.kave.episodes.statistics;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.episodes.eventstream.EventStreamGenerator;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class StreamGenerator3 {

	private Directory contextsDir;

	private Set<ITypeName> seenTypes = Sets.newConcurrentHashSet();
	private Set<IMethodName> seenMethods = Sets.newConcurrentHashSet();
	private List<Event> eventsAll = Lists.newLinkedList();

	@Inject
	public StreamGenerator3(@Named("contexts") Directory directory) {
		this.contextsDir = directory;
	}

	public void generateStream() throws IOException {
		for (String zip : findZips(contextsDir)) {
			Logger.log("Reading zip file %s", zip.toString());
			ReadingArchive ra = contextsDir.getReadingArchive(zip);

			List<Event> events = new PartialEventStreamGenerator().extract(
					ra.getAll(Context.class), seenTypes, seenMethods);
			ra.close();
			eventsAll.addAll(events);
		}
		createStats();
	}
	
	private void createStats() {
		System.out.printf("finished at %s\n", new Date());

		Set<ITypeName> uniqueTypes = Sets.newHashSet();
		Set<IMethodName> uniqueFirstCtxs = Sets.newHashSet();
		Set<IMethodName> uniqueSuperCtxs = Sets.newHashSet();
		Set<IMethodName> uniqueCtxs = Sets.newHashSet();
		Set<IMethodName> uniqueInv = Sets.newHashSet();

		int numType = 0;
		int numFirstCtx = 0;
		int numSuperCtx = 0;
		int numCtxs = 0;
		int numInv = 0;

		for (Event e : eventsAll) {
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
		}

		System.out.printf("typeDecls\t%d (%d unique)\n", numType, uniqueTypes.size());
		System.out.printf("ctxFirst\t%d (%d unique)\n", numFirstCtx, uniqueFirstCtxs.size());
		System.out.printf("ctxSuper\t%d (%d unique)\n", numSuperCtx, uniqueSuperCtxs.size());
		System.out.printf("ctxElem\t%d (%d unique)\n", numCtxs, uniqueCtxs.size());
		System.out.printf("invExpr\t%d (%d unique)\n", numInv, uniqueInv.size());
		System.out.println("-------");
		System.out.printf("full stream %d events (excl. types)\n", numFirstCtx + numSuperCtx + numCtxs + numInv);
		System.out.printf("really finished at %s\n", new Date());
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
