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

import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;

public class JavaNameUtils {
	public static final Map<String,String> C_SHARP_TO_JAVA_VALUE_TYPE_MAPPING =
			ImmutableMap.<String, String>builder()
			.put("System.Boolean", "boolean")
			.put("System.SByte", "byte")
			.put("System.Byte", "byte")
			.put("System.UInt16", "short")
			.put("System.Int16", "short")
			.put("System.UInt32", "int")
			.put("System.Int32", "int")
			.put("System.UInt64", "long")
			.put("System.Int64", "long")
			.put("System.Double", "double")
			.put("System.Single", "float")
			.put("System.Float", "float")
			.put("System.Decimal","double")
			.put("System.Char", "char")
			.put("System.Object", "Object")
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
			String methodIdentifier = String.format("[%1$s] [%2$s].Delegate$%3$s(%4$s)", methodName.getReturnType(),
					methodName.getDeclaringType().getName(), "Invoke", parameterStr);
			return MethodName.newMethodName(methodIdentifier);
		}
		return methodName;
	}

	public static ITypeName transformDelegateTypeName(ITypeName delegateType) {
		return delegateType.getDeclaringType();
	}
}
