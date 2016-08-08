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
package cc.kave.commons.model.naming.impl.v0.codeelements;

import cc.kave.commons.model.naming.codeelements.IAliasName;
import cc.kave.commons.model.naming.impl.v0.BaseName;

/**
 * Aliases are defined by using statements, like "using alias = Some.Reference;"
 * . A special case is the alias "global" that represents the global namespace
 * by convention.
 */
public class AliasName extends BaseName implements IAliasName {

	private AliasName() {
		this(UNKNOWN_NAME_IDENTIFIER);
	}

	private AliasName(String identifier) {
		super(identifier);
	}
}