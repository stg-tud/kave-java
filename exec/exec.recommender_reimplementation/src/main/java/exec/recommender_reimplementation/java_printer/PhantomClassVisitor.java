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

import static exec.recommender_reimplementation.java_printer.JavaNameUtils.getTypeAliasFromFullTypeName;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.pointsto.analysis.visitors.TraversingVisitor;

public class PhantomClassVisitor extends TraversingVisitor<Void, Void> {

	private Map<ITypeName, SST> phantomSSTs;

	private ITypeName className;

	public PhantomClassVisitor() {
		phantomSSTs = new HashMap<>();
	}

	@Override
	public Void visit(ISST sst, Void context) {
		className = sst.getEnclosingType();
		return super.visit(sst, context);
	}

	@Override
	public Void visit(IVariableDeclaration stmt, Void context) {
		ITypeName type = stmt.getType();
		if (isJavaValueType(type) || type.equals(className))
			return super.visit(stmt, context);
		if (!phantomSSTs.containsKey(type)) {
			createNewSST(type);
		}
		return super.visit(stmt, context);
	}

	@Override
	public Void visit(IInvocationExpression invocation, Void context) {
		ITypeName type = invocation.getMethodName().getDeclaringType();
		String identifier = invocation.getReference().getIdentifier();
		if (isReferenceToOutsideClass(type, identifier)) {
			addMethodDeclarationToSST(invocation, getOrCreateSST(type));

		}
		return super.visit(invocation, context);
	}

	@Override
	public Void visit(IFieldReference fieldRef, Void context) {
		ITypeName type = fieldRef.getFieldName().getDeclaringType();
		String identifier = fieldRef.getReference().getIdentifier();
		if (isReferenceToOutsideClass(type, identifier)) {
			addFieldDeclarationToSST(fieldRef, getOrCreateSST(type));
		}
		return super.visit(fieldRef, context);
	}

	@Override
	public Void visit(IPropertyReference propertyRef, Void context) {
		ITypeName type = propertyRef.getPropertyName().getDeclaringType();
		String identifier = propertyRef.getReference().getIdentifier();
		if (isReferenceToOutsideClass(type, identifier)) {
			addPropertyDeclarationToSST(propertyRef, getOrCreateSST(type));
		}
		return super.visit(propertyRef, context);
	}

	private void addFieldDeclarationToSST(IFieldReference fieldRef, SST sst) {
		FieldDeclaration fieldDecl = new FieldDeclaration();
		fieldDecl.setName(fieldRef.getFieldName());
		sst.getFields().add(fieldDecl);
	}

	private void addMethodDeclarationToSST(IInvocationExpression invocation, SST sst) {
		MethodDeclaration methodDecl = new MethodDeclaration();
		methodDecl.setName(invocation.getMethodName());
		sst.getMethods().add(methodDecl);
	}

	private void addPropertyDeclarationToSST(IPropertyReference propertyRef, SST sst) {
		PropertyDeclaration propertyDecl = new PropertyDeclaration();
		propertyDecl.setName(propertyRef.getPropertyName());
		sst.getProperties().add(propertyDecl);
	}

	private SST createNewSST(ITypeName type) {
		SST sst = new SST();
		sst.setEnclosingType(type);
		phantomSSTs.put(type, sst);
		return sst;
	}

	private SST getOrCreateSST(ITypeName type) {
		SST sst;
		if (phantomSSTs.containsKey(type)) {
			sst = phantomSSTs.get(type);
		} else {
			sst = createNewSST(type);
		}
		return sst;
	}

	private boolean isJavaValueType(ITypeName type) {
		if (type.getFullName().equals("System.String")) {
			return true;
		}
		String aliasType = getTypeAliasFromFullTypeName(type.getFullName());
		if (!aliasType.equals(type.getFullName())) {
			return true;
		}
		return false;
	}

	private boolean isReferenceToOutsideClass(ITypeName type, String identifier) {
		return !identifier.equals("this") && !type.equals(className);
	}

	public Map<ITypeName, SST> getPhantomSSTs() {
		return phantomSSTs;
	}

	public Set<ITypeName> getSeenClasses() {
		return phantomSSTs.keySet();
	}

}
