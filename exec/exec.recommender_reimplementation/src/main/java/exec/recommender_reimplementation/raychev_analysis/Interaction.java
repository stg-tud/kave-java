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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.utils.ToStringUtils;

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
		return new HashCodeBuilder().append(methodName).append(position).append(interactionType).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Interaction)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		Interaction other = (Interaction) obj;
		return new EqualsBuilder().append(this.methodName, other.methodName).append(this.position, other.position)
				.append(this.interactionType, other.interactionType).isEquals();
	}
	
	@Override
	public String toString() {
		return ToStringUtils.toString(this);
	}
	
	
	
}
