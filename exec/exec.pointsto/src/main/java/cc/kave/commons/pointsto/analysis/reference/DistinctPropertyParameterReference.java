/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.analysis.reference;

import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class DistinctPropertyParameterReference implements DistinctReference {

	private IVariableReference varRef;
	private PropertyName property;

	public DistinctPropertyParameterReference(IVariableReference varRef, PropertyName property) {
		this.varRef = varRef;
		this.property = property;
	}

	@Override
	public IReference getReference() {
		return varRef;
	}

	@Override
	public TypeName getType() {
		return property.getValueType();
	}

	public PropertyName getProperty() {
		return property;
	}

	@Override
	public <TReturn, TContext> TReturn accept(DistinctReferenceVisitor<TReturn, TContext> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((property == null) ? 0 : property.hashCode());
		result = prime * result + ((varRef == null) ? 0 : varRef.hashCode());
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
		DistinctPropertyParameterReference other = (DistinctPropertyParameterReference) obj;
		if (property == null) {
			if (other.property != null)
				return false;
		} else if (!property.equals(other.property))
			return false;
		if (varRef == null) {
			if (other.varRef != null)
				return false;
		} else if (!varRef.equals(other.varRef))
			return false;
		return true;
	}

}
