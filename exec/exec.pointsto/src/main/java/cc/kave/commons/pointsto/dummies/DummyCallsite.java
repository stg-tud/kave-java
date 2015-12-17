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
package cc.kave.commons.pointsto.dummies;

import com.google.common.base.MoreObjects;
import cc.kave.commons.model.names.IMethodName;

import cc.recommenders.usages.CallSiteKind;

public class DummyCallsite {

	private CallSiteKind kind;
	private IMethodName method;
	private int argIndex = 0;

	private DummyCallsite() {

	}

	public static DummyCallsite parameterCallsite(IMethodName method, int argIndex) {
		DummyCallsite callsite = new DummyCallsite();
		callsite.setKind(CallSiteKind.PARAMETER);
		callsite.setMethod(method);
		callsite.setArgIndex(argIndex);
		return callsite;
	}

	public static DummyCallsite receiverCallsite(IMethodName method) {
		DummyCallsite callsite = new DummyCallsite();
		callsite.setKind(CallSiteKind.RECEIVER);
		callsite.setMethod(method);
		return callsite;
	}

	public CallSiteKind getKind() {
		return kind;
	}

	public void setKind(CallSiteKind kind) {
		this.kind = kind;
	}

	public IMethodName getMethod() {
		return method;
	}

	public void setMethod(IMethodName method) {
		this.method = method;
	}

	public int getArgIndex() {
		return argIndex;
	}

	public void setArgIndex(int argIndex) {
		this.argIndex = argIndex;
	}
	
	@Override
	public String toString() {
		return MoreObjects.toStringHelper(DummyCallsite.class).add("kind", kind).toString();
	}

}
