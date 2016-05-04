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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

public class TypeTestLoader {

	private static final Path TESTDIR = Paths.get(System.getProperty("user.dir"), "src", "test", "java", "cc", "kave",
			"commons", "model", "names", "csharp", "parser", "data");
	private static final Path VALID_TYPENAMES = TESTDIR.resolve("valid-typenames.tsv");
	private static final Path INVALID_TYPENAMES = TESTDIR.resolve("invalid-typenames.tsv");
	private static final Path INVALID_METHODNAMES = TESTDIR.resolve("invalid-methodnames.tsv");
	private static final Path VALID_METHODNAMES = TESTDIR.resolve("valid-methodnames.tsv");

	public static List<String> loadTestFile(Path dir) {
		List<String> testFile = Lists.newArrayList();
		try {
			testFile = Lists.newArrayList(FileUtils.readFileToString(dir.toFile()).replace("\r", "").split("\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return testFile.stream().filter(s -> !s.equals("") && !s.startsWith("#") && !s.startsWith("identifier"))
				.collect(Collectors.toList());
	}

	public static List<TypeNameTestCase> loadTypeNameTestCases(Path dir) {
		List<TypeNameTestCase> testCases = Lists.newArrayList();
		List<String> lines = loadTestFile(dir);
		for (String s : lines) {
			List<String> fields = Lists.newArrayList(s.split("\t"));
			TypeNameTestCase t = new TypeNameTestCase(fields.get(0), fields.get(1), fields.get(2));
			testCases.add(t);
		}
		assert (lines.size() == testCases.size());
		return testCases;
	}

	protected static List<MethodNameTestCase> loadMethodNameTestCases(Path dir) {
		List<MethodNameTestCase> testCases = Lists.newArrayList();
		List<String> lines = loadTestFile(dir);
		for (String s : lines) {
			String[] fields = s.split("\t");
			MethodNameTestCase t = new MethodNameTestCase(fields[0], fields[1], fields[2], fields[3],
					getBoolean(fields[4]), getBoolean(fields[5]), getList(fields[6]), getList(fields[7]));
			testCases.add(t);
		}
		return testCases;
	}

	private static List<String> getList(String string) {
		return string.equals("") ? Lists.newArrayList() : Lists.newArrayList(string.split(";"));
	}

	private static boolean getBoolean(String string) {
		return string.equals("t");
	}

	public static List<TypeNameTestCase> validTypeNames() {
		return loadTypeNameTestCases(VALID_TYPENAMES);
	}

	public static List<MethodNameTestCase> validMethodNames() {
		return loadMethodNameTestCases(VALID_METHODNAMES);
	}

	public static List<String> invalidTypeNames() {
		return loadTestFile(INVALID_TYPENAMES);
	}

	public static List<String> invalidMethodNames() {
		return loadTestFile(INVALID_METHODNAMES);
	}

}
