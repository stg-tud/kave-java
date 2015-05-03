package cc.kave.commons.model.ssts.impl.visitor;

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

public class ToStringVisitor extends AbstractNodeVisitor<StringBuilder> {

	@Override
	public Void visit(ISST sst, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IDelegateDeclaration stmt, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IEventDeclaration stmt, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IFieldDeclaration stmt, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IMethodDeclaration stmt, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IPropertyDeclaration stmt, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IVariableDeclaration stmt, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IAssignment stmt, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IBreakStatement stmt, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IContinueStatement stmt, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IExpressionStatement stmt, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IGotoStatement stmt, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ILabelledStatement stmt, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IReturnStatement stmt, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IThrowStatement stmt, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IDoLoop block, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IForLoop block, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ILockBlock stmt, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ITryBlock block, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUsingBlock block, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ICompletionExpression entity, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IComposedExpression expr, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IIfElseExpression expr, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IInvocationExpression entity, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ILoopHeaderBlockExpression expr, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IConstantValueExpression expr, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(INullExpression expr, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IReferenceExpression expr, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IEventReference eventRef, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IFieldReference fieldRef, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IMethodReference methodRef, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IPropertyReference methodRef, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IVariableReference varRef, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUnknownReference unknownRef, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUnknownExpression unknownExpr, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUnknownStatement unknownStmt, StringBuilder context) {
		// TODO Auto-generated method stub
		return null;
	}

}
