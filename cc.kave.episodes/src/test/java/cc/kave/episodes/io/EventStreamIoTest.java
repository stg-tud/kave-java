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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.utils.json.JsonUtils;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.recommenders.exceptions.AssertionException;

public class EventStreamIoTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private static final String DUMMY_METHOD_NAME = "[You, Can] [Safely, Ignore].ThisDummyValue()";
	private static final IMethodName DUMMY_METHOD = Names
			.newMethod(DUMMY_METHOD_NAME);
	public static final Event DUMMY_EVENT = Events.newContext(DUMMY_METHOD);

	private static final int FREQUENCY = 2;

	File mappingFile;
	File streamTextFile;

	private EventStreamIo sut;

	@Before
	public void setup() {
		streamTextFile = getStreamTextFile();
		mappingFile = getMappingFile();

		sut = new EventStreamIo(tmp.getRoot());
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Events folder does not exist");
		sut = new EventStreamIo(new File("does not exist"));
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = tmp.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Events is not a folder, but a file");
		sut = new EventStreamIo(file);
	}

	@Test
	public void invalidMethodCtx() throws IOException {
		String expected = "";
		expected += "1,0.000\n2,0.001\n";
		expected += "3,5.002\n";
		expected += "2,15.003\n3,15.004\n";

		JsonUtils.toJson(expected, getStreamTextFile());

		String actuals = sut.readStreamText(FREQUENCY);

		assertEquals(actuals, actuals);
	}

	@Test
	public void happyPath() throws IOException {

		EventStream eventStream = new EventStream();
		eventStream.addEvent(firstCtx(1)); // 1
		eventStream.addEvent(inv(2)); // 2
		eventStream.addEvent(firstCtx(0)); // 3
		eventStream.addEvent(inv(5)); // 4
		eventStream.addEvent(firstCtx(0)); // 3
		eventStream.addEvent(firstCtx(0)); // 3
		eventStream.addEvent(inv(2)); // 2
		eventStream.addEvent(inv(5)); // 4

		sut.write(eventStream, FREQUENCY);

		assertTrue(streamTextFile.exists());
		assertTrue(mappingFile.exists());

		List<Event> actMapping = sut.readMapping(FREQUENCY);

		String actStreamText = sut.readStreamText(FREQUENCY);

		assertMapping(eventStream.getMapping(), actMapping);
		assertEquals(eventStream.getStreamText(), actStreamText);
		assertTrue(actMapping.size() == 5);
	}

	private boolean assertMapping(Set<Event> expMapping, List<Event> actMapping) {
		if (expMapping.size() != actMapping.size()) {
			return false;
		}
		for (Event event : expMapping) {
			if (!actMapping.contains(event)) {
				return false;
			}
		}
		return true;
	}

	private File getMappingFile() {
		File fileName = new File(getPath() + "/mapping.txt");
		return fileName;
	}

	private File getStreamTextFile() {
		File fileName = new File(getPath() + "/stream.txt");
		return fileName;
	}

	private String getPath() {
		File path = new File(tmp.getRoot().getAbsolutePath() + "/freq"
				+ FREQUENCY);
		if (!path.isDirectory()) {
			path.mkdirs();
		}
		return path.getAbsolutePath();
	}

	private static Event inv(int i) {
		return Events.newInvocation(m(i));
	}

	private static Event firstCtx(int i) {
		return Events.newFirstContext(m(i));
	}

	private static IMethodName m(int i) {
		return Names.newMethod("[T,P, 1.2.3.4] [T,P, 1.2.3.4].m" + i + "()");
	}
}