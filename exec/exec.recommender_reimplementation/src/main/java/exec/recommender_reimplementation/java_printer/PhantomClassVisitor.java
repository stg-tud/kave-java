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
package exec.recommender_reimplementation.java_printer;

import static exec.recommender_reimplementation.java_printer.PhantomClassGeneratorUtil.addFieldDeclarationToSST;
import static exec.recommender_reimplementation.java_printer.PhantomClassGeneratorUtil.addMethodDeclarationToSST;
import static exec.recommender_reimplementation.java_printer.PhantomClassGeneratorUtil.addPropertyDeclarationToSST;
import static exec.recommender_reimplementation.java_printer.PhantomClassGeneratorUtil.createNewSST;
import static exec.recommender_reimplementation.java_printer.PhantomClassGeneratorUtil.getOrCreateSST;
import static exec.recommender_reimplementation.java_printer.PhantomClassGeneratorUtil.isJavaValueType;
import static exec.recommender_reimplementation.java_printer.PhantomClassGeneratorUtil.isValidType;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IMethodReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;

public class PhantomClassVisitor extends AbstractTraversingNodeVisitor<Map<ITypeName, SST>, Void> {
		
	private ITypeName className;
	private Map<IVariableReference,ITypeName> referenceToTypeMap;

	public PhantomClassVisitor() {
		referenceToTypeMap = Maps.newHashMap();
	}
	
	@Override
	public Void visit(ISST sst, Map<ITypeName, SST> context) {
		className = sst.getEnclosingType();
		return super.visit(sst, context);
	}

	@Override
	public Void visit(IVariableDeclaration stmt, Map<ITypeName, SST> context) {
		ITypeName type = stmt.getType();
		referenceToTypeMap.put(stmt.getReference(), type);
		
		addTypeToMap(context, type);
		return super.visit(stmt, context);
	}

	@Override
	public Void visit(IFieldDeclaration stmt, Map<ITypeName, SST> context) {
		addTypeToMap(context, stmt.getName().getValueType());
		return super.visit(stmt, context);
	}

	@Override
	public Void visit(IMethodDeclaration decl, Map<ITypeName, SST> context) {
		ITypeName returnType = decl.getName().getReturnType();
		if (!returnType.isVoidType()) {
			addTypeToMap(context, returnType);
		}
		handleMethodParameters(decl.getName().getParameters(), context);
		return super.visit(decl, context);
	}

	@Override
	public Void visit(IPropertyDeclaration decl, Map<ITypeName, SST> context) {
		addTypeToMap(context, decl.getName().getValueType());
		return super.visit(decl, context);
	}

	@Override
	public Void visit(ITryBlock block, Map<ITypeName, SST> context) {
		for (ICatchBlock catchBlock : block.getCatchBlocks()) {
			ITypeName type = catchBlock.getParameter().getValueType();
			addTypeToMap(context, type);
		}
		return super.visit(block, context);
	}

	@Override
	public Void visit(IInvocationExpression invocation, Map<ITypeName, SST> context) {
		handleMethod(invocation.getMethodName(), invocation.getReference(), context);
		return super.visit(invocation, context);
	}

	@Override
	public Void visit(IFieldReference fieldRef, Map<ITypeName, SST> context) {
		ITypeName type = fieldRef.getFieldName().getDeclaringType();
		String identifier = fieldRef.getReference().getIdentifier();
		if (isReferenceToOutsideClass(type, identifier) && isValidType(type)) {
			addFieldDeclarationToSST(fieldRef, getOrCreateSST(type, context));
		}
		return super.visit(fieldRef, context);
	}

	@Override
	public Void visit(IMethodReference methodRef, Map<ITypeName, SST> context) {
		handleMethod(methodRef.getMethodName(), methodRef.getReference(), context);
		return super.visit(methodRef, context);
	}

	private void addTypeToMap(Map<ITypeName, SST> context, ITypeName type) {
		if (isJavaValueType(type) || type.equals(className) || !isValidType(type)) {
			return;
		}
		if (!context.containsKey(type)) {
			createNewSST(type, context);
		}
	}

	private void handleMethod(IMethodName methodName, IVariableReference varRef, Map<ITypeName, SST> context) {
		ITypeName type = methodName.getDeclaringType();
		String identifier = varRef.getIdentifier();
		if (isReferenceToOutsideClass(type, identifier) && !isJavaValueType(type) && isValidType(type)) {
			addMethodDeclarationToSST(methodName, getOrCreateSST(type, context));
			handleReceiverType(varRef, methodName, context);
		}
		handleMethodParameters(methodName.getParameters(), context);
	}

	private void handleMethodParameters(List<IParameterName> parameters, Map<ITypeName, SST> context) {
		for (IParameterName parameterName : parameters) {
			ITypeName type = parameterName.getValueType();
			addTypeToMap(context, type);		
		}
	}

	private void handleReceiverType(IVariableReference varRef, IMethodName methodName, Map<ITypeName, SST> context) {
		if (referenceToTypeMap.containsKey(varRef)) {
			ITypeName receiverType = referenceToTypeMap.get(varRef);
			if (isValidType(receiverType)) {
				addMethodDeclarationToSST(methodName, getOrCreateSST(receiverType, context));
			}
		}
	}

	@Override
	public Void visit(IPropertyReference propertyRef, Map<ITypeName, SST> context) {
		ITypeName type = propertyRef.getPropertyName().getDeclaringType();
		String identifier = propertyRef.getReference().getIdentifier();
		if (isReferenceToOutsideClass(type, identifier) && isValidType(type)) {
			addPropertyDeclarationToSST(propertyRef, getOrCreateSST(type, context));
		}
		return super.visit(propertyRef, context);
	}

	private boolean isReferenceToOutsideClass(ITypeName type, String identifier) {
		return !identifier.equals("this") && !type.equals(className);
	}

}