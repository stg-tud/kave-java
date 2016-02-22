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
package cc.kave.commons.pointsto.extraction;

import java.util.stream.Collectors;

import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.INamespaceName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.pointsto.analysis.exceptions.MissingTypeNameException;
import cc.kave.commons.pointsto.analysis.utils.LambdaNameHelper;
import cc.kave.commons.pointsto.analysis.utils.LanguageOptions;
import cc.recommenders.names.IName;
import cc.recommenders.names.VmFieldName;
import cc.recommenders.names.VmMethodName;
import cc.recommenders.names.VmTypeName;

public class CoReNameConverter {

	private static final String VOID_NAME = "LSystem/Void";
	private static final String OBJECT_NAME = "LSystem/Object";
	private static final String UNKNOWN_NAME = "LUnknown";

	private static final LanguageOptions LANGUAGE_OPTIONS = LanguageOptions.getInstance();

	private static String toName(INamespaceName namespace) {
		if (namespace.isGlobalNamespace()) {
			return "";
		}

		INamespaceName parentNamespace = namespace.getParentNamespace();
		if (parentNamespace == null) {
			return "";
		}

		return toName(parentNamespace) + namespace.getName() + "/";
	}

	private static String toName(ITypeName sstType) {
		if (sstType.isTypeParameter()) {
			return OBJECT_NAME;
		}
		if (sstType.isArrayType()) {
			return "[" + toName(sstType.getArrayBaseType());
		}
		if (sstType.isNestedType()) {
			return toName(sstType.getDeclaringType()) + "$" + sstType.getName();
		}
		if (sstType.isUnknownType()) {
			return UNKNOWN_NAME;
		}

		return "L" + toName(sstType.getNamespace()) + sstType.getName();
	}

	public static cc.recommenders.names.ITypeName convert(ITypeName sstType) {
		if (sstType == null) {
			return null;
		}

		return VmTypeName.get(toName(sstType));
	}

	public static cc.recommenders.names.IFieldName convert(IFieldName sstField) {
		if (sstField == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(toName(sstField.getDeclaringType()));
		builder.append('.');
		builder.append(sstField.isUnknown() ? "unknown" : sstField.getName());
		builder.append(';');
		builder.append(toName(sstField.getValueType()));
		return VmFieldName.get(builder.toString());
	}

	public static cc.recommenders.names.IFieldName convert(IPropertyName sstProperty) {
		if (sstProperty == null) {
			return null;
		}

		StringBuilder builder = new StringBuilder();
		builder.append(toName(sstProperty.getDeclaringType()));
		builder.append('.');
		builder.append(sstProperty.isUnknown() ? "unknown" : sstProperty.getName());
		builder.append(';');
		builder.append(toName(sstProperty.getValueType()));
		return VmFieldName.get(builder.toString());
	}

	public static cc.recommenders.names.IMethodName convert(IMethodName sstMethod) {
		if (sstMethod == null) {
			return null;
		}

		boolean isLambda = LANGUAGE_OPTIONS.isLambdaName(sstMethod);
		if (isLambda) {
			sstMethod = LANGUAGE_OPTIONS.removeLambda(sstMethod);
		}
		
		StringBuilder builder = new StringBuilder();
		ITypeName declaringType = sstMethod.getDeclaringType();
		if (declaringType.getName().equals("")) {
			throw new MissingTypeNameException(sstMethod);
		}
		builder.append(toName(declaringType));

		if (sstMethod.isConstructor()) {
			builder.append(".<init>(");
			builder.append(sstMethod.getParameters().stream().map(parameter -> toName(parameter.getValueType()) + ";")
					.collect(Collectors.joining()));
			builder.append(')');
			builder.append(VOID_NAME);
			builder.append(';');
		} else {
			builder.append('.');
			// TODO replace with isUnknown once fixed
			builder.append(MethodName.UNKNOWN_NAME.equals(sstMethod) ? "unknown" : sstMethod.getName());
			builder.append('(');
			builder.append(sstMethod.getParameters().stream().map(parameter -> toName(parameter.getValueType()) + ";")
					.collect(Collectors.joining()));
			builder.append(')');
			builder.append(toName(sstMethod.getReturnType()));
			builder.append(';');
		}

		String identifier = builder.toString();
		if (isLambda) {
			return addLambda(VmMethodName.get(identifier));
		}
		return VmMethodName.get(identifier);
	}

	public static boolean isUnknown(cc.recommenders.names.ITypeName type) {
		return type.getIdentifier().equals(UNKNOWN_NAME);
	}

	public static boolean isLambdaName(IName name) {
		return LambdaNameHelper.isLambdaName(name.getIdentifier());
	}

	public static cc.recommenders.names.IMethodName addLambda(cc.recommenders.names.IMethodName method) {
		return VmMethodName.get(LambdaNameHelper.addLambdaToMethodName(method.getIdentifier(), method.getName()));
	}

	public static cc.recommenders.names.ITypeName addLambda(cc.recommenders.names.ITypeName type) {
		return VmTypeName.get(LambdaNameHelper.addLambdaToTypeName(type.getIdentifier()));
	}

	public static cc.recommenders.names.IMethodName removeLambda(cc.recommenders.names.IMethodName method) {
		return VmMethodName.get(LambdaNameHelper.addLambdaToMethodName(method.getIdentifier(), method.getName()));
	}
}
