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

package cc.kave.commons.model.naming.impl.v0.types;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.IArrayTypeName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.IPredefinedTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;

public class ArrayTypeName extends TypeName implements IArrayTypeName {

	private static final Pattern ArrayTypeNameSuffix = Pattern.compile(".*(\\[[,]*\\])([^()]*).*");

	protected ArrayTypeName(String identifier) {
		super(identifier);
	}

	static ITypeName newCsArrayTypeName(String identifier) {
		return new TypeName(identifier);
	}

	static boolean isArrayTypeIdentifier(String id) {
		if (id.startsWith("d:")) {
			int idx = id.lastIndexOf(')');
			if (id.length() > (idx + 1) && id.charAt(idx + 1) == '[') {
				return true;
			}
		} else {
			int idx = id.indexOf('[');
			if (idx == -1) {
				return false;
			}
			while ((idx + 1) < id.length() && id.charAt(++idx) == ',') {
			}
			if (id.charAt(idx) == ']') {
				return true;
			}
		}

		return false;
	}

	private static String deriveArrayTypeNameIdentifier(ITypeName baseType, int rank) {
		if (baseType.isArray()) {
			IArrayTypeName baseArr = baseType.asArrayTypeName();
			baseType = baseArr.getArrayBaseType();
			rank += getArrayRank(baseType);
		}

		String identifier = baseType.getIdentifier();
		String arrayMarker = createArrayMarker(rank);

		String derivedIdentifier;
		if (baseType.isDelegateType()) {
			derivedIdentifier = identifier + arrayMarker;
		} else {
			derivedIdentifier = insertMarkerAfterRawName(identifier, arrayMarker);
		}
		return derivedIdentifier;
	}

	private static String createArrayMarker(int rank) {
		StringBuilder marker = new StringBuilder();

		for (int i = 0; i < rank - 1; i++) {
			marker.append(",");
		}

		return String.format("[%s]", marker.toString());

	}

	private static String insertMarkerAfterRawName(String identifier, String arrayMarker) {
		int endOfRawName = identifier.indexOf('[');
		if (endOfRawName < 0) {
			endOfRawName = identifier.indexOf(',');
		}
		if (endOfRawName < 0) {
			endOfRawName = identifier.length();
		}

		return new StringBuilder(identifier).insert(endOfRawName, arrayMarker).toString();
	}

	public static ITypeName from(ITypeName baseType, int rank) {
		ITypeName typeName = Names.newType(deriveArrayTypeNameIdentifier(baseType, rank));
		return (ArrayTypeName) typeName;
	}

	public static int getArrayRank(ITypeName arrayTypeName) {
		Matcher matcher = ArrayTypeNameSuffix.matcher(arrayTypeName.getIdentifier());
		if (!matcher.matches()) {
			throw new RuntimeException("Invalid Signature Syntax: " + arrayTypeName.getIdentifier());
		}
		String group = matcher.group(1);
		int count = 1;

		for (int i = 0; i < group.length(); i++) {
			if (group.charAt(i) == ',') {
				count++;
			}
		}
		return count;
	}

	@Override
	public boolean isArray() {
		return true;
	}

	@Override
	public String getFullName() {
		return getArrayBaseType().getFullName() + createArrayMarker(getRank());
	}

	public int getRank() {
		return getArrayRank(this);

	}

	@Override
	public ITypeName getArrayBaseType() {

		String id = identifier;
		int startIdx = -1;
		int endIdx = -1;

		int idx = id.startsWith("d:") ? id.lastIndexOf(')') + 1 : id.indexOf('[');

		startIdx = idx;
		while (id.charAt(++idx) == ',') {
		}
		endIdx = idx;

		String newId = id.substring(0, startIdx) + id.substring(endIdx + 1);
		return Names.newType(newId);

	}

	@Override
	public INamespaceName getNamespace() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IDelegateTypeName asDelegateTypeName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IArrayTypeName asArrayTypeName() {
		return this;
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

	@Override
	public boolean isHashed() {
		// TODO Auto-generated method stub
		return false;
	}
}
