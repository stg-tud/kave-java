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
package cc.kave.commons.model.naming.impl.v0.types.organization;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.impl.v0.BaseName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.IAssemblyVersion;

public class AssemblyName extends BaseName implements IAssemblyName {

	public AssemblyName() {
		super(UNKNOWN_NAME_IDENTIFIER);
	}

	public AssemblyName(String identifier) {
		super(identifier);
	}

	@Override
	public IAssemblyVersion getVersion() {
		if (isUnknown()) {
			return new AssemblyVersion();
		} else if (!identifier.contains(",")) {
			return new AssemblyVersion();
		} else {
			return Names.newAssemblyVersion(identifier.substring(identifier.indexOf(",") + 2, identifier.length()));
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

	@Override
	public boolean isLocalProject() {
		// TODO Auto-generated method stub
		return false;
	}
}