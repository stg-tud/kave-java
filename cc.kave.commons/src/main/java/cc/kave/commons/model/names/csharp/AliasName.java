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

import cc.kave.commons.model.names.IAliasName;

/**
 * Aliases are defined by using statements, like "using alias = Some.Reference;"
 * . A special case is the alias "global" that represents the global namespace
 * by convention.
 */
public class AliasName extends Name implements IAliasName {
	private static final Map<String, AliasName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final IAliasName UNKNOWN_NAME = newAliasName(UNKNOWN_NAME_IDENTIFIER);

	/**
	 * Alias names are valid C# identifiers that are not keywords, plus the
	 * special alias 'global'.
	 */
	public static IAliasName newAliasName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new AliasName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private AliasName(String identifier) {
		super(identifier);
	}
}
