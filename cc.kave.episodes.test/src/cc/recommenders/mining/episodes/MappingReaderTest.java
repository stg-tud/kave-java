/**
 * Copyright (c) 2010, 2011 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Ervina Cergani - initial API and implementation
 */
package cc.recommenders.mining.episodes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import cc.recommenders.exceptions.AssertionException;

public class MappingReaderTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Parser mappingParser;
	private MappingReader sut;

	@Before
	public void setup() {
		mappingParser = mock(Parser.class);
		sut = new MappingReader(rootFolder.getRoot(), mappingParser);
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Home folder does not exist");
		sut = new MappingReader(new File("does not exist"), mappingParser);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Home is not a folder, but a file");
		sut = new MappingReader(file, mappingParser);
	}

	@Test
	public void readerTest() {
		StringBuilder sb = new StringBuilder();
		sb.append("1, {return type 1, declaration context 1, method name 1}\n");
		sb.append("2, {return type 2, declaration context 2, method name 2}\n");
		sb.append("3, {return type 3, declaration context 3, method name 3}");

		String content = sb.toString();

		File file = getFilePath();

		try {
			FileUtils.writeStringToFile(file, content, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		doCallRealMethod().when(mappingParser).parse(eq(file));

		Map<Integer, String> expecteds = new HashMap<Integer, String>();
		expecteds.put(1, "{return type 1, declaration context 1, method name 1}");
		expecteds.put(2, "{return type 2, declaration context 2, method name 2}");
		expecteds.put(3, "{return type 3, declaration context 3, method name 3}");

		Map<Integer, String> actuals = sut.reader();

		verify(mappingParser).parse(eq(file));

		assertEquals(expecteds, actuals);

		file.delete();
	}

	private File getFilePath() {
		String fileName = rootFolder.getRoot().getAbsolutePath() + "/eventMapping.txt";
		File file = new File(fileName);
		return file;
	}
}
