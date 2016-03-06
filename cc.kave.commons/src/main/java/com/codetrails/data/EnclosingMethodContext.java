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
package com.codetrails.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import cc.recommenders.names.IMethodName;
import cc.recommenders.names.ITypeName;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public class EnclosingMethodContext {
	private IMethodName name;
	@Nullable
	private ITypeName introducedBy;
	@Nullable
	private ITypeName superclass;
	private ITypeName[] implementedBy;
	private ITypeName[] annotations;

	public IMethodName getName() {
		return name;
	}

	public void setName(final IMethodName name) {
		this.name = name;
	}

	public ITypeName getSuperclass() {
		return superclass;
	}

	public void setSuperclass(final ITypeName superclass) {
		this.superclass = superclass;
	}

	public ITypeName getIntroducedBy() {
		return introducedBy;
	}

	public void setIntroducedBy(final ITypeName introducedBy) {
		this.introducedBy = introducedBy;
	}

	public ITypeName[] getImplementors() {
		return implementedBy;
	}

	public void setImplementors(final ITypeName[] implementors) {
		this.implementedBy = implementors;
	}

	public ITypeName[] getAnnotations() {
		return annotations;
	}

	public void setAnnotations(final ITypeName[] annotations) {
		this.annotations = annotations;
	}

	public boolean isInit() {
		return name.isInit();
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	public String toString() {
		ToStringHelper _stringHelper = Objects.toStringHelper(this);
		IMethodName _name = this.getName();
		ToStringHelper _add = _stringHelper.add("name", _name);
		ITypeName[] _implementors = this.getImplementors();
		ToStringHelper _add_1 = _add.add("implementors", _implementors.length);
		String _string = _add_1.toString();
		return _string;
	}
}
