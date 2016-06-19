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
import static exec.recommender_reimplementation.heinemann_analysis.ExtractionUtil.stemTokens;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.kave.commons.model.typeshapes.ITypeShape;

import com.google.common.collect.Sets;

import exec.recommender_reimplementation.tokenization.TokenizationContext;
import exec.recommender_reimplementation.tokenization.TokenizationVisitor;

public class HeinemannTokenizationVisitor extends TokenizationVisitor {

	private List<Entry> index;

	int lookback;

	boolean removeStopwords;

	Set<String> stopWords = Sets.newHashSet("a", "able", "about", "across", "after", "all", "almost", "also", "am",
			"among", "an", "and", "any", "are", "as", "at", "be", "because", "been", "but", "by", "can", "cannot",
			"could", "dear", "did", "does", "either", "ever", "every", "from", "get", "got", "had", "has", "have",
			"he", "her", "hers", "him", "his", "how", "however", "i", "in", "into", "is", "it", "its", "just", "least",
			"let", "like", "likely", "may", "me", "might", "most", "must", "my", "neither", "no", "nor", "not", "of",
			"off", "often", "on", "only", "or", "other", "our", "own", "rather", "said", "say", "says", "she",
			"should", "since", "so", "some", "than", "that", "the", "their", "them", "then", "there", "these", "they", "this",
			"tis", "to", "too", "twas", "us", "wants", "was", "we", "were", "what", "when", "where", "which", "who",
			"whom", "why", "will", "with", "would", "yet", "you", "your");

	public HeinemannTokenizationVisitor(ITypeShape typeShape, int lookback, boolean removeStopwords) {
		super(typeShape);
		index = new ArrayList<>();
		this.lookback = lookback;
		this.removeStopwords = removeStopwords;
	}


	@Override
	public Object visit(IInvocationExpression expr, TokenizationContext c) {
		List<String> identifiers = collectTokens(c.getTokenStream(), lookback);
		if(!identifiers.isEmpty()) {
			filterSingleCharacterIdentifier(identifiers);
			identifiers = camelCaseSplitTokens(identifiers);
			identifiers = stemTokens(identifiers);
			createAndStoreNewEntry(expr.getMethodName(), identifiers);
		}

		// call super method to tokenize invocation after extraction of identifiers
		return super.visit(expr, c);
	}

	@Override
	public Object visit(IMethodDeclaration decl, TokenizationContext c) {
		// clears TokenStream on every methodDeclaration to only have identifiers in same method body
		c.getTokenStream().clear();
		return super.visit(decl, c);
	}
	
	public void createAndStoreNewEntry(IMethodName methodName, List<String> identifiers) {
		StringBuilder sb = new StringBuilder();
		sb.append(methodName.getDeclaringType().getFullName());
		sb.append("#");
		sb.append(methodName.getName());
		sb.append("(");
		for (Iterator<IParameterName> iterator = methodName.getParameters().iterator(); iterator.hasNext();) {
			IParameterName parameterName  = iterator.next();
			sb.append(parameterName.getValueType().getName());
			
			if(iterator.hasNext()) sb.append(",");
		}
		sb.append(")");
		
		index.add(new Entry(identifiers, sb.toString()));
	}

	public void filterSingleCharacterIdentifier(List<String> identifiers) {
		List<String> copy = new ArrayList<>(identifiers);
		for (String identifier : copy) {
			if (identifier.length() < 2)
				identifiers.remove(identifier);
		}
	}

	public List<String> collectTokens(List<String> tokenStream, int lookback) {
		if(tokenStream.size() < lookback) return new ArrayList<>();
		List<String> identifiers = new ArrayList<>();
		for (int i = 0; i < lookback; i++) {
			String token = tokenStream.get(tokenStream.size() - 1 - i);
			if (removeStopwords && isStopWord(token))
				continue;
			
			// ignores duplicates
			if (!identifiers.contains(token))
				identifiers.add(0, token);
		}
		return identifiers;
	}

	public boolean isStopWord(String token) {
		if(stopWords.contains(token)) return true;
		return false;
	}

	public List<Entry> getIndex() {
		return index;
	}

}
