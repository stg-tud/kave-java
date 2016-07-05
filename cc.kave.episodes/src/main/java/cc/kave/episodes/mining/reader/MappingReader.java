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
package cc.kave.episodes.mining.reader;

import java.util.List;

import com.google.inject.Inject;

import cc.kave.episodes.mining.reader.EventMappingParser;
import cc.kave.commons.model.episodes.Event;

public class MappingReader {

	private EventMappingParser eventMapper;

	@Inject
	public MappingReader(EventMappingParser eventMapping) {
		this.eventMapper = eventMapping;
	}

	public void read(int numbRepos) {
		List<Event> events = eventMapper.parse(numbRepos);
		int counter = 0;

		for (Event e : events) {
			if (counter == 4440) {
				counter++;
				continue;
			}
			System.out.println("--- " + (counter++) + " ---------------------");
			System.out.println(e);
			System.out.println(e.getMethod().getDeclaringType().getFullName().toString());
		}
		System.out.println(events.size());
	}
}
