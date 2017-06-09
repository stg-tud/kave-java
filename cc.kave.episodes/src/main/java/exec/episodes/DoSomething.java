/**
 * Copyright 2014 Technische Universität Darmstadt
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
package exec.episodes;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipException;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.utils.json.JsonUtils;
import cc.kave.episodes.eventstream.StreamFilterGenerator;
import cc.kave.episodes.eventstream.ToFactsVisitor;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.io.ReadingArchive;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;

public class DoSomething {

	public void run(String fileEventStream, String fileEventMapping, String dirContexts) throws IOException {
		readAllContexts(dirContexts);
		// readMapping(fileEventMapping);
	}

	private void readAllContexts(String dirContexts) throws ZipException, IOException {
		List<File> zips = findAllZips(dirContexts);
		for (File zip : zips) {
			ReadingArchive ra = new ReadingArchive(zip);

			int i = 0;

			StreamFilterGenerator gen = new StreamFilterGenerator();
			System.out.println("found contexts for the following classes:");
			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);

				gen.add(ctx);
				
				
				
				List<Event> events = Lists.newLinkedList(); // get from
															// somewhere
															// ("eventMapping.txt")
				ToFactsVisitor tfv = new ToFactsVisitor(events);

				ISST sst = ctx.getSST();
				for (IMethodDeclaration md : sst.getMethods()) {
					Episode ep = new Episode();
					Set<Fact> facts = Sets.newHashSet();
					md.accept(tfv, facts);

					for (Fact f : facts) {
						ep.addFact(f);
					}
				}

				System.out.println("\t- " + ctx.getSST().getEnclosingType());

				if (i++ > 10) {
					System.out.println("\t... (skipping the rest)");
					ra.close();
					return;
				}
			}
			ra.close();
			
			List<Event> es = gen.getEventStream();
			
//			EventStreamIo.write(es, null, null);
		}
	}

	private List<File> findAllZips(String dir) {
		List<File> zips = Lists.newLinkedList();
		for (File f : FileUtils.listFiles(new File(dir), new String[] { "zip" }, true)) {
			zips.add(f);
		}
		return zips;
	}

	@SuppressWarnings("serial")
	private void readMapping(String fileEventMapping) {
		File in = new File(fileEventMapping);
		Type listType = new TypeToken<LinkedList<Event>>() {
		}.getType();
		List<Event> events = JsonUtils.fromJson(in, listType);

		System.out.printf("found %s events:\n", events.size());
		int i = 1;
		// events.indexOf(event)
		for (Event e : events) {
			if (i == 4978 || i == 8698 || i == 8708 || i == 8718 || i == 8725 || i == 4641) {
				i++;
				continue;
			}
			System.out.println("--- " + (i++) + " ---------------------");
			System.out.println(e);
			if (e.getMethod().getDeclaringType().toString().startsWith("System")) {
				System.out.println(e.getMethod().getDeclaringType().toString());
				// usage.getType().getIdentifier().startsWith("Lorg/eclipse/swt/widgets/");
			}
		}
		System.out.println(events.size());
	}
}