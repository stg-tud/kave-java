/**
 * Copyright 2016 Technische Universität Darmstadt
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
package exec.recommender_reimplementation.java_transformation;

import static cc.kave.commons.model.ssts.impl.SSTUtil.constant;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class ExpressionTransformationHelper {

	public DefaultValueHelper defaultValueHelper;

	public ExpressionTransformationHelper(ISSTNode sstNode) {
		defaultValueHelper = new DefaultValueHelper(sstNode);

	}

	public IAssignableExpression transformComposedExpression(IComposedExpression expression) {
		return constant(defaultValueHelper.getDefaultValueForNode(expression));
	}

	public IAssignableExpression transformIndexAccessExpression(IIndexAccessExpression expr) {
		ITypeName containerType = defaultValueHelper.getTypeForVariableReference(expr.getReference());
		if (containerType != null) {
			if (containerType.isArrayType()) {
				return expr;
			} else {
				InvocationExpression invocation = new InvocationExpression();
				invocation.setMethodName(createIndexAccessMethodName(containerType));
				invocation.setReference(expr.getReference());
				invocation.setParameters(expr.getIndices());
				return invocation;
			}
		}
		return expr;
	}

	public ISimpleExpression transformSimpleExpression(ISimpleExpression expr) {
		if (expr instanceof IUnknownExpression) {
			return constant(defaultValueHelper.getDefaultValueForNode(expr));
		}
		return expr;
	}

	public void transformConstantValueExpression(IConstantValueExpression expr) {
		ConstantValueExpression constantValueExpression = (ConstantValueExpression) expr;
		constantValueExpression.setValue(defaultValueHelper.getDefaultValueForNode(expr));
	}

	private IMethodName createIndexAccessMethodName(ITypeName containerType) {
		return MethodName.newMethodName(
				String.format("[%1$s] [%2$s].%3$s()", containerType.getTypeParameterType(), containerType, "get"));
	}
}
