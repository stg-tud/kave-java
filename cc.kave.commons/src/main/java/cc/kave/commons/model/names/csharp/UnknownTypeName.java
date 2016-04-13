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
package cc.kave.commons.model.names.csharp;

import cc.kave.commons.model.names.IAssemblyName;
import cc.kave.commons.model.names.INamespaceName;
import cc.kave.commons.model.names.ITypeName;

public class UnknownTypeName extends TypeName {
	public static final String IDENTIFIER = "?";

	public static ITypeName getInstance() {
		return TypeName.newTypeName(IDENTIFIER);
	}

	public static boolean isUnknownTypeIdentifier(String identifier) {
		return identifier.equals(IDENTIFIER);
	}

	protected UnknownTypeName(String identifier) {
		super(identifier);
	}

	static ITypeName get(String identifier) {
		return TypeName.newTypeName(identifier);
	}

	@Override
	public boolean isUnknownType() {
		return true;
	}

	@Override
	public IAssemblyName getAssembly() {
		return AssemblyName.getUnknownName();
	}

	@Override
	public INamespaceName getNamespace() {
		return NamespaceName.getUnknownName();
	}
}