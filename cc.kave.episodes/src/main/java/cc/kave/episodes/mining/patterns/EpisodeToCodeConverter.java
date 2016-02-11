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
package cc.kave.episodes.mining.patterns;

import java.util.List;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.OrderedEventsInEpisode;

public class EpisodeToCodeConverter {

	public String LearnedPatternConverter(OrderedEventsInEpisode episode, List<Event> mappingList) {
		String codeRepresentation = "";

		if (!episode.getPartialOrderList().isEmpty()) {
			for (String eventID : episode.getPartialOrderList()) {
				try {
					codeRepresentation += mappingList.get(Integer.getInteger(eventID)).getMethod().getName() + "\n";
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		codeRepresentation += "\n";
		if (!episode.getSequentialOrderList().isEmpty()) {
			for (String partialEventIDs : episode.getSequentialOrderList()) {
				String[] eventIds = partialEventIDs.split(" ");
				if (eventIds.length == 1) {
					try {
						codeRepresentation += mappingList.get(Integer.getInteger(eventIds[0])).getMethod().getName();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// for (int i = 0; i < eventIDs.length(); i++) {
					// char id = eventIDs.charAt(i);
					// }
				}
			}
		}
		return codeRepresentation;
	}
}
