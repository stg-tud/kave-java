/**
 * Copyright 2016 Simon Reuß
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
package cc.kave.commons.pointsto.analysis;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;

public interface ScopingVisitorContext {

	void enterScope();

	void leaveScope();

	void declareParameter(ParameterName parameter, MethodName method);

	void declareParameter(ParameterName parameter, ILambdaExpression lambdaExpr);

	void declareParameter(ParameterName parameter, ICatchBlock catchBlock);

	void declarePropertySetParameter(IPropertyDeclaration propertyDecl);

	void declareVariable(IVariableDeclaration varDecl);

}
