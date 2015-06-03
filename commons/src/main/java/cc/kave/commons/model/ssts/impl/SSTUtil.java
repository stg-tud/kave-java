package cc.kave.commons.model.ssts.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.blocks.LockBlock;
import cc.kave.commons.model.ssts.impl.declarations.VariableDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;

import com.google.common.collect.Lists;

public class SSTUtil {

	public static IVariableDeclaration declare(String identifier, TypeName type) {
		VariableDeclaration variable = new VariableDeclaration();
		variable.setReference(variableReference(identifier));
		variable.setType(type);
		return variable;
	}

	public static IReferenceExpression referenceExprToVariable(String id) {
		ReferenceExpression referenceExpression = new ReferenceExpression();
		referenceExpression.setReference(variableReference(id));
		return referenceExpression;
	}

	public static IVariableReference variableReference(String id) {
		VariableReference variableReference = new VariableReference();
		variableReference.setIdentifier(id);
		return variableReference;
	}

	public static IComposedExpression composedExpression(String... strReference) {
		ComposedExpression composedExpression = new ComposedExpression();
		List<IVariableReference> varRefs = new ArrayList<IVariableReference>();
		for (int i = 0; i < strReference.length; i++)
			varRefs.add(variableReference(strReference[i]));
		composedExpression.setReferences(varRefs);
		return composedExpression;
	}

	public static IAssignment assigmentToLocal(String identifier, IAssignableExpression expr) {
		Assignment assignment = new Assignment();
		assignment.setExpression(expr);
		assignment.setReference(variableReference(identifier));
		return assignment;
	}

	public static IExpressionStatement invocationStatement(MethodName name, Iterator<ISimpleExpression> parameters) {
		ExpressionStatement expressionStatement = new ExpressionStatement();
		expressionStatement.setExpression(invocationExpression(name, parameters));
		return expressionStatement;
	}

	public static IExpressionStatement invocationStatement(String id, MethodName name) {
		ArrayList<ISimpleExpression> simpleExpr = new ArrayList<>();
		return invocationStatement(id, name, simpleExpr.iterator());
	}

	public static IExpressionStatement invocationStatement(String id, MethodName name,
			Iterator<ISimpleExpression> parameters) {
		ExpressionStatement exprStatement = new ExpressionStatement();
		exprStatement.setExpression(invocationExpression(id, name, parameters));
		return exprStatement;
	}

	public static IInvocationExpression invocationExpression(String id, MethodName name) {
		ArrayList<ISimpleExpression> parameters = new ArrayList<>();
		return invocationExpression(id, name, parameters.iterator());
	}

	public static IInvocationExpression invocationExpression(MethodName name, Iterator<ISimpleExpression> parameters) {
		// assert (name.isStatic() || name.isConstructor());
		InvocationExpression invoExpr = new InvocationExpression();
		invoExpr.setMethodName(name);
		invoExpr.setParameters(Lists.newArrayList(parameters));
		return invoExpr;
	}

	public static IInvocationExpression invocationExpression(String id, MethodName name,
			Iterator<ISimpleExpression> parameters) {
		// assert (name.isStatic() || name.isConstructor());
		InvocationExpression invocationExpression = new InvocationExpression();
		invocationExpression.setMethodName(name);
		invocationExpression.setParameters(Lists.newArrayList(parameters));
		invocationExpression.setReference(variableReference(id));
		return invocationExpression;
	}

	public static ILockBlock lockBlock(String id) {
		LockBlock lockBlock = new LockBlock();
		lockBlock.setReference(variableReference(id));
		return lockBlock;
	}

	public static IStatement returnStatement(ISimpleExpression expr) {
		ReturnStatement returnStatement = new ReturnStatement();
		returnStatement.setExpression(expr);
		return returnStatement;
	}

	public static IStatement returnVariable(String id) {
		return returnStatement(referenceExprToVariable(id));
	}

}
