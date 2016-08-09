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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.impl.csharp.CsGenericNameUtils;
import cc.kave.commons.model.naming.impl.csharp.CsNameUtils;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;

public class MethodName extends MemberName implements IMethodName {

	private static final Pattern signatureSyntax = Pattern
			.compile(".*\\]\\.((([^(\\[]+)(?:`[0-9]+\\[[^(]+\\]){0,1})\\(.*\\)).*");
	// "\\]\\.((([^([]+)(?:`[0-9]+\\[[^(]+\\]){0,1})\\(.*\\))$"

	private MethodName() {
		this("[?] [?].???()");
	}

	private MethodName(String identifier) {
		super(identifier);
	}

	@Override
	public List<IParameterName> getParameters() {
		return CsNameUtils.getParameterNames(identifier);
	}

	@Override
	public boolean hasParameters() {
		return !identifier.contains("()");
	}

	@Override
	public boolean isConstructor() {
		return false;
	}

	@Override
	public ITypeName getReturnType() {
		return getValueType();
	}

	public String getFullName() {
		Matcher matcher = signatureSyntax.matcher(identifier);
		if (!matcher.matches()) {
			throw new RuntimeException("Invalid Signature Syntax: " + identifier);
		}
		return matcher.group(2);
	}

	public String getName() {
		Matcher matcher = signatureSyntax.matcher(identifier);
		if (!matcher.matches()) {
			throw new RuntimeException("Invalid Signature Syntax: " + identifier);
		}
		return matcher.group(3);
	}

	@Override
	public boolean hasTypeParameters() {
		return getFullName().contains("[[");
	}

	@Override
	public List<ITypeParameterName> getTypeParameters() {
		return hasTypeParameters() ? CsGenericNameUtils.parseTypeParameters(getFullName())
				: new ArrayList<ITypeParameterName>();
	}

	@Override
	public boolean isExtensionMethod() {
		return isStatic() && getParameters().size() > 0 && getParameters().get(0).isExtensionMethodParameter();
	}
}