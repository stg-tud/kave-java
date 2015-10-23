package cc.kave.commons.model.ssts.impl.visitor;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
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
import cc.kave.commons.utils.json.JsonUtils;

import com.google.common.reflect.TypeToken;

public class ToFactsVisitor extends AbstractNodeVisitor<Set<Fact>, Void> {

	private static TypeName EVENT_TYPE_NAME = CsTypeName
			.newTypeName("KaVE.RS.SolutionAnalysis.Episodes.Event, KaVE.RS.SolutionAnalysis");
	private ArrayList<Event> events;

	public ToFactsVisitor(String fileEventMapping) {
		readMapping(fileEventMapping);
	}

	@Override
	public Void visit(ISST sst, Set<Fact> facts) {
		for (IMethodDeclaration methodDeclaration : sst.getMethods())
			methodDeclaration.accept(this, facts);

		return null;

	}

	@Override
	public Void visit(IDelegateDeclaration stmt, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public Void visit(IEventDeclaration stmt, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;

	}

	@Override
	public Void visit(IFieldDeclaration stmt, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IMethodDeclaration stmt, Set<Fact> facts) {

		Event methodStartEvent = new Event();
		methodStartEvent.setKind(EventKind.METHOD_START);
		methodStartEvent.setMethod(stmt.getName());
		methodStartEvent.setType(EVENT_TYPE_NAME);
		facts.add(new Fact(String.valueOf(events.indexOf(methodStartEvent))));

		for (IStatement bodyStmt : stmt.getBody()) {
			bodyStmt.accept(this, facts);
		}

		return null;

	}

	@Override
	public Void visit(IPropertyDeclaration stmt, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IVariableDeclaration stmt, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IAssignment stmt, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IBreakStatement stmt, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IContinueStatement stmt, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IExpressionStatement stmt, Set<Fact> facts) {
		// stmt.getExpression().accept(visitor, context)
		facts.add(new Fact("boho"));
		return null;
	}

	@Override
	public Void visit(IGotoStatement stmt, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ILabelledStatement stmt, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IReturnStatement stmt, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IThrowStatement stmt, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IDoLoop block, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IForEachLoop block, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IForLoop block, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IIfElseBlock block, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ILockBlock stmt, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ISwitchBlock block, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ITryBlock block, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUncheckedBlock block, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUnsafeBlock block, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUsingBlock block, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IWhileLoop block, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ICompletionExpression entity, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IComposedExpression expr, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IIfElseExpression expr, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IInvocationExpression entity, Set<Fact> facts) {
		Event methodStartEvent = new Event();
		methodStartEvent.setKind(EventKind.INVOCATION);
		methodStartEvent.setMethod(entity.getMethodName());
		methodStartEvent.setType(EVENT_TYPE_NAME);

		Fact newFact = new Fact(String.valueOf(events.indexOf(methodStartEvent)));

		for (Fact factSeenBefore : facts) {
			if (factSeenBefore.isRelationship() && newFact.isRelationship()) {
				facts.add(new Fact(factSeenBefore.getRawFact() + ">" + newFact.getRawFact(), true));
			}
		}

		facts.add(newFact);
		return null;
	}

	@Override
	public Void visit(ILambdaExpression expr, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(ILoopHeaderBlockExpression expr, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IConstantValueExpression expr, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(INullExpression expr, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IReferenceExpression expr, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IEventReference eventRef, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IFieldReference fieldRef, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IMethodReference methodRef, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IPropertyReference methodRef, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IVariableReference varRef, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUnknownReference unknownRef, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUnknownExpression unknownExpr, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Void visit(IUnknownStatement unknownStmt, Set<Fact> facts) {
		// TODO Auto-generated method stub
		return null;
	}

	private void readMapping(String fileEventMapping) {
		File in = new File(fileEventMapping);
		Type listType = new TypeToken<LinkedList<Event>>() {
		}.getType();
		events = JsonUtils.fromJson(in, listType);
	}
}
