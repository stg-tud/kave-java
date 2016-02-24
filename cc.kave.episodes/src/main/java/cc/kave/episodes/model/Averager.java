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

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import static cc.recommenders.assertions.Asserts.assertTrue;

public class Averager {

	private List<Double> data = new LinkedList<Double>();
	
	public void addValue(double value) {
		assertTrue(value >= 0.0 && value <= 1.0, "Enter a proper similarity value, between 0 and 1!");
		this.data.add(value);
	}
	
	public List<Double> getValues() {
		return this.data;
	}
	
	public double average() {
		if (this.data.isEmpty()) {
			return 0.0;
		} 
		double sum = 0;
		for (double value : data) {
			sum += value;
		}
		return (sum / this.data.size());
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
