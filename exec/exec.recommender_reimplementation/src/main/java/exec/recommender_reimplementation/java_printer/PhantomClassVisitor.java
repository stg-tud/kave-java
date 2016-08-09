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

import java.util.Map;

import com.google.common.collect.Maps;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.pointsto.analysis.visitors.TraversingVisitor;

public class PhantomClassVisitor extends TraversingVisitor<Map<ITypeName, SST>, Void> {
		
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
		
		if (isJavaValueType(type) || type.equals(className))
			return super.visit(stmt, context);
		if (!context.containsKey(type)) {
			createNewSST(type, context);
		}
		return super.visit(stmt, context);
	}

	@Override
	public Void visit(IInvocationExpression invocation, Map<ITypeName, SST> context) {
		ITypeName type = invocation.getMethodName().getDeclaringType();
		String identifier = invocation.getReference().getIdentifier();
		if (isReferenceToOutsideClass(type, identifier) && !isJavaValueType(type)) {
			addMethodDeclarationToSST(invocation, getOrCreateSST(type, context));
			handleReceiverType(invocation, context);
		}
		return super.visit(invocation, context);
	}

	private void handleReceiverType(IInvocationExpression invocation, Map<ITypeName, SST> context) {
		if(referenceToTypeMap.containsKey(invocation.getReference())) {
			ITypeName receiverType = referenceToTypeMap.get(invocation.getReference());
			if (!receiverType.isDelegateType()) {
				addMethodDeclarationToSST(invocation, getOrCreateSST(receiverType, context));
			}
		}
	}

	@Override
	public Void visit(IFieldReference fieldRef, Map<ITypeName, SST> context) {
		ITypeName type = fieldRef.getFieldName().getDeclaringType();
		String identifier = fieldRef.getReference().getIdentifier();
		if (isReferenceToOutsideClass(type, identifier)) {
			addFieldDeclarationToSST(fieldRef, getOrCreateSST(type, context));
		}
		return super.visit(fieldRef, context);
	}

	@Override
	public Void visit(IPropertyReference propertyRef, Map<ITypeName, SST> context) {
		ITypeName type = propertyRef.getPropertyName().getDeclaringType();
		String identifier = propertyRef.getReference().getIdentifier();
		if (isReferenceToOutsideClass(type, identifier)) {
			addPropertyDeclarationToSST(propertyRef, getOrCreateSST(type, context));
		}
		return super.visit(propertyRef, context);
	}

	private boolean isReferenceToOutsideClass(ITypeName type, String identifier) {
		return !identifier.equals("this") && !type.equals(className);
	}

}