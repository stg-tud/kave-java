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

import cc.kave.commons.model.naming.codeelements.IMethodName;


public class Entry {
	private Set<String> lookback;
	private IMethodName methodName;
	
	public Entry(Set<String> lookback, IMethodName methodName) {
		this.lookback = lookback;
		this.methodName = methodName;
	}
	
	public Set<String> getLookback() {
		return lookback;
	}

	public void setLookback(Set<String> lookback) {
		this.lookback = lookback;
	}

	public IMethodName getMethodName() {
		return methodName;
	}

	public void setMethodName(IMethodName methodName) {
		this.methodName = methodName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lookback == null) ? 0 : lookback.hashCode());
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entry other = (Entry) obj;
		if (lookback == null) {
			if (other.lookback != null)
				return false;
		} else if (!lookback.equals(other.lookback))
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Entry [Lookback=" + lookback + ", MethodName=" + methodName + "]";
	}
	
	
}
