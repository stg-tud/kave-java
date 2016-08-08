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
package cc.kave.commons.model.naming.impl.v0.types;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.impl.csharp.CsGenericNameUtils;
import cc.kave.commons.model.naming.impl.v0.types.organization.NamespaceName;
import cc.kave.commons.model.naming.types.IArrayTypeName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.IPredefinedTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;

public class TypeName extends BaseTypeName implements ITypeName {

	public static final ITypeName UNKNOWN_NAME = newTypeName("?");

	protected TypeName(String identifier) {
		super(identifier);
	}

	@Override
	public boolean hasTypeParameters() {
		return getFullName().indexOf("[[") > -1;
	}

	@Override
	public List<ITypeParameterName> getTypeParameters() {
		return hasTypeParameters() ? CsGenericNameUtils.parseTypeParameters(getFullName())
				: new ArrayList<ITypeParameterName>();
	}

	@Override
	public IAssemblyName getAssembly() {
		int endOfTypeName = getLengthOfTypeName(identifier);
		String assemblyIdentifier = identifier.substring(endOfTypeName).trim();
		assemblyIdentifier = assemblyIdentifier.replaceFirst(",", "").trim();
		return Names.newAssembly(assemblyIdentifier);
	}

	@Override
	public INamespaceName getNamespace() {
		String id = getRawFullName();
		int endIndexOfNamespaceIdentifier = id.lastIndexOf('.');
		return endIndexOfNamespaceIdentifier < 0 ? NamespaceName.getGlobalNamespace()
				: NamespaceName.newNamespaceName(id.substring(0, endIndexOfNamespaceIdentifier));
	}

	@Override
	public ITypeName getDeclaringType() {

		if (!isNestedType()) {
			return null;
		}

		String fullName = getFullName();
		if (hasTypeParameters()) {
			fullName = takeUntilChar(fullName, new char[] { '[', ',' });
		}
		int endOfDeclaringTypeName = fullName.lastIndexOf('+');
		if (endOfDeclaringTypeName == -1) {
			return UNKNOWN_NAME;
		}

		String declaringTypeName = fullName.substring(0, endOfDeclaringTypeName);
		if (declaringTypeName.indexOf('`') > -1 && hasTypeParameters()) {
			int startIndex = 0;
			int numberOfParameters = 0;

			while ((startIndex = declaringTypeName.indexOf('`', startIndex) + 1) > 0) {
				int endIndex = declaringTypeName.indexOf('+', startIndex);
				if (endIndex > -1) {
					numberOfParameters += Integer.parseInt(declaringTypeName.substring(startIndex, endIndex));
				} else {
					numberOfParameters += Integer.parseInt(declaringTypeName.substring(startIndex));
				}

			}

			List<ITypeParameterName> outerTypeParameters = getTypeParameters().subList(0, numberOfParameters);
			declaringTypeName += "[[";

			for (ITypeName typeName : outerTypeParameters) {
				declaringTypeName += typeName.getIdentifier() + "],[";
			}

			declaringTypeName = declaringTypeName.substring(0, declaringTypeName.length() - 3);
			declaringTypeName += "]]";
		}
		return newTypeName(declaringTypeName + ", " + getAssembly());
	}

	private String takeUntilChar(String fullName, char[] stopChars) {
		int i = 0;
		for (char c : getFullName().toCharArray()) {
			for (int j = 0; j < stopChars.length; j++) {
				if (stopChars[j] == c) {
					return fullName.substring(0, i);
				}
			}
			i++;
		}

		return fullName.substring(0, i);
	}

	@Override
	public String getFullName() {
		int length = getLengthOfTypeName(identifier);
		return identifier.substring(0, length);
	}

	protected static int getLengthOfTypeName(String identifier) {
		if (TypeUtils.isUnknownTypeIdentifier(identifier)) {
			return identifier.length();
		}
		int length = identifier.lastIndexOf(']') + 1;
		if (length > 0) {
			return length;
		}
		return identifier.indexOf(',');
	}

	@Override
	public String getName() {
		String rawFullName = getRawFullName();
		int endOfOutTypeName = rawFullName.lastIndexOf('+');

		if (endOfOutTypeName > -1) {
			rawFullName = rawFullName.substring(endOfOutTypeName + 1);
		}

		int endOfTypeName = rawFullName.lastIndexOf('`');

		if (endOfTypeName > -1) {
			rawFullName = rawFullName.substring(0, endOfTypeName);
		}

		int startIndexOfSimpleName = rawFullName.lastIndexOf('.');
		return rawFullName.substring(startIndexOfSimpleName + 1);
	}

	public String getRawFullName() {
		String fullName = getFullName();
		int indexOfGenericList = fullName.indexOf("[[");

		if (indexOfGenericList < 0) {
			indexOfGenericList = fullName.indexOf(", ");
		}
		return indexOfGenericList < 0 ? fullName : fullName.substring(0, indexOfGenericList);
	}

	@Override
	public boolean isVoidType() {
		return false;
	}

	@Override
	public boolean isValueType() {
		return isStructType() || isEnumType() || isVoidType();
	}

	@Override
	public boolean isSimpleType() {
		return false;
	}

	@Override
	public boolean isEnumType() {
		return false;
	}

	@Override
	public boolean isStructType() {
		return false;
	}

	@Override
	public boolean isNullableType() {
		return false;
	}

	@Override
	public boolean isReferenceType() {
		return isClassType() || isInterfaceType() || isArray() || isDelegateType();
	}

	@Override
	public boolean isClassType() {
		return !isValueType() && !isInterfaceType() && !isArray() && !isDelegateType() && !isUnknown();
	}

	@Override
	public boolean isInterfaceType() {
		return false;
	}

	@Override
	public boolean isDelegateType() {
		return false;
	}

	@Override
	public boolean isNestedType() {
		return getRawFullName().contains("+");
	}

	@Override
	public boolean isTypeParameter() {
		return false;
	}

	@Override
	public boolean isUnknown() {
		return this.equals(UNKNOWN_NAME);
	}

	public static ITypeName newTypeName(String string, Object... args) {
		return newTypeName(String.format(string, args));
	}

	@Override
	public IDelegateTypeName asDelegateTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isArray() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IArrayTypeName asArrayTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITypeParameterName asTypeParameterName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPredefined() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IPredefinedTypeName asPredefinedTypeName() {
		// TODO Auto-generated method stub
		return null;
	}
}