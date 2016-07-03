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

import java.util.Set;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.ICompletionExpression;
import cc.kave.commons.model.typeshapes.ITypeShape;
import exec.recommender_reimplementation.tokenization.TokenizationContext;
import exec.recommender_reimplementation.tokenization.TokenizationSettings;
import exec.recommender_reimplementation.tokenization.TokenizationVisitor;

public class QueryExtractor {
	public HeinemannQuery extractQueryFromCompletion(CompletionEvent completion, int lookback, boolean removeStopwords,
			boolean removeKeywords) {
		Context context = completion.context;
		TokenizationSettings settings = new TokenizationSettings();
		settings.setActiveBrackets(false);
		if (!removeKeywords) {
			settings.setActiveKeywords(true);
			settings.setActiveWrapKeywords(true);
		}
		settings.setActiveOperators(false);
		settings.setActivePuncutuation(false);

		TokenizationContext tokenizationContext = new TokenizationContext(settings);
		QueryVisitor queryVisitor = new QueryVisitor(context.getTypeShape(), lookback, removeStopwords);
		context.getSST().accept(queryVisitor, tokenizationContext);

		return queryVisitor.getQuery();
	}

	public class QueryVisitor extends TokenizationVisitor {

		private int lookback;
		private boolean removeStopwords;

		private HeinemannQuery query;

		public QueryVisitor(ITypeShape typeShape, int lookback, boolean removeStopwords) {
			super(typeShape);
			this.lookback = lookback;
			this.removeStopwords = removeStopwords;
		}

		@Override
		public Object visit(ICompletionExpression expr, TokenizationContext c) {
			Set<String> identifiers = collectTokens(c.getTokenStream(), lookback, removeStopwords);
			filterSingleCharacterIdentifier(identifiers);
			identifiers = camelCaseSplitTokens(identifiers);
			identifiers = stemTokens(identifiers);

			query = new HeinemannQuery(identifiers, expr.getTypeReference());

			return null;
		}

		@Override
		public Object visit(IMethodDeclaration decl, TokenizationContext c) {
			// clears TokenStream on every methodDeclaration to only have identifiers in same method body
			c.getTokenStream().clear();
			return super.visit(decl, c);
		}

		public HeinemannQuery getQuery() {
			return query;
		}

	}
}
