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
package cc.kave.commons.pointsto;

import java.util.Collections;
import java.util.Set;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.TypeName;
import cc.recommenders.names.IMethodName;
import cc.recommenders.names.ITypeName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.Usage;

/**
 * Dummy class to act as temporary {@link Usage} implementation.
 */
public class DummyUsage implements Usage {

	public void setType(TypeName typeName) {

	}

	@Override
	public ITypeName getType() {
		return null;
	}

	public void setClassContext(TypeName typeName) {

	}

	@Override
	public ITypeName getClassContext() {
		return null;
	}

	public void setMethodContext(MethodName methodName) {

	}

	@Override
	public IMethodName getMethodContext() {

		return null;
	}

	public void setDefinitionSite(DefinitionSite defsite) {

	}

	@Override
	public DefinitionSite getDefinitionSite() {

		return null;
	}

	@Override
	public Set<CallSite> getAllCallsites() {

		return Collections.emptySet();
	}

	public void addCallsite(CallSite callsite) {

	}

	@Override
	public Set<CallSite> getReceiverCallsites() {

		return null;
	}

	@Override
	public Set<CallSite> getParameterCallsites() {

		return null;
	}

}
