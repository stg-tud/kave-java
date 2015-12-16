/*
 * Copyright 2015 Carina Oberle
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.ssts.impl.transformation.constants;

import java.util.Set;

import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.expressions.simple.IConstantValueExpression;
import cc.kave.commons.model.ssts.expressions.simple.INullExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.expressions.simple.IUnknownExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractThrowingNodeVisitor;
import cc.kave.commons.model.ssts.references.IFieldReference;

public class SimpleExpressionVisitor extends AbstractThrowingNodeVisitor<Set<IFieldDeclaration>, Boolean> {

	@Override
	public Boolean visit(IConstantValueExpression expr, Set<IFieldDeclaration> constants) {
		return false;
	}

	@Override
	public Boolean visit(INullExpression expr, Set<IFieldDeclaration> constants) {
		return false;
	}

	@Override
	public Boolean visit(IReferenceExpression expr, Set<IFieldDeclaration> constants) {
		IReference reference = expr.getReference();
		if (reference instanceof IFieldReference) {
			for (IFieldDeclaration constant : constants) {
				if (constant.getName().equals(((IFieldReference) reference).getFieldName()))
					return true;
			}
		}
		return false;
	}

	@Override
	public Boolean visit(IUnknownExpression unknownExpr, Set<IFieldDeclaration> constants) {
		return false;
	}

}
