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
package cc.kave.commons.mining.reader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import cc.kave.commons.model.episodes.Method;
import cc.recommenders.exceptions.AssertionException;

public class EventStreamAsListOfMethodsParserTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private FileReader reader;
	private EventStreamAsListOfMethodsParser sut;

	@Before
	public void setup() {
		reader = mock(FileReader.class);
		sut = new EventStreamAsListOfMethodsParser(rootFolder.getRoot(), reader);
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Event stream folder does not exist!");
		sut = new EventStreamAsListOfMethodsParser(new File("does not exist"), reader);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Event stream folder is not a folder, but a file!");
		sut = new EventStreamAsListOfMethodsParser(file, reader);
	}

	@Test
	public void oneQuery() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("1,0.000\n");
		sb.append("2,0.001\n");
		sb.append("3,0.002\n");
		sb.append("4,0.003\n");
		sb.append("5,0.004\n");
		sb.append("6,0.005\n");
		sb.append("7,0.006\n");
		sb.append("7,0.007\n");
		sb.append("8,0.008\n");

		String content = sb.toString();

		File file = getFilePath();

		try {
			FileUtils.writeStringToFile(file, content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		List<Method> expected = new LinkedList<Method>();
		Method method = new Method();
		method.setMethodName("1");
		method.addStringsOfFacts("2", "3", "4", "5", "6", "7", "8");
		method.setNumberOfInvocations(7);

		expected.add(method);

		doCallRealMethod().when(reader).readFile(eq(file));

		List<Method> actual = sut.parse();

		verify(reader).readFile(file);

		assertEquals(expected, actual);
	}

	@Test
	public void twoQuery() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("1,0.000\n");
		sb.append("2,0.001\n");
		sb.append("3,0.002\n");
		sb.append("4,0.003\n");
		sb.append("5,0.004\n");
		sb.append("6,0.005\n");
		sb.append("7,0.006\n");
		sb.append("7,0.007\n");
		sb.append("8,0.008\n");

		sb.append("9,0.509\n");
		sb.append("10,0.510\n");
		sb.append("8,0.511\n");
		sb.append("11,0.512\n");

		String content = sb.toString();

		File file = getFilePath();

		try {
			FileUtils.writeStringToFile(file, content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		List<Method> expected = new LinkedList<Method>();
		Method method = new Method();
		method.setMethodName("1");
		method.addStringsOfFacts("2", "3", "4", "5", "6", "7", "8");
		method.setNumberOfInvocations(7);
		expected.add(method);

		method = new Method();
		method.setMethodName("9");
		method.addStringsOfFacts("10", "8", "11");
		method.setNumberOfInvocations(3);
		expected.add(method);

		doCallRealMethod().when(reader).readFile(eq(file));

		List<Method> actual = sut.parse();

		verify(reader).readFile(file);

		assertEquals(expected, actual);
	}

	@Test
	public void threeQuery() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("1,0.000\n");
		sb.append("2,0.001\n");
		sb.append("3,0.002\n");
		sb.append("4,0.003\n");
		sb.append("5,0.004\n");
		sb.append("6,0.005\n");
		sb.append("7,0.006\n");
		sb.append("7,0.007\n");
		sb.append("8,0.008\n");

		sb.append("9,0.509\n");
		sb.append("10,0.510\n");
		sb.append("8,0.511\n");
		sb.append("11,0.512\n");

		sb.append("5,1.013\n");

		String content = sb.toString();

		File file = getFilePath();

		try {
			FileUtils.writeStringToFile(file, content);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		List<Method> expected = new LinkedList<Method>();
		Method method = new Method();
		method.setMethodName("1");
		method.addStringsOfFacts("2", "3", "4", "5", "6", "7", "8");
		method.setNumberOfInvocations(7);
		expected.add(method);

		method = new Method();
		method.setMethodName("9");
		method.addStringsOfFacts("10", "8", "11");
		method.setNumberOfInvocations(3);
		expected.add(method);

		method = new Method();
		method.setMethodName("5");
		method.setNumberOfInvocations(0);
		expected.add(method);

		doCallRealMethod().when(reader).readFile(eq(file));

		List<Method> actual = sut.parse();

		verify(reader).readFile(file);

		assertEquals(expected, actual);
	}

	private File getFilePath() {
		File fileName = new File(rootFolder.getRoot().getAbsolutePath() + "/eventstreamModified.txt");
		return fileName;
	}
}
