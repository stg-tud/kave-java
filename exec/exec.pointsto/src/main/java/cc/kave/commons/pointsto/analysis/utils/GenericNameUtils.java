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
package cc.kave.commons.pointsto.analysis.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import com.google.common.collect.Iterables;

import cc.kave.commons.model.names.IEventName;
import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.IMemberName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.EventName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.pointsto.analysis.exceptions.UnexpectedNameException;

public class GenericNameUtils {

	private static final String INSTANTIATION_ARROW = "->";

	@SuppressWarnings("unchecked")
	public static <T extends IMemberName> T eraseGenericInstantiations(T member) {
		if (member instanceof IMethodName) {
			return (T) eraseGenericInstantiations((IMethodName) member);
		} else if (member instanceof IPropertyName) {
			return (T) eraseGenericInstantiations((IPropertyName) member);
		} else if (member instanceof IFieldName) {
			return (T) eraseGenericInstantiations((IFieldName) member);
		} else if (member instanceof IEventName) {
			return (T) eraseGenericInstantiations((IEventName) member);
		} else {
			throw new UnexpectedNameException(member);
		}
	}

	public static IEventName eraseGenericInstantiations(IEventName event) {
		if (!event.getIdentifier().contains(INSTANTIATION_ARROW)) {
			return event;
		}

		return EventName
				.newEventName(replaceTypes(event.getIdentifier(), event.getHandlerType(), event.getDeclaringType()));
	}

	public static IFieldName eraseGenericInstantiations(IFieldName field) {
		if (!field.getIdentifier().contains(INSTANTIATION_ARROW)) {
			return field;
		}

		return FieldName
				.newFieldName(replaceTypes(field.getIdentifier(), field.getValueType(), field.getDeclaringType()));
	}

	public static IPropertyName eraseGenericInstantiations(IPropertyName property) {
		if (!property.getIdentifier().contains(INSTANTIATION_ARROW)) {
			return property;
		}

		return PropertyName.newPropertyName(
				replaceTypes(property.getIdentifier(), property.getValueType(), property.getDeclaringType()));
	}

	public static IMethodName eraseGenericInstantiations(IMethodName method) {
		if (!method.getIdentifier().contains(INSTANTIATION_ARROW)) {
			return method;
		}

		List<IParameterName> parameters = method.getParameters();
		List<ITypeName> parameterTypes = new ArrayList<>(parameters.size());
		for (IParameterName parameter : parameters) {
			parameterTypes.add(parameter.getValueType());
		}

		return MethodName.newMethodName(replaceTypes(method.getIdentifier(),
				Iterables.concat(Arrays.asList(method.getReturnType(), method.getDeclaringType()), parameterTypes)));
	}

	public static ITypeName eraseGenericInstantiations(ITypeName type) {
		String id = type.getIdentifier();
		if (!id.contains(INSTANTIATION_ARROW)) {
			return type;
		}

		for (ITypeName typeParam : type.getTypeParameters()) {
			String name = typeParam.getTypeParameterShortName();
			String pattern = name + " " + INSTANTIATION_ARROW + " "
					+ Pattern.quote(typeParam.getTypeParameterType().getIdentifier());
			id = id.replaceAll(pattern, name);
		}
		return TypeName.newTypeName(id);
	}

	private static String replaceTypes(String id, ITypeName... types) {
		return replaceTypes(id, Arrays.asList(types));
	}

	private static String replaceTypes(String id, Iterable<ITypeName> types) {
		for (ITypeName type : types) {
			String oldType = type.getIdentifier();
			String newType = eraseGenericInstantiations(type).getIdentifier();
			if (!oldType.equals(newType)) {
				id = id.replace("[" + oldType + "]", "[" + newType + "]");
			}
		}
		return id;
	}

}
