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

import cc.kave.commons.model.names.ITypeName;

public class StructTypeName extends TypeName {

	protected StructTypeName(String identifier) {
		super(identifier);
	}

	private static final String NamePrefix = "s:";

	static boolean isStructTypeIdentifier(String identifier) {
		return identifier.startsWith(NamePrefix) || isSimpleTypeIdentifier(identifier)
				|| isNullableTypeIdentifier(identifier) || isVoidTypeIdentifier(identifier);
	}

	static ITypeName newCsStructTypeName(String identifier) {
		return TypeName.newTypeName(identifier);
	}

	@Override
	public String getFullName() {

		String fullName = super.getFullName();
		if (fullName.startsWith(NamePrefix)) {
			fullName = fullName.substring(NamePrefix.length());
		}
		return fullName;

	}

	@Override
	public boolean isUnknownType() {
		return false;
	}

	@Override
	public boolean isVoidType() {
		return isVoidTypeIdentifier(identifier);
	}

	private static boolean isVoidTypeIdentifier(String identifier) {
		return identifier.startsWith("System.Void,");
	}

	@Override
	public boolean isStructType() {
		return true;
	}

	@Override
	public boolean isEnumType() {
		return false;
	}

	@Override
	public boolean isSimpleType() {
		return isSimpleTypeIdentifier(identifier);
	}

	private static boolean isSimpleTypeIdentifier(String identifier) {
		return isNumericTypeName(identifier) || identifier.startsWith("System.Boolean,");
	}

	private static boolean isNumericTypeName(String identifier) {
		return isIntegralTypeName(identifier) || isFloatingPointTypeName(identifier)
				|| identifier.startsWith("System.Decimal,");
	}

	private static final String[] IntegralTypeNames = { "System.SByte,", "System.Byte,", "System.Int16,",
			"System.UInt16,", "System.Int32,", "System.UInt32,", "System.Int64,", "System.UInt64,", "System.Char," };

	private static boolean isIntegralTypeName(String identifier) {
		for (int i = 0; i < IntegralTypeNames.length; i++) {
			if (identifier.startsWith(IntegralTypeNames[i])) {
				return true;
			}
		}
		return false;
	}

	private static final String[] FloatingPointTypeNames = { "System.Single,", "System.Double," };

	private static boolean isFloatingPointTypeName(String identifier) {
		for (int i = 0; i < FloatingPointTypeNames.length; i++) {
			if (identifier.startsWith(FloatingPointTypeNames[i])) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isNullableType() {
		return isNullableTypeIdentifier(identifier);
	}

	private static boolean isNullableTypeIdentifier(String identifier) {
		return identifier.startsWith("System.Nullable`1[[");
	}

	@Override
	public boolean isArrayType() {
		return false;
	}

}
