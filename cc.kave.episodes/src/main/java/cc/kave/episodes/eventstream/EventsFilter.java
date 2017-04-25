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
package cc.kave.episodes.eventstream;

import java.util.List;
import java.util.Map;

import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.statistics.StreamStatistics;

public class EventsFilter {

	private static StreamStatistics statistics = new StreamStatistics();

	// private static final String FRAMEWORKNAME = "mscorlib, 4.0.0.0";

	public static EventStream filterStream(List<Event> stream, int freqThresh) {
		Map<Event, Integer> occurrences = statistics.getFrequencies(stream);
		EventStream es = new EventStream();

		for (Event e : stream) {
			if ((e.getKind() == EventKind.FIRST_DECLARATION)
					|| (e.getKind() == EventKind.METHOD_DECLARATION)) {
				es.addEvent(e);
				continue;
			}
			if (occurrences.get(e) >= freqThresh) {
				IAssemblyName asm = null;
				try {
					asm = e.getMethod().getDeclaringType().getAssembly();
				} catch (Exception e1) {
					continue;
				}
				// predefined types have always an unknown version, but come
				// from mscorlib, so they should be included
				if (!asm.getName().equals("mscorlib")
						&& asm.getVersion().isUnknown()) {
					continue;
				}
				es.addEvent(e);
			}
		}
		occurrences.clear();
		return es;
	}
}