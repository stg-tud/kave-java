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

import cc.kave.commons.model.names.IEventName;
import cc.kave.commons.model.names.ITypeName;

public class EventName extends MemberName implements IEventName {
	private static final Map<String, EventName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final IEventName UNKNOWN_NAME = newEventName("[?] [?].???");

	public static IEventName newEventName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new EventName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private EventName(String identifier) {
		super(identifier);
	}

	@Override
	public ITypeName getHandlerType() {
		return getValueType();
	}

	public boolean isUnknown() {
		return this.equals(UNKNOWN_NAME);
	}
}
