/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.model.naming.impl.v0.types;

import java.util.List;

import cc.kave.commons.model.naming.impl.v0.BaseName;
import cc.kave.commons.model.naming.types.IArrayTypeName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.IPredefinedTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;

public abstract class BaseTypeName extends BaseName implements ITypeName {

	protected BaseTypeName(String identifier) {
		super(identifier);
	}

	@Override
	public boolean hasTypeParameters() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ITypeParameterName> getTypeParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAssemblyName getAssembly() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public INamespaceName getNamespace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFullName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isVoidType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isValueType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSimpleType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnumType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isStructType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNullableType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isReferenceType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isClassType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInterfaceType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNestedType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ITypeName getDeclaringType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDelegateType() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IDelegateTypeName asDelegateTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isArray() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IArrayTypeName asArrayTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTypeParameter() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ITypeParameterName asTypeParameterName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPredefined() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IPredefinedTypeName asPredefinedTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

}