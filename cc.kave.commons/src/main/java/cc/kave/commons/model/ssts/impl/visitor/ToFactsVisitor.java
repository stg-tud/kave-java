package cc.kave.commons.model.ssts.impl.visitor;

import java.util.ArrayList;
import java.util.Set;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;

public class ToFactsVisitor extends AbstractTraversingNodeVisitor<Set<Fact>, Void> {

	private static ITypeName EVENT_TYPE_NAME = TypeName
			.newTypeName("KaVE.RS.SolutionAnalysis.Episodes.Event, KaVE.RS.SolutionAnalysis");
	private ArrayList<Event> events;

	public ToFactsVisitor(ArrayList<Event> events) {
		this.events = events;
	}

	@Override
	public Void visit(IMethodDeclaration stmt, Set<Fact> facts) {

		Event methodStartEvent = new Event();
		methodStartEvent.setKind(EventKind.METHOD_DECLARATION);
		methodStartEvent.setMethod(stmt.getName());
		methodStartEvent.setType(EVENT_TYPE_NAME);
		facts.add(new Fact(String.valueOf(events.indexOf(methodStartEvent))));

		return super.visit(stmt, facts);

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
