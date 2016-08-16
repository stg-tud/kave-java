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
import static exec.recommender_reimplementation.java_printer.JavaNameUtils.transformDelegateTypeInMethodName;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.expressions.assignable.BinaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.visitor.IdentityVisitor;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class JavaTransformationVisitor extends IdentityVisitor<Void> {

	private static final List<Class<?>> statementTypesToDelete = Lists.newArrayList(IGotoStatement.class,
			IUnsafeBlock.class, IUnknownStatement.class, IEventSubscriptionStatement.class);
	private PropertyTransformationHelper propertyTransformationHelper;
	private ExpressionTransformationHelper expressionTransformationHelper;

	private static final String BOOLEAN_DEFAULT_VALUE = "false";

	public JavaTransformationVisitor(ISSTNode sstNode) {
		expressionTransformationHelper = new ExpressionTransformationHelper(sstNode);
		propertyTransformationHelper = new PropertyTransformationHelper();
	}

	public <T extends ISSTNode> T transform(T node, Class<T> type) {
		if (node == null || !type.isInstance(node)) {
			return node;
		}
		T transformedNode = type.cast(node.accept(this, null));
		transformedNode = JavaArrayTypeTransformer.transform(transformedNode);
		return transformedNode;
	}

	@SuppressWarnings("unchecked")
	public <T extends ISSTNode> T transform(T node) {
		if (node == null) {
			return node;
		}
		T transformedNode = (T) node.accept(this, null);
		transformedNode = JavaArrayTypeTransformer.transform(transformedNode);
		return transformedNode;
	}

	@Override
	public ISSTNode visit(ISST sst, Void context) {
		// removes DelegateDeclarations
		sst.getDelegates().clear();

		// removes EventDeclarations
		sst.getEvents().clear();

		// transforms and removes PropertyDeclarations
		Set<IPropertyDeclaration> properties = new HashSet<>(sst.getProperties());
		for (IPropertyDeclaration propertyDeclaration : properties) {
			propertyTransformationHelper.transformPropertyDeclaration(propertyDeclaration, sst);
			sst.getProperties().remove(propertyDeclaration);
		}

		return super.visit(sst, context);
	}

	@Override
	public ISSTNode visit(IMethodDeclaration methodDecl, Void context) {
		List<IStatement> statements = methodDecl.getBody();
		visit(statements, context);
		return methodDecl;
	}

	@Override
	public ISSTNode visit(IVariableDeclaration variableDecl, Void context) {
		expressionTransformationHelper.defaultValueHelper.addVariableReferenceToTypeMapping(variableDecl);
		return super.visit(variableDecl, context);
	}

	@Override
	public ISSTNode visit(IExpressionStatement stmt, Void context) {
		ExpressionStatement expressionStatement = (ExpressionStatement) stmt;
		if (expressionStatement.getExpression() instanceof IComposedExpression) {
			expressionStatement.setExpression(
					expressionTransformationHelper
							.transformComposedExpression((IComposedExpression) expressionStatement.getExpression()));
		}
		if (expressionStatement.getExpression() instanceof IIndexAccessExpression) {
			expressionStatement.setExpression(
					expressionTransformationHelper.transformIndexAccessExpression(
							(IIndexAccessExpression) expressionStatement.getExpression()));
		}
		return super.visit(stmt, context);
	}

	@Override
	public ISSTNode visit(IIfElseBlock block, Void context) {
		IfElseBlock ifElseBlock = (IfElseBlock) block;
		ifElseBlock.setCondition(expressionTransformationHelper.transformSimpleExpression(block.getCondition()));
		return super.visit(block, context);
	}

	@Override
	public ISSTNode visit(ISwitchBlock block, Void context) {
		super.visit(block, context);
		for (ICaseBlock caseBlock : block.getSections()) {
			CaseBlock caseBlockImpl = (CaseBlock) caseBlock;
			caseBlockImpl.setLabel(constant("0"));
		}
		return block;
	}

	@Override
	public ISSTNode visit(IBinaryExpression expr, Void context) {
		BinaryExpression binaryExpr = (BinaryExpression) expr;
		binaryExpr.setLeftOperand(expressionTransformationHelper.transformSimpleExpression(expr.getLeftOperand()));
		binaryExpr.setRightOperand(expressionTransformationHelper.transformSimpleExpression(expr.getRightOperand()));
		return super.visit(expr, context);
	}

	@Override
	public ISSTNode visit(IConstantValueExpression expr, Void context) {
		if (expr.getValue() == null || !expr.getValue().equals(BOOLEAN_DEFAULT_VALUE)) {
			expressionTransformationHelper.transformConstantValueExpression(expr);
		}
		return super.visit(expr, context);
	}


	@Override
	public ISSTNode visit(IInvocationExpression entity, Void context) {
		InvocationExpression invoke = (InvocationExpression) entity;
		invoke.setMethodName(transformDelegateTypeInMethodName(entity.getMethodName()));
		List<ISimpleExpression> parameters = invoke.getParameters();
		List<ISimpleExpression> parametersClone = new ArrayList<>(parameters);
		for (ISimpleExpression simpleExpression : parametersClone) {
			parameters.add(parameters.indexOf(simpleExpression),
					expressionTransformationHelper.transformSimpleExpression(simpleExpression));
			parameters.remove(simpleExpression);
		}
		return super.visit(entity, context);
	}

	@Override
	public ISSTNode visit(IReturnStatement stmt, Void context) {
		ReturnStatement returnStmt = (ReturnStatement) stmt;
		returnStmt.setExpression(expressionTransformationHelper.transformSimpleExpression(stmt.getExpression()));
		return super.visit(stmt, context);
	}

	@Override
	public ISSTNode visit(IUnaryExpression expr, Void context) {
		UnaryExpression unaryExpr = (UnaryExpression) expr;
		unaryExpr.setOperand(expressionTransformationHelper.transformSimpleExpression(expr.getOperand()));
		return super.visit(expr, context);
	}

	@Override
	public ISSTNode visit(IReferenceExpression expr, Void context) {
		// Transforms property references into field references
		if (expr.getReference() instanceof IPropertyReference) {
			propertyTransformationHelper.transformPropertyReference(expr);
		} else {
			expr.getReference().accept(this, context);
		}
		return expr;
	}

	private IStatement transformAssignment(Assignment assignment) {
		// Handle Property Get
		propertyTransformationHelper.transformLeftHandPropertyAssignment(assignment);

		// Handle right-hand Composed Expression
		if (assignment.getExpression() instanceof IComposedExpression) {
			assignment.setExpression(expressionTransformationHelper
					.transformComposedExpression((IComposedExpression) assignment.getExpression()));
		}

		// Handle right-hand IndexAccessExpression
		if (assignment.getExpression() instanceof IIndexAccessExpression) {
			assignment
					.setExpression(expressionTransformationHelper
							.transformIndexAccessExpression((IIndexAccessExpression) assignment.getExpression()));
		}

		// Handle right-hand Simple Expression
		if (assignment.getExpression() instanceof ISimpleExpression) {
			assignment.setExpression(expressionTransformationHelper
					.transformSimpleExpression((ISimpleExpression) assignment.getExpression()));
		}

		// Handle Property Set
		return propertyTransformationHelper.transformRightHandPropertyAssignment(assignment, this);
	}

	private boolean isStatementToDelete(IStatement statement) {
		for (Class<?> type : statementTypesToDelete) {
			if (type.isInstance(statement)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void visit(List<IStatement> statements, Void context) {
		List<IStatement> statementsClone = new ArrayList<>(statements);
		for (IStatement stmt : statementsClone) {
			if (isStatementToDelete(stmt)) {
				statements.remove(stmt);
				continue;
			}
			if (stmt instanceof IAssignment) {
				IStatement replacementNode = transformAssignment((Assignment) stmt);
				statements.add(statements.indexOf(stmt), replacementNode);
				statements.remove(stmt);
				replacementNode.accept(this, context);
				continue;
			}
			stmt.accept(this, context);
		}
	}

}
