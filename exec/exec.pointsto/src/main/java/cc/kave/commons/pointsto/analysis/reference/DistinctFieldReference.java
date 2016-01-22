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

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.references.IFieldReference;

public class DistinctFieldReference implements DistinctReference {

	private IFieldReference fieldRef;

	public DistinctFieldReference(IFieldReference fieldRef) {
		this.fieldRef = fieldRef;
	}

	@Override
	public IReference getReference() {
		return fieldRef;
	}

	@Override
	public TypeName getType() {
		return fieldRef.getFieldName().getValueType();
	}

	@Override
	public <TReturn, TContext> TReturn accept(DistinctReferenceVisitor<TReturn, TContext> visitor, TContext context) {
		return visitor.visit(this, context);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fieldRef == null) ? 0 : fieldRef.hashCode());
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
		DistinctFieldReference other = (DistinctFieldReference) obj;
		if (fieldRef == null) {
			if (other.fieldRef != null)
				return false;
		} else if (!fieldRef.equals(other.fieldRef))
			return false;
		return true;
	}

}