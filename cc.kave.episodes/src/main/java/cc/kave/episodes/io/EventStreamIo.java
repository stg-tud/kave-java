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
import java.util.List;

import javax.inject.Named;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.utils.json.JsonUtils;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Lists;
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

	public void write(EventStream stream, int frequency, int foldNum) {
		try {
			FileUtils.writeStringToFile(
					new File(getTrainPath(frequency, foldNum).streamTextPath),
					stream.getStreamText());
			JsonUtils.toJson(stream.getMapping(),
					new File(getTrainPath(frequency, foldNum).mappingPath));
			JsonUtils.toJson(stream.getStreamData(),
					new File(getTrainPath(frequency, foldNum).streamDataPath));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public List<Event> readMapping(int freq, int foldNum) {
		String mappingPath = getTrainPath(freq, foldNum).mappingPath;

		@SuppressWarnings("serial")
		Type type = new TypeToken<List<Event>>() {
		}.getType();
		return JsonUtils.fromJson(new File(mappingPath), type);
	}

	public String readStreamText(int frequency, int foldNum) throws IOException {
		String streamPath = getTrainPath(frequency, foldNum).streamTextPath;

		String stream = FileUtils.readFileToString(new File(streamPath));

		return stream;
	}

	public List<Tuple<Event, List<Fact>>> parseStream(int frequency, int foldNum) {
		List<Tuple<Event, List<Fact>>> results = Lists.newLinkedList();

		List<Tuple<Event, String>> stream = readStreamData(frequency, foldNum);

		for (Tuple<Event, String> methodTuple : stream) {
			List<Fact> methodFacts = Lists.newLinkedList();
			String[] lines = methodTuple.getSecond().split("\n");

			for (String line : lines) {
				String[] eventTime = line.split(",");
				int eventID = Integer.parseInt(eventTime[0]);
				methodFacts.add(new Fact(eventID));
			}
			results.add(Tuple.newTuple(methodTuple.getFirst(), methodFacts));
		}
		return results;
	}

	private List<Tuple<Event, String>> readStreamData(int frequency, int foldNum) {
		String streamPath = getTrainPath(frequency, foldNum).streamDataPath;

		@SuppressWarnings("serial")
		Type type = new TypeToken<List<Tuple<Event, String>>>() {
		}.getType();
		List<Tuple<Event, String>> stream = JsonUtils.fromJson(new File(
				streamPath), type);
		assertMethods(stream);
		return stream;
	}

	private void assertMethods(List<Tuple<Event, String>> stream) {
		for (Tuple<Event, String> tuple : stream) {
			assertTrue(
					tuple.getFirst().getKind() == EventKind.METHOD_DECLARATION,
					"Stream contexts contains invalid mehod contexts");
		}

	}

	private class TrainingPath {
		String streamTextPath = "";
		String mappingPath = "";
		String streamDataPath = "";
	}

	private TrainingPath getTrainPath(int freq, int foldNum) {
		File path = new File(eventsDir.getAbsolutePath() + "/freq" + freq
				+ "/TrainingData/fold" + foldNum);
		if (!path.isDirectory()) {
			path.mkdirs();
		}
		TrainingPath trainPath = new TrainingPath();
		trainPath.streamTextPath = path.getAbsolutePath() + "/streamText.txt";
		trainPath.mappingPath = path.getAbsolutePath() + "/mapping.txt";
		trainPath.streamDataPath = path.getAbsolutePath() + "/streamData.json";

		return trainPath;
	}
}