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
import static cc.kave.commons.utils.TypeErasure.ErasureStrategy.FULL;
import static exec.recommender_reimplementation.java_printer.JavaNameUtils.getTypeAliasFromFullTypeName;

import java.util.Map;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.impl.v0.types.TypeName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.utils.TypeErasure;

public class PhantomClassGeneratorUtil {

	public static void addFieldDeclarationToSST(IFieldName fieldname, SST sst) {
		FieldDeclaration fieldDecl = new FieldDeclaration();
		fieldDecl.setName(Names.newField(TypeErasure.of(fieldname.getIdentifier(), FULL)));
		sst.getFields().add(fieldDecl);
	}

	public static void addMethodDeclarationToSST(IMethodName methodName, SST sst) {
		MethodDeclaration methodDecl = new MethodDeclaration();
		IMethodName nameWithoutGenerics = TypeErasure.of(methodName, FULL);
		methodDecl.setName(nameWithoutGenerics);
		if (!nameWithoutGenerics.getReturnType().isVoidType()) {
			addReturnStatement(methodDecl, nameWithoutGenerics);
		}

		sst.getMethods().add(methodDecl);
	}

	public static void addReturnStatement(MethodDeclaration methodDecl, IMethodName methodName) {
		ReturnStatement returnStatement = new ReturnStatement();
		ITypeName returnType = methodName.getReturnType();
		if (isJavaValueType(methodName.getReturnType())) {
			String defaultValue = JavaPrintingUtils.getDefaultValueForType(returnType);
			returnStatement.setExpression(constant(defaultValue));
		} else {
			returnStatement.setExpression(constant("null"));
		}
		methodDecl.getBody().add(returnStatement);
	}

	public static void addPropertyDeclarationToSST(IPropertyName propertyName, SST sst) {
		PropertyDeclaration propertyDecl = new PropertyDeclaration();
		propertyDecl
				.setName(Names.newProperty(TypeErasure.of(propertyName.getIdentifier(), FULL)));
		sst.getProperties().add(propertyDecl);
	}

	public static SST createNewSST(ITypeName type, Map<ITypeName, SST> context) {
		SST sst = new SST();
		ITypeName transformedType = getTransformedType(type);
		sst.setEnclosingType(transformedType);
		context.put(transformedType, sst);
		return sst;
	}

	public static SST getOrCreateSST(ITypeName type, Map<ITypeName, SST> context) {
		SST sst;
		ITypeName transformedType = getTransformedType(type);
		if (context.containsKey(transformedType)) {
			sst = context.get(transformedType);
		} else {
			sst = createNewSST(transformedType, context);
		}
		return sst;
	}

	public static ITypeName getTransformedType(ITypeName type) {
		ITypeName typeWithoutGenerics = TypeErasure.of(type, FULL);
		ITypeName typeWithoutNestedTypes = transformNestedType(typeWithoutGenerics);
		ITypeName typeWithoutQualifier = removesQualifier(typeWithoutNestedTypes);
		return typeWithoutQualifier;
	}

	public static ITypeName removesQualifier(ITypeName typeName) {
		String identifier = typeName.getIdentifier();
		if (typeName.getIdentifier().contains(TypeName.PrefixEnum)) {
			return Names.newType(identifier.replace(TypeName.PrefixEnum, ""));
		}
		if (typeName.getIdentifier().contains(TypeName.PrefixInterface)) {
			return Names.newType(identifier.replace(TypeName.PrefixInterface, ""));
		}
		if (typeName.getIdentifier().contains(TypeName.PrefixStruct)) {
			return Names.newType(identifier.replace(TypeName.PrefixStruct, ""));
		}
		return typeName;
	}

	public static ITypeName transformNestedType(ITypeName typeName) {
		if (typeName.isNestedType()) {
			String identifier = typeName.getIdentifier();
			String newIdentifier = identifier.replace("+", "_");
			return Names.newType(newIdentifier);
		}
		return typeName;
	}

	public static boolean isJavaValueType(ITypeName type) {
		String aliasType = getTypeAliasFromFullTypeName(type.getFullName());
		if (!aliasType.equals(type.getFullName())) {
			return true;
		}
		return false;
	}

	public static boolean isValidType(ITypeName type) {
		return !type.isUnknown() && !type.isDelegateType();
	}
	
	public static boolean isValidValueType(ITypeName valueType) {
		return !valueType.isVoidType() && !valueType.isTypeParameter();
	}

}
