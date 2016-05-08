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
		if (isUnknown()) {
			return new CsTypeName(ctx.UNKNOWN().getText());
		} else if (isConstructor()) {
			return new CsTypeName(ctx.regularMethod().staticCctor() != null ? ctx.regularMethod().staticCctor().type()
					: ctx.regularMethod().nonStaticCtor().type());
		}
		return new CsTypeName(ctx.regularMethod().customMethod().type(1));
	}

	@Override
	public ITypeName getValueType() {
		if (!isUnknown() && !isConstructor()) {
			return new CsTypeName(ctx.regularMethod().customMethod().type(0));
		}
		return new CsTypeName("?");
	}

	@Override
	public boolean isStatic() {
		return !isUnknown()
				&& (ctx.regularMethod().staticCctor() != null || ctx.regularMethod().nonStaticCtor() != null);
	}

	@Override
	public String getName() {
		if (isConstructor()) {
			return ctx.regularMethod().staticCctor() != null ? ".cctor" : ".ctor";
		} else if (isUnknown()) {
			return ctx.UNKNOWN().getText();
		}
		return ctx.regularMethod().customMethod().id().getText();
	}

	@Override
	public String getIdentifier() {
		return ctx.getText();
	}

	@Override
	public boolean isUnknown() {
		return ctx.UNKNOWN() != null;
	}

	@Override
	public boolean isGenericEntity() {
		return !isUnknown() && !isConstructor() && ctx.regularMethod().customMethod() != null
				&& ctx.regularMethod().customMethod().genericTypePart() != null;
	}

	@Override
	public boolean hasTypeParameters() {
		return isGenericEntity() && ctx.regularMethod().customMethod().genericTypePart().genericParam() != null;
	}

	@Override
	public List<ITypeName> getTypeParameters() {
		List<ITypeName> typeParameters = Lists.newArrayList();
		if (isGenericEntity() && hasTypeParameters()) {
			for (GenericParamContext c : ctx.regularMethod().customMethod().genericTypePart().genericParam()) {
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
			for (FormalParamContext c : ctx.regularMethod().methodParameters().formalParam()) {
				parameters.add(ParameterName.newParameterName(c.getText()));
			}
		}
		return parameters;
	}

	@Override
	public boolean hasParameters() {
		return !isUnknown() && !isConstructor() && ctx.regularMethod().methodParameters().formalParam() != null;
	}

	@Override
	public boolean isConstructor() {
		return !isUnknown()
				&& (ctx.regularMethod().staticCctor() != null || ctx.regularMethod().nonStaticCtor() != null);
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
