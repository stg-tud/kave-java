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
package cc.kave.episodes.evaluation.generation2;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.utils.TypeErasure;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;

public class PartialEventStreamGenerator {

	private final ISSTNodeVisitor<ITypeShape, Void> visitor = new EventStreamGenerationVisitor();

	private Set<ITypeName> seenTypes;
	private Set<IMethodName> seenMethods;
	private List<Event> events;

	public List<Event> extract(List<Context> ctxs, Set<ITypeName> seenTypes, Set<IMethodName> seenMethods) {
		this.seenTypes = seenTypes;
		this.seenMethods = seenMethods;
		events = Lists.newLinkedList();

		for (Context ctx : ctxs) {
			ISST sst = ctx.getSST();
			ITypeShape typeShape = ctx.getTypeShape();

			sst.accept(visitor, typeShape);
		}

		return events;
	}

	private class EventStreamGenerationVisitor extends AbstractTraversingNodeVisitor<ITypeShape, Void> {

		@Override
		public Void visit(ISST sst, ITypeShape context) {

			if (isGenerated(sst)) {
				return null;
			}

			ITypeName type = sst.getEnclosingType();
			if (!seenTypes.add(type) && !sst.isPartialClass()) {
				return null;
			}

			events.add(Events.newType(type));
			return super.visit(sst, context);
		}

		private boolean isGenerated(ISST td) {
			String partId = td.getPartialClassIdentifier();
			return td.isPartialClass() && partId != null
					&& (partId.contains(".Designer") || partId.contains(".designer"));
		}

		private IMethodName ctxElem;
		private IMethodName ctxSuper;
		private IMethodName ctxFirst;

		@Override
		public Void visit(IMethodDeclaration decl, ITypeShape context) {

			IMethodName m = decl.getName();
			if (!seenMethods.add(TypeErasure.of(m))) {
				return null;
			}

			IMethodName name = decl.getName();
			ctxElem = name;
			for (IMethodHierarchy h : context.getMethodHierarchies()) {
				if (h.getElement().equals(name)) {
					ctxSuper = h.getSuper();
					ctxFirst = h.getFirst();
				}
			}

			return super.visit(decl, context);
		}

		@Override
		public Void visit(IInvocationExpression inv, ITypeShape context) {
			IMethodName m = TypeErasure.of(inv.getMethodName());
			if (shouldInclude(m)) {
				addEnclosingMethodIfAvailable();
				events.add(Events.newInvocation(m));
			}
			return null;
		}

		private boolean shouldInclude(IMethodName name) {
			if (name.isUnknown()) {
				return false;
			}
			return !name.getDeclaringType().getAssembly().isLocalProject();
		}

		private void addEnclosingMethodIfAvailable() {
			if (ctxFirst != null) {
				events.add(Events.newFirstContext(TypeErasure.of(ctxFirst)));
				ctxFirst = null;
			}
			if (ctxSuper != null) {
				events.add(Events.newSuperContext(TypeErasure.of(ctxSuper)));
				ctxSuper = null;
			}
			if (ctxElem != null) {
				events.add(Events.newContext(TypeErasure.of(ctxElem)));
				ctxElem = null;
			}
		}

		@Override
		public Void visit(ILambdaExpression inv, ITypeShape context) {
			// stop here for now!
			return null;
		}
	}
}