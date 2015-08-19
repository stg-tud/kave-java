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

import cc.kave.commons.model.names.TypeName;

public class CsEnumTypeName extends CsTypeName {

	public CsEnumTypeName(String identifier) {
		super(identifier);
	}

	private static final String NAME_PREFIX = "e:";

	static boolean isEnumTypeIdentifier(String identifier) {
		return identifier.startsWith(NAME_PREFIX);
	}

	static TypeName newCsEnumTypeName(String identifier) {
		return CsTypeName.newTypeName(identifier);
	}

	@Override
	public String getFullName() {
		return super.getFullName().substring(NAME_PREFIX.length());
	}

	@Override
	public boolean isEnumType() {
		return true;
	}

}
