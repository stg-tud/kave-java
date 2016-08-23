/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.recommender_reimplementation.heinemann_analysis;

import java.util.Set;

import cc.kave.commons.model.naming.types.ITypeName;


public class HeinemannQuery {
	
	private Set<String> lookback;
	
	private double minimumSimilarity;
	
	private ITypeName declaringType;
	
	public HeinemannQuery(Set<String> lookback, ITypeName declaringType) {
		this.lookback = lookback;
		this.declaringType = declaringType;
	}
	
	public Set<String> getLookback() {
		return lookback;
	}

	public void setLookback(Set<String> lookback) {
		this.lookback = lookback;
	}

	public double getMinimumSimilarity() {
		return minimumSimilarity;
	}

	public void setMinimumSimilarity(double minimumSimilarity) {
		this.minimumSimilarity = minimumSimilarity;
	}

	public ITypeName getDeclaringType() {
		return declaringType;
	}

	public void setDeclaringType(ITypeName declaringType) {
		this.declaringType = declaringType;
	}
}
