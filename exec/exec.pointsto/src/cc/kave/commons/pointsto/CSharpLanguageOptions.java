/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto;

import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;

public class CSharpLanguageOptions extends LanguageOptions {

	@Override
	public String getThisName() {
		return "this";
	}

	@Override
	public String getSuperName() {
		return "base";
	}

	@Override
	public TypeName getTopClass() {
		return CsTypeName.newTypeName("System.Object, mscorlib, 4.0.0.0");
	}

	@Override
	public TypeName getSuperType(ITypeHierarchy typeHierarchy) {
		if (typeHierarchy.hasSuperclass()) {
			return typeHierarchy.getExtends().getElement();
		} else {
			return this.getTopClass();
		}
	}
}
