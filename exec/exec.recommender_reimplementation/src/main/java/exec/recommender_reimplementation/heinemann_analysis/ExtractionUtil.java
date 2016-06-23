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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import opennlp.tools.stemmer.snowball.SnowballStemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer.ALGORITHM;
import cc.kave.commons.model.names.IMethodName;

import com.google.common.collect.Sets;

public class ExtractionUtil {
	
	// source for stopwords: http://www.textfixer.com/resources/common-english-words.txt
	// removed following java keywords from this list: do, else,for,if,this,while
	static Set<String> stopWords = Sets.newHashSet("a", "able", "about", "across", "after", "all", "almost", "also", "am",
			"among", "an", "and", "any", "are", "as", "at", "be", "because", "been", "but", "by", "can", "cannot",
			"could", "dear", "did", "does", "either", "ever", "every", "from", "get", "got", "had", "has", "have",
			"he", "her", "hers", "him", "his", "how", "however", "i", "in", "into", "is", "it", "its", "just", "least",
			"let", "like", "likely", "may", "me", "might", "most", "must", "my", "neither", "no", "nor", "not", "of",
			"off", "often", "on", "only", "or", "other", "our", "own", "rather", "said", "say", "says", "she",
			"should", "since", "so", "some", "than", "that", "the", "their", "them", "then", "there", "these", "they",
			"tis", "to", "too", "twas", "us", "wants", "was", "we", "were", "what", "when", "where", "which", "who",
			"whom", "why", "will", "with", "would", "yet", "you", "your");
	
	static final String EMPTY_TOKEN = "<empty>";
	
	// source for regex is http://stackoverflow.com/a/7594052/6471124
	private static final String camelCaseSplitRegex = "(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])";

	public static Set<String> camelCaseSplitTokens(Set<String> tokens) {
		Set<String> result = new HashSet<>();
		for (String token : tokens) {
			String[] splitTokens = camelCaseSplit(token);
			for (String splitToken : splitTokens) {
				// converts all tokens to lowercase
				result.add(splitToken.toLowerCase());
			}
		}
		
		return result;
	}

	public static String[] camelCaseSplit(String token) {
		return token.split(camelCaseSplitRegex);
	}
	
	public static Set<String> stemTokens(Set<String> tokens)  {
		Set<String> result = new HashSet<>();
		SnowballStemmer stemmer = new SnowballStemmer(ALGORITHM.ENGLISH);
		for (String token : tokens) {
			String stemmedToken = (String) stemmer.stem(token);
			result.add(stemmedToken);
		}		
	
		return result;
	}
	
	public static Entry createNewEntry(IMethodName methodName, Set<String> lookback) {
		return new Entry(lookback, methodName);
	}

	public static void filterSingleCharacterIdentifier(Set<String> identifiers) {
		Set<String> copy = new HashSet<>(identifiers);
		for (String identifier : copy) {
			if (identifier.length() < 2)
				identifiers.remove(identifier);
		}
	}

	public static Set<String> collectTokens(List<String> tokenStream, int lookback, boolean removeStopwords) {
		if(tokenStream.size() < lookback) return Sets.newHashSet(EMPTY_TOKEN);
		Set<String> identifiers = new HashSet<>();
		for (int i = 0; i < lookback; i++) {
			String token = tokenStream.get(tokenStream.size() - 1 - i);
			if (removeStopwords && isStopWord(token))
				continue;
			
			// ignores duplicates
			if (!identifiers.contains(token))
				identifiers.add(token);
		}
		return identifiers;
	}

	public static boolean isStopWord(String token) {
		if(stopWords.contains(token)) return true;
		return false;
	}
}
