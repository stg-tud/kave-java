package cc.kave.commons.model.episodes;

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
			partialEvents += event;
		}
		this.sequentialOrderList.add(partialEvents);
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
