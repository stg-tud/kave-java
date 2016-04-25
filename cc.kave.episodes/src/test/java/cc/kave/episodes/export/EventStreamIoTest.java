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

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Lists;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Events;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;

public class EventStreamIoTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	
	private static final String DUMMY_METHOD_NAME = "[You, Can] [Safely, Ignore].ThisDummyValue()";
	private static final IMethodName DUMMY_METHOD = MethodName.newMethodName(DUMMY_METHOD_NAME);
	public static final Event DUMMY_EVENT = Events.newContext(DUMMY_METHOD);
	
	@Test
	public void happyPath() throws IOException {

		File a = file();
		File b = file();

		List<Event> stream = Lists.newArrayList(ctx(1), inv(2), inv(3), ctx(4), inv(5), ctx(4), ctx(1), inv(5));
		List<Event> mapping = Lists.newArrayList(DUMMY_EVENT, ctx(1), ctx(4), inv(5));
		String expectedTxt = "0,0.000\n" + //
				"1,0.501\n" + //
				"2,1.002\n" + //
				"3,1.003\n" + //
				"2,1.504\n" + //
				"1,2.005\n" + //
				"3,2.006\n";

		EventStreamIo.write(stream, a.getAbsolutePath(), b.getAbsolutePath());

		assertTrue(a.exists());
		assertTrue(b.exists());

		List<Event> actuals = EventStreamIo.readMapping(b.getAbsolutePath());
		String actualTxt = FileUtils.readFileToString(a);
		assertEquals(mapping, actuals);
		assertEquals(expectedTxt, actualTxt);
	}
//
//	@Test
//	public void mappingContainsUniqueEvents() throws IOException {
//		
//		File a = file();
//		File b = file();
//
//		List<Event> stream = Lists.newArrayList(inv(1), inv(1), inv(1));
//		List<Event> expected = Lists.newArrayList(DUMMY_EVENT, inv(1));
//		String expectedTxt = "0,0.000\n" + //
//				"1,0.001\n" + //
//				"1,0.002\n" + //
//				"1,0.003\n";
//
//		EventStreamIo.write(stream, a.getAbsolutePath(), b.getAbsolutePath());
//
//		assertTrue(a.exists());
//		assertTrue(b.exists());
//
//		List<Event> actuals = EventStreamIo.readMapping(a.getAbsolutePath());
//		String actualTxt = FileUtils.readFileToString(b);
//		assertEquals(expected, actuals);
//		assertEquals(expectedTxt, actualTxt);
//	}

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

	private static IMethodName m(int i) {
		return MethodName.newMethodName("[T,P] [T,P].m" + i + "()");
	}
}