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
package cc.kave.commons.model.names.csharp.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TypeTestLoaderTest {

	private static final String expectedFirstCsv = "identifier\tnamespace\tassembly\nT,P,0.0.0.0\t???\tP,0.0.0.0\n"
			+ "T\t???\t???";

	private Path dir;
	private Path firstTestCsv;

	@Before
	public void buildTestDirectory() throws IOException {
		dir = Files.createTempDirectory(null);
		firstTestCsv = dir.resolve("test.csv");
		Files.write(firstTestCsv, expectedFirstCsv.getBytes());
	}

	@After
	public void beleteTmpFolders() throws IOException {
		File baseDir = dir.toFile();
		if (baseDir.exists()) {
			FileUtils.deleteDirectory(baseDir);
		}
	}

	@Test
	public void loadFile() {
		List<String> testFile = TypeTestLoader.loadTestFile(firstTestCsv);
		assertEquals(2, testFile.size());
		assertEquals("T,P,0.0.0.0\t???\tP,0.0.0.0", testFile.get(0));
		assertEquals("T\t???\t???", testFile.get(1));
	}

	@Test
	public void loadTypeNameTestCases() {
		List<TypeNameTestCase> actual = TypeTestLoader.loadTypeNameTestCases(firstTestCsv);
		assertEquals(2, actual.size());
		TypeNameTestCase actualTest = actual.get(0);
		assertEquals("T,P,0.0.0.0", actualTest.getIdentifier());
		assertEquals("???", actualTest.getNamespace());
		assertEquals("P,0.0.0.0", actualTest.getAssembly());
		TypeNameTestCase actualTest2 = actual.get(1);
		assertEquals("T", actualTest2.getIdentifier());
		assertEquals("???", actualTest2.getNamespace());
		assertEquals("???", actualTest2.getAssembly());
	}
}
