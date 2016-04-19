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
package cc.recommenders.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.google.common.collect.Lists;

import cc.kave.commons.utils.json.JsonUtils;
import cc.recommenders.exceptions.AssertionException;

public class ZipFolderLRUCacheTest {

	@Rule
	public TemporaryFolder tmp = new TemporaryFolder();
	private File _root;
	private ZipFolderLRUCache<String> _sut;

	@Before
	public void setUp() throws IOException {
		_root = tmp.newFolder("data");
		_sut = new ZipFolderLRUCache<String>(_root.getAbsolutePath(), 2);
	}

	@Test
	public void sizeAndCache() {
		assertEquals(0, _sut.size());
		assertFalse(_sut.isCached("a"));

		_sut.getArchive("a");

		assertEquals(1, _sut.size());
		assertTrue(_sut.isCached("a"));
	}

	@Test
	public void filesAreCreatedInCorrectSubfolders() throws IOException {
		_sut.getArchive(relFile("a"));
		_sut.getArchive(relFile("b", "b"));
		_sut.getArchive(relFile("c", "c", "c"));
		_sut.getArchive(relFile("d", "d", "d", "d"));

		_sut.close();

		assertTrue(_root.exists());
		assertTrue(file(_root, "a", ".zipfolder").exists());
		assertTrue(file(_root, "b", "b", ".zipfolder").exists());
		assertTrue(file(_root, "c", "c", "c", ".zipfolder").exists());
		assertTrue(file(_root, "d", "d", "d", "d", ".zipfolder").exists());
	}

	@Test
	public void doubleSlashIsNotAnIssue() throws IOException {
		_sut.getArchive("La//a");

		_sut.close();

		assertTrue(_root.exists());
		assertTrue(file(_root, "La", "a", ".zipfolder").exists());
	}

	@Test
	public void keyIsPassedAsMetaDataWithoutReplacement() throws IOException {
		String key = "La/A.1/_!";

		_sut.getArchive(key);
		_sut.close();

		File metaFile = file(_root, "La", "A", "1", "__", ".zipfolder");
		String actual = FileUtils.readFileToString(metaFile);
		assertEquals(JsonUtils.toJson(key), actual);
	}

	@Test
	public void complexKeysArePossibleThoughNotRecommended() throws IOException {
		ZipFolderLRUCache<List<String>> sut = new ZipFolderLRUCache<List<String>>(_root.getAbsolutePath(), 2);
		List<String> key = Lists.newArrayList("a", "b");

		sut.getArchive(key);
		sut.close();

		File metaFile = file(_root, "[a,b]", ".zipfolder");
		String actual = FileUtils.readFileToString(metaFile);
		String expected = JsonUtils.toJson(key);
		assertEquals(expected, actual);
	}

	@Test
	public void replacementInKeysWorks() {
		String a = "a,.+-\\_$()[]{}:*?\"<>|";
		String e = "a,\\+-\\_$()[]{}______\\0.zip";
		try (IWritingArchive wa = _sut.getArchive(a)) {
			wa.add("something");
		}

		assertTrue(_root.exists());
		assertTrue(file(_root, e).exists());
	}

	@Test
	public void cacheDoesNotGrowLargerThanCapacity() {
		_sut.getArchive("a");
		_sut.getArchive("b");
		_sut.getArchive("c");

		assertEquals(2, _sut.size());
	}

	@Test
	public void existingFilesAreNotOverwritten() throws IOException {
		_sut.close();

		File metaFile = file(_root, "a", ".zipfolder");
		File zip0 = file(_root, "a", "0.zip");
		File zip1 = file(_root, "a", "1.zip");

		// setup fake data from previous run
		FileUtils.writeStringToFile(metaFile, "test");
		try (WritingArchive testZip = new WritingArchive(zip0)) {
			testZip.add("test");
		}

		// new initialization
		_sut = new ZipFolderLRUCache<String>(_root.getAbsolutePath(), 2);
		IWritingArchive a = _sut.getArchive("a");
		a.add("x");
		_sut.close();

		assertTrue(metaFile.exists());
		assertTrue(zip0.exists());
		assertTrue(zip1.exists());

		assertEquals("test", FileUtils.readFileToString(metaFile));
		assertZipContent(zip0, "test");
		assertZipContent(zip1, "x");
	}

	private File file(File dir, String... parts) {
		return Paths.get(dir.getAbsolutePath(), parts).toFile();
	}

	private String relFile(String... tokens) {
		return String.join(File.separator, tokens);
	}

	private void assertZipContent(File file, String... expectedsArr) {
		try {
			IReadingArchive ra = new ReadingArchive(file);
			List<String> expecteds = Lists.newArrayList(expectedsArr);
			List<String> actuals = Lists.newArrayList();
			while (ra.hasNext()) {
				actuals.add(ra.getNext(String.class));
			}
			assertEquals(expecteds, actuals);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void leastRecentlyUsedKeyIsRemoved() {
		_sut.getArchive("a");
		_sut.getArchive("b");
		_sut.getArchive("c");

		assertFalse(_sut.isCached("a"));
		assertTrue(_sut.isCached("b"));
		assertTrue(_sut.isCached("c"));
	}

	@Test
	public void refreshingWorks() {
		_sut.getArchive("a");
		_sut.getArchive("b");
		_sut.getArchive("a");
		_sut.getArchive("c");

		assertFalse(_sut.isCached("b"));
		assertTrue(_sut.isCached("a"));
		assertTrue(_sut.isCached("c"));
	}

	@Test
	public void cacheRemoveClosesOpenArchive() throws IOException {
		File expectedFileName = file(_root, "a", "0.zip");

		IWritingArchive wa = _sut.getArchive("a");
		wa.add("something");
		_sut.getArchive("b");

		assertFalse(expectedFileName.exists());
		_sut.getArchive("c");
		assertTrue(expectedFileName.exists());
	}

	@Test
	public void closeClosesAllOpenArchives() throws IOException {
		IWritingArchive wa = _sut.getArchive("a");
		wa.add("something");

		_sut.close();

		assertFalse(_sut.isCached("a"));
		assertEquals(0, _sut.size());
		assertTrue(file(_root, "a", "0.zip").exists());
	}

	@SuppressWarnings("resource")
	@Test(expected = AssertionException.class)
	public void directoryHasToExist() {
		new ZipFolderLRUCache<String>("/does/not/exist/", 10);
	}

	@SuppressWarnings("resource")
	@Test(expected = AssertionException.class)
	public void directoryMustNotBeAFile() {
		fail("TODO create file first");
		new ZipFolderLRUCache<String>("", 10);
	}

	@SuppressWarnings("resource")
	@Test(expected = AssertionException.class)
	public void capacityMustBeLargerThanZero() {
		new ZipFolderLRUCache<String>(_root.getAbsolutePath(), 0);
	}
}