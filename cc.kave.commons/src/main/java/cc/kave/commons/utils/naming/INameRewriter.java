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

public interface INameRewriter {

	public IName rewrite(IName n);

	// types

	public IArrayTypeName rewrite(IArrayTypeName n);

	public IDelegateTypeName rewrite(IDelegateTypeName n);

	public IPredefinedTypeName rewrite(IPredefinedTypeName n);

	public ITypeName rewrite(ITypeName n);

	public ITypeParameterName rewrite(ITypeParameterName n);

	// organization

	public IAssemblyName rewrite(IAssemblyName n);

	public IAssemblyVersion rewrite(IAssemblyVersion n);

	public INamespaceName rewrite(INamespaceName n);

	// code elements

	public IAliasName rewrite(IAliasName n);

	public IEventName rewrite(IEventName n);

	public IFieldName rewrite(IFieldName n);

	public ILambdaName rewrite(ILambdaName n);

	public ILocalVariableName rewrite(ILocalVariableName n);

	public IMethodName rewrite(IMethodName n);

	public IParameterName rewrite(IParameterName n);

	public IPropertyName rewrite(IPropertyName n);

	// ide components

	public ICommandBarControlName rewrite(ICommandBarControlName n);

	public ICommandName rewrite(ICommandName n);

	public IDocumentName rewrite(IDocumentName n);

	public IIDEComponentName rewrite(IIDEComponentName n);

	public IProjectItemName rewrite(IProjectItemName n);

	public IProjectName rewrite(IProjectName n);

	public ISolutionName rewrite(ISolutionName n);

	public IWindowName rewrite(IWindowName n);

	// others

	public IReSharperLiveTemplateName rewrite(IReSharperLiveTemplateName n);

}