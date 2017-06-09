package cc.kave.episodes.statistics;

import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.utils.TypeErasure;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;

import com.google.common.collect.Lists;

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

			ITypeName type = TypeErasure.of(sst.getEnclosingType());
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
		public Void visit(IPropertyDeclaration pdecl, ITypeShape context) {
			return null;
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
		
		@Override
		public Void visit(ILambdaExpression inv, ITypeShape context) {
			// stop here for now!
			return null;
		}

		private boolean shouldInclude(IMethodName name) {
			if (name.isUnknown()) {
				return false;
			}
			return !name.getDeclaringType().getAssembly().isLocalProject();
		}
		
		private void addEnclosingMethodIfAvailable() {
			if (ctxElem != null) {
				events.add(Events.newContext(TypeErasure.of(ctxElem)));
				ctxElem = null;
			}
			if (ctxFirst != null) {
				events.add(Events.newFirstContext(TypeErasure.of(ctxFirst)));
				ctxFirst = null;
			}
			if (ctxSuper != null) {
				events.add(Events.newSuperContext(TypeErasure.of(ctxSuper)));
				ctxSuper = null;
			}
		}
	}
}
