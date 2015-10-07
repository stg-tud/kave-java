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

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class MappingReader {

	private File rootFolder;
	private Parser mappParser;

	@Inject
	public MappingReader(@Named("mapping") File directory, Parser parser) {
		assertTrue(directory.exists(), "Home folder does not exist");
		assertTrue(directory.isDirectory(), "Home is not a folder, but a file");
		this.rootFolder = directory;
		this.mappParser = parser;
	}

	public Map<Integer, String> reader() {
		File filePath = getFilePath();
		List<String> lines = mappParser.parse(filePath);
		Map<Integer, String> mapping = new HashMap<Integer, String>();

		for (String line : lines) {
			int idx = line.indexOf("{");
			String str = line.substring(0, idx - 2);
			int eventID = Integer.parseInt(str);
			String event = line.substring(idx);
			mapping.put(eventID, event);
		}
		return mapping;
	}

	private File getFilePath() {
		String fileName = rootFolder.getAbsolutePath() + "/eventMapping.txt";
		File file = new File(fileName);
		return file;
	}
}
