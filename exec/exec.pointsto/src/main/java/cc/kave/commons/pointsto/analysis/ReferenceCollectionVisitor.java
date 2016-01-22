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
package cc.kave.commons.pointsto.analysis;

import com.google.common.collect.Multimap;

import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.LambdaName;
import cc.kave.commons.model.names.ParameterName;
import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.declarations.IVariableDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.pointsto.SSTBuilder;

public class ReferenceCollectionVisitor extends TraversingVisitor<Multimap<IReference, TypeName>, Void> {

	@Override
	public Void visit(IFieldDeclaration fieldDecl, Multimap<IReference, TypeName> context) {
		FieldName field = fieldDecl.getName();
		IReference fieldRef = SSTBuilder.fieldReference(field);
		context.put(fieldRef, field.getValueType());
		return null;
	}

	@Override
	public Void visit(IFieldReference fieldRef, Multimap<IReference, TypeName> context) {
		context.put(fieldRef, fieldRef.getFieldName().getValueType());
		return null;
	}

	@Override
	public Void visit(IPropertyDeclaration propertyDecl, Multimap<IReference, TypeName> context) {
		PropertyName property = propertyDecl.getName();
		IReference propertyRef = SSTBuilder.propertyReference(property);
		context.put(propertyRef, property.getValueType());

		return super.visit(propertyDecl, context);
	}

	@Override
	public Void visit(IPropertyReference propertyRef, Multimap<IReference, TypeName> context) {
		context.put(propertyRef, propertyRef.getPropertyName().getValueType());
		return null;
	}

	@Override
	public Void visit(IVariableDeclaration varDecl, Multimap<IReference, TypeName> context) {
		context.put(varDecl.getReference(), varDecl.getType());
		return null;
	}

	@Override
	public Void visit(IMethodDeclaration methodDecl, Multimap<IReference, TypeName> context) {
		for (ParameterName parameter : methodDecl.getName().getParameters()) {
			IReference parameterRef = SSTBuilder.variableReference(parameter.getName());
			context.put(parameterRef, parameter.getValueType());
		}

		return super.visit(methodDecl, context);
	}

	@Override
	public Void visit(ITryBlock block, Multimap<IReference, TypeName> context) {
		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			if (catchBlock.getKind() == CatchBlockKind.Default) {
				ParameterName parameter = catchBlock.getParameter();
				IReference parameterRef = SSTBuilder.variableReference(parameter.getName());
				context.put(parameterRef, parameter.getValueType());
			}
		}

		return super.visit(block, context);
	}

	@Override
	public Void visit(ILambdaExpression expr, Multimap<IReference, TypeName> context) {
		LambdaName lambda = expr.getName();
		for (ParameterName parameter : lambda.getParameters()) {
			IReference parameterRef = SSTBuilder.variableReference(parameter.getName());
			context.put(parameterRef, parameter.getValueType());
		}

		visitStatements(expr.getBody(), context);
		return null;
	}

}