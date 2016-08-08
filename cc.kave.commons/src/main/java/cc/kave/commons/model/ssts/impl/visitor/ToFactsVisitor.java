/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
		if (!events.contains(e)) {
			events.add(e);
		}
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

		// remove double order in queries (a b a)
		Set<Fact> oldFacts = Sets.newHashSet();
		if (facts.contains(newFact)) {
			for (Fact fact : facts) {
				Fact order = new Fact(newFact, fact);
				if (facts.contains(order)) {
					oldFacts.add(order);
				}
			}
			facts.removeAll(oldFacts);
			return super.visit(expr, facts);
		}

		Set<Fact> newFacts = Sets.newHashSet(newFact);
		for (Fact fact : facts) {
			if (!fact.isRelation()) {
				newFacts.add(new Fact(fact, newFact));
			}
		}
		facts.addAll(newFacts);

		return super.visit(expr, facts);
	}
}