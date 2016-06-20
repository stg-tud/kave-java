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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IParameterName;

import com.google.common.collect.Sets;

import opennlp.tools.stemmer.snowball.SnowballStemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer.ALGORITHM;

public class ExtractionUtil {
	
	static Set<String> stopWords = Sets.newHashSet("a", "able", "about", "across", "after", "all", "almost", "also", "am",
			"among", "an", "and", "any", "are", "as", "at", "be", "because", "been", "but", "by", "can", "cannot",
			"could", "dear", "did", "does", "either", "ever", "every", "from", "get", "got", "had", "has", "have",
			"he", "her", "hers", "him", "his", "how", "however", "i", "in", "into", "is", "it", "its", "just", "least",
			"let", "like", "likely", "may", "me", "might", "most", "must", "my", "neither", "no", "nor", "not", "of",
			"off", "often", "on", "only", "or", "other", "our", "own", "rather", "said", "say", "says", "she",
			"should", "since", "so", "some", "than", "that", "the", "their", "them", "then", "there", "these", "they", "this",
			"tis", "to", "too", "twas", "us", "wants", "was", "we", "were", "what", "when", "where", "which", "who",
			"whom", "why", "will", "with", "would", "yet", "you", "your");
	
	// source of regex is http://stackoverflow.com/a/7594052/6471124
	private static final String camelCaseSplitRegex = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";

	public static List<String> camelCaseSplitTokens(List<String> tokens) {
		List<String> result = new ArrayList<>();
		for (String token : tokens) {
			String[] splitTokens = token.split(camelCaseSplitRegex);
			for (String splitToken : splitTokens) {
				// converts all tokens to lowercase
				result.add(splitToken.toLowerCase());
			}
		}
		
		return result;
	}
	
	public static List<String> stemTokens(List<String> tokens)  {
		List<String> result = new ArrayList<>();
		SnowballStemmer stemmer = new SnowballStemmer(ALGORITHM.ENGLISH);
		for (String token : tokens) {
			String stemmedToken = (String) stemmer.stem(token);
			if(!result.contains(stemmedToken)) result.add(stemmedToken);
		}		
	
		return result;
	}
	
	public static Entry createNewEntry(IMethodName methodName, List<String> identifiers) {
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
		
		return new Entry(identifiers, sb.toString());
	}

	public static void filterSingleCharacterIdentifier(List<String> identifiers) {
		List<String> copy = new ArrayList<>(identifiers);
		for (String identifier : copy) {
			if (identifier.length() < 2)
				identifiers.remove(identifier);
		}
	}

	public static List<String> collectTokens(List<String> tokenStream, int lookback, boolean removeStopwords) {
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

	public static boolean isStopWord(String token) {
		if(stopWords.contains(token)) return true;
		return false;
	}
}
