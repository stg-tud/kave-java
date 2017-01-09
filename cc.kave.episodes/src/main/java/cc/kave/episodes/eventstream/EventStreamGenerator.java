/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.episodes.eventstream;

import java.util.List;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.utils.TypeErasure;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;

import com.google.common.collect.Lists;

public class EventStreamGenerator {

	private List<Event> events = Lists.newLinkedList();

	public void add(Context ctx) {
		ISST sst = ctx.getSST();
		if (sst.isPartialClass()) {
			String fileName = sst.getPartialClassIdentifier();
			if (!(fileName.contains(".designer") || fileName.contains(".Designer"))) {
				sst.accept(new EventStreamGenerationVisitor(), ctx.getTypeShape());
			}
		} else {
			sst.accept(new EventStreamGenerationVisitor(), ctx.getTypeShape());
		}
	}

	public List<Event> getEventStream() {
		return events;
	}

	private class EventStreamGenerationVisitor extends AbstractTraversingNodeVisitor<ITypeShape, Void> {

		private IMethodName firstCtx;
		private IMethodName superCtx;
		private IMethodName elementCtx;

		@Override
		public Void visit(IMethodDeclaration decl, ITypeShape context) {

			firstCtx = Names.getUnknownMethod();
			superCtx = Names.getUnknownMethod();
			elementCtx = Names.getUnknownMethod();
			IMethodName name = decl.getName();
			for (IMethodHierarchy h : context.getMethodHierarchies()) {
				if (h.getElement().equals(name)) {
					if (h.getFirst() != null) {
						firstCtx = h.getFirst();
					}
					if (h.getSuper() != null) {
						superCtx = h.getSuper();
					}
					elementCtx = h.getElement();
				}
			}
			return super.visit(decl, context);
		}

		@Override
		public Void visit(IInvocationExpression inv, ITypeShape context) {
			if (shouldInclude(inv.getMethodName())) {
				addEnclosingMethodIfAvailable();
				events.add(Events.newInvocation(TypeErasure.of(inv.getMethodName())));
			}
			return null;
		}

		@Override
		public Void visit(ILambdaExpression inv, ITypeShape context) {
			// stop here for now!
			return null;
		}

		private boolean shouldInclude(IMethodName name) {
			if (name.isUnknown()) {
				return false;
			}
			return true;
		}

		private void addEnclosingMethodIfAvailable() {
			if (firstCtx != null) {
				events.add(Events.newFirstContext(TypeErasure.of(firstCtx)));
				firstCtx = null;
			}
			if (superCtx != null) {
				Event superEvent = Events.newSuperContext(TypeErasure.of(superCtx));
				if (!superEvent.getMethod().isUnknown()) {
					events.add(superEvent);
				}
				superCtx = null;
			}
			if (elementCtx != null) {
				events.add(Events.newContext(TypeErasure.of(elementCtx)));
				elementCtx = null;
			}
		}
	}
}