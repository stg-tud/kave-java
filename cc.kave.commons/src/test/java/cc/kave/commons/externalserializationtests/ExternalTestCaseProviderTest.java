/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package cc.kave.commons.externalserializationtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ExternalTestCaseProviderTest {

	private static final String expectedFirstName = "TestSuite\\TestCases\\FirstTest";
	private static final String expectedFirstInput = "firstInputContent";
	private static final String expectedCompact = "expectedCompactContent";
	private static final String expectedFormatted = "expectedFormattedContent";
	private static final Class<?> expectedSerializedType = ExternalTestCaseProviderTest.class;

	private Path baseDirectory;
	private Path settingsFile;
	private Path expectedFormattedFile;

	@Before
	public void buildTestCaseStructure() throws IOException {
		baseDirectory = Files.createTempDirectory(null);

		Path testSuiteDir = Files.createDirectory(baseDirectory.resolve("TestSuite"));
		Path testCasesDir = Files.createDirectory(testSuiteDir.resolve("TestCases"));

		settingsFile = testCasesDir.resolve("settings.ini");
		expectedFormattedFile = testCasesDir.resolve("expected-formatted.json");

		Files.write(testCasesDir.resolve("FirstTest.json"), expectedFirstInput.getBytes());
		Files.write(testCasesDir.resolve("SecondTest.json"), "Some input".getBytes());
		Files.write(testCasesDir.resolve("expected-compact.json"), expectedCompact.getBytes());
		Files.write(expectedFormattedFile, expectedFormatted.getBytes());

		try (PrintWriter settingsFileWriter = new PrintWriter(settingsFile.toFile())) {
			settingsFileWriter.println("[CSharp]");
			settingsFileWriter.println("SerializedType=SomeCSharpType");
			settingsFileWriter.println("[Java]");
			settingsFileWriter.println("SerializedType=" + expectedSerializedType.getName());
		}
	}

	@After
	public void beleteTmpFolders() throws IOException {
		File baseDir = baseDirectory.toFile();
		if (baseDir.exists()) {
			FileUtils.deleteDirectory(baseDir);
		}
	}

	@Test
	public void shouldFindTestCasesRecursively() throws ClassNotFoundException, IOException {
		List<TestCase[]> testCases = ExternalTestCaseProvider.getTestCases(baseDirectory);
		assertEquals(2, testCases.size());
	}

	@Test
	public void shouldFindInput() throws ClassNotFoundException, IOException {
		TestCase firstTestCase = getFirstTestCase();
		assertEquals(expectedFirstInput, firstTestCase.input);
	}

	@Test
	public void shouldFindCompactExpected() throws ClassNotFoundException, IOException {
		TestCase firstTestCase = getFirstTestCase();
		assertEquals(expectedCompact, firstTestCase.expectedCompact);
	}

	@Test
	public void shouldFindFormattedExpected() throws ClassNotFoundException, IOException {
		TestCase firstTestCase = getFirstTestCase();
		assertEquals(expectedFormatted, firstTestCase.expectedFormatted);
	}

	@Ignore
	@Test
	public void shouldFindName() throws ClassNotFoundException, IOException {
		TestCase firstTestCase = getFirstTestCase();
		assertEquals(expectedFirstName, firstTestCase.name);
	}

	private TestCase getFirstTestCase() throws ClassNotFoundException, IOException {
		return ExternalTestCaseProvider.getTestCases(baseDirectory).get(0)[0];
	}

	@Test
	public void shouldFindSerializedType() throws ClassNotFoundException, IOException {
		TestCase firstTestCase = getFirstTestCase();
		assertEquals(expectedSerializedType, firstTestCase.serializedType);
	}

	@Test
	public void settingsFileShouldBeOptional() throws IOException, ClassNotFoundException {
		Files.delete(settingsFile);
		TestCase firstTestCase = getFirstTestCase();
		assertEquals(Object.class, firstTestCase.serializedType);
	}

	@Test
	public void expectedFormattedFileShouldBeOptional() throws IOException, ClassNotFoundException {
		Files.delete(expectedFormattedFile);
		TestCase firstTestCase = getFirstTestCase();
		assertNull(firstTestCase.expectedFormatted);
	}
}
