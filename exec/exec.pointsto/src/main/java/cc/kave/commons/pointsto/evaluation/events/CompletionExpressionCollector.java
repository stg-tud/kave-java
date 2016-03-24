/**
 * Copyright 2016 Simon Reuß
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
package cc.kave.commons.pointsto.evaluation.events;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.pointsto.analysis.visitors.TraversingVisitor;

public class CompletionExpressionCollector extends TraversingVisitor<List<ICompletionExpression>, Void> {

	public List<ICompletionExpression> collect(Context context) {
		return collect(context.getSST());
	}

	public List<ICompletionExpression> collect(ISST sst) {
		List<ICompletionExpression> completionExprs = new ArrayList<>();
		sst.accept(this, completionExprs);
		return completionExprs;
	}

	@Override
	public Void visit(ICompletionExpression entity, List<ICompletionExpression> context) {
		context.add(entity);
		return null;
	}
}
