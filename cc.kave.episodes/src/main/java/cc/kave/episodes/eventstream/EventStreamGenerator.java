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
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
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
import com.google.common.collect.Sets;

public class EventStreamGenerator {

	private Set<IMethodName> uniqueMethods = Sets.newHashSet();
	private List<Event> events = Lists.newLinkedList();
	private Set<ITypeName> seenTypes = Sets.newLinkedHashSet();

	public Set<IMethodName> add(Context ctx) {
		ISST sst = ctx.getSST();
		if (!isGenerated(sst)) {
			sst.accept(new EventStreamGenerationVisitor(uniqueMethods),
					ctx.getTypeShape());
		}
		return uniqueMethods;
	}

	private boolean isGenerated(ISST sst) {
		if (sst.isPartialClass()) {
			String fileName = sst.getPartialClassIdentifier();
			if (fileName.contains(".designer")
					|| fileName.contains(".Designer")) {
				return true;
			}
		}
		return false;
	}

	public void addAny(Context ctx) {
		ISST sst = ctx.getSST();
		sst.accept(new EventStreamGenerationVisitor(uniqueMethods),
				ctx.getTypeShape());
	}

	public List<Event> getEventStream() {
		return events;
	}
	
	public Set<IMethodName> getMethodNames() {
		return uniqueMethods;
	}
	
	private class EventStreamGenerationVisitor extends
			AbstractTraversingNodeVisitor<ITypeShape, Void> {

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
		
		private final Set<IMethodName> uniqueMethods;

		private IMethodName firstCtx;
		private IMethodName superCtx;
		private IMethodName elementCtx;

		public EventStreamGenerationVisitor(Set<IMethodName> uniqueMethods) {
			this.uniqueMethods = uniqueMethods;
		}

		@Override
		public Void visit(IMethodDeclaration decl, ITypeShape context) {

			firstCtx = Names.getUnknownMethod();
			superCtx = Names.getUnknownMethod();
			elementCtx = Names.getUnknownMethod();
			IMethodName name = decl.getName();
			if (uniqueMethods.contains(name)) {
				return super.visit(decl, context);
			}
			uniqueMethods.add(name);

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
				IMethodName erased = erase(inv.getMethodName());
				events.add(Events.newInvocation(erased));
			}
			return null;
		}

		private IMethodName erase(IMethodName methodName) {
			IMethodName erased = TypeErasure.of(methodName);
			// String crashingId =
			// "[p:double] [i:Accord.Math.Distances.IDistance`2[[T],[U -> System.Tuple`2[[T1], mscorlib, 4.0.0.0]], Accord.Math].Distance([T] x, [U] y)";
			// String crashingPart = "Accord.Math.Distances.IDistance";
			// if(erased.getIdentifier().equals(crashingId)){
			// System.out.println("!!!! problem !!!!");
			// System.out.println("input: " + methodName.getIdentifier());
			// System.out.println("erased: " + erased.getIdentifier());
			// System.out.println();
			// }
			return erased;
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
				events.add(Events.newFirstContext(erase(firstCtx)));
				firstCtx = null;
			}
			if (superCtx != null) {
				Event superEvent = Events.newSuperContext(erase(superCtx));
				if (!superEvent.getMethod().isUnknown()) {
					events.add(superEvent);
				}
				superCtx = null;
			}
			if (elementCtx != null) {
				events.add(Events.newContext(erase(elementCtx)));
				elementCtx = null;
			}
		}
	}
}