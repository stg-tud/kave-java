package cc.kave.episodes.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static cc.recommenders.assertions.Asserts.assertTrue;

public class Thresholds {

	private int frequency;
	private double entropy;

	private int noGens = 0;
	private int noSpecs = 0;

	public Thresholds(int freq, double ent) {
		assertTrue(freq > 1, "Not valid frequency value!");
		assertTrue(ent >= 0.0 && ent <= 1.0, "Entropy is a probability value!");
		this.frequency = freq;
		this.entropy = ent;
	}

	public void addGenPattern() {
		this.noGens++;
	}

	public void addSpecPattern() {
		this.noSpecs++;
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

	public boolean equals(Thresholds threshs) {
		if (this.frequency != threshs.frequency) {
			return false;
		}
		if (this.entropy != threshs.entropy) {
			return false;
		}
		return true;
	}
}
