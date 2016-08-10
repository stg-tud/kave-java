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

import static exec.recommender_reimplementation.java_printer.JavaPrintingUtils.getDefaultValueForType;

import java.util.Map;

import com.google.common.collect.Maps;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.IMemberDeclaration;
import cc.kave.commons.model.ssts.blocks.IIfElseBlock;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.references.IAssignableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.statements.IReturnStatement;
import cc.kave.commons.model.ssts.statements.IVariableDeclaration;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.utils.SSTNodeHierarchy;

public class DefaultValueHelper {

	protected SSTNodeHierarchy sstNodeHierarchy;
	protected Map<IVariableReference, ITypeName> variableTypeMap;

	public DefaultValueHelper(ISSTNode sstNode) {
		sstNodeHierarchy = new SSTNodeHierarchy(sstNode);
		variableTypeMap = Maps.newHashMap();
	}

	public void addVariableReferenceToTypeMapping(IVariableDeclaration varDecl) {
		variableTypeMap.put(varDecl.getReference(), varDecl.getType());
	}

	public ITypeName getTypeForVariableReference(IVariableReference varRef) {
		if (variableTypeMap.containsKey(varRef)) {
			return variableTypeMap.get(varRef);
		}
		return null;
	}

	private IMemberDeclaration findMemberDeclaration(ISSTNode sstNode) {
		ISSTNode parent = sstNodeHierarchy.getParent(sstNode);
		while (!(parent instanceof IMemberDeclaration)) {
			parent = sstNodeHierarchy.getParent(parent);
		}

		return (IMemberDeclaration) parent;
	}

	public String getDefaultValueForNode(ISSTNode node) {
		ISSTNode parent = sstNodeHierarchy.getParent(node);
		if (parent instanceof IAssignableExpression) {
			return getDefaultValueForAssignableExpression((IAssignableExpression) parent);
		}

		if (parent instanceof IIfElseBlock) {
			return "false";
		}

		if (parent instanceof IReturnStatement) {
			return getDefaultValueForReturnStatement((IReturnStatement) parent);
		}

		if (parent instanceof IAssignment) {
			return getDefaultValueForAssignment((IAssignment) parent);
		}
		return "null";
	}

	protected String getDefaultValueForAssignableExpression(IAssignableExpression assignableExpression) {
		ISSTNode parentStatement = sstNodeHierarchy.getParent(assignableExpression);
		if (parentStatement instanceof IAssignment) {
			return getDefaultValueForAssignment((IAssignment) parentStatement);
		}
		return "null";
	}

	protected String getDefaultValueForAssignment(IAssignment assignment) {
		IAssignableReference assignableReference = assignment.getReference();
		if (assignableReference instanceof IVariableReference) {
			return getDefaultValueForVariableReference((IVariableReference) assignment.getReference());
		}

		return "null";
	}

	protected String getDefaultValueForReturnStatement(IReturnStatement returnStatement) {
		IMemberDeclaration memberDeclaration = findMemberDeclaration(returnStatement);

		if (memberDeclaration instanceof IMethodDeclaration) {
			IMethodDeclaration methodDeclaration = (IMethodDeclaration) memberDeclaration;
			return getDefaultValueForType(methodDeclaration.getName().getReturnType());
		}
		if (memberDeclaration instanceof IPropertyDeclaration) {
			IPropertyDeclaration propertyDeclaration = (IPropertyDeclaration) memberDeclaration;
			return getDefaultValueForType(propertyDeclaration.getName().getValueType());
		}

		return "null";
	}

	protected String getDefaultValueForVariableReference(IVariableReference reference) {
		if (variableTypeMap.containsKey(reference)) {
			return getDefaultValueForType(variableTypeMap.get(reference));
		}
		return "null";
	}
}
