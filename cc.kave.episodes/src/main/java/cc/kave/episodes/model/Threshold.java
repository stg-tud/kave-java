package cc.kave.episodes.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Threshold {

	private int frequency;
	private double entropy;

	private int noGens = 0;
	private int noSpecs = 0;

	public Threshold() {
		this.frequency = 1;
		this.entropy = 0.0;
	}
	
	public void addGenPattern() {
		this.noGens++;
	}

	public void addSpecPattern() {
		this.noSpecs++;
	}
	
	public void setFrequency(int freq) {
		this.frequency = freq;
	}
	 
	public void setEntropy(double ent) {
		this.entropy = ent;
	}

	public int getFrequency() {
		return this.frequency;
	}

	public double getEntropy() {
		return this.entropy;
	}

	public int getNoGenPatterns() {
		return this.noGens;
	}

	public int getNoSpecPatterns() {
		return this.noSpecs;
	}

	public double getFraction() {
		int numPatterns = this.noGens + this.noSpecs;
		if (numPatterns == 0) {
			return 0.0;
		} else {
			return ((1.0) * (this.noGens)) / ((1.0) * numPatterns);
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

	public boolean equals(Threshold threshs) {
		if (this.frequency != threshs.frequency) {
			return false;
		}
		if (this.entropy != threshs.entropy) {
			return false;
		}
		return true;
	}
}
