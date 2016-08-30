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
import static cc.kave.commons.model.ssts.impl.SSTUtil.expr;

import com.google.common.collect.Lists;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class ExpressionTransformationHelper {

	private static final String OBJECT_TYPE_IDENTIFIER = "p:object";

	public static final String INT_TYPE_IDENTIFIER = "p:int";

	public DefaultValueHelper defaultValueHelper;

	public ExpressionTransformationHelper(ISSTNode sstNode) {
		defaultValueHelper = new DefaultValueHelper(sstNode);
	}

	public IAssignableExpression transformComposedExpression(IComposedExpression expression) {
		return constant(defaultValueHelper.getDefaultValueForNode(expression));
	}

	public IAssignableExpression transformBinaryExpression(IBinaryExpression expression) {
		return constant(defaultValueHelper.getDefaultValueForNode(expression));
	}

	public IAssignableExpression transformUnaryExpression(IUnaryExpression expression) {
		return constant(defaultValueHelper.getDefaultValueForNode(expression));
	}

	public IAssignableExpression transformLambdaExpression(ILambdaExpression expression) {
		return constant(defaultValueHelper.getDefaultValueForNode(expression));
	}

	public IAssignableExpression transformIndexAccessExpression(IIndexAccessExpression expr) {
		ITypeName containerType = defaultValueHelper.getTypeForVariableReference(expr.getReference());
		if (containerType != null) {
			InvocationExpression invocation = new InvocationExpression();
			invocation.setMethodName(createIndexAccessGetMethodName(containerType));
			invocation.setReference(expr.getReference());
			invocation.setParameters(expr.getIndices());
			return invocation;
		}
		return expr;
	}

	public ISimpleExpression transformSimpleExpression(ISimpleExpression expr) {
		if (expr instanceof IUnknownExpression) {
			return constant(defaultValueHelper.getDefaultValueForNode(expr));
		}
		if (expr instanceof IReferenceExpression) {
			IReferenceExpression refExpr = (IReferenceExpression) expr;
			if (refExpr.getReference() instanceof IUnknownReference) {
				return constant("null");
			}
		}
		return expr;
	}

	public void transformConstantValueExpression(IConstantValueExpression expr) {
		ConstantValueExpression constantValueExpression = (ConstantValueExpression) expr;
		constantValueExpression.setValue(defaultValueHelper.getDefaultValueForNode(expr));
	}

	private IMethodName createIndexAccessGetMethodName(ITypeName containerType) {
		return Names.newMethod(
				String.format("[%1$s] [%2$s].%3$s([%4$s] index)",
						getBaseTypeFromTransformedArrayName(containerType).getIdentifier(),
						containerType.getIdentifier(),
						"get", INT_TYPE_IDENTIFIER));
	}

	private IMethodName createIndexAccessSetMethodName(ITypeName containerType) {
		return Names.newMethod(String.format("[%1$s] [%2$s].%3$s([%4$s] index, [%5$s] value)",
				getBaseTypeFromTransformedArrayName(containerType).getIdentifier(), containerType.getIdentifier(), "set", INT_TYPE_IDENTIFIER,
				OBJECT_TYPE_IDENTIFIER));
	}
	private ITypeName getBaseTypeFromTransformedArrayName(ITypeName containerType) {
		if (containerType.isArray()) {
			return containerType.asArrayTypeName().getArrayBaseType();
		}
		return Names.newType(OBJECT_TYPE_IDENTIFIER);
	}

	public IStatement transformIndexAccessReference(IIndexAccessReference reference) {
		IIndexAccessExpression indexAccessExpression = reference.getExpression();
		ITypeName containerType = defaultValueHelper.getTypeForVariableReference(indexAccessExpression.getReference());
		IMethodName methodName;
		if (containerType != null) {
			methodName = createIndexAccessSetMethodName(containerType);
		} else {
			methodName = createIndexAccessSetMethodName(Names.newType(OBJECT_TYPE_IDENTIFIER));
		}

		InvocationExpression invocation = new InvocationExpression();
		invocation.setMethodName(methodName);
		invocation.setReference(indexAccessExpression.getReference());
		invocation.setParameters(Lists.newArrayList(constant("null"), constant("null")));
		return expr(invocation);
	}

}
