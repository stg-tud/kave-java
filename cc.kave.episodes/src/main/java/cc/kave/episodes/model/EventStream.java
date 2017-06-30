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

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;

import com.google.common.collect.Maps;

public class EventStream {
	private static final double DELTA = 0.001;
	private static final double TIMEOUT = 5.0;

	private Map<Event, Integer> eventsMapper = Maps.newLinkedHashMap();
	private StringBuilder sb = new StringBuilder();

	private double time = 0.000;

	public EventStream() {
		this.eventsMapper.put(Events.newDummyEvent(), 0);
	}

	public Set<Event> getMapping() {
		return this.eventsMapper.keySet();
	}

	public String getStreamText() {
		return sb.toString();
	}

	public void addEvent(Event event) {

		int idx = ensureEventExistsAndGetId(event);

		addEventIdToStream(idx);
	}
	
	public void increaseTimeout() {
		time += TIMEOUT;
	}

	public void delete() {
		this.sb.delete(0, sb.length());
		this.time = 0.000;
		this.eventsMapper.clear();
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
		return true;
	}
}
