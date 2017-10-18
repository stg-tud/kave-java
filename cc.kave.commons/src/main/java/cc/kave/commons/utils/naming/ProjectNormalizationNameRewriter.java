/**
 * Copyright 2017 University of Zurich
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.commons.utils.naming;

import java.util.List;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.impl.v0.types.organization.AssemblyName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.utils.TypeErasure;

public class ProjectNormalizationNameRewriter extends FailsafeNameRewriter {

	@Override
	public IMethodName rewrite(IMethodName n) {
		
		IMethodName erased = erase(n);
		
		ITypeName retType = erased.getReturnType();
		String normReturn = normalize(erased.getIdentifier(), retType);

		ITypeName declType = erased.getDeclaringType();
		String normType = normalize(normReturn, declType);
		
		List<IParameterName> paramTypes = erased.getParameters();
		
		for (IParameterName pm : paramTypes) {
			ITypeName type = pm.getValueType();
			normType = normalize(normType, type);
		}
		return Names.newMethod(normType);
	}
	
	@Override
	public ITypeName rewrite(ITypeName n) {
		
		IAssemblyName asm = n.getAssembly();
		IAssemblyName normAsm = rewrite(asm);

		String normType = n.getIdentifier().replace(asm.getIdentifier(),
				normAsm.getIdentifier());
		return Names.newType(normType);
	}

	@Override
	public IAssemblyName rewrite(IAssemblyName n) {
		if (n.isLocalProject()) {
			return new AssemblyName();
		}
		return n;
	}
	
	private String normalize(String method, ITypeName type) {
		ITypeName normType = rewrite(type);
		
		String normName = method.replace(type.getIdentifier(), normType.getIdentifier());
		
		return normName;
	}
	
	private IMethodName erase(IMethodName methodName) {
		IMethodName erased = TypeErasure.of(methodName);
		return erased;
	}

	// TODO: all the other code elements that are relevant...
}