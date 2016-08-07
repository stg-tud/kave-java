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

import static cc.kave.commons.model.ssts.impl.SSTUtil.constant;
import static exec.recommender_reimplementation.java_printer.JavaNameUtils.getTypeAliasFromFullTypeName;

import java.util.Map;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;

public class PhantomClassGeneratorUtil {
	
	public static void addFieldDeclarationToSST(IFieldReference fieldRef, SST sst) {
		FieldDeclaration fieldDecl = new FieldDeclaration();
		fieldDecl.setName(fieldRef.getFieldName());
		sst.getFields().add(fieldDecl);
	}

	public static void addMethodDeclarationToSST(IInvocationExpression invocation, SST sst) {
		MethodDeclaration methodDecl = new MethodDeclaration();
		IMethodName methodName = invocation.getMethodName();
		methodDecl.setName(methodName);
		if(!methodName.getReturnType().isVoidType()) {
			addReturnStatement(methodDecl, methodName);
		}
		
		sst.getMethods().add(methodDecl);
	}

	public static void addReturnStatement(MethodDeclaration methodDecl, IMethodName methodName) {
		ReturnStatement returnStatement = new ReturnStatement();
		ITypeName returnType = methodName.getReturnType();
		if(isJavaValueType(methodName.getReturnType())) {
			String defaultValue = JavaPrintingUtils.getDefaultValueForType(returnType);
			returnStatement.setExpression(constant(defaultValue));
		}
		else{
			returnStatement.setExpression(constant("null"));
		}
		methodDecl.getBody().add(returnStatement);
	}

	public static void addPropertyDeclarationToSST(IPropertyReference propertyRef, SST sst) {
		PropertyDeclaration propertyDecl = new PropertyDeclaration();
		propertyDecl.setName(propertyRef.getPropertyName());
		sst.getProperties().add(propertyDecl);
	}

	public static SST createNewSST(ITypeName type, Map<ITypeName, SST> context) {
		SST sst = new SST();
		sst.setEnclosingType(type);
		context.put(type, sst);
		return sst;
	}

	public static SST getOrCreateSST(ITypeName type, Map<ITypeName, SST> context) {
		SST sst;
		if (context.containsKey(type)) {
			sst = context.get(type);
		} else {
			sst = createNewSST(type, context);
		}
		return sst;
	}

	public static boolean isJavaValueType(ITypeName type) {
		if (type.getFullName().equals("System.String")) {
			return true;
		}
		String aliasType = getTypeAliasFromFullTypeName(type.getFullName());
		if (!aliasType.equals(type.getFullName())) {
			return true;
		}
		return false;
	}
}
