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

import cc.recommenders.names.ICoReFieldName;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;

import com.google.common.base.Objects;

public class DefinitionSite {

	// make sure the naming is consistent to the hardcoded names in "UsageTypeAdapter"

	private DefinitionKind kind;
	private ICoReTypeName type;
	private ICoReMethodName method;
	private ICoReFieldName field;
	private int arg = -1;

	public DefinitionKind getKind() {
		return this.kind;
	}

	public void setKind(final DefinitionKind kind) {
		this.kind = kind;
	}

	public ICoReTypeName getType() {
		return type;
	}

	public void setType(final ICoReTypeName type) {
		this.type = type;
	}

	public ICoReMethodName getMethod() {
		return method;
	}

	public void setMethod(final ICoReMethodName method) {
		this.method = method;
	}

	public ICoReFieldName getField() {
		return field;
	}

	public void setField(final ICoReFieldName field) {
		this.field = field;
	}

	/**
	 * Any positive index or -1 if it's initialized as array element e.g. in
	 * "void m(Object[] o)"
	 */
	public int getArgumentIndex() {
		return arg;
	}

	/**
	 * Any positive index or -1 if it's initialized as array element e.g. in
	 * "void m(Object[] o)"
	 */
	public void setArgumentIndex(final int argumentIndex) {
		this.arg = argumentIndex;
	}

	public boolean isContainerArgument() {
		boolean _and = false;
		DefinitionKind _kind = this.getKind();
		boolean _equals = Objects.equal(_kind, DefinitionKind.PARAM);
		if (!_equals) {
			_and = false;
		} else {
			Integer _argumentIndex = this.getArgumentIndex();
			int _minus = (-1);
			boolean _equals_1 = ((_argumentIndex).intValue() == _minus);
			_and = (_equals && _equals_1);
		}
		return _and;
	}

	public boolean isArray() {
		ICoReTypeName _type = this.getType();
		boolean _isArrayType = _type.isArrayType();
		return _isArrayType;
	}

	@Override
	public boolean equals(Object obj) {
		return EqualsBuilder.reflectionEquals(this, obj);
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		switch (kind) {
		case CONSTANT:
			sb.append("CONSTANT");
			break;
		case FIELD:
			sb.append("FIELD:");
			sb.append(field);
			break;
		case NEW:
			sb.append("INIT:");
			sb.append(method);
			break;
		case PARAM:
			sb.append("PARAM(");
			sb.append(arg);
			sb.append("):");
			sb.append(method);
			break;
		case RETURN:
			sb.append("RETURN:");
			sb.append(method);
			break;
		case THIS:
			sb.append(DefinitionKind.THIS);
			break;
		case UNKNOWN:
			sb.append(DefinitionKind.UNKNOWN);
			break;
		}
		return sb.toString();

	}
}