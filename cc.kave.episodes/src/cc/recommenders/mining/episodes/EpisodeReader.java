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
		assertTrue(directory.exists(), "Home folder does not exist");
		assertTrue(directory.isDirectory(), "Home is not a folder, but a file");
		this.rootFolder = directory;
		this.episodeParser = parser;
	}
	
	public Map<Integer, List<Episode>> read() {
		
		File filePath = getFilePath();
		
		List<String> lines = episodeParser.parse(filePath);
		
		Map<Integer, List<Episode>> results = new HashMap<Integer,List<Episode>>();

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
				Episode episode = new Episode();
				episode.facts = new LinkedList<String>();
				episode.numEvents = numNodes;
				for (int i = 0; i < numNodes; i++) {
					episode.facts.add(rawValues[i]);
				}
				if (rawValues[rawValues.length - 1].contains(",")) {
					String[] order = rawValues[rawValues.length - 1].split(",");
					for (int i = 0; i < order.length; i++) {
						episode.facts.add(order[i]);
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
