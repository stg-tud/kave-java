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
package cc.kave.episodes.model;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Events;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class EventStream {
	private static final double DELTA = 0.001;
	private static final double TIMEOUT = 5.0;

	private Map<Event, Integer> eventsMapper = Maps.newLinkedHashMap();
	private List<Tuple<Event, String>> stream = Lists.newLinkedList();
	private Event methodCtx = null;
	private StringBuilder sb = new StringBuilder();

	private boolean isFirstMethod = true;
	private double time = 0.000;

	public EventStream() {
		this.eventsMapper.put(Events.newDummyEvent(), 0);
	}

	public Set<Event> getMapping() {
		return this.eventsMapper.keySet();
	}

	public String getStreamText() {
		StringBuilder streamBuilder = new StringBuilder();
		IsLastMethodIncluded();

		for (Tuple<Event, String> tuple : stream) {
			streamBuilder.append(tuple.getSecond());
		}
		return streamBuilder.toString();
	}

	public List<Tuple<Event, String>> getStreamData() {
		IsLastMethodIncluded();
		return this.stream;
	}

	public void addEvent(Event event) {

		possiblyIncreaseTimout(event);

		if (event.getKind() == EventKind.METHOD_DECLARATION) {
			methodCtx = event;
			return;
		}
		
		if (event.getMethod().isUnknown()) {
			return;
		}

		if (isLocal(event)) {
			return;
		}

		int idx = ensureEventExistsAndGetId(event);

		addEventIdToStream(idx);
	}
	
	private boolean isLocal(Event e) {
	    // predefined types have always an unknown version, but come
	    // from mscorlib, so they should be included
	    boolean oldVal = false;
	    boolean newVal = false;
	    ITypeName type = e.getMethod().getDeclaringType();
	    IAssemblyName asm = type.getAssembly();
	    if (!asm.getName().equals("mscorlib") && asm.getVersion().isUnknown()) {
	        oldVal = true;
	    }
	    if (asm.isLocalProject()) {
	        newVal = true;
	    }
	    if (oldVal != newVal) {
	        System.out.printf("different localness for: %s\n", type);
	    }
	    return newVal;
	}
	
	private void addEventIdToStream(int idx) {
		sb.append(idx);
		sb.append(',');
		sb.append(String.format("%.3f", time));
		sb.append('\n');

		time += DELTA;
	}

	private int ensureEventExistsAndGetId(Event event) {
		if (eventsMapper.containsKey(event)) {
			return eventsMapper.get(event);
		} else {
			int idx = this.eventsMapper.size();
			eventsMapper.put(event, idx);
			return idx;
		}
	}

	private void possiblyIncreaseTimout(Event event) {
		if (event.getKind() == EventKind.FIRST_DECLARATION) {
			increaseTimeout();
		}
		isFirstMethod = false;
	}

	public void increaseTimeout() {
		if (isFirstMethod) {
			return;
		}
		addStream();
		time += TIMEOUT;
	}

	private void addStream() {
		if (!(sb.toString().isEmpty())) {
			assertTrue(methodCtx != null, "Method element is null!");

			stream.add(Tuple.newTuple(methodCtx, sb.toString()));
			methodCtx = null;
			sb = new StringBuilder();
		}
	}

	private void IsLastMethodIncluded() {
		if (!(sb.toString().isEmpty())) {
			assertTrue(methodCtx != null, "Method element is null!");
			Tuple<Event, String> lastMethod = Tuple.newTuple(methodCtx,
					sb.toString());

			if (!stream.contains(lastMethod)) {
				stream.add(lastMethod);
			}
		}
	}

	public void delete() {
		this.sb.delete(0, sb.length());
		this.methodCtx = null;
		this.isFirstMethod = true;
		this.time = 0.000;
		this.eventsMapper.clear();
		this.stream.clear();
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	public boolean equals(EventStream sm) {
		if (!(this.getMapping().equals(sm.getMapping()))) {
			return false;
		}
		if (!(this.getStreamText().equals(sm.getStreamText()))) {
			return false;
		}
		if (!(this.getStreamData().equals(sm.getStreamData()))) {
			return false;
		}
		return true;
	}
}
