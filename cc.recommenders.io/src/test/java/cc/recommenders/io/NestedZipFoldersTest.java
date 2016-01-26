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
package cc.recommenders.io;

import static cc.recommenders.assertions.Asserts.assertFalse;
import static cc.recommenders.assertions.Asserts.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class NestedZipFoldersTest {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	private Directory rootDir;

	private NestedZipFolders<String> sut;

	@Before
	public void setup() {
		String tempFileName = tempFolder.getRoot().getAbsolutePath();
		rootDir = new Directory(tempFileName);
		sut = new NestedZipFolders<String>(rootDir, String.class);
	}

	@Test
	public void getUrl() throws MalformedURLException {
		String folder = tempFolder.getRoot().getAbsolutePath() + "/";
		URL actual = sut.getUrl();
		URL expected = new URL("file:" + folder);
		assertEquals(expected, actual);
	}

	@Test
	public void findAllMarkers() throws IOException {
		rootDir.writeContent("a", ".zipfolder");
		Directory dirB = rootDir.createDirectory("b");
		dirB.write("b", ".zipfolder");
		dirB.write("x", "dontcare");

		Set<String> actuals = sut.findKeys();
		Set<String> expecteds = Sets.newHashSet("a", "b");
		assertEquals(expecteds, actuals);
	}

	@Test
	public void canAlsoReadComplexKeys() throws IOException {
		NestedZipFolders<TestClass> sut = new NestedZipFolders<TestClass>(rootDir, TestClass.class);

		rootDir.writeContent("{\"items\":[\"a\"]}", ".zipfolder");

		Set<TestClass> actuals = sut.findKeys();
		Set<TestClass> expecteds = Sets.newHashSet(TestClass.create("a"));
		assertEquals(expecteds, actuals);
	}

	@Test
	public void hasZips_unknown() throws IOException {
		assertFalse(sut.hasZips("a"));
	}

	@Test
	public void hasZips_noZips() throws IOException {
		Directory dirA = rootDir.createDirectory("a");
		dirA.write("a", ".zipfolder");
		assertFalse(sut.hasZips("a"));
	}

	@Test
	public void hasZips_happyPath() throws IOException {
		Directory dirA = rootDir.createDirectory("a");
		dirA.write("a", ".zipfolder");
		dirA.getWritingArchive("0.zip").close();

		assertTrue(sut.hasZips("a"));
	}

	@Test
	public void readAll_unknown() throws IOException {
		List<String> actuals = sut.readAllZips("a", String.class);
		List<String> expecteds = Lists.newLinkedList();
		assertEquals(expecteds, actuals);
	}

	@Test
	public void readAll_noZips() throws IOException {
		Directory dirA = rootDir.createDirectory("a");
		dirA.write("a", ".zipfolder");

		List<String> actuals = sut.readAllZips("a", String.class);
		List<String> expecteds = Lists.newLinkedList();
		assertEquals(expecteds, actuals);
	}

	@Test
	public void readAll_happyPath() throws IOException {
		Directory dirA = rootDir.createDirectory("a");
		dirA.write("a", ".zipfolder");

		WritingArchive wa1 = dirA.getWritingArchive("0.zip");
		wa1.add("1-1");
		wa1.add("1-2");
		wa1.close();

		WritingArchive wa2 = dirA.getWritingArchive("1.zip");
		wa2.add("2-1");
		wa2.close();

		List<String> actuals = sut.readAllZips("a", String.class);
		List<String> expecteds = Lists.newArrayList("2-1", "1-1", "1-2");
		assertEquals(expecteds, actuals);
	}

	public static class TestClass {
		public List<String> items = Lists.newLinkedList();

		public static TestClass create(String... args) {
			TestClass t = new TestClass();
			for (String item : args) {
				t.items.add(item);
			}
			return t;
		}

		@Override
		public boolean equals(Object obj) {
			return EqualsBuilder.reflectionEquals(this, obj);
		}

		@Override
		public int hashCode() {
			return HashCodeBuilder.reflectionHashCode(this);
		}
	}
}