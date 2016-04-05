/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package exec.validate_evaluation.queryhistory;

import java.util.List;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.ssts.impl.visitor.AbstractTraversingNodeVisitor;
import cc.kave.commons.pointsto.analysis.PointsToContext;
import cc.kave.commons.pointsto.analysis.TypeBasedAnalysis;
import cc.kave.commons.pointsto.extraction.PointsToUsageExtractor;
import cc.recommenders.usages.Usage;

public class UsageExtractor implements IUsageExtractor {

	@Override
	public IAnalysisResult analyse(Context ctx) {
		return new AnalysisResult(ctx);
	}

	private class AnalysisResult extends AbstractTraversingNodeVisitor<Void, Void> implements IAnalysisResult {

		private PointsToContext p2ctx;
		private ICompletionExpression firstCompletionExpr;

		public AnalysisResult(Context ctx) {
			findCompletion(ctx);
			createCompletionContext(ctx);
		}

		private void findCompletion(Context ctx) {
			ctx.getSST().accept(this, null);
		}

		@Override
		public Void visit(ICompletionExpression expr, Void context) {
			if (firstCompletionExpr == null) {
				firstCompletionExpr = expr;
			}
			return null;
		}

		private void createCompletionContext(Context ctx) {
			TypeBasedAnalysis analysis = new TypeBasedAnalysis();
			p2ctx = analysis.compute(ctx);
		}

		@Override
		public List<Usage> getUsages() {
			PointsToUsageExtractor p2ue = new PointsToUsageExtractor();
			return p2ue.extract(p2ctx);
		}

		@Override
		public Usage getFirstQuery() {
			PointsToUsageExtractor p2ue = new PointsToUsageExtractor();
			List<Usage> queries = null;
			// TODO: was before... p2ue.extractQueries(firstCompletionExpr, p2ctx);
			if (queries.isEmpty()) {
				return null;
			}
			return queries.iterator().next();
		}
	}
}