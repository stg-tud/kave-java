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
package cc.kave.episodes.io;

import static cc.kave.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import javax.inject.Named;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.utils.json.JsonUtils;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.io.Logger;

import com.google.common.collect.Sets;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;

public class EventStreamIo {

	private File eventsDir;

	@Inject
	public EventStreamIo(@Named("events") File folder) {
		assertTrue(folder.exists(), "Events folder does not exist");
		assertTrue(folder.isDirectory(), "Events is not a folder, but a file");
		this.eventsDir = folder;
	}

	public void write(EventStream stream, int frequency) {
		try {
			FileUtils
					.writeStringToFile(new File(
							getTrainPath(frequency).streamPath), stream
							.getStreamText());
			JsonUtils.toJson(stream.getMapping(), new File(
					getTrainPath(frequency).mappingPath));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Event> readMapping(int frequency) {
		String mappingPath = getTrainPath(frequency).mappingPath;

		@SuppressWarnings("serial")
		Type type = new TypeToken<List<Event>>() {
		}.getType();
		return JsonUtils.fromJson(new File(mappingPath), type);
	}

	public String readStreamText(int frequency) throws IOException {
		String streamPath = getTrainPath(frequency).streamPath;

		String stream = FileUtils.readFileToString(new File(streamPath));

		return stream;
	}

	public void streamStats(int frequency) throws IOException {
		String stream = readStreamText(frequency);
		List<Event> events = readMapping(frequency);
		
		Set<Event> ctxFirst = Sets.newHashSet();
		Set<Event> ctxSuper = Sets.newHashSet();
		Set<Event> invs = Sets.newHashSet();
		
		int numFirst = 0;
		int numSuper = 0;
		int numInvs = 0;

		String[] lines = stream.split("\n");
		for (String line : lines) {
			String[] eventTime = line.split(",");
			int eventID = Integer.parseInt(eventTime[0]);
			Event event = events.get(eventID);
			
			switch (event.getKind()) {
			case FIRST_DECLARATION:
				ctxFirst.add(event);
				numFirst++;
				break;
			case SUPER_DECLARATION:
				ctxSuper.add(event);
				numSuper++;
				break;
			case INVOCATION:
				invs.add(event);
				numInvs++;
				break;
			default:
				System.err.println("should not happen");
			}
		}
		Logger.log("Statistics from event stream:");
		Logger.log("ctxFirst: %d (%d unique)", numFirst, ctxFirst.size());
		Logger.log("ctxSuper %d (%d unique)", numSuper, ctxSuper.size());
		Logger.log("invs: %d (%d unique)", numInvs, invs.size());
	}

	private class TrainingPath {
		String streamPath = "";
		String mappingPath = "";
	}

	private TrainingPath getTrainPath(int frequency) {
		File path = new File(eventsDir.getAbsolutePath() + "/freq" + frequency);
		if (!path.isDirectory()) {
			path.mkdir();
		}
		TrainingPath trainPath = new TrainingPath();
		trainPath.streamPath = path.getAbsolutePath() + "/stream.txt";
		trainPath.mappingPath = path.getAbsolutePath() + "/mapping.txt";

		return trainPath;
	}
}