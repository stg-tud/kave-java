/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.extraction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.visitor.ISSTNode;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.pointsto.analysis.AbstractLocation;
import cc.kave.commons.pointsto.analysis.Callpath;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.PointsToQuery;
import cc.kave.commons.pointsto.analysis.exceptions.UnexpectedSSTNodeException;
import cc.kave.commons.pointsto.statistics.NopUsageStatisticsCollector;
import cc.kave.commons.pointsto.statistics.UsageStatisticsCollector;
import cc.recommenders.exceptions.AssertionException;
import cc.recommenders.names.IMethodName;
import cc.recommenders.names.ITypeName;
import cc.recommenders.usages.DefinitionSiteKind;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

public class PointsToUsageExtractor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PointsToUsageExtractor.class);

	private final DescentStrategy descentStrategy;

	private UsageStatisticsCollector collector;

	public PointsToUsageExtractor() {
		this(new SimpleDescentStrategy(), new NopUsageStatisticsCollector());
	}

	public PointsToUsageExtractor(UsageStatisticsCollector collector) {
		this(new SimpleDescentStrategy(), collector);
	}

	public PointsToUsageExtractor(DescentStrategy descentStrategy, UsageStatisticsCollector collector) {
		this.descentStrategy = descentStrategy;
		this.collector = collector;
	}

	public void setStatisticsCollector(UsageStatisticsCollector collector) {
		this.collector = collector;
	}

	public UsageStatisticsCollector getStatisticsCollector() {
		return collector;
	}

	public List<Usage> extract(PointsToContext context) {
		collector.onProcessContext(context);

		List<Usage> contextUsages = new ArrayList<>();
		UsageExtractionVisitor visitor = new UsageExtractionVisitor();
		UsageExtractionVisitorContext visitorContext = new UsageExtractionVisitorContext(context, descentStrategy);
		String className = context.getSST().getEnclosingType().getName();
		for (IMethodDeclaration methodDecl : context.getSST().getEntryPoints()) {
			try {
				visitor.visitEntryPoint(methodDecl, visitorContext);
			} catch (AssertionException ex) {
				throw ex;
			} catch (RuntimeException ex) {
				LOGGER.error("Failed to extract usages from " + className + ":" + methodDecl.getName().getName(), ex);
				continue;
			}

			List<Query> rawUsages = visitorContext.getUsages();
			List<? extends Usage> processedUsages = processUsages(rawUsages, context.getTypeShape());
			contextUsages.addAll(processedUsages);

			LOGGER.info("Extracted {} usages from {}:{}", processedUsages.size(), className,
					methodDecl.getName().getName());
			collector.onEntryPointUsagesExtracted(methodDecl, processedUsages);
		}

		return contextUsages;
	}

	public List<Usage> extractQueries(ICompletionExpression completionExpression, PointsToContext context) {
		if (completionExpression.getTypeReference() == null && completionExpression.getVariableReference() == null) {
			LOGGER.error("Cannot extract queries if neither type nor variable information is available");
			return Collections.emptyList();
		}

		// find enclosing method and statement
		Map<Class<? extends ISSTNode>, ISSTNode> completionContext = new HashMap<>();
		completionContext.put(ICompletionExpression.class, completionExpression);
		context.getSST().accept(new CompletionExpressionContextVisitor(), completionContext);
		IMethodDeclaration enclosingMethod = (IMethodDeclaration) completionContext.get(IMethodDeclaration.class);
		if (enclosingMethod == null) {
			LOGGER.error("Failed to find enclosing method of completion expression");
			return Collections.emptyList();
		}
		IStatement enclosingStatement = (IStatement) completionContext.get(IStatement.class);

		UsageExtractionVisitor visitor = new UsageExtractionVisitor();
		UsageExtractionVisitorContext visitorContext = new UsageExtractionVisitorContext(context, descentStrategy);

		try {
			// treat the enclosing method as entry point although it might not be one
			visitor.visitEntryPoint(enclosingMethod, visitorContext);

			PointsToQuery ptQuery = new PointsToQuery(completionExpression.getVariableReference(), enclosingStatement,
					completionExpression.getTypeReference(), new Callpath(enclosingMethod.getName()));
			Set<AbstractLocation> locations = context.getPointerAnalysis().query(ptQuery);
			List<Query> usages = new ArrayList<>();
			for (AbstractLocation location : locations) {
				Query usage = visitorContext.getUsage(location);
				if (usage != null) {
					usages.add(usage);
				}
			}

			// just rewrite, do not prune
			rewriteUsages(usages, context.getTypeShape());

			return new ArrayList<Usage>(usages);
		} catch (AssertionException | UnexpectedSSTNodeException ex) {
			throw ex;
		} catch (RuntimeException ex) {
			LOGGER.error("Failed to extract queries", ex);
			return Collections.emptyList();
		}

	}

	private List<Query> processUsages(List<Query> rawUsages, ITypeShape typeShape) {
		List<Query> processedUsages = pruneUsages(rawUsages);
		rewriteUsages(processedUsages, typeShape);
		return processedUsages;
	}

	private List<Query> pruneUsages(List<Query> usages) {
		List<Query> retainedUsages = new ArrayList<>(usages.size());

		for (Query usage : usages) {
			// prune usages that have no call sites or have an unknown type
			ITypeName usageType = usage.getType();
			if (!usage.getAllCallsites().isEmpty() && !CoReNameConverter.isUnknown(usageType)) {
				retainedUsages.add(usage);
			}
		}

		return retainedUsages;
	}

	private void rewriteUsages(List<Query> usages, ITypeShape typeShape) {
		rewriteThisType(usages, typeShape);
		rewriteContexts(usages, typeShape);
	}

	private void rewriteThisType(List<Query> usages, ITypeShape typeShape) {
		// TODO what about the methods of the call sites?

		ITypeHierarchy typeHierarchy = typeShape.getTypeHierarchy();
		if (typeHierarchy.hasSuperclass()) {
			ITypeName superType = CoReNameConverter.convert(typeHierarchy.getExtends().getElement());

			for (Query usage : usages) {
				// change type of usages referring to the enclosing class to the super class
				if (usage.getDefinitionSite().getKind() == DefinitionSiteKind.THIS) {
					// TODO maybe add check whether this is safe (call sites do not refer to 'this')
					usage.setType(superType);
				}
			}

		}
	}

	private void rewriteContexts(List<Query> usages, ITypeShape typeShape) {
		for (Query usage : usages) {
			usage.setClassContext(getClassContext(usage.getClassContext(), typeShape.getTypeHierarchy()));
			usage.setMethodContext(getMethodContext(usage.getMethodContext(), typeShape.getMethodHierarchies()));
		}
	}

	private ITypeName getClassContext(ITypeName currentContext, ITypeHierarchy typeHierarchy) {
		boolean wasLambdaContext = CoReNameConverter.isLambdaName(currentContext);

		if (typeHierarchy.hasSuperclass()) {
			ITypeName superType = CoReNameConverter.convert(typeHierarchy.getExtends().getElement());

			if (wasLambdaContext && !CoReNameConverter.isLambdaName(superType)) {
				return CoReNameConverter.addLambda(superType);
			} else {
				return superType;
			}
		} else {
			return currentContext;
		}
	}

	private IMethodName getMethodContext(IMethodName currentContext, Collection<IMethodHierarchy> hierarchies) {
		boolean wasLambdaContext = CoReNameConverter.isLambdaName(currentContext);
		IMethodName restoredMethod = currentContext;
		if (wasLambdaContext) {
			// remove lambda qualifiers in order to find the fitting method hierarchy
			restoredMethod = CoReNameConverter.removeLambda(currentContext);
		}

		for (IMethodHierarchy methodHierarchy : hierarchies) {
			IMethodName method = CoReNameConverter.convert(methodHierarchy.getElement());

			if (restoredMethod.equals(method)) {
				IMethodName firstMethod = CoReNameConverter.convert(methodHierarchy.getFirst());
				IMethodName superMethod = CoReNameConverter.convert(methodHierarchy.getSuper());

				IMethodName newMethodContext = currentContext;
				if (firstMethod != null) {
					newMethodContext = firstMethod;
				} else if (superMethod != null) {
					newMethodContext = superMethod;
				} else {
					return currentContext;
				}

				if (wasLambdaContext) {
					newMethodContext = CoReNameConverter.addLambda(newMethodContext);
				}

				return newMethodContext;
			}
		}

		return currentContext;
	}
}
