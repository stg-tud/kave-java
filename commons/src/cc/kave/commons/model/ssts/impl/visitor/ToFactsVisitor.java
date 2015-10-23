package cc.kave.commons.model.ssts.impl.visitor;

import java.util.ArrayList;
import java.util.Set;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;

public class ToFactsVisitor extends AbstractNodeVisitor<Set<Fact>, Void> {

	private static TypeName EVENT_TYPE_NAME = CsTypeName
			.newTypeName("KaVE.RS.SolutionAnalysis.Episodes.Event, KaVE.RS.SolutionAnalysis");
	private ArrayList<Event> events;

	public ToFactsVisitor(ArrayList<Event> events) {
		this.events = events;
	}

	@Override
	public Void visit(ISST sst, Set<Fact> facts) {
		for (IMethodDeclaration methodDeclaration : sst.getMethods())
			methodDeclaration.accept(this, facts);

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
	public Void visit(IInvocationExpression entity, Set<Fact> facts) {
		Event invocationEvent = new Event();
		invocationEvent.setKind(EventKind.INVOCATION);
		invocationEvent.setMethod(entity.getMethodName());
		invocationEvent.setType(EVENT_TYPE_NAME);

		Fact newFact = new Fact(String.valueOf(events.indexOf(invocationEvent)));

		for (Fact factSeenBefore : facts) {
			if (!factSeenBefore.isRelationship() && !newFact.isRelationship()) {
				facts.add(new Fact(factSeenBefore.getRawFact() + ">" + newFact.getRawFact()));
			}
		}

		facts.add(newFact);
		return null;
	}

}
