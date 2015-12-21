package cc.kave.commons.model.ssts.impl.visitor;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;

public class ToFactsVisitor extends AbstractTraversingNodeVisitor<Set<Fact>, Void> {

	private List<Event> events;

	public ToFactsVisitor(List<Event> events) {
		this.events = events;
	}

	private Fact toFact(Event e) {
		return new Fact(events.indexOf(e));
	}

	@Override
	public Void visit(IMethodDeclaration stmt, Set<Fact> facts) {

		Event e = new Event();
		e.setKind(EventKind.METHOD_DECLARATION);
		e.setMethod(stmt.getName());

		facts.add(toFact(e));

		return super.visit(stmt, facts);

	}

	@Override
	public Void visit(IInvocationExpression expr, Set<Fact> facts) {
		Event e = new Event();
		e.setKind(EventKind.INVOCATION);
		e.setMethod(expr.getMethodName());

		Fact newFact = toFact(e);

		Set<Fact> newFacts = Sets.newHashSet(newFact);
		for (Fact fact : facts) {
			if (!fact.isRelationship()) {
				newFacts.add(new Fact(fact, newFact));
			}
		}
		facts.addAll(newFacts);

		return super.visit(expr, facts);
	}
}