/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.recommender_reimplementation.java_printer;

import java.util.Arrays;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;


public class JavaNameUtils {
	public static final Map<String,String> C_SHARP_TO_JAVA_VALUE_TYPE_MAPPING =
			ImmutableMap.<String, String>builder()
					.put("p:bool", "boolean")
			.build();
	
    public static String getTypeAliasFromFullTypeName(String typeName)
    {
        if (C_SHARP_TO_JAVA_VALUE_TYPE_MAPPING.containsKey(typeName))
        {
            return C_SHARP_TO_JAVA_VALUE_TYPE_MAPPING.get(typeName);
        }
        return typeName;
    }

	public static IMethodName transformDelegateTypeInMethodName(IMethodName methodName) {
		if (methodName.getDeclaringType().isDelegateType()) {
			String parameterStr = Joiner.on(", ")
					.join(methodName.getParameters().stream().map(p -> p.getIdentifier()).toArray());
			String methodIdentifier = String.format("[%1$s] [%2$s, %3$s].Delegate$%4$s(%5$s)",
					methodName.getReturnType().getIdentifier(),
					methodName.getDeclaringType().getName(), methodName.getDeclaringType().getAssembly().getIdentifier(), "Invoke", parameterStr);
			return Names.newMethod(methodIdentifier);
		}
		return methodName;
	}

	public static ITypeName transformDelegateTypeName(ITypeName delegateType) {
		return delegateType.getDeclaringType();
	}

	public static IFieldName createFieldName(ITypeName valType, ITypeName declType, String fieldName,
			boolean isStatic) {
		String staticModifier = isStatic ? "static " : "";
		String field = String.format("%1$s[%2$s] [%3$s].%4$s", staticModifier, valType.getIdentifier(),
				declType.getIdentifier(), fieldName);
		return Names.newField(field);
	}

	public static IMethodName createMethodName(ITypeName returnType, ITypeName declType, String simpleName,
			boolean isStatic,
			IParameterName... parameters) {
		String staticModifier = isStatic ? "static " : "";
		String parameterStr = Joiner.on(", ")
				.join(Arrays.asList(parameters).stream().map(p -> p.getIdentifier()).toArray());
		String methodIdentifier = String.format("%1$s[%2$s] [%3$s].%4$s(%5$s)", staticModifier,
				returnType.getIdentifier(), declType.getIdentifier(),
				simpleName,
				parameterStr);
		return Names.newMethod(methodIdentifier);
	}

}
