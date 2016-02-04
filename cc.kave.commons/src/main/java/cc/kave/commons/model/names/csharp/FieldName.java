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

import cc.kave.commons.model.names.IFieldName;

public class FieldName extends MemberName implements IFieldName {
	private static final Map<String, FieldName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final IFieldName UNKNOWN_NAME = newFieldName("[?] [?].???");

	public static IFieldName newFieldName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new FieldName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private FieldName(String identifier) {
		super(identifier);
	}

	@Override
	public boolean isUnknown() {
		return this.equals(UNKNOWN_NAME);
	}
}
