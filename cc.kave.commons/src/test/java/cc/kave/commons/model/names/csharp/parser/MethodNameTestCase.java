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
package cc.kave.commons.model.names.csharp.parser;

import java.util.List;

public class MethodNameTestCase {

	private String identifier;
	private String declaringType;
	private String returnType;
	private String simpleName;
	private boolean isStatic;
	private boolean isGeneric;
	private List<String> parameters;
	private List<String> typeParameters;

	public MethodNameTestCase(String identifier, String declaringType, String returnType, String simpleName,
			boolean isStatic, boolean isGeneric, List<String> parameters, List<String> typeParameters) {
		this.identifier = identifier;
		this.declaringType = declaringType;
		this.returnType = returnType;
		this.simpleName = simpleName;
		this.isStatic = isStatic;
		this.parameters = parameters;
		this.isGeneric = isGeneric;
		this.typeParameters = typeParameters;
	}

	public String getIdentifier() {
		return identifier;
	}

	public String getDeclaringType() {
		return declaringType;
	}

	public String getReturnType() {
		return returnType;
	}

	public String getSimpleName() {
		return simpleName;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public boolean isGeneric() {
		return isGeneric;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public List<String> getTypeParameters() {
		return typeParameters;
	}

}
