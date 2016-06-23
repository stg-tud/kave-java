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
import static exec.recommender_reimplementation.heinemann_analysis.ExtractionUtil.createNewEntry;
import static exec.recommender_reimplementation.heinemann_analysis.ExtractionUtil.filterSingleCharacterIdentifier;
import static exec.recommender_reimplementation.heinemann_analysis.ExtractionUtil.stemTokens;

import java.util.HashSet;
import java.util.Set;

import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.typeshapes.ITypeShape;
import exec.recommender_reimplementation.tokenization.TokenizationContext;
import exec.recommender_reimplementation.tokenization.TokenizationVisitor;

public class HeinemannTokenizationVisitor extends TokenizationVisitor {

	private Set<Entry> index;

	int lookback;

	boolean removeStopwords;

	public HeinemannTokenizationVisitor(ITypeShape typeShape, int lookback, boolean removeStopwords) {
		super(typeShape);
		index = new HashSet<>();
		this.lookback = lookback;
		this.removeStopwords = removeStopwords;
	}

	@Override
	public Object visit(IInvocationExpression expr, TokenizationContext c) {
		Set<String> identifiers = collectTokens(c.getTokenStream(), lookback, removeStopwords);
		if(!identifiers.isEmpty()) {
			filterSingleCharacterIdentifier(identifiers);
			identifiers = camelCaseSplitTokens(identifiers);
			identifiers = stemTokens(identifiers);
			index.add(createNewEntry(expr.getMethodName(), identifiers));
		}

		// call super method to tokenize invocation after extraction of identifiers
		return super.visit(expr, c);
	}

	@Override
	public Object visit(IMethodDeclaration decl, TokenizationContext c) {
		// clears TokenStream on every methodDeclaration to only have identifiers in same method body 
		// and ignores method header
		c.getTokenStream().clear();

		visitStatements(decl.getBody(), c);
		return null;	}
	
	public Set<Entry> getIndex() {
		return index;
	}

}
