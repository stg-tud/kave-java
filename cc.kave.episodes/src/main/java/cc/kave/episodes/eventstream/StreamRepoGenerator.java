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
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.utils.TypeErasure;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;

import com.google.common.collect.Lists;

public abstract class StreamRepoGenerator {

	private List<Event> events = Lists.newLinkedList();
	
	public void add(Context ctx) {
		ISST sst = ctx.getSST();
		sst.accept(new EventStreamGenerationVisitor(), ctx.getTypeShape());
	}

	public List<Event> getEventStream() {
		return events;
	}

	public class EventStreamGenerationVisitor extends
			AbstractTraversingNodeVisitor<ITypeShape, Void> {

		private IMethodName firstCtx;
		private IMethodName superCtx;
		private IMethodName elementCtx;

		@Override
		public Void visit(IMethodDeclaration decl, ITypeShape context) {

			firstCtx = null;
			superCtx = null;
			IMethodName name = decl.getName();
			elementCtx = name;
			for (IMethodHierarchy h : context.getMethodHierarchies()) {
				if (h.getElement().equals(name)) {
					firstCtx = h.getFirst();
					superCtx = h.getSuper();
				}
			}
			return super.visit(decl, context);
		}
		
		@Override
		public Void visit(IPropertyDeclaration pdecl, ITypeShape context) {
			return null;
		}

		@Override
		public Void visit(IInvocationExpression inv, ITypeShape context) {
			IMethodName erased = erase(inv.getMethodName());
			addEnclosingMethodIfAvailable();
			events.add(Events.newInvocation(erased));
			return null;
		}

		private IMethodName erase(IMethodName methodName) {
			IMethodName erased = TypeErasure.of(methodName);
			return erased;
		}

		@Override
		public Void visit(ILambdaExpression inv, ITypeShape context) {
			// stop here for now!
			return null;
		}

		private void addEnclosingMethodIfAvailable() {
			if (elementCtx != null) {
				events.add(Events.newElementContext(erase(elementCtx)));
				elementCtx = null;
			}
			if (firstCtx != null) {
				events.add(Events.newFirstContext(erase(firstCtx)));
				firstCtx = null;
			}
			if (superCtx != null) {
				events.add(Events.newSuperContext(erase(superCtx)));
				superCtx = null;
			}
		}
	}
}