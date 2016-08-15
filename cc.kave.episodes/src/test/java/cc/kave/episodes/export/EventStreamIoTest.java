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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Events;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.model.EventStream;

public class EventStreamIoTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();

	private static final String DUMMY_METHOD_NAME = "[You, Can] [Safely, Ignore].ThisDummyValue()";
	private static final IMethodName DUMMY_METHOD = Names.newMethod(DUMMY_METHOD_NAME);
	public static final Event DUMMY_EVENT = Events.newContext(DUMMY_METHOD);
	public static final int STREAMIDX = 8;

	@Test
	public void happyPath() throws IOException {

		File a = file();
		File b = file();

		EventStream expected = new EventStream();
		expected.addEvent(DUMMY_EVENT);
		expected.addEvent(ctx(1));
		expected.addEvent(inv(2));
		expected.addEvent(unknown());
		expected.addEvent(inv(5));
		expected.addEvent(unknown());
		expected.addEvent(unknown());
		expected.addEvent(inv(2));
		expected.addEvent(ctx(1));
		expected.addEvent(inv(5));

		EventStreamIo.write(expected, a.getAbsolutePath(), b.getAbsolutePath());

		assertTrue(a.exists());
		assertTrue(b.exists());

		List<Event> actualsMapping = EventStreamIo.readMapping(b.getAbsolutePath());
		String actualTxt = FileUtils.readFileToString(a);
		assertMapping(expected.getMapping().keySet(), actualsMapping);
		assertEquals(expected.getStream(), actualTxt);
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

	private static Event unknown() {
		return Events.newContext(Names.getUnknownMethod());
	}

	private static IMethodName m(int i) {
		return Names.newMethod("[T,P] [T,P].m" + i + "()");
	}
}