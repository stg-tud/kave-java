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
package exec.validate_evaluation;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.utils.json.JsonUtils;
import cc.recommenders.io.ReadingArchive;

public class ReadAllContextsRunner {

	public void run(String fileEventStream, String fileEventMapping, String dirContexts) {
		readAllContexts(dirContexts);
		// readMapping(fileEventMapping);
	}

	private void readAllContexts(String dirContexts) {
		for (File zip : findAllZips(dirContexts)) {
			System.out.printf("reading '%s':\n", zip);
			try {
				ReadingArchive ra = new ReadingArchive(zip);

				while (ra.hasNext()) {
					Context ctx = ra.getNext(Context.class);
					if (ctx != null) {
						System.out.println("\t- " + ctx.getSST().getEnclosingType());
					}

					// if (i++ > 10) {
					// System.out.println("\t... (skipping the rest)");
					// ra.close();
					// return;
					// }
				}
				ra.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
			}
		}
		System.out.println(events.size());
	}
}