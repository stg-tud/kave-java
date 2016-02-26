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

public class ProposalResults {

	private int targetNo;
	private List<Double> results = new LinkedList<Double>();
	
	public int getTargetNo() {
		return this.targetNo;
	}
	
	public void setTargetNo(int targetNo) {
		this.targetNo = targetNo;
	}
	
	public List<Double> getAllResults() {
		return this.results;
	}
	
	public double getResult(int proposal) {
		return this.results.get(proposal);
	}
	
	public void setResult(double result) {
		this.results.add(result);
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
	
	public boolean equals(ProposalResults ps) {
		if (this.targetNo != ps.targetNo) {
			return false;
		}
		if (this.results.size() != ps.results.size()) {
			return false;
		}
		for (int ind = 0; ind < this.results.size(); ind++) {
			if (this.results.get(ind) != ps.getResult(ind)) {
				return false;
			}
		}
		return true;
	}
}
