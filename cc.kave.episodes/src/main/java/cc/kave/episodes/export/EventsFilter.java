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

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Events;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.statistics.StreamStatistics;

public class EventsFilter {
	
	private static StreamStatistics statistics = new StreamStatistics();
//	private static final String FRAMEWORKNAME = "mscorlib, 4.0.0.0";
	
	private static final double DELTA = 0.001;
	private static final double TIMEOUT = 0.5;
	private static double time = 0.0;
	private static boolean firstMethod = true;
	
	public static EventStream filterStream(List<Event> stream, int remFreqs) {
		
		Map<Event, Integer> occurrences = statistics.getFrequences(stream);
		EventStream sm = new EventStream();
		
		for (Event e : stream) {
			if (e.getKind() == EventKind.METHOD_DECLARATION) {
				if (occurrences.get(e) == remFreqs) {
					sm.addEvent(Events.newUnknownEvent());
				} else {
					sm.addEvent(e);
				}
				continue;
			}
			if (occurrences.get(e) > remFreqs) {
				sm.addEvent(e);;
			}
//			if (occurrences.get(e) > remFreqs) {
//				IAssemblyName asm = e.getMethod().getDeclaringType().getAssembly();
//				if(asm.getIdentifier().equalsIgnoreCase(FRAMEWORKNAME)) {
//					sm.addEvent(e);;
//				}
//			}
		}
		return sm;
	}
	
	public static String filterPartition(List<Event> partition, Map<Event, Integer> stream) {
		StringBuilder sb = new StringBuilder();
		time = 0.0;
		firstMethod = true;
		
		for (Event e : partition) {
			if (stream.keySet().contains(e)) {
				sb.append(addToPartitionStream(e, stream.get(e)));
			} else {
				if (e.getKind() == EventKind.METHOD_DECLARATION  && !firstMethod) {
					time += TIMEOUT;
				}
			}
			firstMethod = false;
		}
		return sb.toString();
	}
	
	private static String addToPartitionStream(Event event, int eventId) {
		if ((event.getKind() == EventKind.METHOD_DECLARATION) && !firstMethod) {
			time += TIMEOUT;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(eventId);
		sb.append(',');
		sb.append(String.format("%.3f", time));
		sb.append('\n');

		time += DELTA;
		
		return sb.toString();
	}
}
