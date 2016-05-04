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

import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.parser.TypeNameParseUtil;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.FormalParamContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.GenericParamContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.MethodContext;

public class CsMethodName implements IMethodName {

	private MethodContext ctx;

	public CsMethodName(String type) {
		MethodContext ctx = TypeNameParseUtil.validateMethodName(type);
		this.ctx = ctx;
	}

	public CsMethodName(MethodContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public ITypeName getDeclaringType() {
		return ctx.customMethod() != null ? new CsTypeName(ctx.customMethod().type(1))
				: new CsTypeName(ctx.constructor().type());
	}

	@Override
	public ITypeName getValueType() {
		if (ctx.customMethod() != null) {
			return new CsTypeName(ctx.customMethod().type(0));
		}
		return new CsTypeName("?");
	}

	@Override
	public boolean isStatic() {
		return ctx.customMethod() != null && ctx.customMethod().staticModifier() != null;
	}

	@Override
	public String getName() {
		// TODO implement for constructors
		return ctx.customMethod() != null ? ctx.customMethod().id().getText() : ".cctor";
	}

	@Override
	public String getIdentifier() {
		return ctx.getText();
	}

	@Override
	public boolean isUnknown() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isGenericEntity() {
		return ctx.customMethod() != null && ctx.customMethod().genericTypePart() != null;
	}

	@Override
	public boolean hasTypeParameters() {
		return isGenericEntity() && ctx.customMethod().genericTypePart().genericParam() != null;
	}

	@Override
	public List<ITypeName> getTypeParameters() {
		List<ITypeName> typeParameters = Lists.newArrayList();
		if (isGenericEntity() && hasTypeParameters()) {
			for (GenericParamContext c : ctx.customMethod().genericTypePart().genericParam()) {
				typeParameters.add(TypeParameterName.newTypeParameterName(c.getText()));
			}
		}
		return typeParameters;
	}

	@Override
	public String getSignature() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IParameterName> getParameters() {
		List<IParameterName> parameters = Lists.newArrayList();
		if (hasParameters()) {
			for (FormalParamContext c : ctx.formalParam()) {
				parameters.add(ParameterName.newParameterName(c.getText()));
			}
		}
		return parameters;
	}

	@Override
	public boolean hasParameters() {
		return ctx.formalParam() != null;
	}

	@Override
	public boolean isConstructor() {
		return ctx.constructor() != null;
	}

	@Override
	public ITypeName getReturnType() {
		return getValueType();
	}

	@Override
	public boolean isExtensionMethod() {
		// TODO Auto-generated method stub
		return false;
	}
}
