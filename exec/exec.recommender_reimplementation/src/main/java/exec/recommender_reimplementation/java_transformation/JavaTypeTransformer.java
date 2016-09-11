/**
 * Copyright 2016 Technische Universität Darmstadt
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
package exec.recommender_reimplementation.java_transformation;

import static cc.kave.commons.utils.StringUtils.f;
import static exec.recommender_reimplementation.java_printer.JavaNameUtils.createFieldName;
import static exec.recommender_reimplementation.java_printer.JavaNameUtils.createMethodName;
import static exec.recommender_reimplementation.java_printer.PhantomClassGeneratorUtil.transformNestedType;

import java.text.MessageFormat;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IParameterName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.statements.VariableDeclaration;
import cc.kave.commons.model.ssts.impl.visitor.IdentityVisitor;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;

public class JavaTypeTransformer extends IdentityVisitor<Void> {

	@SuppressWarnings("unchecked")
	public static <T extends ISSTNode> T transform(T node) {
		if (node == null) {
			return node;
		}
		T transformedNode = (T) node.accept(new JavaTypeTransformer(), null);
		return transformedNode;
	}

	@Override
	public ISSTNode visit(IVariableDeclaration stmt, Void context) {
		ITypeName type = stmt.getType();
		VariableDeclaration varDeclImpl = (VariableDeclaration) stmt;
		varDeclImpl.setType(transformType(type));
		return super.visit(stmt, context);
	}

	@Override
	public ISSTNode visit(IFieldDeclaration stmt, Void context) {
		IFieldName fieldName = stmt.getName();
		FieldDeclaration fieldDeclImpl = (FieldDeclaration) stmt;
		fieldDeclImpl.setName(transformFieldName(fieldName));
		return super.visit(stmt, context);
	}

	@Override
	public ISSTNode visit(IMethodDeclaration decl, Void context) {
		IMethodName methodName = decl.getName();
		MethodDeclaration methodDeclImpl = (MethodDeclaration) decl;
		methodDeclImpl.setName(transformMethodName(methodName));
		return super.visit(decl, context);
	}

	@Override
	public ISSTNode visit(IInvocationExpression invocation, Void context) {
		IMethodName methodName = invocation.getMethodName();
		InvocationExpression methodDeclImpl = (InvocationExpression) invocation;
		methodDeclImpl.setMethodName(transformMethodName(methodName));
		return super.visit(invocation, context);
	}

	@Override
	public ISSTNode visit(IFieldReference fieldRef, Void context) {
		IFieldName fieldName = fieldRef.getFieldName();
		FieldReference fieldDeclImpl = (FieldReference) fieldRef;
		fieldDeclImpl.setFieldName(transformFieldName(fieldName));
		return super.visit(fieldRef, context);
	}

	@Override
	public ISSTNode visit(IMethodReference methodRef, Void context) {
		IMethodName methodName = methodRef.getMethodName();
		MethodReference methodRefImpl = (MethodReference) methodRef;
		methodRefImpl.setMethodName(transformMethodName(methodName));
		return super.visit(methodRef, context);
	}

	private ITypeName transformType(ITypeName type) {
		if (type.isArray()) {
			ITypeName baseType = type.asArrayTypeName().getArrayBaseType();
			if (baseType.isUnknown()) {
				baseType = Names.newType("p:object");
			}
			ITypeName newType = Names
				.newType(MessageFormat.format("{0}$Array,{1}", baseType.getFullName(),
						baseType.getAssembly().getIdentifier()));
			return newType;
		}
		if (type.isTypeParameter()) {
			return Names.newType("p:object");
		}
		if (type.isNestedType()) {
			return transformNestedType(type);
		}
		return type;
	}

	private IFieldName transformFieldName(IFieldName fieldName) {
		return createFieldName(transformType(fieldName.getValueType()),
				transformType(fieldName.getDeclaringType()), fieldName.getName(), fieldName.isStatic());
	}

	private IMethodName transformMethodName(IMethodName methodName) {
		IParameterName[] newParameterArray = new IParameterName[methodName.getParameters().size()];
		for (int i = 0; i < methodName.getParameters().size(); i++) {
			IParameterName parameterName = methodName.getParameters().get(i);
			newParameterArray[i] = changeTypeInParameterName(parameterName);

		}
		return createMethodName(transformType(methodName.getReturnType()),
				transformType(methodName.getDeclaringType()),
				methodName.getName(), methodName.isStatic(), newParameterArray);
	}

	private IParameterName changeTypeInParameterName(IParameterName parameterName) {
		if (parameterName.isParameterArray()) {
			return parameterName;
		}
		if (parameterName.getValueType().isArray() || parameterName.getValueType().isTypeParameter()) {
			String identifier = parameterName.getIdentifier();
			String newIdentifier = identifier.replace(f("[%s]", parameterName.getValueType().getIdentifier()),
					f("[%s]", transformType(parameterName.getValueType()).getIdentifier()));
			return Names.newParameter(newIdentifier);
		}
		return parameterName;
	}

}
