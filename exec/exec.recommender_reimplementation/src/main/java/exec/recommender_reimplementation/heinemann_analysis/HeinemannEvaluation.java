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
package exec.recommender_reimplementation.heinemann_analysis;

import static exec.recommender_reimplementation.heinemann_analysis.ExtractionUtil.camelCaseSplitTokens;
import static exec.recommender_reimplementation.heinemann_analysis.ExtractionUtil.collectTokens;
import static exec.recommender_reimplementation.heinemann_analysis.ExtractionUtil.filterSingleCharacterIdentifier;
import static exec.recommender_reimplementation.heinemann_analysis.ExtractionUtil.stemTokens;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.pointsto.extraction.CoReNameConverter;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;
import exec.recommender_reimplementation.tokenization.TokenizationContext;
import exec.recommender_reimplementation.tokenization.TokenizationSettings;

public class HeinemannEvaluation {
	private HeinemannRecommender recommender;

	private double reciprocalRank;

	private int invocationCount;

	private int lookback;

	private boolean removeStopwords;

	private boolean removeKeywords;

	public HeinemannEvaluation(HeinemannRecommender recommender, int lookback, boolean removeStopwords,
			boolean removeKeywords) {
		this.recommender = recommender;
		this.lookback = lookback;
		this.removeStopwords = removeStopwords;
		this.removeKeywords = removeKeywords;
	}

	public double evaluateSet(List<Context> contextList) {
		TokenizationSettings settings = new TokenizationSettings();
		settings.setActiveBrackets(false);
		if (!removeKeywords) {
			settings.setActiveKeywords(true);
			settings.setActiveWrapKeywords(true);
		}
		settings.setActiveOperators(false);
		settings.setActivePuncutuation(false);

		for (Context context : contextList) {
			context.getSST().accept(new EvaluationVisitor(context.getTypeShape(), lookback, removeStopwords),
					new TokenizationContext(settings));
		}

		return reciprocalRank / invocationCount;
	}

	public class EvaluationVisitor extends HeinemannTokenizationVisitor {

		public EvaluationVisitor(ITypeShape typeShape, int lookback, boolean removeStopwords) {
			super(typeShape, lookback, removeStopwords);
		}

		@Override
		public Object visit(IInvocationExpression expr, TokenizationContext c) {
			IMethodName expectedMethodName = expr.getMethodName();
			if(expectedMethodName.equals(MethodName.UNKNOWN_NAME)) return super.visit(expr, c);
			Set<String> identifiers = collectTokens(c.getTokenStream(), lookback, removeStopwords);
			filterSingleCharacterIdentifier(identifiers);
			identifiers = camelCaseSplitTokens(identifiers);
			identifiers = stemTokens(identifiers);
			ICoReMethodName expectedCoReMethodName = CoReNameConverter.convert(expectedMethodName);
			
			HeinemannQuery query = new HeinemannQuery(identifiers, expr.getMethodName().getDeclaringType());
			
			Set<Tuple<ICoReMethodName, Double>> proposals = recommender.query(query);
			
			calculateReciprocalRank(expectedCoReMethodName, proposals);
			
			invocationCount++;
			
			// call super method to tokenize invocation after extraction of identifiers
			return super.visit(expr, c);
		}

		public void calculateReciprocalRank(ICoReMethodName coReMethodName,
				Set<Tuple<ICoReMethodName, Double>> proposals) {
			Iterator<Tuple<ICoReMethodName, Double>> iterator = proposals.iterator();
			int i = 1;
			while (iterator.hasNext()) {
				Tuple<ICoReMethodName, Double> proposal = iterator.next();
				if(coReMethodName.equals(proposal.getFirst())) {
					reciprocalRank += 1 / (double) i;
					break;
				}
				i++;
			}
		}

	}
}
