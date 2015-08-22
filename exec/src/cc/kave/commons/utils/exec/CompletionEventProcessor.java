/**
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.utils.exec;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.visitor.inlining.InliningContext;
import cc.kave.commons.utils.Asserts;
import cc.kave.commons.utils.zip.ZipReader;
import cc.kave.commons.utils.zip.ZipWriter;

public class CompletionEventProcessor {

	private String dirIn;
	private String dirOut;

	public CompletionEventProcessor(String dirIn, String dirOut) {
		this.dirIn = dirIn;
		this.dirOut = dirOut;
	}

	public void run() {
		List<String> zips = findAllZips(dirIn);
		log("processing %d zips...", zips.size());
		int current = 1;
		for (String inZip : zips) {
			log("#### %d/%d ####################", current++, zips.size());
			String outZip = getOutName(inZip);
			log("in: %s", inZip);
			log("out: %s", outZip);
			process(inZip, outZip);
		}
	}

	private List<String> findAllZips(String dir) {
		List<String> zips = Lists.newLinkedList();
		for (File f : FileUtils.listFiles(new File(dir), new String[] { "zip" }, true)) {
			zips.add(f.getAbsolutePath());
		}
		return zips;
	}

	private String getOutName(String inName) {
		Asserts.assertThat(inName.startsWith(dirIn));
		String relativeName = inName.substring(dirIn.length());
		String outName = dirOut + relativeName;
		return outName;
	}

	private void process(String inZip, String outZip) {
		log("reading... ");
		List<CompletionEvent> events = read(inZip);
		append("found %d events", events.size());
		log("");
		for (CompletionEvent event : events) {
			append(".");
			inline(event);
		}
		log("writing...");
		write(events, outZip);
		append("done");
	}

	private List<CompletionEvent> read(String inZip) {
		List<CompletionEvent> events = Lists.newLinkedList();
		ZipReader r = new ZipReader(inZip);
		while (r.hasNext()) {
			CompletionEvent e = (CompletionEvent) r.getNext(ICompletionEvent.class);
			events.add(e);
		}
		return events;
	}

	private void write(List<CompletionEvent> outEvents, String outZip) {
		ZipWriter w = new ZipWriter(outZip);
		for (CompletionEvent e : outEvents) {
			w.add(e, ICompletionEvent.class);
		}
		w.dispose();
	}

	private void inline(CompletionEvent orig) {
		orig.Context = inline(orig.Context);
	}

	private Context inline(Context orig) {
		Context inlined = new Context();
		inlined.setTypeShape(orig.getTypeShape());
		inlined.setSST(inline(orig.getSST()));
		return inlined;
	}

	private ISST inline(ISST sst) {
		InliningContext context = new InliningContext();
		sst.accept(context.getStatementVisitor(), context);
		return context.getSST();
	}

	private static void log(String msg, Object... args) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
		String date = LocalDateTime.now().format(formatter);
		System.out.printf("\n%s %s", date, String.format(msg, args));
	}

	private static void append(String msg, Object... args) {
		System.out.printf(msg, args);
	}
}