/**
 * Copyright 2014 Technische Universität Darmstadt
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
package cc.kave.commons.model.names.resharper;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.Name;

public class LiveTemplateName implements Name {
	private static final Map<String, LiveTemplateName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static LiveTemplateName newLiveTemplateName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new LiveTemplateName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private String identifier;

	protected LiveTemplateName(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public boolean isUnknown() {
		return false;
	}

	// TODO write utility methods
}