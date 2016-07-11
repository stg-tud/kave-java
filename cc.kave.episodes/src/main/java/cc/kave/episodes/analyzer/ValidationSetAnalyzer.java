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
package cc.kave.episodes.analyzer;

import static cc.recommenders.io.Logger.append;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import com.google.inject.Inject;

import cc.kave.commons.model.episodes.Event;
import cc.kave.episodes.mining.reader.MappingParser;
import cc.kave.episodes.mining.reader.ValidationContextsParser;
import cc.kave.episodes.model.Episode;
import cc.recommenders.io.Logger;

public class ValidationSetAnalyzer {

	private MappingParser mappingParser;
	private ValidationContextsParser validationParser;
	
	private Map<Integer, Integer> structure = new HashMap<Integer, Integer>();
	
	@Inject
	public ValidationSetAnalyzer(MappingParser mParser, ValidationContextsParser vParser) {
		this.mappingParser = mParser;
		this.validationParser = vParser;
	}
	
	public void categorize(int numbRepos) throws ZipException, IOException {
		Logger.setPrinting(true);
		
		Logger.log("Reading the events mapping file ...");
		List<Event> events = mappingParser.parse(numbRepos);
		
		Logger.log("Reading validation data ...");
		Set<Episode> targets = validationParser.parse(events);
		
		Logger.log("Categorizing targets ...");
		for (Episode target : targets) {
			int numInv = target.getNumEvents() - 1;
			
			if (structure.containsKey(numInv)) {
				int counter = structure.get(numInv);
				structure.put(numInv, counter + 1);
			} else {
				structure.put(numInv, 1);
			}
		}
		
		append("\n#Invocations\t#Targets\n");
		for (Map.Entry<Integer, Integer> entry : structure.entrySet()) {
			append("%d\t%d\n", entry.getKey(), entry.getValue());
		}
	}
}
