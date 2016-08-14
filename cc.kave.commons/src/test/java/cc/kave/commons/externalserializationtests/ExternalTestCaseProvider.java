/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package cc.kave.commons.externalserializationtests;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.io.FilenameUtils;

import com.google.common.collect.Lists;

public class ExternalTestCaseProvider {
	private static final String settingsFile = "settings.ini";
	private static final String expectedCompactFile = "expected-compact.json";
	private static final String expectedFormattedFile = "expected-formatted.json";

	@Nonnull
	public static List<TestCase[]> getTestCases(Path baseDirectory) throws ClassNotFoundException, IOException {
		if (!baseDirectory.toFile().exists()) {
			return Lists.newLinkedList();
		}
		return recursiveGetTestCases(baseDirectory.toFile(), baseDirectory.toString());
	}

	private static List<TestCase[]> recursiveGetTestCases(File currentDirectory, String rootPrefix)
			throws ClassNotFoundException, IOException {
		List<TestCase[]> testCases = getTestCasesInCurrentFolder(currentDirectory, rootPrefix);

		for (File subdirectory : getSubdirectories(currentDirectory)) {
			testCases.addAll(recursiveGetTestCases(subdirectory, rootPrefix));
		}

		return testCases;
	}

	private static List<TestCase[]> getTestCasesInCurrentFolder(File currentDirectory, String rootPrefix)
			throws ClassNotFoundException, IOException {
		List<TestCase[]> testCases = new LinkedList<TestCase[]>();

		Class<?> serializedType = Object.class;
		String expectedCompact = null;
		String expectedFormatted = null;

		for (File file : currentDirectory.listFiles()) {
			if (file.getName().equals(settingsFile)) {
				String className;
				className = TestSettingsReader.readSection(file, "Java").get(ExternalTestSetting.SerializedType);
				serializedType = Class.forName(className);
			}

			if (file.getName().equals(expectedCompactFile)) {
				expectedCompact = new String(Files.readAllBytes(file.toPath()));
			}

			if (file.getName().equals(expectedFormattedFile)) {
				expectedFormatted = new String(Files.readAllBytes(file.toPath()));
			}
		}

		if (expectedCompact == null) {
			return new LinkedList<TestCase[]>();
		}

		for (File file : currentDirectory.listFiles()) {
			boolean isInputFile = !file.getName().equals(settingsFile) && !file.getName().equals(expectedCompactFile)
					&& !file.getName().equals(expectedFormattedFile);
			if (!isInputFile) {
				continue;
			}

			String input = new String(Files.readAllBytes(file.toPath()));
			testCases.add(new TestCase[] { new TestCase(getTestCaseName(file.getAbsolutePath(), rootPrefix),
					serializedType, input, expectedCompact, expectedFormatted) });
		}

		return testCases;
	}

	private static File[] getSubdirectories(File currentDirectory) {
		return currentDirectory.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
	}

	private static String getTestCaseName(String fileName, String rootPrefix) {
		String fileNameWithoutExtension = FilenameUtils.removeExtension(fileName);
		return fileNameWithoutExtension.substring((rootPrefix + "\\").length());
	}
}
