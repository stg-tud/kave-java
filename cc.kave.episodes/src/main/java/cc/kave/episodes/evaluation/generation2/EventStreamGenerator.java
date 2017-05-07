/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.episodes.evaluation.generation2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.utils.json.JsonUtils;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.io.IReadingArchive;
import cc.recommenders.io.ReadingArchive;

public class EventStreamGenerator {

	private static final int NUM_CORES = 4;

	private final ExecutorService pool = Executors.newFixedThreadPool(NUM_CORES);
	private final String dirZips;

	private ConcurrentLinkedQueue<String> zips;
	private Set<ITypeName> seenTypes = Sets.newConcurrentHashSet();
	private Set<IMethodName> seenMethods = Sets.newConcurrentHashSet();
	private List<Event> eventsAll = Lists.newLinkedList();

	public EventStreamGenerator(String dirZips) {
		this.dirZips = dirZips;
	}

	public void run() throws InterruptedException, ExecutionException, IOException {
		zips = findZips(dirZips);

		List<Future<?>> futures = Lists.newLinkedList();
		for (int i = 0; i < NUM_CORES; i++) {
			futures.add(pool.submit(new Worker(i)));
		}

		// block until done
		for (Future<?> f : futures) {
			f.get();
		}
		pool.shutdown();

		createStats();

		System.out.printf("writing event stream (%s)\n", new Date());
		File esFile = new File(dirZips + "es.txt");
		try (FileWriter fw = new FileWriter(esFile)) {
			fw.write('[');
			fw.write('\n');

			boolean isFirst = true;
			for (Event e : eventsAll) {
				if (!isFirst) {
					fw.write(',');
					fw.write('\n');
				}
				isFirst = false;
				fw.write(JsonUtils.toJson(e));
			}

			fw.write('\n');
			fw.write(']');
		}
		System.out.printf("done (%s)\n", new Date());
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

	int total;
	int started;
	private Object workerLock = new Object();

	public class Worker implements Runnable {
		private int workerId;

		public Worker(int workerId) {
			this.workerId = workerId;
		}

		@Override
		public void run() {
			System.out.printf("(%d) startup\n", workerId);
			String zip;
			while ((zip = getNext()) != null) {
				process(zip);
			}
			System.out.printf("(%d) shutdown\n", workerId);
		}

		private String getNext() {
			synchronized (workerLock) {
				String zip = zips.poll();
				if (zip != null) {
					double perc = ++started / (double) total * 100;
					System.out.printf("(%d) zip: %s (%d/%d started -- %.1f%%)\n", workerId, zip, started, total, perc);
				}
				return zip;
			}
		}
	}

	public void process(String zip) {
		List<Context> ctxs = readContexts(zip);
		List<Event> events = new PartialEventStreamGenerator().extract(ctxs, seenTypes, seenMethods);

		synchronized (eventsAll) {
			eventsAll.addAll(events);
		}
	}

	private ConcurrentLinkedQueue<String> findZips(String dirZips) {
		System.out.printf("started at %s\n", new Date());
		System.out.printf("searching for zips... ");
		Collection<File> fs = FileUtils.listFiles(new File(dirZips), new String[] { "zip" }, true);
		System.out.printf("%d found\n", fs.size());

		total = fs.size();

		ConcurrentLinkedQueue<String> zips = new ConcurrentLinkedQueue<String>();
		for (File f : fs) {
			String shortPath = f.getAbsolutePath().substring(dirZips.length());
			zips.add(shortPath);
		}

		return zips;
	}

	private List<Context> readContexts(String zip) {
		String fullPath = dirZips + zip;
		try (IReadingArchive ra = new ReadingArchive(new File(fullPath))) {
			return ra.getAll(Context.class);
		}
	}
}