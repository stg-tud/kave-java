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
package cc.kave.commons.pointsto.analysis.names;

import static cc.kave.commons.pointsto.analysis.utils.GenericNameUtils.eraseGenericInstantiations;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;

public class DistinctMethodName extends DistinctMemberName {

	private final List<ITypeName> parameterTypes;

	public DistinctMethodName(IMethodName method) {
		super(method.getValueType(), method.getDeclaringType(), method.getName());

		List<IParameterName> parameters = method.getParameters();
		parameterTypes = new ArrayList<>(parameters.size());
		for (IParameterName parameter : parameters) {
			parameterTypes.add(eraseGenericInstantiations(parameter.getValueType()));
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((parameterTypes == null) ? 0 : parameterTypes.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DistinctMethodName other = (DistinctMethodName) obj;
		if (parameterTypes == null) {
			if (other.parameterTypes != null)
				return false;
		} else if (!parameterTypes.equals(other.parameterTypes))
			return false;
		return true;
	}

}
