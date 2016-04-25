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
package cc.kave.episodes.export;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.utils.json.JsonUtils;
import cc.recommenders.assertions.Asserts;
import cc.recommenders.io.Logger;

public class EventStreamIo {

	public static final double DELTA = 0.001;
	public static final double TIMEOUT = 0.5;

	public static void write(List<Event> stream, String fileStream, String fileMapping) {
		try {
			List<Event> mapping = createMapping(stream);
			String streamTxt = toString(stream, mapping);
			Logger.log("Writing stream data file!");
			FileUtils.writeStringToFile(new File(fileStream), streamTxt);
			Logger.log("Writing mapping data file!");
			JsonUtils.toJson(mapping, new File(fileMapping));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static List<Event> createMapping(List<Event> stream) {
		List<Event> mapping = Lists.newLinkedList();
		int eventNumber = 0;
		for (Event e : stream) {
			eventNumber++;
			if (mapping.indexOf(e) == -1) {
				mapping.add(e);
			}
			if (eventNumber % 100000 == 0){
				Logger.log("Created mapping for %d/%d", eventNumber, stream.size());
			}
		}
		return mapping;
	}

	private static String toString(List<Event> stream, List<Event> mapping) {
		StringBuilder sb = new StringBuilder();

		boolean isFirstMethod = true;
		double time = 0.000;
		int eventNumber = 0;

		for (Event e : stream) {
			eventNumber++;
			int idx = mapping.indexOf(e);
			Asserts.assertNotNegative(idx);

			if (e.getKind() == EventKind.METHOD_DECLARATION && !isFirstMethod) {
				time += TIMEOUT;
			}
			isFirstMethod = false;

			sb.append(idx);
			sb.append(',');
			sb.append(String.format("%.3f",time));
			sb.append('\n');

			time += DELTA;
			
			if (eventNumber % 100000 == 0){
				Logger.log("Converting to a string %d/%d", eventNumber, stream.size());
			}
		}
		return sb.toString();
	}

	public static List<Event> readMapping(String path) {
		@SuppressWarnings("serial")
		Type type = new TypeToken<List<Event>>() {
		}.getType();
		return JsonUtils.fromJson(new File(path), type);
	}
}