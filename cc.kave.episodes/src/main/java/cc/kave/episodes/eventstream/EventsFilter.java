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
import java.util.Set;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.statistics.StreamStatistics;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class EventsFilter {

	private static StreamStatistics statistics = new StreamStatistics();
	// private static final String FRAMEWORKNAME = "mscorlib, 4.0.0.0";

	public static EventStream filterStream(List<Event> stream, int freqThresh) {
		List<Event> streamWithoutDublicates = removeMethodDublicates(stream);
		Map<Event, Integer> occurrences = statistics.getFrequencies(stream);
		EventStream es = new EventStream();

		for (Event e : streamWithoutDublicates) {
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
		streamWithoutDublicates.clear();
		occurrences.clear();
		return es;
	}

	private static List<Event> removeMethodDublicates(List<Event> stream) {
		List<Event> results = Lists.newLinkedList();
		List<Event> methodStream = Lists.newLinkedList();
		Set<IMethodName> observedMethods = Sets.newLinkedHashSet();
		Set<List<Event>> obsUnknownMethods = Sets.newLinkedHashSet();
		IMethodName methodName = null;

		for (Event event : stream) {
			if ((event.getKind() == EventKind.FIRST_DECLARATION)
					&& (methodName != null)) {
				if ((!observedMethods.contains(methodName))
						&& (!obsUnknownMethods.contains(methodStream))) {
					results.addAll(methodStream);
					if (methodName.equals(Names.getUnknownMethod())) {
						obsUnknownMethods.add(methodStream);
					} else {
						observedMethods.add(methodName);
					}
				}
				methodStream.clear();
				methodStream = Lists.newLinkedList();
				methodName = null;
			} else if (event.getKind() == EventKind.METHOD_DECLARATION) {
				methodName = event.getMethod();
			}
			methodStream.add(event);
		}
		if ((methodName != null)
				&& ((!observedMethods.contains(methodName)) && (!obsUnknownMethods
						.contains(methodStream)))) {
			results.addAll(methodStream);
		}
		observedMethods.clear();
		obsUnknownMethods.clear();
		return results;
	}
}