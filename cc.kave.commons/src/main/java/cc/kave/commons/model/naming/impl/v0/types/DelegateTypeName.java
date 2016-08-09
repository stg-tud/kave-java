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
package cc.kave.commons.model.naming.impl.v0.types;

import java.util.List;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;

public class DelegateTypeName extends TypeName implements IDelegateTypeName {

	public DelegateTypeName() {
		this("d:[?] [?].()");
	}

	public DelegateTypeName(String identifier) {
		super(identifier);
	}

	static boolean isDelegateTypeIdentifier(String identifier) {
		return identifier.startsWith("d:");
	}

	private IMethodName getDelegateMethod() {
		return Names.newMethod(identifier.substring(2));
	}

	public ITypeName getDelegateType() {
		return getDelegateMethod().getDeclaringType();
	}

	@Override
	public List<IParameterName> getParameters() {
		return getDelegateMethod().getParameters();
	}

	@Override
	public boolean hasParameters() {
		return getDelegateMethod().hasParameters();
	}

	@Override
	public ITypeName getReturnType() {
		return getDelegateMethod().getReturnType();
	}

	@Override
	public boolean isInterfaceType() {
		return false;
	}

	@Override
	public boolean isDelegateType() {
		return true;
	}

	@Override
	public boolean isNestedType() {
		return getDelegateType().isNestedType();
	}

	@Override
	public boolean isArray() {
		return false;
	}

	public ITypeName deriveArrayTypeName(int rank) {
		return ArrayTypeName.from(this, rank);
	}

	@Override
	public boolean isTypeParameter() {
		return false;
	}

	@Override
	public IAssemblyName getAssembly() {
		return getDelegateType().getAssembly();
	}

	@Override
	public String getFullName() {
		return getDelegateMethod().getDeclaringType().getFullName();
	}

	@Override
	public INamespaceName getNamespace() {
		return getDelegateType().getNamespace();
	}

	@Override
	public ITypeName getDeclaringType() {
		return getDelegateType().getDeclaringType();
	}

	@Override
	public boolean isRecursive() {
		// TODO Auto-generated method stub
		return false;
	}

	public static boolean isDelegateTypeNameIdentifier(String id) {
		// TODO Auto-generated method stub
		return false;
	}
}