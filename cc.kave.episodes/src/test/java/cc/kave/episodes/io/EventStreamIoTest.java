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
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.exceptions.AssertionException;

import com.google.common.collect.Lists;

public class EventStreamIoTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private static final String DUMMY_METHOD_NAME = "[You, Can] [Safely, Ignore].ThisDummyValue()";
	private static final IMethodName DUMMY_METHOD = Names
			.newMethod(DUMMY_METHOD_NAME);
	public static final Event DUMMY_EVENT = Events.newContext(DUMMY_METHOD);
	
	private static final int FOLDNUM = 2;
	
	private EventStreamIo sut;
	
	@Before
	public void setup() {
		sut = new EventStreamIo(tmp.getRoot());
	}
	
	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Repositories folder does not exist");
		sut = new EventStreamIo(new File("does not exist"));
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = tmp.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Repositories is not a folder, but a file");
		sut = new EventStreamIo(file);
	}

	@Test
	public void happyPath() throws IOException {

		File streamFile = getStreamFile();
		File mappingFile = getMappingFile();
		File methodsFile = getMethodsFile();

		EventStream expected = new EventStream();
		expected.addEvent(firstCtx(1)); // 1
		expected.addEvent(ctx(1));
		expected.addEvent(inv(2)); // 2
		expected.addEvent(firstCtx(0));
		expected.addEvent(ctx(2));
		expected.addEvent(inv(5)); // 3
		expected.addEvent(firstCtx(0));
		expected.addEvent(ctx(3));
		expected.addEvent(firstCtx(0));
		expected.addEvent(ctx(4));
		expected.addEvent(inv(2)); // 2
		expected.addEvent(inv(5)); // 3

		List<List<Fact>> expParseStream = Lists.newLinkedList();
		expParseStream.add(Lists.newArrayList(new Fact(1), new Fact(2)));
		expParseStream.add(Lists.newArrayList(new Fact(3)));
		expParseStream.add(Lists.newLinkedList());
		expParseStream.add(Lists.newArrayList(new Fact(2), new Fact(3)));

		sut.write(expected, FOLDNUM);

		assertTrue(streamFile.exists());
		assertTrue(mappingFile.exists());
		assertTrue(methodsFile.exists());

		List<Event> actMapping = sut.readMapping(mappingFile
				.getAbsolutePath());
		List<Event> actMethods = sut.readMethods(methodsFile
				.getAbsolutePath());

		String actStringStream = sut.readStream(streamFile
				.getAbsolutePath());
		List<List<Fact>> actParserStream = sut.parseStream(streamFile
				.getAbsolutePath());

		assertMapping(expected.getMapping().keySet(), actMapping);
		
		assertEquals(expected.getEnclMethods(), actMethods);

		assertEquals(expected.getStream(), actStringStream);
		assertEquals(expParseStream, actParserStream);
		
		assertTrue(actParserStream.size() == actMethods.size());
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
	
	private File getMethodsFile() {
		File fileName = new File(getPath() + "/methods.txt");
		return fileName;
	}

	private File getMappingFile() {
		File fileName = new File(getPath() + "/mapping.txt");
		return fileName;
	}

	private File getStreamFile() {
		File fileName = new File(getPath() + "/stream.txt");
		return fileName;
	}

	private String getPath() {
		File path = new File(tmp.getRoot().getAbsolutePath() + "/TrainingData/fold" + FOLDNUM);
		if (!path.isDirectory()) {
			path.mkdirs();
		}
		return path.getAbsolutePath();
	}

	private static Event inv(int i) {
		return Events.newInvocation(m(i));
	}

	private static Event ctx(int i) {
		return Events.newContext(m(i));
	}

	private static Event firstCtx(int i) {
		return Events.newFirstContext(m(i));
	}

	private static IMethodName m(int i) {
		if (i == 0) {
			return Names.getUnknownMethod();
		}
		return Names.newMethod("[T,P] [T,P].m" + i + "()");
	}
}