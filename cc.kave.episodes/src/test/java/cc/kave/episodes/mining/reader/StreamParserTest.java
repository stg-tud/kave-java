/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.episodes.mining.reader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Events;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.recommenders.exceptions.AssertionException;

public class StreamParserTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Mock
	private FileReader reader;
	@Mock
	private MappingParser mapper;
	
	private static final int NUMBREPOS = 3;
	
	private List<String> stream = Lists.newLinkedList();
	private List<Event> map = Lists.newLinkedList();
	
	private StreamParser sut;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		
		stream.add("1,0.000");
		stream.add("2,0.001");
		stream.add("3,0.002");
		stream.add("4,0.003");
		stream.add("5,0.004");
		stream.add("6,0.505");
		stream.add("7,1.006");
		stream.add("8,1.007");
		
		map.add(Events.newDummyEvent());
		map.add(Events.newFirstContext(m(1, 1)));
		map.add(Events.newSuperContext(m(1, 2)));
		map.add(Events.newInvocation(m(1, 3)));
		map.add(Events.newInvocation(m(1, 4)));
		map.add(Events.newInvocation(m(1, 5)));
		map.add(Events.newFirstContext(m(2, 6)));
		map.add(Events.newFirstContext(m(3, 7)));
		map.add(Events.newSuperContext(m(3, 8)));
		
		sut = new StreamParser(rootFolder.getRoot(), reader, mapper);
		
		when(reader.readFile(any(File.class))).thenReturn(stream);
		when(mapper.parse(anyInt())).thenReturn(map);
	}
	
	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Event stream folder does not exist");
		sut = new StreamParser(new File("does not exist"), reader, mapper);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Event stream is not a folder, but a file");
		sut = new StreamParser(file, reader, mapper);
	}
	
	@Test
	public void MocksAreCalled() {
		sut.parseStream(NUMBREPOS);
		
		verify(reader).readFile(any(File.class));
		verify(mapper).parse(anyInt());
	}
	
	@Test
	public void testContent() {
		Set<Set<Event>> expected = Sets.newLinkedHashSet();
		
		Set<Event> events = Sets.newLinkedHashSet();
		events.add(Events.newFirstContext(m(1, 1)));
		events.add(Events.newSuperContext(m(1, 2)));
		events.add(Events.newInvocation(m(1, 3)));
		events.add(Events.newInvocation(m(1, 4)));
		events.add(Events.newInvocation(m(1, 5)));
		expected.add(events);
		
		events = Sets.newLinkedHashSet();
		events.add(Events.newFirstContext(m(2, 6)));
		expected.add(events);
		
		events = Sets.newLinkedHashSet();
		events.add(Events.newFirstContext(m(3, 7)));
		events.add(Events.newSuperContext(m(3, 8)));
		expected.add(events);
		
		Set<Set<Event>> actuals = sut.parseStream(NUMBREPOS);
		
		assertEquals(expected, actuals);
	}
	
	private IMethodName m(int typeNum, int methodNum) {
		return MethodName.newMethodName(String.format("[R,P] [%s].m%d()", t(typeNum), methodNum));
	}

	private ITypeName t(int typeNum) {
		return TypeName.newTypeName(String.format("T%d,P", typeNum));
	}
}
