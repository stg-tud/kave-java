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
package cc.kave.commons.model.ssts.impl.visitor.inlining;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.expressions.assignable.IIndexAccessExpression;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.IndexAccessReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
import cc.kave.commons.model.ssts.references.IEventReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IIndexAccessReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IUnknownReference;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class InliningIReferenceVisitor extends AbstractThrowingNodeVisitor<InliningContext, IReference> {

	public IReference visit(IVariableReference ref, InliningContext context) {
		return context.resolve(ref);
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

	@Override
	public IReference visit(IIndexAccessReference indexAccessRef, InliningContext context) {
		IndexAccessReference ref = new IndexAccessReference();
		ref.setExpression((IIndexAccessExpression) indexAccessRef.getExpression().accept(context.getExpressionVisitor(), context));;
		return ref;
	}

}