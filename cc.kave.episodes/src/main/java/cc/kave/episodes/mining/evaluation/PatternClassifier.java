package cc.kave.episodes.mining.evaluation;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class PatternClassifier {

	private boolean general = false;
	private boolean  multApis = false;
	private boolean partial = false;
	
	public boolean isGeneral() {
		return general;
	}
	
	public void setGeneral(boolean general) {
		this.general = general;
	}
	
	public boolean isMultApis() {
		return multApis;
	}
	
	public void setMultApis(boolean multApis) {
		this.multApis = multApis;
	}
	
	public boolean isPartial() {
		return partial;
	}
	
	public void setPartial(boolean partial) {
		this.partial = partial;
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

	public boolean equals(PatternClassifier pc) {
		if (this.general != pc.general) {
			return false;
		}
		if (this.partial != pc.partial) {
			return false;
		}
		if (this.multApis != pc.multApis) {
			return false;
		}
		return true;
	}
}
