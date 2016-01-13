/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.analysis.reference;

import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.impl.visitor.AbstractNodeVisitor;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.pointsto.ScopedMap;

public class DistinctReferenceVisitor
		extends AbstractNodeVisitor<ScopedMap<String, IVariableDeclaration>, DistinctReference> {

	@Override
	public DistinctReference visit(IFieldReference fieldRef, ScopedMap<String, IVariableDeclaration> context) {
		return new DistinctFieldReference(fieldRef);
	}

	@Override
	public DistinctReference visit(IPropertyReference propertyRef, ScopedMap<String, IVariableDeclaration> context) {
		return new DistinctPropertyReference(propertyRef);
	}

	@Override
	public DistinctReference visit(IVariableReference varRef, ScopedMap<String, IVariableDeclaration> context) {
		IVariableDeclaration varDecl = context.get(varRef.getIdentifier());
		return new DistinctVariableReference(varRef, varDecl);
	}

}
