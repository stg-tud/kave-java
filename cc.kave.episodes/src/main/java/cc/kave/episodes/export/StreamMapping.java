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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Lists;

import cc.kave.commons.model.episodes.Event;

public class StreamMapping {
	private List<Event> streamData = Lists.newLinkedList();
	private List<Event> mappingData = Lists.newLinkedList();
	
	public List<Event> getStreamData() {
		return streamData;
	}
	
	public void addEvent(Event event) {
		this.streamData.add(event);
		if (this.mappingData.indexOf(event) == -1) {
			this.mappingData.add(event);
		}
	}
	
	public List<Event> getMappingData() {
		return mappingData;
	}
	
	public int getStreamLength() {
		return this.streamData.size();
	}
	
	public int getNumberEvents() {
		return this.mappingData.size();
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
}
