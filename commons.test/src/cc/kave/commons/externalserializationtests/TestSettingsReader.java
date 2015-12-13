/**
 * Copyright (c) 2011-2013 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package cc.kave.commons.externalserializationtests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;

public class TestSettingsReader {
	public static HashMap<ExternalTestSetting, String> readSection(File settingsFilePath, String section)
			throws FileNotFoundException, IOException {
		HashMap<ExternalTestSetting, String> settings = new HashMap<ExternalTestSetting, String>();

		boolean inSection = false;

		List<String> lines = Files.readAllLines(settingsFilePath.toPath());
		for (String currentLine : lines) {
			if (inSection) {
				if (currentLine.startsWith("[") && currentLine.endsWith("]")) {
					break;
				}

				ExternalTestSetting setting = ExternalTestSetting
						.valueOf(currentLine.substring(0, currentLine.indexOf("=")));
				String value = currentLine.substring(currentLine.indexOf("=") + 1, currentLine.length());

				settings.put(setting, value);
			} else {
				if (currentLine.equals("[" + section + "]")) {
					inSection = true;
				}
			}
		}

		return settings;
	}
}
