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
import java.util.List;

import opennlp.tools.stemmer.snowball.SnowballStemmer;
import opennlp.tools.stemmer.snowball.SnowballStemmer.ALGORITHM;

public class ExtractionUtil {
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
}
