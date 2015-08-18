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

package cc.kave.commons.model.names.csharp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.kave.commons.model.names.BundleName;
import cc.kave.commons.model.names.TypeName;

public class CsArrayTypeName extends CsTypeName {

	private static final Pattern ArrayTypeNameSuffix = Pattern.compile(".*(\\[[,]*\\])([^()]*).*");

	protected CsArrayTypeName(String identifier) {
		super(identifier);
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

	private static String deriveArrayTypeNameIdentifier(TypeName baseType, int rank) {
		if (baseType.isArrayType()) {
			rank += getArrayRank(baseType);
			baseType = baseType.getArrayBaseType();
		}

		String identifier = baseType.getIdentifier();
		String arrayMarker = CreateArrayMarker(rank);

		String derivedIdentifier;
		if (baseType.isDelegateType()) {
			derivedIdentifier = identifier + arrayMarker;
		} else {
			derivedIdentifier = InsertMarkerAfterRawName(identifier, arrayMarker);
		}
		return derivedIdentifier;
	}

	private static String CreateArrayMarker(int rank) {
		return String.format("[{%s}]", ",", rank - 1);
	}

	private static String InsertMarkerAfterRawName(String identifier, String arrayMarker) {
		int endOfRawName = identifier.indexOf('[');
		if (endOfRawName < 0) {
			endOfRawName = identifier.indexOf(',');
		}
		if (endOfRawName < 0) {
			endOfRawName = identifier.length();
		}

		return new StringBuilder(identifier).insert(endOfRawName, arrayMarker).toString();
	}

	public static CsArrayTypeName from(TypeName baseType, int rank) {
		// TODO: ???
		// Asserts.That(rank > 0, "rank smaller than 1");
		TypeName typeName = CsArrayTypeName.newTypeName(deriveArrayTypeNameIdentifier(baseType, rank));
		return (CsArrayTypeName) typeName;
	}

	// TODO:
	public static int getArrayRank(TypeName arrayTypeName) {

		Matcher matcher = ArrayTypeNameSuffix.matcher(arrayTypeName.getIdentifier());
		if (matcher.matches()) {
			String group = matcher.group(1);
			int count = 1;

			for (int i = 0; i < group.length(); i++) {
				if (group.charAt(i) == ',') {
					count++;
				}
			}
			return count;
		} else {
			throw new RuntimeException("Invalid ArrayTypeName Identifier: " + arrayTypeName.getIdentifier());
		}
	}

	@Override
	public boolean isArrayType() {
		return true;
	}

	@Override
	public String getFullName() {
		return getArrayBaseType().getFullName() + CreateArrayMarker(Rank());
	}

	public int Rank() {
		return getArrayRank(this);

	}

	@Override
	public BundleName getAssembly() {
		return getArrayBaseType().getAssembly();
	}

	@Override
	public TypeName getArrayBaseType() {

		String id = identifier;
		int startIdx = -1;
		int endIdx = -1;

		int idx = id.startsWith("d:") ? id.lastIndexOf(')') + 1 : id.indexOf('[');

		startIdx = idx;
		while (id.charAt(++idx) == ',') {
		}
		endIdx = idx;

		String newId = id.substring(0, startIdx) + id.substring(endIdx + 1);
		return CsTypeName.newTypeName(newId);

	}
}
