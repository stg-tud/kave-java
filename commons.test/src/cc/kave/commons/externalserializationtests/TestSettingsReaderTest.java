/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package cc.kave.commons.externalserializationtests;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestSettingsReaderTest {
	private File testSettingsFile;

	private static final String expectedSerializedType = "java.io.File";

	@Before
	public void createTestSettingsFile() throws IOException {
		testSettingsFile = File.createTempFile("Settings", ".ini", null);
		
		try (PrintWriter settingsFileWriter = new PrintWriter(testSettingsFile)){
			settingsFileWriter.println("[CSharp]");
			settingsFileWriter.println("SerializedType=SomeCSharpType");
			settingsFileWriter.println("[Java]");
			settingsFileWriter.println("SerializedType=" + expectedSerializedType);
		}
	}

	@After
	public void deleteTestSettingsFile() {
		if (testSettingsFile.exists()) {
			testSettingsFile.delete();
		}
	}

	@Test
	public void shouldReadSerializedTypeFromSection() throws FileNotFoundException, IOException {
		HashMap<ExternalTestSetting, String> actualSetttings = TestSettingsReader
				.readSection(testSettingsFile, "Java");
		assertEquals(expectedSerializedType, actualSetttings.get(ExternalTestSetting.SerializedType));
	}
}
