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

import cc.kave.commons.model.names.IBundleVersion;

import com.google.common.collect.MapMaker;

public class AssemblyVersion extends Name implements IBundleVersion {
	private static final Map<String, AssemblyVersion> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final IBundleVersion UNKNOWN_NAME = newAssemblyVersion(UNKNOWN_NAME_IDENTIFIER);

	/**
	 * Alias names are valid C# identifiers that are not keywords, plus the
	 * special alias 'global'.
	 */
	public static IBundleVersion newAssemblyVersion(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new AssemblyVersion(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private AssemblyVersion(String identifier) {
		super(identifier);
	}

	@Override
	public int getMajor() {
		return splitVersion()[0];
	}

	@Override
	public int getMinor() {
		return splitVersion()[1];
	}

	@Override
	public int getBuild() {
		return splitVersion()[2];
	}

	@Override
	public int getRevision() {
		return splitVersion()[3];
	}

	private int[] splitVersion() {
		int[] versions = new int[4];
		String[] split = identifier.split("\\.");
		for (int i = 0; i < split.length; i++) {
			versions[i] = Integer.valueOf(split[i]);
		}
		return versions;
	}
}
