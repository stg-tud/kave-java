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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Maps;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;

public class EventStream {
	public static final double DELTA = 0.001;
	public static final double TIMEOUT = 0.5;

	private Map<Event, Integer> mappingData = Maps.newLinkedHashMap();
	private StringBuilder sb = new StringBuilder();
	private int streamLength = 0;

	boolean isFirstMethod = true;
	double time = 0.000;

	public String getStream() {
		return this.sb.toString();
	}

	public Map<Event, Integer> getMapping() {
		return this.mappingData;
	}
	
	public int getStreamLength() {
		return this.streamLength;
	}

	public int getEventNumber() {
		return this.mappingData.size();
	}

	public void addEvent(Event event) {
		this.streamLength++;
		
		Integer idx = this.mappingData.get(event);

		if (event.getKind() == EventKind.CONTEXT_HOLDER) {
			this.time += TIMEOUT;
		} else {
			if (idx == null) {
				idx = getEventNumber();
				this.mappingData.put(event, idx);
			}
		}
		if (event.getKind() == EventKind.METHOD_DECLARATION && !this.isFirstMethod) {
			this.time += TIMEOUT;
		}
		this.isFirstMethod = false;
		
		if ((idx != null) && (idx > 0)) {
			this.sb.append(idx);
			this.sb.append(',');
			this.sb.append(String.format("%.3f", this.time));
			this.sb.append('\n');

			this.time += DELTA;
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
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
		if (!this.mappingData.equals(sm.getMapping())) {
			return false;
		}
		if (!this.sb.toString().equals(sm.getStream())) {
			return false;
		}
		return true;
	}
}
