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

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Lists;

public class OrderedEventsInEpisode {

	private List<String> partialOrderList = Lists.newLinkedList();
	private List<String> sequentialOrderList = Lists.newLinkedList();

	public List<String> getPartialOrderList() {
		return partialOrderList;
	}

	public void setPartialOrderList(List<String> partialOrderList) {
		this.partialOrderList = partialOrderList;
	}

	public List<String> getSequentialOrderList() {
		return sequentialOrderList;
	}

	public void setSequentialOrderList(List<String> sequentialOrderList) {
		this.sequentialOrderList = sequentialOrderList;
	}

	public void addEventIDInSequentialOrderList(String event) {
		this.sequentialOrderList.add(event);
	}

	public void addPartialEventsIDInSequentialOrderList(String... events) {
		String partialEvents = "";
		for (String event : events) {
			partialEvents += event + " ";
		}
		this.sequentialOrderList.add(partialEvents.substring(0, partialEvents.length() - 1));
	}

	public boolean eventInSequentialOrderList(String event) {
		for (String eventFromList : sequentialOrderList) {
			if (eventFromList.contains(event)) {
				return true;
			}
		}
		return false;
	}

	public void addEventIDInPartialOrderList(String event) {
		partialOrderList.add(event);
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
