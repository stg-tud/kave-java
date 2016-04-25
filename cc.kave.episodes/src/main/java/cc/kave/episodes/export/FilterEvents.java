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
package cc.kave.episodes.export;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Events;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.recommenders.io.Logger;

public class FilterEvents {
	
	private static final String DUMMY_METHOD_NAME = "[You, Can] [Safely, Ignore].ThisDummyValue()";
	private static final IMethodName DUMMY_METHOD = MethodName.newMethodName(DUMMY_METHOD_NAME);
	public static final Event DUMMY_EVENT = Events.newContext(DUMMY_METHOD);

	public static StreamMapping filter(List<Event> stream) {
		StreamMapping sm = new StreamMapping();
		
		Map<Event, Integer> occurrences = Maps.newHashMap();
		int eventNumber = 0;
		
		for (Event e : stream) {
			eventNumber++;
			if (occurrences.keySet().contains(e)) {
				int freq = occurrences.get(e);
				occurrences.put(e, freq + 1);
			} else {
				occurrences.put(e, 1);
			}
			if (eventNumber % 100000 == 0){
				Logger.log("Counting frequency for %d/%d", eventNumber, stream.size());
			}
		}
		eventNumber = 0;
		int singletons = 0;
		
		sm.addEvent(DUMMY_EVENT);
		for (Event e : stream) {
			if (occurrences.get(e) > 1) {
				sm.addEvent(e);
			} else {
				singletons++;
			}
			if (eventNumber % 100000 == 0){
				Logger.log("Created mapping for %d/%d", eventNumber, occurrences.size());
			}
		}
		Logger.log("One time occured events are: %d", singletons);
		Logger.log("Number of unique events is: %d", occurrences.size());
		Logger.log("Total number of events is: %d", stream.size());
		
		return sm;
	}
}
