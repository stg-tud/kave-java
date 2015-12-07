package cc.kave.commons.model.episodes;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.google.common.collect.Lists;

public class Method {

	private String methodName = "";
	private List<Fact> facts = Lists.newLinkedList();
	private int numberOfInvocations;

	public Iterable<Fact> getFacts() {
		return facts;
	}

	public int getNumberOfInvocations() {
		return numberOfInvocations;
	}

	public void setNumberOfInvocations(int numberOfEvents) {
		this.numberOfInvocations = numberOfEvents;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void addStringsOfFacts(String... rawFacts) {
		for (String rawFact : rawFacts) {
			addFact(rawFact);
		}
	}

	public void addFact(String rawFact) {
		facts.add(new Fact(rawFact));
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

	public boolean equals(Method method) {
		if (!(this.methodName.equalsIgnoreCase(method.methodName))) {
			return false;
		}
		if (this.facts.size() != method.facts.size()) {
			return false;
		}
		for (Fact fact : this.facts) {
			if (!method.facts.contains(fact)) {
				return false;
			}
		}
		return true;
	}
}
