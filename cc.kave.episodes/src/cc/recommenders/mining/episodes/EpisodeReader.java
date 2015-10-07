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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Episode;

public class EpisodeReader {

	private File rootFolder;
	private Parser episodeParser;

	@Inject
	public EpisodeReader(@Named("episode") File directory, Parser parser) {
		assertTrue(directory.exists(), "Frequent episode folder does not exist");
		assertTrue(directory.isDirectory(), "Frequent episode folder is not a folder, but a file");
		this.rootFolder = directory;
		this.episodeParser = parser;
	}

	public Map<Integer, List<Episode>> read() {

		File filePath = getFilePath();

		List<String> lines = episodeParser.parse(filePath);

		Map<Integer, List<Episode>> results = new HashMap<Integer, List<Episode>>();

		int totalLines = lines.size();
		List<Episode> episodeList = new LinkedList<Episode>();

		int numNodes = 0;
		int lineNumber = 0;

		for (String line : lines) {
			String[] rawValues = line.split("\\s+");
			if (rawValues.length == 4 && rawValues[1].equalsIgnoreCase("Episodes")) {
				if (!episodeList.isEmpty()) {
					results.put(numNodes, episodeList);
				}
				if (Integer.parseInt(rawValues[3]) > 0) {
					String[] nodeString = rawValues[0].split("-");
					numNodes = Integer.parseInt(nodeString[0]);
					episodeList = new LinkedList<Episode>();
				}
			} else {
				String[] episodeElements = line.split(":");
				Episode episode = new Episode();
				episode.setNumEvents(numNodes);
				int episodeFrequency = Integer.parseInt(episodeElements[1].trim());
				episode.setFrequency(episodeFrequency);
				for (int i = 0; i < numNodes; i++) {
					episode.addFact(rawValues[i]);
				}
				if (rawValues[rawValues.length - 1].contains(",")) {
					String[] order = rawValues[rawValues.length - 1].split(",");
					for (int i = 0; i < order.length; i++) {
						episode.addFact(order[i]);
					}
				}
				episodeList.add(episode);
			}
			lineNumber++;
			if ((lineNumber == totalLines) && (!episodeList.isEmpty())) {
				results.put(numNodes, episodeList);
			}
		}
		return results;
	}

	private File getFilePath() {
		String fileName = rootFolder.getAbsolutePath() + "/output.txt";
		File file = new File(fileName);
		return file;
	}
}
