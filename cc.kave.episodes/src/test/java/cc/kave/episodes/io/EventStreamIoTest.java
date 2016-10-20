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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.kave.episodes.model.events.Fact;

public class EventStreamIoTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();

	private static final String DUMMY_METHOD_NAME = "[You, Can] [Safely, Ignore].ThisDummyValue()";
	private static final IMethodName DUMMY_METHOD = Names
			.newMethod(DUMMY_METHOD_NAME);
	public static final Event DUMMY_EVENT = Events.newContext(DUMMY_METHOD);

	@Test
	public void happyPath() throws IOException {

		File streamFile = file();
		File mappingFile = file();
		File methodsFile = file();

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

		EventStreamIo.write(expected, streamFile.getAbsolutePath(),
				mappingFile.getAbsolutePath(), methodsFile.getAbsolutePath());

		assertTrue(streamFile.exists());
		assertTrue(mappingFile.exists());
		assertTrue(methodsFile.exists());

		List<Event> actMapping = EventStreamIo.readMapping(mappingFile
				.getAbsolutePath());
		List<Event> actMethods = EventStreamIo.readMethods(methodsFile
				.getAbsolutePath());

		String actStringStream = EventStreamIo.readStream(streamFile
				.getAbsolutePath());
		List<List<Fact>> actParserStream = EventStreamIo.parseStream(streamFile
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

	private File file() throws IOException {
		File file = tmp.newFile();
		file.delete();
		assertFalse(file.exists());
		return file;
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