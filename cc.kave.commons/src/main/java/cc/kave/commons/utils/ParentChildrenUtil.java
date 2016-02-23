package cc.kave.commons.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
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
import cc.kave.commons.model.ssts.expressions.assignable.IBinaryExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICastExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IComposedExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIfElseExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ITypeCheckExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IUnaryExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IBreakStatement;
import cc.kave.commons.model.ssts.statements.IContinueStatement;
import cc.kave.commons.model.ssts.statements.IEventSubscriptionStatement;
import cc.kave.commons.model.ssts.statements.IExpressionStatement;
import cc.kave.commons.model.ssts.statements.IGotoStatement;
import cc.kave.commons.model.ssts.statements.ILabelledStatement;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IThrowStatement;
import cc.kave.commons.model.ssts.statements.IUnknownStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class ParentChildrenUtil {

	private ISSTNode sst;

	private Map<Integer, List<ISSTNode>> childrenMap;

	private Map<Integer, ISSTNode> parentMap;

	public ParentChildrenUtil(ISSTNode sst) {
		this.sst = sst;
		this.childrenMap = new HashMap<>();
		this.parentMap = new HashMap<>();
		ParentChildrenVisitor visitor = new ParentChildrenVisitor();
		sst.accept(visitor, this);
	}

	public ISSTNode getParent(ISSTNode n) {
		return parentMap.get(System.identityHashCode(n));
	}

	public Iterable<ISSTNode> getChildren(ISSTNode n) {
		return childrenMap.get(System.identityHashCode(n));
	}

	private void addChildren(ISSTNode n, List<ISSTNode> children) {
		int identityHashCode = System.identityHashCode(n);
		for (ISSTNode child : children) {
			addParent(child, n);
		}
		childrenMap.put(identityHashCode, children);
	}

	private void addParent(ISSTNode n, ISSTNode parent) {
		parentMap.put(System.identityHashCode(n), parent);
	}

	private class ParentChildrenVisitor extends AbstractThrowingNodeVisitor<ParentChildrenUtil, Void> {

		@Override
		public Void visit(ISST sst, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(sst.getDelegates(), context));
			children.addAll(visit(sst.getEvents(), context));
			children.addAll(visit(sst.getFields(), context));
			children.addAll(visit(sst.getMethods(), context));
			children.addAll(visit(sst.getProperties(), context));
			context.addChildren(sst, children);
			return null;
		}

		@Override
		public Void visit(IDelegateDeclaration stmt, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			context.addChildren(stmt, children);
			return null;
		}

		@Override
		public Void visit(IEventDeclaration stmt, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			context.addChildren(stmt, children);
			return null;
		}

		@Override
		public Void visit(IFieldDeclaration stmt, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			context.addChildren(stmt, children);
			return null;
		}

		@Override
		public Void visit(IMethodDeclaration stmt, ParentChildrenUtil context) {
			context.addChildren(stmt, visit(stmt.getBody(), context));
			return null;
		}

		@Override
		public Void visit(IPropertyDeclaration stmt, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(stmt.getSet(), context));
			children.addAll(visit(stmt.getGet(), context));
			context.addChildren(stmt, children);
			return null;
		}

		@Override
		public Void visit(IContinueStatement stmt, ParentChildrenUtil context) {
			context.addChildren(stmt, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(IBreakStatement stmt, ParentChildrenUtil context) {
			context.addChildren(stmt, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(IAssignment stmt, ParentChildrenUtil context) {
			context.addChildren(stmt, Lists.newArrayList(stmt.getExpression(), stmt.getReference()));
			stmt.getExpression().accept(this, context);
			stmt.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IVariableDeclaration stmt, ParentChildrenUtil context) {
			context.addChildren(stmt, Lists.newArrayList(stmt.getReference()));
			stmt.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IExpressionStatement stmt, ParentChildrenUtil context) {
			context.addChildren(stmt, Lists.newArrayList(stmt.getExpression()));
			stmt.getExpression().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IGotoStatement stmt, ParentChildrenUtil context) {
			context.addChildren(stmt, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(ILabelledStatement stmt, ParentChildrenUtil context) {
			context.addChildren(stmt, Lists.newArrayList(stmt.getStatement()));
			stmt.getStatement().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IReturnStatement stmt, ParentChildrenUtil context) {
			context.addChildren(stmt, Lists.newArrayList(stmt.getExpression()));
			stmt.getExpression().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IThrowStatement stmt, ParentChildrenUtil context) {
			context.addChildren(stmt, Lists.newArrayList(stmt.getReference()));
			stmt.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IDoLoop block, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(block.getBody(), context));
			children.add(block.getCondition());
			block.getCondition().accept(this, context);
			context.addChildren(block, children);
			return null;
		}

		@Override
		public Void visit(IForEachLoop block, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(block.getBody(), context));
			children.add(block.getDeclaration());
			children.add(block.getLoopedReference());
			block.getDeclaration().accept(this, context);
			block.getLoopedReference().accept(this, context);
			context.addChildren(block, children);
			return null;
		}

		@Override
		public Void visit(IForLoop block, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(block.getBody(), context));
			children.add(block.getCondition());
			block.getCondition().accept(this, context);
			children.addAll(visit(block.getInit(), context));
			children.addAll(visit(block.getStep(), context));
			context.addChildren(block, children);
			return null;
		}

		@Override
		public Void visit(IIfElseBlock block, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.add(block.getCondition());
			block.getCondition().accept(this, context);
			children.addAll(visit(block.getElse(), context));
			children.addAll(visit(block.getThen(), context));
			context.addChildren(block, children);
			return null;
		}

		@Override
		public Void visit(ILockBlock stmt, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(stmt.getBody(), context));
			children.add(stmt.getReference());
			stmt.getReference().accept(this, context);
			context.addChildren(stmt, children);
			return null;
		}

		@Override
		public Void visit(ISwitchBlock block, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(block.getDefaultSection(), context));
			children.add(block.getReference());
			for (ICaseBlock caseblock : block.getSections()) {
				children.addAll(visit(caseblock.getBody(), context));
			}
			context.addChildren(block, children);
			block.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(ITryBlock block, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(block.getBody(), context));
			for (ICatchBlock catchblock : block.getCatchBlocks()) {
				children.addAll(visit(catchblock.getBody(), context));
			}
			children.addAll(visit(block.getFinally(), context));
			context.addChildren(block, children);
			return null;
		}

		@Override
		public Void visit(IUncheckedBlock block, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(block.getBody(), context));
			context.addChildren(block, children);
			return null;
		}

		@Override
		public Void visit(IUnsafeBlock block, ParentChildrenUtil context) {
			context.addChildren(block, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(IUsingBlock block, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(block.getBody(), context));
			children.add(block.getReference());
			context.addChildren(block, children);
			block.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IWhileLoop block, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(block.getBody(), context));
			children.add(block.getCondition());
			context.addChildren(block, children);
			block.getCondition().accept(this, context);
			return null;
		}

		@Override
		public Void visit(ICompletionExpression entity, ParentChildrenUtil context) {
			context.addChildren(entity, Lists.newArrayList(entity.getVariableReference()));
			entity.getVariableReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IComposedExpression expr, ParentChildrenUtil context) {
			context.addChildren(expr, visit(expr.getReferences(), context));
			return null;
		}

		@Override
		public Void visit(IIfElseExpression expr, ParentChildrenUtil context) {
			context.addChildren(expr,
					Lists.newArrayList(expr.getCondition(), expr.getThenExpression(), expr.getElseExpression()));
			expr.getCondition().accept(this, context);
			expr.getElseExpression().accept(this, context);
			expr.getThenExpression().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IInvocationExpression entity, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(entity.getParameters(), context));
			children.add(entity.getReference());
			entity.getReference().accept(this, context);
			context.addChildren(entity, children);
			return null;
		}

		@Override
		public Void visit(ILambdaExpression expr, ParentChildrenUtil context) {
			context.addChildren(expr, visit(expr.getBody(), context));
			return null;
		}

		@Override
		public Void visit(ILoopHeaderBlockExpression expr, ParentChildrenUtil context) {
			context.addChildren(expr, visit(expr.getBody(), context));
			return null;
		}

		@Override
		public Void visit(IConstantValueExpression expr, ParentChildrenUtil context) {
			context.addChildren(expr, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(INullExpression expr, ParentChildrenUtil context) {
			context.addChildren(expr, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(IReferenceExpression expr, ParentChildrenUtil context) {
			context.addChildren(expr, Lists.newArrayList(expr.getReference()));
			expr.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IEventReference eventRef, ParentChildrenUtil context) {
			context.addChildren(eventRef, Lists.newArrayList(eventRef.getReference()));
			eventRef.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IFieldReference fieldRef, ParentChildrenUtil context) {
			context.addChildren(fieldRef, Lists.newArrayList(fieldRef.getReference()));
			fieldRef.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IMethodReference methodRef, ParentChildrenUtil context) {
			context.addChildren(methodRef, Lists.newArrayList(methodRef.getReference()));
			methodRef.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IPropertyReference methodRef, ParentChildrenUtil context) {
			context.addChildren(methodRef, Lists.newArrayList(methodRef.getReference()));
			methodRef.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IVariableReference varRef, ParentChildrenUtil context) {
			context.addChildren(varRef, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(IUnknownReference unknownRef, ParentChildrenUtil context) {
			context.addChildren(unknownRef, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(IUnknownExpression unknownExpr, ParentChildrenUtil context) {
			context.addChildren(unknownExpr, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(IUnknownStatement unknownStmt, ParentChildrenUtil context) {
			context.addChildren(unknownStmt, Lists.newArrayList());
			return null;
		}

		@Override
		public Void visit(IEventSubscriptionStatement stmt, ParentChildrenUtil context) {
			context.addChildren(stmt, Lists.newArrayList(stmt.getExpression(), stmt.getReference()));
			stmt.getExpression().accept(this, context);
			stmt.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(ICastExpression expr, ParentChildrenUtil context) {
			context.addChildren(expr, Lists.newArrayList(expr.getReference()));
			expr.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IIndexAccessExpression expr, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			children.addAll(visit(expr.getIndices(), context));
			children.add(expr.getReference());
			expr.getReference().accept(this, context);
			context.addChildren(expr, children);
			return null;
		}

		@Override
		public Void visit(ITypeCheckExpression expr, ParentChildrenUtil context) {
			context.addChildren(expr, Lists.newArrayList(expr.getReference()));
			expr.getReference().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IIndexAccessReference indexAccessRef, ParentChildrenUtil context) {
			context.addChildren(indexAccessRef, Lists.newArrayList(indexAccessRef.getExpression()));
			indexAccessRef.getExpression().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IBinaryExpression expr, ParentChildrenUtil context) {
			context.addChildren(expr, Lists.newArrayList(expr.getLeftOperand(), expr.getRightOperand()));
			expr.getLeftOperand().accept(this, context);
			expr.getRightOperand().accept(this, context);
			return null;
		}

		@Override
		public Void visit(IUnaryExpression expr, ParentChildrenUtil context) {
			context.addChildren(expr, Lists.newArrayList(expr.getOperand()));
			expr.getOperand().accept(this, context);
			return null;
		}

		public List<ISSTNode> visit(Iterable<? extends ISSTNode> nodes, ParentChildrenUtil context) {
			List<ISSTNode> children = Lists.newArrayList();
			for (ISSTNode node : nodes) {
				children.add(node);
				node.accept(this, context);
			}
			return children;
		}
	}
}
