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
package cc.kave.episodes.io;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

import cc.kave.episodes.io.FileReader;
import cc.kave.episodes.io.StreamParser;
import cc.kave.episodes.model.events.Fact;
import cc.kave.exceptions.AssertionException;

public class StreamParserTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Mock
	private FileReader reader;
	
	private static final int NUMBREPOS = 3;
	
	private List<String> stream = Lists.newLinkedList();
	
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
		
		sut = new StreamParser(rootFolder.getRoot(), reader);
		
		when(reader.readFile(any(File.class))).thenReturn(stream);
	}
	
	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Event stream folder does not exist");
		sut = new StreamParser(new File("does not exist"), reader);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Event stream is not a folder, but a file");
		sut = new StreamParser(file, reader);
	}
	
	@Test
	public void MocksAreCalled() {
		sut.parse(NUMBREPOS);
		
		verify(reader).readFile(any(File.class));
	}
	
	@Test
	public void testContent() {
		List<List<Fact>> expected = new LinkedList<>();
		List<Fact> method = new LinkedList<>();
		
		method.add(new Fact(1));
		method.add(new Fact(2));
		method.add(new Fact(3));
		method.add(new Fact(4));
		method.add(new Fact(5));
		expected.add(method);
		
		method = new LinkedList<>();
		method.add(new Fact(6));
		expected.add(method);
		
		method = new LinkedList<>();
		method.add(new Fact(7));
		method.add(new Fact(8));
		expected.add(method);
		
		List<List<Fact>> actuals = sut.parse(NUMBREPOS);
		
		assertEquals(expected, actuals);
	}
}
