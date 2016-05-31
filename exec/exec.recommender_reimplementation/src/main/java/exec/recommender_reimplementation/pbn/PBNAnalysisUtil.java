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
package exec.recommender_reimplementation.pbn;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.pointsto.analysis.types.TypeCollector;
import cc.kave.commons.utils.SSTNodeHierarchy;
import cc.recommenders.usages.Usage;

public class PBNAnalysisUtil {

	public static List<ITypeName> createTypeListFromParameters(List<ISimpleExpression> parameters, TypeCollector typeCollector) {
		List<ITypeName> typeList = new ArrayList<ITypeName>();
		for (ISimpleExpression parameter : parameters) {
			if (parameter instanceof IReferenceExpression) {
				IReferenceExpression refExpr = (IReferenceExpression) parameter;
				IReference reference = refExpr.getReference();
				ITypeName type = typeCollector.getType(reference);
				if (type != null)
					typeList.add(type);
			}
		}
		return typeList;
	}

	public static IMethodName findFirstMethodName(IMethodName methodName, ITypeShape typeShape) {
		Set<IMethodHierarchy> methodHierarchies = typeShape.getMethodHierarchies();
		Optional<IMethodHierarchy> methodHierarchy = methodHierarchies.stream()
				.filter(mh -> mh.getElement().equals(methodName)).findAny();
		IMethodName firstMethodName = null;
		if (methodHierarchy.isPresent()) {
			firstMethodName = methodHierarchy.get().getSuper();
		}
		return firstMethodName != null ? firstMethodName : methodName;
	}

	public static ITypeName findTypeForVarReference(IInvocationExpression invocation, TypeCollector typeCollector) {
		return typeCollector.getType(invocation.getReference());
	}

	public static boolean isCallToSuperClass(IInvocationExpression expr, ISST sst) {
		if (!expr.getReference().getIdentifier().equals("this"))
			return false;
		IMethodName methodName = expr.getMethodName();
		return sst.getMethods().stream()
				.noneMatch(methodDecl -> methodDecl.getName().equals(methodName));
	}

	public static boolean isMethodCallToEntryPoint(IMethodName methodName, ISST sst) {
		for (IMethodDeclaration methodDecl : sst.getEntryPoints()) {
			if (methodDecl.getName().equals(methodName))
				return true;
		}
		return false;
	}

	public static List<IAssignment> getAssignmentList(List<IStatement> body) {
		return body.stream().filter(statement -> statement instanceof IAssignment)
				.map(statement -> (IAssignment) statement).collect(Collectors.toList());
	}

	public static int getIndexOfParameter(List<ISimpleExpression> parameters, ITypeName parameterType, TypeCollector typeCollector) {
		for (int i = 0; i < parameters.size(); i++) {
			ISimpleExpression expr = parameters.get(i);
			if (expr instanceof IReferenceExpression) {
				IReferenceExpression refExpr = (IReferenceExpression) expr;
				if (parameterType.equals(typeCollector.getType(refExpr.getReference())))
					return i;
			}
		}
		return -1;
	}

	public static int getParameterIndexInEntryPoint(IInvocationExpression expr, int parameterIndex, IMethodDeclaration entryPoint) {
		IReference reference = parameterIndex == -1 ? expr.getReference() : ((IReferenceExpression) expr
				.getParameters().get(parameterIndex)).getReference();
		if (!(reference instanceof IVariableReference))
			return -1;
		IVariableReference varRef = (IVariableReference) reference;
		return getParameterIndexInEntryPoint(varRef, entryPoint);
	}
	
	public static int getParameterIndexInEntryPoint(IVariableReference varRef, IMethodDeclaration entryPoint) {
		List<IParameterName> parameterNames = entryPoint.getName().getParameters();
		for (int i = 0; i < parameterNames.size(); i++) {
			IParameterName parameterName = parameterNames.get(i);
			if (parameterName.getName().equals(varRef.getIdentifier()))
				return i;
		}
		return -1;
	}

	public static IStatement getStatementParentForExpression(IAssignableExpression expr, SSTNodeHierarchy sstNodeHierarchy) {
		ISSTNode parent = sstNodeHierarchy.getParent(expr);
		while(!(parent instanceof IStatement)) {
			parent = sstNodeHierarchy.getParent(parent);
		}
		return (IStatement) parent;
	}

	public static boolean similarUsage(Usage usage, Usage otherUsage) {
		return Objects.equals(usage.getClassContext(),otherUsage.getClassContext()) &&
				Objects.equals(usage.getDefinitionSite(),otherUsage.getDefinitionSite()) &&
				Objects.equals(usage.getMethodContext(),otherUsage.getMethodContext()) &&
				Objects.equals(usage.getType(),otherUsage.getType());
	}

	public static Optional<Usage> usageListContainsSimilarUsage(List<Usage> usages, Usage otherUsage) {
		return usages.stream().filter(usage -> similarUsage(usage, otherUsage)).findFirst();
	}
	
}
