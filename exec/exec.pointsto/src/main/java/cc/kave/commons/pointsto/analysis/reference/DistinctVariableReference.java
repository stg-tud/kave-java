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

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class DistinctVariableReference implements DistinctReference {

	private IVariableReference varRef;
	private IVariableDeclaration varDecl;

	@Override
	public IReference getReference() {
		return varRef;
	}

	public IVariableDeclaration getDeclaration() {
		return varDecl;
	}

	public DistinctVariableReference(IVariableReference varRef, IVariableDeclaration varDecl) {
		this.varRef = varRef;
		this.varDecl = varDecl;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((varDecl == null) ? 0 : varDecl.hashCode());
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
		DistinctVariableReference other = (DistinctVariableReference) obj;
		if (varDecl == null) {
			if (other.varDecl != null)
				return false;
		} else if (varDecl != other.varDecl) // reference equality
			return false;
		if (varRef == null) {
			if (other.varRef != null)
				return false;
		} else if (!varRef.equals(other.varRef))
			return false;
		return true;
	}

}
