/**
 * Copyright 2015 Simon Reuß
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
package cc.kave.commons.pointsto.analysis;

import com.google.common.base.MoreObjects;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;

public class PointsToQuery {

	private IReference reference;
	private IStatement stmt;
	private ITypeName type;
	private Callpath callpath;

	public PointsToQuery(IReference reference, IStatement stmt, ITypeName type, Callpath callpath) {
		this.reference = reference;
		this.stmt = stmt;
		this.type = type;
		this.callpath = callpath;
	}

	public IReference getReference() {
		return reference;
	}

	public void setReference(IReference reference) {
		this.reference = reference;
	}

	public IStatement getStmt() {
		return stmt;
	}

	public void setStmt(IStatement stmt) {
		this.stmt = stmt;
	}

	public ITypeName getType() {
		return type;
	}

	public void setType(ITypeName type) {
		this.type = type;
	}

	public Callpath getCallpath() {
		return callpath;
	}

	public void setCallpath(Callpath callpath) {
		this.callpath = callpath;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(PointsToQuery.class).add("reference", reference).add("stmt", stmt)
				.add("type", type).add("callpath", callpath).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((callpath == null) ? 0 : callpath.hashCode());
		result = prime * result + ((reference == null) ? 0 : reference.hashCode());
		result = prime * result + ((stmt == null) ? 0 : stmt.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		PointsToQuery other = (PointsToQuery) obj;
		if (callpath == null) {
			if (other.callpath != null)
				return false;
		} else if (!callpath.equals(other.callpath))
			return false;
		if (reference == null) {
			if (other.reference != null)
				return false;
		} else if (!reference.equals(other.reference))
			return false;
		if (stmt == null) {
			if (other.stmt != null)
				return false;
		} else if (stmt != other.stmt) // instance equality
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
