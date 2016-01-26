/*
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.recommenders.mining.calls.pbn;

import static cc.recommenders.mining.calls.pbn.PBNModelConstants.PATTERN_TITLE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cc.recommenders.io.Directory;
import cc.recommenders.io.IoUtils;
import smile.Network;

public class SmileUtilsTest {

	private String tempFile;
	private String tempFileName;
	private Directory parentDir;
	private IoUtils io;

	private SmileUtils sut;

	@Before
	public void setup() throws IOException {
		Path p = Files.createTempFile("prefix", ".xdsl");
		File file = p.toFile();
		tempFile = file.getAbsolutePath();
		tempFileName = file.getName();

		parentDir = mock(Directory.class);
		when(parentDir.readContent(tempFileName + ".xdsl")).thenReturn("xyz");

		io = mock(IoUtils.class);
		when(io.getRandomTempFile()).thenReturn(tempFile);
		when(io.getParentDirectory(tempFile + ".xdsl")).thenReturn(parentDir);

		sut = new SmileUtils(io);
	}

	@After
	public void teardown() {
		new File(tempFile).delete();
	}

	@Test
	public void ioIsUsed() throws IOException {
		sut.toString(new Network());

		verify(io).getRandomTempFile();
		verify(io).getParentDirectory(tempFile + ".xdsl");
		verify(parentDir).readContent(tempFileName + ".xdsl");
		verify(parentDir).delete(tempFileName + ".xdsl");
	}

	@Test
	public void correctContent() {
		assertEquals("xyz", sut.toString(new Network()));
	}

	@Test
	public void numPatterns() {
		Network network = new Network();

		int handle = network.addNode(Network.NodeType.Cpt);
		network.setNodeId(handle, PATTERN_TITLE);
		network.setNodeName(handle, PATTERN_TITLE);

		String[] states = new String[] { "a", "b", "c" };
		double[] probabilities = new double[] { 0.6, 0.3, 0.1 };

		for (String s : states) {
			network.addOutcome(handle, s);
		}
		network.deleteOutcome(handle, "State0");
		network.deleteOutcome(handle, "State1");

		network.setNodeDefinition(handle, probabilities);

		assertEquals(3, sut.getNumPatterns(network));
	}
}