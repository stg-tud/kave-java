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

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Named;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.utils.json.JsonUtils;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;

public class EventStreamIo {

	private File eventsDir;

	public static final double TIMEOUT = 0.5;

	@Inject
	public EventStreamIo(@Named("events") File folder) {
		assertTrue(folder.exists(), "Events folder does not exist");
		assertTrue(folder.isDirectory(), "Events is not a folder, but a file");
		this.eventsDir = folder;
	}

	public void write(EventStream stream, int freq) {
		try {
			FileUtils
					.writeStringToFile(new File(getTrainPath(freq).streamPath),
							stream.getStream());
			JsonUtils.toJson(stream.getMapping().keySet(), new File(
					getTrainPath(freq).mappingPath));
			JsonUtils.toJson(stream.getEnclMethods(), new File(
					getTrainPath(freq).methodsPath));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String readStream(int freq) throws IOException {
		String streamFile = getTrainPath(freq).streamPath;
		String stream = FileUtils.readFileToString(new File(streamFile));
		return stream;
	}

	public List<List<Fact>> parseStream(int freq) {
		List<List<Fact>> stream = Lists.newLinkedList();
		List<Fact> method = Lists.newLinkedList();

		String streamPath = getTrainPath(freq).streamPath;
		List<String> lines = readlines(streamPath);

		double timer = 0.0;

		for (String line : lines) {
			String[] eventTime = line.split(",");
			int eventID = Integer.parseInt(eventTime[0]);
			double timestamp = Double.parseDouble(eventTime[1]);
			while ((timestamp - timer) >= TIMEOUT) {
				stream.add(method);
				method = new LinkedList<Fact>();
				timer += TIMEOUT;
			}
			timer = timestamp;
			method.add(new Fact(eventID));
		}
		stream.add(method);
		return stream;
	}

	public List<Event> readMethods(int freq) {
		String methodsPath = getTrainPath(freq).methodsPath;

		@SuppressWarnings("serial")
		Type type = new TypeToken<List<Event>>() {
		}.getType();
		List<Event> methods = JsonUtils.fromJson(new File(methodsPath), type);
		assertMethods(methods);
		return methods;
	}

	public List<Tuple<List<Fact>, Event>> parseEventStream(int freq) {
		List<Tuple<List<Fact>, Event>> result = Lists.newLinkedList();

		Logger.log("Reading stream ...");
		List<List<Fact>> stream = parseStream(freq);
		Logger.log("Reading enclosing methods ...");
		List<Event> methods = readMethods(freq);

		assertTrue(stream.size() <= methods.size(),
				"Wrong parsing of stream and method files!");

		Logger.log("Merging stream with enclosing methods ....");
		int streamId = 0;
		for (int idx = 0; idx < stream.size(); idx++) {
			if ((streamId % 100000) == 0) {
				Logger.log("Stream method %d", streamId);
			}
			result.add(Tuple.newTuple(stream.get(idx), methods.get(idx)));
			streamId++;
		}
		return result;
	}

	private void assertMethods(List<Event> methods) {
		for (Event ctx : methods) {
			assertTrue(ctx.getKind() == EventKind.METHOD_DECLARATION,
					"List of methods does not contain only element cotexts!");
		}

	}

	public List<Event> readMapping(int freq) {
		String mappingPath = getTrainPath(freq).mappingPath;

		@SuppressWarnings("serial")
		Type type = new TypeToken<List<Event>>() {
		}.getType();
		return JsonUtils.fromJson(new File(mappingPath), type);
	}

	private List<String> readlines(String path) {
		List<String> lines = new LinkedList<String>();

		try {
			lines = FileUtils.readLines(new File(path));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return lines;
	}

	private class TrainingPath {
		String streamPath = "";
		String mappingPath = "";
		String methodsPath = "";
	}

	private TrainingPath getTrainPath(int freq) {
		File path = new File(eventsDir.getAbsolutePath() + "/freq" + freq
				+ "/TrainingData/fold0");
		if (!path.isDirectory()) {
			path.mkdirs();
		}
		TrainingPath trainPath = new TrainingPath();
		trainPath.streamPath = path.getAbsolutePath() + "/stream.txt";
		trainPath.mappingPath = path.getAbsolutePath() + "/mapping.txt";
		trainPath.methodsPath = path.getAbsolutePath() + "/methods.txt";

		return trainPath;
	}
}