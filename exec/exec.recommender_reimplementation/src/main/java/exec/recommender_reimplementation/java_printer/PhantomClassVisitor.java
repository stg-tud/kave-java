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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.pointsto.analysis.visitors.TraversingVisitor;

public class PhantomClassVisitor extends TraversingVisitor<Void, Void> {

	private Set<ITypeName> seenClasses;
	private Map<ITypeName, SST> phantomSSTs;

	public PhantomClassVisitor() {
		seenClasses = new HashSet<>();
		phantomSSTs = new HashMap<>();
	}
	
	// TODO: test phantom class visitor
	
	@Override
	public Void visit(IVariableDeclaration stmt, Void context) {
		ITypeName type = stmt.getType();
		if(isJavaValueType(type)) return super.visit(stmt, context);
		seenClasses.add(type);
		if(!phantomSSTs.containsKey(type)) {
			SST sst = createNewSST(type);
			phantomSSTs.put(type, sst);
		}
		return super.visit(stmt, context);
	}

	private boolean isJavaValueType(ITypeName type) {
		if(type.getFullName().equals("System.String")) {
			return true;
		}
		String aliasType = getTypeAliasFromFullTypeName(type.getFullName());
		if(!aliasType.equals(type.getFullName())) {
			return true;
		}
		return false;
	}

	private SST createNewSST(ITypeName type) {
		SST sst = new SST();
		sst.setEnclosingType(type);
		return sst;
	}

	@Override
	public Void visit(IInvocationExpression invocation, Void context) {
		if(invocation.getReference().getIdentifier().equals("this")) return super.visit(invocation, context);
		ITypeName type = invocation.getMethodName().getDeclaringType();
		if(phantomSSTs.containsKey(type)) {
			SST sst = phantomSSTs.get(type);
			addMethodDeclarationToSST(invocation, sst);
		}
		else {
			// static invocation 
			SST newSST = createNewSST(type);
			seenClasses.add(type);
			phantomSSTs.put(type, newSST);
			addMethodDeclarationToSST(invocation, newSST);
		}
		return super.visit(invocation, context);
	}

	@Override
	public Void visit(IPropertyReference propertyRef, Void context) {
		if(!propertyRef.getReference().getIdentifier().equals("this")) {
			ITypeName type = propertyRef.getPropertyName().getDeclaringType();
			seenClasses.add(type);
			if(phantomSSTs.containsKey(type)) {
				SST sst = phantomSSTs.get(type);
				addPropertyDeclarationToSST(propertyRef, sst);
			}
			else {
				SST newSST = createNewSST(type);
				phantomSSTs.put(type, newSST);
				addPropertyDeclarationToSST(propertyRef, newSST);
			}
		}
		return super.visit(propertyRef, context);
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

	public Map<ITypeName, SST> getPhantomSSTs() {
		return phantomSSTs;
	}

	public Set<ITypeName> getSeenClasses() {
		return seenClasses;
	}

}
