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

import cc.kave.commons.model.naming.IName;
import cc.kave.commons.model.naming.codeelements.IAliasName;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.ILambdaName;
import cc.kave.commons.model.naming.codeelements.ILocalVariableName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.idecomponents.ICommandBarControlName;
import cc.kave.commons.model.naming.idecomponents.ICommandName;
import cc.kave.commons.model.naming.idecomponents.IDocumentName;
import cc.kave.commons.model.naming.idecomponents.IIDEComponentName;
import cc.kave.commons.model.naming.idecomponents.IProjectItemName;
import cc.kave.commons.model.naming.idecomponents.IProjectName;
import cc.kave.commons.model.naming.idecomponents.ISolutionName;
import cc.kave.commons.model.naming.idecomponents.IWindowName;
import cc.kave.commons.model.naming.others.IReSharperLiveTemplateName;
import cc.kave.commons.model.naming.types.IArrayTypeName;
import cc.kave.commons.model.naming.types.IDelegateTypeName;
import cc.kave.commons.model.naming.types.IPredefinedTypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.ITypeParameterName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.naming.types.organization.IAssemblyVersion;
import cc.kave.commons.model.naming.types.organization.INamespaceName;

public class FailsafeNameRewriter implements INameRewriter {

	@Override
	public IName rewrite(IName n) {
		return n;
	}

	@Override
	public IArrayTypeName rewrite(IArrayTypeName n) {
		return n;
	}

	@Override
	public IDelegateTypeName rewrite(IDelegateTypeName n) {
		return n;
	}

	@Override
	public IPredefinedTypeName rewrite(IPredefinedTypeName n) {
		return n;
	}

	@Override
	public ITypeName rewrite(ITypeName n) {
		return n;
	}

	@Override
	public ITypeParameterName rewrite(ITypeParameterName n) {
		return n;
	}

	@Override
	public IAssemblyName rewrite(IAssemblyName n) {
		return n;
	}

	@Override
	public IAssemblyVersion rewrite(IAssemblyVersion n) {
		return n;
	}

	@Override
	public INamespaceName rewrite(INamespaceName n) {
		return n;
	}

	@Override
	public IAliasName rewrite(IAliasName n) {
		return n;
	}

	@Override
	public IEventName rewrite(IEventName n) {
		return n;
	}

	@Override
	public IFieldName rewrite(IFieldName n) {
		return n;
	}

	@Override
	public ILambdaName rewrite(ILambdaName n) {
		return n;
	}

	@Override
	public ILocalVariableName rewrite(ILocalVariableName n) {
		return n;
	}

	@Override
	public IMethodName rewrite(IMethodName n) {
		return n;
	}

	@Override
	public IParameterName rewrite(IParameterName n) {
		return n;
	}

	@Override
	public IPropertyName rewrite(IPropertyName n) {
		return n;
	}

	@Override
	public ICommandBarControlName rewrite(ICommandBarControlName n) {
		return n;
	}

	@Override
	public ICommandName rewrite(ICommandName n) {
		return n;
	}

	@Override
	public IDocumentName rewrite(IDocumentName n) {
		return n;
	}

	@Override
	public IIDEComponentName rewrite(IIDEComponentName n) {
		return n;
	}

	@Override
	public IProjectItemName rewrite(IProjectItemName n) {
		return n;
	}

	@Override
	public IProjectName rewrite(IProjectName n) {
		return n;
	}

	@Override
	public ISolutionName rewrite(ISolutionName n) {
		return n;
	}

	@Override
	public IWindowName rewrite(IWindowName n) {
		return n;
	}

	@Override
	public IReSharperLiveTemplateName rewrite(IReSharperLiveTemplateName n) {
		return n;
	}
}