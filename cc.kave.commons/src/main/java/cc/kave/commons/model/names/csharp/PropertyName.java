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

import cc.kave.commons.model.names.IPropertyName;

public class PropertyName extends MemberName implements IPropertyName {
	private static final Map<String, PropertyName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final IPropertyName UNKNOWN_NAME = newPropertyName("[?] [?].???");
	public final String SETTER_MODIFIER = "set";
	public final String GETTER_MODIFIER = "get";

	public static IPropertyName newPropertyName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new PropertyName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	@Override
	public boolean isUnknown() {
		return this.equals(UNKNOWN_NAME);
	}

	private PropertyName(String identifier) {
		super(identifier);
	}

	@Override
	public boolean hasSetter() {
		return getModifiers().contains(SETTER_MODIFIER);
	}

	@Override
	public boolean hasGetter() {
		return getModifiers().contains(GETTER_MODIFIER);
	}
}
