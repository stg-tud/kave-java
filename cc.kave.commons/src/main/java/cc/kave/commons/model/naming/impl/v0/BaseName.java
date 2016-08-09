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
package cc.kave.commons.model.naming.impl.v0;

import cc.kave.commons.model.naming.IName;

public abstract class BaseName implements IName {

	protected static final String UNKNOWN_NAME_IDENTIFIER = "???";

	protected String identifier;

	protected BaseName(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public final String getIdentifier() {
		return identifier;
	}

	@Override
	public boolean isHashed() {
		return false;
	}

	@Override
	public boolean isUnknown() {
		return this.getIdentifier().equals(UNKNOWN_NAME_IDENTIFIER);
	}

	@Override
	public final String toString() {
		return identifier;
	}
}
