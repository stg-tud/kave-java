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
import static exec.recommender_reimplementation.pbn.PBNAnalysisUtil.*;

import java.util.List;
import java.util.Optional;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.ISimpleExpression;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.ssts.expressions.assignable.ILambdaExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.PointsToQueryBuilder;
import cc.kave.commons.pointsto.analysis.types.TypeCollector;
import cc.kave.commons.utils.SSTNodeHierarchy;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

public class PBNAnalysisVisitor extends AbstractTraversingNodeVisitor<List<Usage>, Object> {

	private PointsToContext pointsToContext;
	private PointsToQueryBuilder queryBuilder;
	private SSTNodeHierarchy sstNodeHierarchy;
	private TypeCollector typeCollector;

	private IMethodDeclaration currentEntryPoint;
	
	private UsageContextHelper usageContextHelper;

	public PBNAnalysisVisitor(PointsToContext pointsToContext) {
		this.pointsToContext = pointsToContext;
		setSSTNodeHierarchy(new SSTNodeHierarchy(pointsToContext.getSST()));
		setTypeCollector(new TypeCollector(pointsToContext));
		queryBuilder = new PointsToQueryBuilder(getTypeCollector(), getSSTNodeHierarchy());
		
		usageContextHelper = new UsageContextHelper(typeCollector, pointsToContext, queryBuilder, sstNodeHierarchy);
	}

	@Override
	public Object visit(IMethodDeclaration decl, List<Usage> context) {
		setCurrentEntryPoint(decl);
		return super.visit(decl, context);
	}

	@Override
	public Object visit(IInvocationExpression expr, List<Usage> usages) {
		ITypeName type;
		if (isThisOrSuper(expr.getReference().getIdentifier())) {
			// Handle possible call to super type
			if (isMethodCallToEntryPoint(expr.getMethodName(), pointsToContext.getSST()))
				return super.visit(expr, usages);
			ITypeHierarchy typeHierarchy = pointsToContext.getTypeShape().getTypeHierarchy();
			if(typeHierarchy.hasSuperclass()) type = typeHierarchy.getExtends().getElement();
			else type = typeHierarchy.getElement();
		} else {
			type = findTypeForVarReference(expr, getTypeCollector());
		}
		
		// Handle Receiver first
		if(isSupportedType(type)) 
			handleObjectInstance(expr, usages, -1, convert(type));

		// Handle Parameters
		List<ISimpleExpression> parameters = expr.getParameters();
		List<ITypeName> parameterTypes = createTypeListFromParameters(parameters, getTypeCollector());
		for (int i = 0; i < parameterTypes.size(); i++) {
			ITypeName parameterType = parameterTypes.get(i);
			int parameterIndex = getIndexOfParameter(parameters, parameterType, getTypeCollector());
			if (isSupportedType(parameterType)) {
				handleObjectInstance(expr, usages, parameterIndex, convert(parameterType));
			}
		}

		return super.visit(expr, usages);
	}

	public boolean isSupportedType(ITypeName type) {
		// ignore Unknown Types, Array, Delegate and Enum Types
		return type != null && !type.isUnknownType() && !type.isArrayType() && !type.isEnumType() && !type.isDelegateType();
	}
	
	@Override
	public Object visit(ILambdaExpression expr, List<Usage> context) {
		// ignore lambda expressions
		return null;
	}

	@Override
	public Object visit(ITryBlock block, List<Usage> context) {
		visit(block.getBody(), context);
		// ignores Catch Block because exception-handling is ignored in analysis
		visit(block.getFinally(), context);
		return null;
	}

	public void handleObjectInstance(IInvocationExpression expr, List<Usage> usages, int parameterIndex, ICoReTypeName parameterType) {

		Query newUsage = usageContextHelper.createNewObjectUsage(expr, parameterType);
		usageContextHelper.addClassContext(newUsage);
		usageContextHelper.addMethodContext(newUsage, getCurrentEntryPoint());
		usageContextHelper.addDefinitionSite(newUsage, expr, parameterIndex, getCurrentEntryPoint());
		usageContextHelper.addCallSite(newUsage, expr, parameterIndex);
				
		Optional<Usage> similarUsage = usageListContainsSimilarUsage(usages, newUsage);
		if (similarUsage.isPresent()) {
			usageContextHelper.addCallSite((Query) similarUsage.get(), expr, parameterIndex);
		}
		else {
			usages.add(newUsage);
		}
	}

	public IMethodDeclaration getCurrentEntryPoint() {
		return currentEntryPoint;
	}

	public void setCurrentEntryPoint(IMethodDeclaration currentEntryPoint) {
		this.currentEntryPoint = currentEntryPoint;
	}

	public SSTNodeHierarchy getSSTNodeHierarchy() {
		return sstNodeHierarchy;
	}

	public void setSSTNodeHierarchy(SSTNodeHierarchy sstNodeHierarchy) {
		this.sstNodeHierarchy = sstNodeHierarchy;
	}

	public TypeCollector getTypeCollector() {
		return typeCollector;
	}

	public void setTypeCollector(TypeCollector typeCollector) {
		this.typeCollector = typeCollector;
	}

}
