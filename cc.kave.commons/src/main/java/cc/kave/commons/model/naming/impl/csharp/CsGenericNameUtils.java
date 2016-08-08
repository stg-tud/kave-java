/**
 * Copyright 2015 Waldemar Graf
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

package cc.kave.commons.model.naming.impl.csharp;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;

public class CsGenericNameUtils {

	/// <summary>
	/// Parses the type parameter list from a type's full name or a method's
	/// signature.
	/// </summary>
	/// <param name="fullNameOrSignature">Expected to contain a type-parameter
	/// list.</param>
	public static List<ITypeParameterName> parseTypeParameters(String fullNameOrSignature) {
		ArrayList<ITypeParameterName> parameters = new ArrayList<ITypeParameterName>();
		int openBraces = 0;
		int startIndex = fullNameOrSignature.indexOf('[') + 1;
		for (int currentIndex = startIndex; currentIndex < fullNameOrSignature.length(); currentIndex++) {
			char c = fullNameOrSignature.charAt(currentIndex);

			if (c == '[') {
				openBraces++;
			} else if (c == ']') {
				openBraces--;

				if (openBraces == 0) {
					String descriptor = fullNameOrSignature.substring(startIndex + 1, currentIndex);
					ITypeName parameterTypeName = Names.newType(descriptor);
					parameters.add(parameterTypeName.asTypeParameterName());
					startIndex = fullNameOrSignature.indexOf('[', currentIndex);
				}
			}
		}
		return parameters;
	}

}
