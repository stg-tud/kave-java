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

import static com.codetrails.data.CallSiteKind.RECEIVER_CALL_SITE;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.CoReNames;

import com.google.common.base.Objects;
import com.google.common.base.Objects.ToStringHelper;

public class CallSite {

	// make sure the naming is consistent to the hardcoded names in "UsageTypeAdapter"

	private CallSiteKind kind;
	private ICoReMethodName call;
	private int argumentIndex;

	public CallSiteKind getKind() {
		return kind;
	}

	public void setKind(final CallSiteKind kind) {
		this.kind = kind;
	}

	public ICoReMethodName getCall() {
		return call;
	}

	public void setCall(final ICoReMethodName call) {
		this.call = call;
	}

	public void setArgumentIndex(int argumentIndex) {
		this.argumentIndex = argumentIndex;
	}

	public int getArgumentIndex() {
		return argumentIndex;
	}

	public int hashCode() {
		int _reflectionHashCode = HashCodeBuilder.reflectionHashCode(this);
		return _reflectionHashCode;
	}

	public boolean equals(final Object other) {
		boolean _reflectionEquals = EqualsBuilder.reflectionEquals(this, other);
		return _reflectionEquals;
	}

	public String toString() {
		String _xblockexpression = null;
		{
			String key = kind == RECEIVER_CALL_SITE ? "call" : "param";
			ToStringHelper _stringHelper = Objects.toStringHelper(this);
			ICoReMethodName _call = this.getCall();
			String _sourceName = CoReNames.vm2srcQualifiedMethod(_call);
			ToStringHelper _add = _stringHelper.add(key, _sourceName);
			String _string = _add.toString();
			_xblockexpression = (_string);
		}
		return _xblockexpression;
	}
}
