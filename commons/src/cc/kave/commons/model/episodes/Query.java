package cc.kave.commons.model.episodes;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Lists;

public class Query {

	private List<Fact> facts = Lists.newLinkedList();
	private int numberOfFacts;

	public Iterable<Fact> getFacts() {
		return facts;
	}

	public void addStringsOfFacts(String... rawFacts) {
		for (String rawFact : rawFacts) {
			addFact(rawFact);
		}
	}

	public void addFact(String rawFact) {
		facts.add(new Fact(rawFact));
	}

	public int getNumberOfFacts() {
		return numberOfFacts;
	}

	public void setNumberOfFacts(int numberOfEvents) {
		this.numberOfFacts = numberOfEvents;
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

	public boolean equals(Query query) {
		if (this.facts.size() != query.facts.size()) {
			return false;
		}
		for (Fact fact : this.facts) {
			if (!query.facts.contains(fact)) {
				return false;
			}
		}
		return true;
	}
}
