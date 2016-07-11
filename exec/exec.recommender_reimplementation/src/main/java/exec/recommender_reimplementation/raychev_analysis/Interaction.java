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
package exec.recommender_reimplementation.raychev_analysis;

import cc.kave.commons.model.names.IMethodName;

public class Interaction {
	
	public static final int RETURN = -1; 
	
	private IMethodName methodName;

	private int position;
	
	private InteractionType interactionType;
	
	public Interaction(IMethodName methodName, int position, InteractionType interactionType) {
		this.methodName = methodName;
		this.position = position;
		this.interactionType = interactionType;
	}
	
	public IMethodName getMethodName() {
		return methodName;
	}

	@Override
	public String toString() {
		return "Interaction [methodName=" + methodName + ", position=" + position + ", interactionType="
				+ interactionType + "]";
	}

	public void setMethodName(IMethodName methodName) {
		this.methodName = methodName;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public InteractionType getInteractionType() {
		return interactionType;
	}

	public void setInteractionType(InteractionType interactionType) {
		this.interactionType = interactionType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((interactionType == null) ? 0 : interactionType.hashCode());
		result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
		result = prime * result + position;
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
		Interaction other = (Interaction) obj;
		if (interactionType != other.interactionType)
			return false;
		if (methodName == null) {
			if (other.methodName != null)
				return false;
		} else if (!methodName.equals(other.methodName))
			return false;
		if (position != other.position)
			return false;
		return true;
	}
	
	
	
}
