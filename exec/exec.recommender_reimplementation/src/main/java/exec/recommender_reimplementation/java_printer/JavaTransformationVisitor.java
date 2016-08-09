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
package exec.recommender_reimplementation.java_printer;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.constant;
import static cc.kave.commons.model.ssts.impl.SSTUtil.expr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.invocationExpression;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.returnStatement;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.BinaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class JavaTransformationVisitor extends TraversingIdentityVisitor<Void> {

	public static final String GET_PROPERTY_METHOD_NAME_FORMAT = "[%1$s] [%2$s].%3$s$%4$s()";
	public static final String SET_PROPERTY_METHOD_NAME_FORMAT = "[%1$s] [%2$s].%3$s$%4$s([%5$s] value)";
	public static final String PROPERTY_FIELD_NAME_FORMAT = "[%1$s] [%2$s].Property$%3$s";
	public static final ITypeName VOID_TYPE = TypeName.newTypeName("System.Void, mscorlib, 4.0.0.0");

	private static final List<Class<?>> statementTypesToDelete = Lists.newArrayList(IGotoStatement.class,
			IUnsafeBlock.class, IUnknownStatement.class, IEventSubscriptionStatement.class);
	private DefaultValueHelper defaultValueHelper;

	public JavaTransformationVisitor(ISSTNode sstNode) {
		defaultValueHelper = new DefaultValueHelper(sstNode);
	}

	public <T extends ISSTNode> T transform(T node, Class<T> type) {
		if (node == null || !type.isInstance(node)) {
			return node;
		}
		T transformedNode = type.cast(node.accept(this, null));
		return transformedNode;
	}

	@SuppressWarnings("unchecked")
	public <T extends ISSTNode> T transform(T node) {
		if (node == null) {
			return node;
		}
		T transformedNode = (T) node.accept(this, null);
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
			transformPropertyDeclaration(propertyDeclaration, sst);
			sst.getProperties().remove(propertyDeclaration);
		}

		return super.visit(sst, context);
	}

	@Override
	public ISSTNode visit(IMethodDeclaration methodDecl, Void context) {
		List<IStatement> statements = methodDecl.getBody();
		visitStatements(statements, context);
		return methodDecl;
	}

	@Override
	public ISSTNode visit(IVariableDeclaration variableDecl, Void context) {
		defaultValueHelper.addVariableReferenceToTypeMapping(variableDecl);
		return super.visit(variableDecl, context);
	}

	@Override
	public ISSTNode visit(IExpressionStatement stmt, Void context) {
		ExpressionStatement expressionStatement = (ExpressionStatement) stmt;
		if (expressionStatement.getExpression() instanceof IComposedExpression) {
			expressionStatement.setExpression(
					transformComposedExpression((IComposedExpression) expressionStatement.getExpression()));
		}
		if (expressionStatement.getExpression() instanceof IIndexAccessExpression) {
			expressionStatement.setExpression(
					transformIndexAccessExpression((IIndexAccessExpression) expressionStatement.getExpression()));
		}
		return super.visit(stmt, context);
	}

	@Override
	public ISSTNode visit(IIfElseBlock block, Void context) {
		IfElseBlock ifElseBlock = (IfElseBlock) block;
		ifElseBlock.setCondition(transformSimpleExpression(block.getCondition()));
		return super.visit(block, context);
	}

	@Override
	public ISSTNode visit(ISwitchBlock block, Void context) {
		for (ICaseBlock caseBlock : block.getSections()) {
			CaseBlock caseBlockImpl = (CaseBlock) caseBlock;
			caseBlockImpl.setLabel(constant("0"));
		}
		return super.visit(block, context);
	}

	@Override
	public ISSTNode visit(IBinaryExpression expr, Void context) {
		BinaryExpression binaryExpr = (BinaryExpression) expr;
		binaryExpr.setLeftOperand(transformSimpleExpression(expr.getLeftOperand()));
		binaryExpr.setRightOperand(transformSimpleExpression(expr.getRightOperand()));
		return super.visit(expr, context);
	}

	@Override
	public ISSTNode visit(IConstantValueExpression expr, Void context) {
		if (expr.getValue() == null) {
			ConstantValueExpression constantValueExpression = (ConstantValueExpression) expr;
			constantValueExpression.setValue(defaultValueHelper.getDefaultValueForNode(expr));
		}
		return super.visit(expr, context);
	}

	@Override
	public ISSTNode visit(IInvocationExpression entity, Void context) {
		InvocationExpression invoke = (InvocationExpression) entity;
		List<ISimpleExpression> parameters = invoke.getParameters();
		List<ISimpleExpression> parametersClone = new ArrayList<>(parameters);
		for (ISimpleExpression simpleExpression : parametersClone) {
			parameters.add(parameters.indexOf(simpleExpression), transformSimpleExpression(simpleExpression));
			parameters.remove(simpleExpression);
		}
		return super.visit(entity, context);
	}

	@Override
	public ISSTNode visit(IReturnStatement stmt, Void context) {
		ReturnStatement returnStmt = (ReturnStatement) stmt;
		returnStmt.setExpression(transformSimpleExpression(stmt.getExpression()));
		return super.visit(stmt, context);
	}

	@Override
	public ISSTNode visit(IUnaryExpression expr, Void context) {
		UnaryExpression unaryExpr = (UnaryExpression) expr;
		unaryExpr.setOperand(transformSimpleExpression(expr.getOperand()));
		return super.visit(expr, context);
	}

	@Override
	public ISSTNode visit(IReferenceExpression expr, Void context) {
		// Transforms property references into field references
		if (expr.getReference() instanceof IPropertyReference) {
			IPropertyReference propertyReference = (IPropertyReference) expr.getReference();
			IFieldReference fieldReference = fieldReferenceToLocalField(
					createPropertyFieldName(propertyReference.getPropertyName()));
			ReferenceExpression refExpr = (ReferenceExpression) expr;
			refExpr.setReference(fieldReference);
		} else {
			expr.getReference().accept(this, context);
		}
		return expr;
	}

	private IStatement transformAssignment(Assignment assignment) {
		// Handle Property Get
		IPropertyReference propertyReferenceGet = tryGetPropertyReferenceFromExpression(assignment.getExpression());
		if (propertyReferenceGet != null) {
			IPropertyName propertyName = propertyReferenceGet.getPropertyName();
			IInvocationExpression invocation = invocationExpression(propertyReferenceGet.getReference().getIdentifier(),
					createGetterPropertyMethodName(propertyName));
			assignment.setExpression(invocation);
		}

		// Handle right-hand Composed Expression
		if (assignment.getExpression() instanceof IComposedExpression) {
			assignment.setExpression(transformComposedExpression((IComposedExpression) assignment.getExpression()));
		}

		// Handle right-hand IndexAccessExpression
		if (assignment.getExpression() instanceof IIndexAccessExpression) {
			assignment
					.setExpression(transformIndexAccessExpression((IIndexAccessExpression) assignment.getExpression()));
		}

		// Handle right-hand Simple Expression
		if (assignment.getExpression() instanceof ISimpleExpression) {
			assignment.setExpression(transformSimpleExpression((ISimpleExpression) assignment.getExpression()));
		}

		// Handle Property Set
		IAssignableReference reference = assignment.getReference();
		if (reference instanceof IPropertyReference) {
			IPropertyReference propertyReferenceSet = (IPropertyReference) reference;
			IPropertyName propertyName = propertyReferenceSet.getPropertyName();
			IAssignableExpression assignableExpression = (IAssignableExpression) assignment.getExpression().accept(this,
					null);

			if (assignableExpression instanceof ISimpleExpression) {
				// transform to setter call
				IInvocationExpression invocation = generateSetterInvocation(propertyReferenceSet.getReference(),
						createSetterPropertyMethodName(propertyName), (ISimpleExpression) assignableExpression);
				return expr(invocation);
			} else {
				IFieldReference fieldReference = fieldReferenceToLocalField(
						createPropertyFieldName(propertyReferenceSet.getPropertyName()));
				assignment.setReference(fieldReference);
			}

		}

		return assignment;
	}

	private IAssignableExpression transformComposedExpression(IComposedExpression expression) {
		return constant(defaultValueHelper.getDefaultValueForNode(expression));
	}

	private IAssignableExpression transformIndexAccessExpression(IIndexAccessExpression expr) {
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

	private void transformPropertyDeclaration(IPropertyDeclaration propertyDeclaration, ISST sst) {
		boolean hasGetter = !propertyDeclaration.getGet().isEmpty();
		boolean hasSetter = !propertyDeclaration.getSet().isEmpty();
		boolean hasBody = hasGetter || hasSetter;

		if (hasBody) { // add methods for getter and setter; no backing field
			if (hasGetter) {
				sst.getMethods().add(generateGetterMethodDeclarationWithBody(propertyDeclaration));
			}
			if (hasSetter) {
				sst.getMethods().add(generateSetterMethodDeclarationWithBody(propertyDeclaration));
			}
		} else { // add methods for getter and setter + backing field
			sst.getFields().add(generatePropertyBackingField(propertyDeclaration));
			IPropertyName propertyName = propertyDeclaration.getName();
			if (propertyName.hasGetter()) {
				sst.getMethods().add(generateGetterMethodDeclarationWithoutBody(propertyDeclaration));
			}
			if (propertyName.hasSetter()) {
				sst.getMethods().add(generateSetterMethodDeclarationWithoutBody(propertyDeclaration));
			}
		}
	}

	private ISimpleExpression transformSimpleExpression(ISimpleExpression expr) {
		if (expr instanceof IUnknownExpression) {
			return constant(defaultValueHelper.getDefaultValueForNode(expr));
		}
		return expr;
	}

	private IMethodDeclaration generateMethodDeclaration(IMethodName methodName, List<IStatement> statementList) {
		MethodDeclaration methodDecl = new MethodDeclaration();
		methodDecl.setName(methodName);
		methodDecl.setBody(statementList);
		methodDecl.setEntryPoint(true);
		return methodDecl;
	}

	private IFieldDeclaration generatePropertyBackingField(IPropertyDeclaration propertyDeclaration) {
		FieldDeclaration fieldDecl = new FieldDeclaration();
		fieldDecl.setName(createPropertyFieldName(propertyDeclaration.getName()));
		return fieldDecl;
	}

	private IMethodDeclaration generateSetterMethodDeclarationWithBody(IPropertyDeclaration propertyDeclaration) {
		return generateMethodDeclaration(createSetterPropertyMethodName(propertyDeclaration.getName()),
				propertyDeclaration.getSet());
	}

	private IMethodDeclaration generateSetterMethodDeclarationWithoutBody(IPropertyDeclaration propertyDeclaration) {
		return generateMethodDeclaration(createSetterPropertyMethodName(propertyDeclaration.getName()),
				Lists.newArrayList( //
						assign( //
								fieldReferenceToLocalField(createPropertyFieldName(propertyDeclaration.getName())),
								refExpr(variableReference("value")))));
	}

	private IMethodDeclaration generateGetterMethodDeclarationWithBody(IPropertyDeclaration propertyDeclaration) {
		return generateMethodDeclaration(createGetterPropertyMethodName(propertyDeclaration.getName()),
				propertyDeclaration.getGet());
	}

	private IMethodDeclaration generateGetterMethodDeclarationWithoutBody(IPropertyDeclaration propertyDeclaration) {
		return generateMethodDeclaration(createGetterPropertyMethodName(propertyDeclaration.getName()),
				Lists.newArrayList( //
						returnStatement( //
								refExpr( //
										fieldReferenceToLocalField(
												createPropertyFieldName(propertyDeclaration.getName()))))));
	}

	private IInvocationExpression generateSetterInvocation(IVariableReference reference,
			IMethodName createSetterPropertyMethodName, ISimpleExpression assignableExpression) {
		InvocationExpression invocation = new InvocationExpression();
		invocation.setMethodName(createSetterPropertyMethodName);
		invocation.setReference(reference);
		invocation.setParameters(Lists.newArrayList(assignableExpression));
		return invocation;
	}

	private IMethodName createGetterPropertyMethodName(IPropertyName propertyName) {
		String methodName = String.format(GET_PROPERTY_METHOD_NAME_FORMAT, propertyName.getValueType(),
				propertyName.getDeclaringType(), "get", getNameForProperty(propertyName));
		methodName = insertStaticModifier(propertyName, methodName);
		return MethodName.newMethodName(methodName);
	}

	private IMethodName createSetterPropertyMethodName(IPropertyName propertyName) {
		String methodName = String.format(SET_PROPERTY_METHOD_NAME_FORMAT, VOID_TYPE,
				propertyName.getDeclaringType(), "set", getNameForProperty(propertyName), propertyName.getValueType());
		methodName = insertStaticModifier(propertyName, methodName);
		return MethodName.newMethodName(methodName);
	}

	private IMethodName createIndexAccessMethodName(ITypeName containerType) {
		return MethodName.newMethodName(
				String.format("[%1$s] [%2$s].%3$s()", containerType.getTypeParameterType(), containerType, "get"));
	}

	private IFieldName createPropertyFieldName(IPropertyName propertyName) {
		String fieldName = String.format(PROPERTY_FIELD_NAME_FORMAT, propertyName.getValueType(),
				propertyName.getDeclaringType(), getNameForProperty(propertyName));
		fieldName = insertStaticModifier(propertyName, fieldName);
		return FieldName.newFieldName(fieldName);
	}

	private String insertStaticModifier(IPropertyName propertyName, String memberName) {
		if (propertyName.isStatic())
			memberName = "static " + memberName;
		return memberName;
	}

	private String getNameForProperty(IPropertyName propertyName) {
		String name = propertyName.getName();
		if (name.endsWith("()")) {
			return name.substring(0, name.length() - 2);
		}
		return name;
	}

	private IFieldReference fieldReferenceToLocalField(IFieldName fieldName) {
		FieldReference fieldRef = new FieldReference();
		fieldRef.setReference(variableReference("this"));
		fieldRef.setFieldName(fieldName);
		return fieldRef;
	}

	private IPropertyReference tryGetPropertyReferenceFromExpression(IAssignableExpression expression) {
		if (expression instanceof IReferenceExpression) {
			IReferenceExpression refExpr = (IReferenceExpression) expression;
			if (refExpr.getReference() instanceof IPropertyReference) {
				return (IPropertyReference) refExpr.getReference();
			}
		}
		return null;
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
	protected void visitStatements(List<IStatement> statements, Void context) {
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
