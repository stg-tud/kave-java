package cc.kave.commons.model.ssts.impl.visitor.inlining;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.impl.visitor.AbstractNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class InliningIReferenceVisitor extends AbstractNodeVisitor<InliningContext, IReference> {

	public IReference visit(IVariableReference ref, InliningContext context) {
		return context.resolveRef(ref);
	}

	@Override
	public IReference visit(IUnknownReference unknownRef, InliningContext context) {
		UnknownReference reference = new UnknownReference();
		return reference;
	}

	@Override
	public IReference visit(IEventReference eventRef, InliningContext context) {
		EventReference reference = new EventReference();
		reference.setEventName(eventRef.getEventName());
		reference.setReference(eventRef.getReference());
		return reference;
	}

	@Override
	public IReference visit(IFieldReference fieldRef, InliningContext context) {
		FieldReference reference = new FieldReference();
		reference.setFieldName(fieldRef.getFieldName());
		reference.setReference(fieldRef.getReference());
		return reference;
	}

	@Override
	public IReference visit(IPropertyReference methodRef, InliningContext context) {
		PropertyReference reference = new PropertyReference();
		reference.setPropertyName(methodRef.getPropertyName());
		reference.setReference(methodRef.getReference());
		return reference;
	}

	@Override
	public IReference visit(IMethodReference methodRef, InliningContext context) {
		MethodReference reference = new MethodReference();
		reference.setMethodName(methodRef.getMethodName());
		reference.setReference(methodRef.getReference());
		return reference;
	}

}
