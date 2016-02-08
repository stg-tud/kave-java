/*
 * Copyright 2016 Carina Oberle
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
package cc.kave.commons.model.ssts.impl.transformation.booleans;

import static cc.kave.commons.model.ssts.impl.SSTUtil.and;
import static cc.kave.commons.model.ssts.impl.SSTUtil.binExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.not;
import static cc.kave.commons.model.ssts.impl.SSTUtil.or;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.FALSE;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.TRUE;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.define;
import static cc.kave.commons.model.ssts.impl.transformation.BooleanDeclarationUtil.mainCondition;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableBiMap;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.BinaryOperator;
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.UnaryOperator;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.UnaryExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.transformation.AbstractExpressionNormalizationVisitor;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;

public class ExpressionNormalizationVisitor extends AbstractExpressionNormalizationVisitor<RefLookup> {
	private ReferenceCollectorVisitor referenceCollector;
	private List<IStatement> newDeclarations;
	private int variableCount;

	public ExpressionNormalizationVisitor() {
		referenceCollector = new ReferenceCollectorVisitor();
		newDeclarations = new ArrayList<IStatement>();
		variableCount = 0;
	}

	//@formatter:off
	private static final ImmutableBiMap<BinaryOperator, BinaryOperator> opNegation = ImmutableBiMap.of(
			BinaryOperator.And, BinaryOperator.Or, 
			BinaryOperator.Equal, BinaryOperator.NotEqual, 
			BinaryOperator.LessThan, BinaryOperator.GreaterThanOrEqual, 
			BinaryOperator.GreaterThan, BinaryOperator.LessThanOrEqual);
	//@formatter:on

	public static BinaryOperator getNegated(BinaryOperator op) {
		return opNegation.getOrDefault(op, opNegation.inverse().get(op));
	}

	private int next() {
		return variableCount++;
	}

	public List<IStatement> clearNewDeclarations() {
		List<IStatement> newDeclarations = new ArrayList<IStatement>();
		newDeclarations.addAll(this.newDeclarations);
		this.newDeclarations.clear();
		return newDeclarations;
	}

	private void registerNewDeclarations(List<IStatement> statements, RefLookup context) {
		/* update context with newly assigned references */
		statements.stream().filter(s -> s instanceof IAssignment).map(s -> (IAssignment) s).forEach(a -> {
			IReference ref = a.getReference();
			if (ref instanceof IVariableReference)
				context.put((IVariableReference) ref, a.getExpression());
		});
		/* normalize newly created statements */
		visit(statements, context);
		newDeclarations.addAll(statements);
	}

	@Override
	public IAssignableExpression visit(IMethodDeclaration decl, RefLookup context) {
		RefLookup lookup = referenceCollector.visit(decl);
		visit(decl.getBody(), lookup);
		return null;
	}

	@Override
	public IAssignableExpression visit(IAssignment stmt, RefLookup context) {
		IAssignableExpression normalized = stmt.getExpression().accept(this, context);
		if (normalized != null && stmt instanceof Assignment) {
			Assignment assignment = (Assignment) stmt;
			assignment.setExpression(normalized);
			IAssignableReference ref = assignment.getReference();

			/* update context */
			if (ref instanceof IVariableReference)
				context.put((IVariableReference) ref, normalized);
		}
		return null;
	}

	@Override
	public IAssignableExpression visit(IIfElseBlock block, RefLookup context) {
		super.visit(block, context);
		IAssignableExpression expr = tryLookup(block.getCondition(), context);
		if (expr instanceof IUnaryExpression) {
			IUnaryExpression unary = (IUnaryExpression) expr;

			/* if condition is negated, remove negation and switch branches */
			if (isNegated(unary) && block instanceof IfElseBlock) {
				IfElseBlock ifElse = (IfElseBlock) block;
				List<IStatement> thenPart = ifElse.getThen();
				List<IStatement> elsePart = ifElse.getElse();
				ifElse.setCondition(unary.getOperand());
				ifElse.setThen(elsePart);
				ifElse.setElse(thenPart);
			}
		}
		return null;
	}

	@Override
	public IAssignableExpression visit(IIfElseExpression expr, RefLookup context) {
		super.visit(expr, context);
		IAssignableExpression referenced = tryLookup(expr.getCondition(), context);
		if (referenced instanceof IUnaryExpression) {
			IUnaryExpression unary = (IUnaryExpression) referenced;

			/* if condition is negated, remove negation and switch parts */
			if (isNegated(unary) && expr instanceof IfElseExpression) {
				IfElseExpression ifElse = (IfElseExpression) expr;
				ISimpleExpression thenPart = ifElse.getThenExpression();
				ISimpleExpression elsePart = ifElse.getElseExpression();
				ifElse.setCondition(unary.getOperand());
				ifElse.setThenExpression(elsePart);
				ifElse.setElseExpression(thenPart);
			}
		}
		return null;
	}

	@Override
	public IAssignableExpression visit(IReferenceExpression expr, RefLookup context) {
		IAssignableExpression referencedExpr = context.get(expr.getReference());

		/* inline constant */
		if (referencedExpr instanceof IConstantValueExpression)
			return referencedExpr;

		/* inline reference */
		if (referencedExpr instanceof IReferenceExpression) {
			IReferenceExpression referencedRefExpr = (IReferenceExpression) referencedExpr;
			IReferenceExpression referencedExprNormalized = (IReferenceExpression) referencedExpr.accept(this, context);
			if (expr instanceof ReferenceExpression) {
				IReference newRef = referencedExprNormalized != null ? referencedExprNormalized.getReference()
						: referencedRefExpr.getReference();
				/* only inline references that are assigned exactly once */
				if (context.containsKey(newRef))
					((ReferenceExpression) expr).setReference(newRef);
			}
		}
		return null;
	}

	// ----------------------- binary expressions -----------------------------

	@Override
	public IAssignableExpression visit(IBinaryExpression expr, RefLookup context) {
		// normalize operands first
		super.visit(expr, context);
		ISimpleExpression normalized0 = idempotence(expr, context);
		if (normalized0 != null)
			return normalized0;
		ISimpleExpression normalized1 = constantOperand(expr, context);
		if (normalized1 != null)
			return normalized1;
		ISimpleExpression normalized2 = absorption(expr, context);
		if (normalized2 != null)
			return normalized2;
		IBinaryExpression normalized3 = disjunctiveNormalForm(expr, context);
		IBinaryExpression normalized4 = normalized3 == null ? toLeftAssociative(expr, context)
				: toLeftAssociative(normalized3, context);
		return normalized4 != null ? normalized4 : normalized3;
	}

	/**
	 * or(x, x) -> x, and(x, x) -> x
	 */
	private ISimpleExpression idempotence(IBinaryExpression expr, RefLookup context) {
		if (!(isDisjunction(expr) || isConjunction(expr)))
			return null;
		ISimpleExpression lhs = expr.getLeftOperand();
		ISimpleExpression rhs = expr.getRightOperand();
		IAssignableExpression leftReferenced = tryLookup(lhs, context);
		IAssignableExpression rightReferenced = tryLookup(rhs, context);
		if (lhs.equals(rhs)
				|| leftReferenced != null && rightReferenced != null && leftReferenced.equals(rightReferenced))
			return lhs;
		return null;
	}

	/**
	 * e.g. or(x, false) -> x, or(x, true) -> true
	 */
	private ISimpleExpression constantOperand(IBinaryExpression expr, RefLookup context) {
		boolean isDisjunction = isDisjunction(expr);
		boolean isConjunction = isConjunction(expr);
		if (!(isDisjunction || isConjunction))
			return null;

		ISimpleExpression lhs = expr.getLeftOperand();
		ISimpleExpression rhs = expr.getRightOperand();
		IAssignableExpression leftReferenced = tryLookup(lhs, context);
		IAssignableExpression rightReferenced = tryLookup(rhs, context);

		ISimpleExpression normalized = null;
		if (lhs.equals(TRUE) || leftReferenced != null && leftReferenced.equals(TRUE))
			normalized = isDisjunction ? TRUE : rhs;
		else if (rhs.equals(TRUE) || rightReferenced != null && rightReferenced.equals(TRUE))
			normalized = isDisjunction ? TRUE : lhs;
		else if (lhs.equals(FALSE) || leftReferenced != null && leftReferenced.equals(FALSE))
			normalized = isDisjunction ? rhs : FALSE;
		else if (rhs.equals(FALSE) || rightReferenced != null && rightReferenced.equals(FALSE))
			normalized = isDisjunction ? lhs : FALSE;
		return normalized;
	}

	/**
	 * and(x, or(x, y)) -> x, or(x, and(x, y)) -> x
	 */
	private ISimpleExpression absorption(IBinaryExpression expr, RefLookup context) {
		BinaryOperator negatedOp = getNegated(expr.getOperator());

		if (!(isDisjunction(expr) || isConjunction(expr)))
			return null;

		ISimpleExpression lhs = expr.getLeftOperand();
		ISimpleExpression rhs = expr.getRightOperand();
		IAssignableExpression leftReferenced = tryLookup(lhs, context);
		IAssignableExpression rightReferenced = tryLookup(rhs, context);
		IBinaryExpression leftBinary = (leftReferenced instanceof IBinaryExpression)
				? (IBinaryExpression) leftReferenced : null;
		IBinaryExpression rightBinary = (rightReferenced instanceof IBinaryExpression)
				? (IBinaryExpression) rightReferenced : null;

		if (leftBinary != null && leftBinary.getOperator().equals(negatedOp)) {
			if (containsMember(leftBinary, rhs, context))
				return rhs;
		} else if (rightBinary != null && rightBinary.getOperator().equals(negatedOp)) {
			if (containsMember(rightBinary, lhs, context))
				return lhs;
		}
		return null;
	}

	/**
	 * Determine whether a given expression is a member of a
	 * disjunctive/conjunctive chain.
	 */
	private boolean containsMember(IBinaryExpression expr, ISimpleExpression member, RefLookup context) {
		BinaryOperator op = expr.getOperator();
		ISimpleExpression lhs = expr.getLeftOperand();
		ISimpleExpression rhs = expr.getRightOperand();
		IAssignableExpression leftReferenced = tryLookup(lhs, context);
		IAssignableExpression rightReferenced = tryLookup(rhs, context);

		if (lhs.equals(member) || rhs.equals(member) || leftReferenced != null && leftReferenced.equals(member)
				|| rightReferenced != null && rightReferenced.equals(member))
			return true;

		if (leftReferenced instanceof IBinaryExpression) {
			IBinaryExpression leftReferencedBinary = (IBinaryExpression) leftReferenced;
			if (leftReferencedBinary.getOperator().equals(op) && containsMember(leftReferencedBinary, member, context))
				return true;
		}
		if (rightReferenced instanceof IBinaryExpression) {
			IBinaryExpression rightReferencedBinary = (IBinaryExpression) rightReferenced;
			if (rightReferencedBinary.getOperator().equals(op)
					&& containsMember(rightReferencedBinary, member, context))
				return true;
		}
		return false;

	}

	/**
	 * Convert binary expression into its left associative equivalent.
	 * 
	 * (e.g. or(a, or(b, c)) -> or(or(a, b), c)
	 */
	private IBinaryExpression toLeftAssociative(IBinaryExpression expr, RefLookup context) {
		BinaryOperator op = expr.getOperator();
		ISimpleExpression lhs = expr.getLeftOperand();
		ISimpleExpression rhs = expr.getRightOperand();
		IAssignableExpression rightReferenced = tryLookup(rhs, context);

		if (rightReferenced instanceof IBinaryExpression) {
			IBinaryExpression rightReferencedBinary = (IBinaryExpression) rightReferenced;
			if (rightReferencedBinary.getOperator().equals(op)) {
				List<IStatement> innerBinary = define(next(), binExpr(op, lhs, rightReferencedBinary.getLeftOperand()));
				registerNewDeclarations(innerBinary, context);
				return binExpr(op, mainCondition(innerBinary), rightReferencedBinary.getRightOperand());
			}
		}
		return null;
	}

	/**
	 * Convert binary expression into its disjunctive normal form.
	 * 
	 * (e.g. and(a, or(b, c)) -> or(and(a, b), and(a, c))
	 */
	private IBinaryExpression disjunctiveNormalForm(IBinaryExpression expr, RefLookup context) {
		/* already in DNF, or no logical expression --> nothing to do */
		if (!isConjunction(expr))
			return null;

		ISimpleExpression lhs = expr.getLeftOperand();
		ISimpleExpression rhs = expr.getRightOperand();
		IAssignableExpression leftReferenced = tryLookup(lhs, context);
		IAssignableExpression rightReferenced = tryLookup(rhs, context);

		if (leftReferenced instanceof IBinaryExpression) {
			IBinaryExpression leftBinary = (IBinaryExpression) leftReferenced;
			if (isDisjunction(leftBinary))
				return applyDistributivity(leftBinary, rhs, context);
		}

		if (rightReferenced instanceof IBinaryExpression) {
			IBinaryExpression rightBinary = (IBinaryExpression) rightReferenced;
			if (isDisjunction(rightBinary))
				return applyDistributivity(rightBinary, lhs, context);
		}
		return null;
	}

	private IBinaryExpression applyDistributivity(IBinaryExpression disjunction, ISimpleExpression simple,
			RefLookup context) {
		List<IStatement> andLeft = define(next(), and(disjunction.getLeftOperand(), simple));
		List<IStatement> andRight = define(next(), and(disjunction.getRightOperand(), simple));
		registerNewDeclarations(andLeft, context);
		registerNewDeclarations(andRight, context);
		return or(mainCondition(andLeft), mainCondition(andRight));
	}

	private boolean isConjunction(IBinaryExpression expr) {
		return expr.getOperator().equals(BinaryOperator.And);
	}

	private boolean isDisjunction(IBinaryExpression expr) {
		return expr.getOperator().equals(BinaryOperator.Or);
	}

	// ------------------------ unary expressions -----------------------------

	@Override
	public IAssignableExpression visit(IUnaryExpression expr, RefLookup context) {
		// normalize operand first
		super.visit(expr, context);
		ISimpleExpression operandNormalized = (ISimpleExpression) expr.getOperand().accept(this, context);
		if (operandNormalized != null && expr instanceof UnaryExpression)
			((UnaryExpression) expr).setOperand(operandNormalized);
		IAssignableExpression normalized = handleNegation(expr, context);
		return normalized;
	}

	private IAssignableExpression handleNegation(IUnaryExpression unaryExpr, RefLookup context) {
		if (!isNegated(unaryExpr))
			return null;

		IAssignableExpression operandExpr = tryLookup(unaryExpr.getOperand(), context);

		if (operandExpr instanceof IUnaryExpression)
			return handleNegatedUnaryExpression((IUnaryExpression) operandExpr);
		else if (operandExpr instanceof IBinaryExpression)
			return handleNegatedBinaryExpression((IBinaryExpression) operandExpr, context);

		return null;
	}

	private IAssignableExpression handleNegatedUnaryExpression(IUnaryExpression negatedExpr) {
		/* handle double negation */
		if (isNegated(negatedExpr))
			return negatedExpr.getOperand();
		return null;
	}

	private IAssignableExpression handleNegatedBinaryExpression(IBinaryExpression negatedExpr, RefLookup context) {
		BinaryOperator op = negatedExpr.getOperator();
		ISimpleExpression lhs = negatedExpr.getLeftOperand();
		ISimpleExpression rhs = negatedExpr.getRightOperand();
		BinaryOperator negatedOp = getNegated(op);

		switch (op) {
		case And: // --> or(not(lhs), not(rhs))
		case Or: // --> and(not(lhs), not(rhs))
			/* apply De Morgan's laws */
			List<IStatement> negatedLeft = define(next(), not(lhs));
			List<IStatement> negatedRight = define(next(), not(rhs));
			registerNewDeclarations(negatedLeft, context);
			registerNewDeclarations(negatedRight, context);
			return binExpr(negatedOp, mainCondition(negatedLeft), mainCondition(negatedRight));
		case Equal:
		case NotEqual:
		case GreaterThan:
		case GreaterThanOrEqual:
		case LessThan:
		case LessThanOrEqual:
			/* negate operator */
			return binExpr(negatedOp, lhs, rhs);
		default:
			return null;
		}
	}

	private boolean isNegated(IUnaryExpression unaryExpr) {
		return unaryExpr.getOperator().equals(UnaryOperator.Not);
	}

	private IAssignableExpression tryLookup(ISimpleExpression simple, RefLookup lookup) {
		if (!(simple instanceof IReferenceExpression))
			return null;

		IReference ref = ((IReferenceExpression) simple).getReference();
		if (!(ref instanceof IVariableReference))
			return null;

		return lookup.get((IVariableReference) ref);
	}

}
