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
package cc.kave.commons.model.naming.impl.v0.codeelements;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.impl.v0.BaseName;
import cc.kave.commons.model.naming.impl.v0.types.TypeName;
import cc.kave.commons.model.naming.types.ITypeName;

public class ParameterName extends BaseName implements IParameterName {

	public final String passByReferenceModifier = "ref";
	public final String outputModifier = "out";
	public final String varArgsModifier = "params";
	public final String optionalModifier = "opt";
	public final String ExtensionMethodModifier = "this";

	private ParameterName(String identifier) {
		super(identifier);
	}

	public boolean isUnknown() {
		return this.equals(Names.getUnknownParameter());
	}

	@Override
	public ITypeName getValueType() {
		int startOfValueTypeIdentifier = identifier.indexOf('[') + 1;
		int endOfValueTypeIdentifier = identifier.lastIndexOf(']') + 1;
		int lengthOfValueTypeIdentifier = endOfValueTypeIdentifier - startOfValueTypeIdentifier
				+ getModifiers().length();
		return TypeName.newTypeName(identifier.substring(startOfValueTypeIdentifier, lengthOfValueTypeIdentifier));
	}

	@Override
	public String getName() {
		return identifier.substring(identifier.lastIndexOf(" ") + 1);
	}

	@Override
	public boolean isPassedByReference() {
		return getValueType().isReferenceType() || getModifiers().contains(passByReferenceModifier);
	}

	@Override
	public boolean isOutput() {
		return getModifiers().contains(outputModifier);
	}

	@Override
	public boolean isParameterArray() {
		return getModifiers().contains(varArgsModifier);
	}

	@Override
	public boolean isOptional() {
		return getModifiers().contains(optionalModifier);
	}

	private String getModifiers() {
		return identifier.substring(0, identifier.indexOf('['));
	}

	public boolean isExtensionMethodParameter() {
		return getModifiers().contains(ExtensionMethodModifier);
	}
}
