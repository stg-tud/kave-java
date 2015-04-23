package cc.kave.commons.model.ssts.visitor;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.blocks.IDoLoop;
import cc.kave.commons.model.ssts.blocks.IForEachLoop;
import cc.kave.commons.model.ssts.blocks.IForLoop;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.blocks.ILockBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.blocks.IUncheckedBlock;
import cc.kave.commons.model.ssts.blocks.IUnsafeBlock;
import cc.kave.commons.model.ssts.blocks.IUsingBlock;
import cc.kave.commons.model.ssts.blocks.IWhileLoop;
import cc.kave.commons.model.ssts.declarations.IDelegateDeclaration;
import cc.kave.commons.model.ssts.declarations.IEventDeclaration;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;

public interface ISSTNodeVisitor<TContext, TReturn> {

	TReturn Visit(ISST sst, TContext context);

	// declarations
	TReturn Visit(IDelegateDeclaration stmt, TContext context);

	TReturn Visit(IEventDeclaration stmt, TContext context);

	TReturn Visit(IFieldDeclaration stmt, TContext context);

	TReturn Visit(IMethodDeclaration stmt, TContext context);

	TReturn Visit(IPropertyDeclaration stmt, TContext context);

	TReturn Visit(IVariableDeclaration stmt, TContext context);

	// statements
	TReturn Visit(IAssignment stmt, TContext context);

	TReturn Visit(IBreakStatement stmt, TContext context);

	TReturn Visit(IContinueStatement stmt, TContext context);

	TReturn Visit(IExpressionStatement stmt, TContext context);

	TReturn Visit(IGotoStatement stmt, TContext context);

	TReturn Visit(ILabelledStatement stmt, TContext context);

	TReturn Visit(IReturnStatement stmt, TContext context);

	TReturn Visit(IThrowStatement stmt, TContext context);

	// blocks
	TReturn Visit(IDoLoop block, TContext context);

	TReturn Visit(IForEachLoop block, TContext context);

	TReturn Visit(IForLoop block, TContext context);

	TReturn Visit(IIfElseBlock block, TContext context);

	TReturn Visit(ILockBlock stmt, TContext context);

	TReturn Visit(ISwitchBlock block, TContext context);

	TReturn Visit(ITryBlock block, TContext context);

	TReturn Visit(IUncheckedBlock block, TContext context);

	TReturn Visit(IUnsafeBlock block, TContext context);

	TReturn Visit(IUsingBlock block, TContext context);

	TReturn Visit(IWhileLoop block, TContext context);

	// Expressions
	TReturn Visit(ICompletionExpression entity, TContext context);

	TReturn Visit(IComposedExpression expr, TContext context);

	TReturn Visit(IIfElseExpression expr, TContext context);

	TReturn Visit(IInvocationExpression entity, TContext context);

	TReturn Visit(ILambdaExpression expr, TContext context);

	TReturn Visit(ILoopHeaderBlockExpression expr, TContext context);

	TReturn Visit(IConstantValueExpression expr, TContext context);

	TReturn Visit(INullExpression expr, TContext context);

	TReturn Visit(IReferenceExpression expr, TContext context);

	// References
	TReturn Visit(IEventReference eventRef, TContext context);

	TReturn Visit(IFieldReference fieldRef, TContext context);

	TReturn Visit(IMethodReference methodRef, TContext context);

	TReturn Visit(IPropertyReference methodRef, TContext context);

	TReturn Visit(IVariableReference varRef, TContext context);

	// unknowns
	TReturn Visit(IUnknownReference unknownRef, TContext context);

	TReturn Visit(IUnknownExpression unknownExpr, TContext context);

	TReturn Visit(IUnknownStatement unknownStmt, TContext context);

}
