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
package exec.recommender_reimplementation.frequency_recommender;

import cc.kave.commons.model.naming.codeelements.IMethodName;

public class MethodFrequency {
	
	public IMethodName methodName;
	
	public int frequency;

	public MethodFrequency(IMethodName methodName) {
		this.methodName = methodName;
		this.frequency = 0;
	}

	public MethodFrequency(IMethodName methodName, int frequency) {
		this.methodName = methodName;
		this.frequency = frequency;
	}

	@Override
	public String toString() {
		return "MethodFrequency [methodName=" + methodName + ", frequency=" + frequency + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + frequency;
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
		MethodFrequency other = (MethodFrequency) obj;
		if (frequency != other.frequency)
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		return true;
	}
	
	
}
