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

import cc.kave.commons.model.names.INamespaceName;

public class NamespaceName extends Name implements INamespaceName {
	private static final Map<String, NamespaceName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final String GLOBAL_NAMESPACE_IDENTIFIER = "";
	public static final INamespaceName a = NamespaceName.newNamespaceName(GLOBAL_NAMESPACE_IDENTIFIER);
	public static final INamespaceName UNKNOWN_NAME = newNamespaceName(UNKNOWN_NAME_IDENTIFIER);

	public static INamespaceName newNamespaceName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new NamespaceName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private NamespaceName(String identifier) {
		super(identifier);
	}

	@Override
	public INamespaceName getParentNamespace() {
		if (isGlobalNamespace()) {
			return null;
		}

		int i = identifier.lastIndexOf(".");
		if (i == -1) {
			return NamespaceName.newNamespaceName(GLOBAL_NAMESPACE_IDENTIFIER);
		} else {
			return NamespaceName.newNamespaceName(identifier.substring(0, i));
		}
	}

	@Override
	public String getName() {
		return identifier.substring(identifier.lastIndexOf(".") + 1);
	}

	@Override
	public boolean isGlobalNamespace() {
		return identifier.equals(GLOBAL_NAMESPACE_IDENTIFIER);
	}

	public static INamespaceName getGlobalNamespace() {
		return NamespaceName.newNamespaceName(GLOBAL_NAMESPACE_IDENTIFIER);
	}

	public static INamespaceName getUnknownName() {
		return NamespaceName.newNamespaceName(Name.UNKNOWN_NAME_IDENTIFIER);
	}
}
