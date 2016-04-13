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
package cc.kave.commons.model.names.csharp;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.IAssemblyName;
import cc.kave.commons.model.names.IAssemblyVersion;

public class AssemblyName extends Name implements IAssemblyName {
	private static final Map<String, AssemblyName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final IAssemblyName UNKNOWN_NAME = newAssemblyName(UNKNOWN_NAME_IDENTIFIER);

	public static IAssemblyName newAssemblyName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new AssemblyName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private AssemblyName(String identifier) {
		super(identifier);
	}

	@Override
	public IAssemblyVersion getVersion() {
		if (isUnknown()) {
			return AssemblyVersion.UNKNOWN_NAME;
		} else if (!identifier.contains(",")) {
			return AssemblyVersion.UNKNOWN_NAME;
		} else {
			return AssemblyVersion
					.newAssemblyVersion(identifier.substring(identifier.indexOf(",") + 2, identifier.length()));
		}

	}

	@Override
	public String getName() {
		if (isUnknown()) {
			return identifier;
		} else if (!identifier.contains(",")) {
			return identifier;
		} else {
			return identifier.substring(0, identifier.indexOf(",")).trim();
		}
	}

	public static IAssemblyName getUnknownName() {
		return AssemblyName.newAssemblyName(UNKNOWN_NAME_IDENTIFIER);
	}

	public boolean isUnknown() {
		return this.equals(getUnknownName());
	}
}
