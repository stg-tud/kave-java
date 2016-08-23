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
import static com.google.common.base.MoreObjects.firstNonNull;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.PointsToQueryBuilder;
import cc.kave.commons.pointsto.analysis.types.TypeCollector;
import cc.kave.commons.utils.SSTNodeHierarchy;
import cc.recommenders.usages.Query;

public class QueryExtraction {
	
	public Query extractQueryFromCompletion(CompletionEvent completion) {
		Context context = completion.context;
		TypeCollector typeCollector = new TypeCollector(context);
		PointsToContext pointsToContext = UsageExtractor.performPointsToAnalysis(context);
		SSTNodeHierarchy sstNodeHierarchy = new SSTNodeHierarchy(context.getSST());
		PointsToQueryBuilder pointsToQueryBuilder = new PointsToQueryBuilder(typeCollector, sstNodeHierarchy);
		
		UsageContextHelper usageContextHelper = new UsageContextHelper(typeCollector, pointsToContext, pointsToQueryBuilder, sstNodeHierarchy);
		
		QueryExtractionVisitor queryExtractionVisitor = new QueryExtractionVisitor(typeCollector, usageContextHelper);
		pointsToContext.getSST().accept(queryExtractionVisitor, null);
		
		return queryExtractionVisitor.getQuery();
	}
	
	public class QueryExtractionVisitor extends AbstractTraversingNodeVisitor<Object,Object> {
		private UsageContextHelper usageContextHelper;

		private IMethodDeclaration currentEntryPoint;
		private Query query;

		private TypeCollector typeCollector;
		
		public QueryExtractionVisitor(TypeCollector typeCollector, UsageContextHelper usageContextHelper) {
			this.typeCollector = typeCollector;
			this.usageContextHelper = usageContextHelper;
		}
		
		@Override
		public Object visit(IMethodDeclaration stmt, Object context) {
			currentEntryPoint = stmt;
			return super.visit(stmt, context);
		}
		
		@Override
		public Object visit(ICompletionExpression entity, Object context) {
			IVariableReference receiverRef = firstNonNull(entity.getVariableReference(), SSTUtil.variableReference("this")); 
			ITypeName typeName = firstNonNull(entity.getTypeReference(), typeCollector.getType(receiverRef));
			
			query = usageContextHelper.createNewObjectUsage(convert(typeName));
			usageContextHelper.addClassContext(query);
			usageContextHelper.addDefinitionSite(query, receiverRef, entity , currentEntryPoint);
			usageContextHelper.addMethodContext(query, currentEntryPoint);
			
			return super.visit(entity, context);
		}

		public Query getQuery() {
			return query;
		}

		public void setQuery(Query query) {
			this.query = query;
		}
	}
}
