/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package cc.kave.commons.externalserializationtests;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeThat;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import cc.kave.commons.utils.json.JsonUtils;

@Ignore
@RunWith(Parameterized.class)
public class SerializationTestSuite {
	public static final Path TestCasesDirectory = Paths.get(System.getProperty("user.dir"), "src", "cc", "kave",
			"commons", "externalserializationtests", "Data");

	@Parameters(name = "{0}")
	public static Collection<TestCase> testCases() throws ClassNotFoundException, IOException {
		return ExternalTestCaseProvider.getTestCases(TestCasesDirectory);
	}

	private final TestCase testCase;

	public SerializationTestSuite(TestCase testCase) {
		this.testCase = testCase;
	}

	@Test
	public void sringEquality_Compact() {
		String actual = JsonUtils.toJson(JsonUtils.fromJson(testCase.input, testCase.serializedType));
		assertEquals(testCase.expectedCompact, actual);
	}

	@Test
	public void objectEquality_Compact() {
		Object actual = JsonUtils.fromJson(testCase.input, testCase.serializedType);
		Object expected = JsonUtils.fromJson(testCase.expectedCompact, testCase.serializedType);
		assertEquals(expected, actual);
	}

	@Test
	@Ignore("No toFormattedJson available")
	public void stringEquality_Formatted() {
		assumeThat("No expectedFormatted", testCase.expectedFormatted, is(nullValue()));
		// TODO: implement this when toFormattedJson is implemented
	}

	@Test
	@Ignore("No toFormattedJson available")
	public void objectEquality_Formatted() {
		assumeThat("No expectedFormatted", testCase.expectedFormatted, is(nullValue()));
		// TODO: implement this when toFormattedJson is implemented
	}

	@Test
	@Ignore("No toFormattedJson available")
	public void assertEqualityOfExpectationFiles() {
		assumeThat("No expectedFormatted", testCase.expectedFormatted, is(nullValue()));
		// TODO: implement this when toFormattedJson is implemented
	}
}
