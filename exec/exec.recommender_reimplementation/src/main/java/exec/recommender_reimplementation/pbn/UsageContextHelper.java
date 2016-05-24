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

import static cc.kave.commons.pointsto.extraction.CoReNameConverter.convert;
import static exec.recommender_reimplementation.pbn.PBNAnalysisUtil.findTypeForVarReference;
import static exec.recommender_reimplementation.pbn.PBNAnalysisUtil.getAssignmentList;
import static exec.recommender_reimplementation.pbn.PBNAnalysisUtil.getParameterIndexInEntryPoint;
import static exec.recommender_reimplementation.pbn.PBNAnalysisUtil.getStatementParentForInvocation;
import static exec.recommender_reimplementation.pbn.PBNAnalysisUtil.isCallToSuperClass;

import java.util.List;
import java.util.Set;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.simple.IReferenceExpression;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.statements.IAssignment;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.PointsToAnalysis;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.PointsToQuery;
import cc.kave.commons.pointsto.analysis.PointsToQueryBuilder;
import cc.kave.commons.pointsto.analysis.types.TypeCollector;
import cc.kave.commons.utils.SSTNodeHierarchy;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;

public class UsageContextHelper {

	private TypeCollector typeCollector;
	private ITypeShape typeShape;
	private ISST sst;
	private PointsToQueryBuilder pointsToQueryBuilder;
	private SSTNodeHierarchy sstNodeHierarchy;
	private PointsToAnalysis pointerAnalysis;

	public UsageContextHelper(TypeCollector typeCollector, PointsToContext pointsToContext,
			PointsToQueryBuilder pointsToQueryBuilder,
			SSTNodeHierarchy sstNodeHierarchy) {
				this.typeCollector = typeCollector;
				this.typeShape = pointsToContext.getTypeShape();
				this.sst = pointsToContext.getSST();
				this.pointsToQueryBuilder = pointsToQueryBuilder;
				this.sstNodeHierarchy = sstNodeHierarchy;
				this.pointerAnalysis = pointsToContext.getPointerAnalysis();
	}
	
	public Query createNewObjectUsage(IInvocationExpression expr,
			ICoReTypeName parameterType) {
		Query result = new Query();
		ICoReTypeName type = parameterType == null ? convert(findTypeForVarReference(
				expr, typeCollector)) : parameterType;
		result.setType(type);

		return result;
	}

	public void addMethodContext(Query newUsage, IMethodDeclaration entryPoint) {
		IMethodName firstMethodName = PBNAnalysisUtil.findFirstMethodName(
				entryPoint.getName(), typeShape);
		newUsage.setMethodContext(convert(firstMethodName));
	}

	public void addCallSite(Query usage, IInvocationExpression expr,
			int argIndex) {
		if (argIndex > -1) {
			CallSite callSite = CallSites.createParameterCallSite(
					convert(expr.getMethodName()), argIndex);
			usage.addCallSite(callSite);
		} else {
			IMethodName firstMethodName = PBNAnalysisUtil.findFirstMethodName(
					expr.getMethodName(), typeShape);
			CallSite callSite = CallSites
					.createReceiverCallSite(convert(firstMethodName));
			usage.addCallSite(callSite);
		}
	}

	public void addClassContext(Query newUsage) {
		ITypeName classType = findClassContext();
		newUsage.setClassContext(convert(classType));
	}

	public boolean addDefinitionSite(Query newUsage,
			IInvocationExpression expr, int parameterIndex,
			IMethodDeclaration currentEntryPoint) {
		if (isCallToSuperClass(expr, sst)) {
			newUsage.setDefinition(DefinitionSites.createDefinitionByThis());
			return true;
		}
		int parameterOfEntryPointIndex = getParameterIndexInEntryPoint(expr,
				parameterIndex, currentEntryPoint);
		if (parameterOfEntryPointIndex > -1) {
			newUsage.setDefinition(DefinitionSites.createDefinitionByParam(
					convert(currentEntryPoint.getName()),
					parameterOfEntryPointIndex));
			return true;
		}
		DefinitionSite definitionSite = findDefinitionSiteByReference(expr, parameterIndex,currentEntryPoint);
		if (definitionSite != null) {
			newUsage.setDefinition(definitionSite);
			return true;
		}
		return false;
	}

	public ITypeName findClassContext() {
		ITypeHierarchy typeHierarchy = typeShape.getTypeHierarchy();
		// TODO: only use super class or also add interfaces as class context?
		ITypeName classType = typeHierarchy.hasSuperclass() ? typeHierarchy
				.getExtends().getElement() : typeHierarchy.getElement();
		return classType;
	}

	public DefinitionSite findDefinitionSiteByReference(
			IInvocationExpression expr, int parameterIndex,
			IMethodDeclaration methodDecl) {
		IReference reference = parameterIndex == -1 ? expr.getReference()
				: ((IReferenceExpression) expr.getParameters().get(
						parameterIndex)).getReference();

		if (reference instanceof IFieldReference) {
			IFieldReference fieldRef = (IFieldReference) reference;
			if (fieldRef.getReference().getIdentifier().equals("this")) {
				return DefinitionSites.createDefinitionByField(convert(fieldRef
						.getFieldName()));
			}
		}

		List<IStatement> body = methodDecl.getBody();
		PointsToQuery queryForVarReference = pointsToQueryBuilder.newQuery(
				reference,
				getStatementParentForInvocation(expr, sstNodeHierarchy));
		Set<AbstractLocation> varRefLocations = pointerAnalysis
				.query(queryForVarReference);

		List<IAssignment> assignments = getAssignmentList(body);

		for (IAssignment assignment : assignments) {
			// TODO: maybe traverse backwards to use last assignment for reference
			PointsToQuery queryForLeftSide = pointsToQueryBuilder.newQuery(
					assignment.getReference(), assignment);
			Set<AbstractLocation> leftSideLocations = pointerAnalysis
					.query(queryForLeftSide);
			if (varRefLocations.equals(leftSideLocations)) {
				// found assignment with Variable Reference as right side
				IAssignableExpression assignExpr = assignment.getExpression();
				DefinitionSite fieldSite = tryGetFieldDefinitionSite(assignExpr);
				if (fieldSite != null)
					return fieldSite;

				DefinitionSite invocationSite = tryGetInvocationDefinition(assignExpr);
				if (invocationSite != null)
					return invocationSite;

				break;
			}
		}
		return null;
	}

	public static DefinitionSite tryGetInvocationDefinition(
			IAssignableExpression assignExpr) {
		if (assignExpr instanceof IInvocationExpression) {
			IInvocationExpression invocation = (IInvocationExpression) assignExpr;
			IMethodName methodName = invocation.getMethodName();
			if (methodName.isConstructor()) {
				return DefinitionSites
						.createDefinitionByConstructor(convert(methodName));
			}
			return DefinitionSites
					.createDefinitionByReturn(convert(methodName));
		}
		return null;
	}

	public static DefinitionSite tryGetFieldDefinitionSite(IAssignableExpression assignExpr) {
		if (assignExpr instanceof IReferenceExpression) {
			IReferenceExpression refExpr = (IReferenceExpression) assignExpr;
			IReference reference = refExpr.getReference();
			if (reference instanceof IFieldReference) {
				IFieldReference fieldRef = (IFieldReference) reference;
				if (fieldRef.getReference().getIdentifier().equals("this")) {
					return DefinitionSites
							.createDefinitionByField(convert(fieldRef
									.getFieldName()));
				}
			}
		}
		return null;
	}

}
