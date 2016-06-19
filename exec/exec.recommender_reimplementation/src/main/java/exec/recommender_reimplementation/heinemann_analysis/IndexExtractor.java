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

import java.util.List;

import cc.kave.commons.model.events.completionevents.Context;
import exec.recommender_reimplementation.tokenization.TokenizationContext;
import exec.recommender_reimplementation.tokenization.TokenizationSettings;

public class IndexExtractor {
	public static List<Entry> extractIndex(Context context, int lookback, boolean removeStopwords, boolean removeKeywords) {
		TokenizationSettings settings = new TokenizationSettings();
		settings.setActiveBrackets(false);
		if (!removeKeywords) {
			settings.setActiveKeywords(true);
			settings.setActiveWrapKeywords(true);
		}
		settings.setActiveOperators(false);
		settings.setActivePuncutuation(false);
		
		TokenizationContext tokenizationContext = new TokenizationContext(settings);
		
		HeinemannTokenizationVisitor analysisVisitor = new HeinemannTokenizationVisitor(context.getTypeShape(), lookback, removeStopwords);
		context.getSST().accept(analysisVisitor, tokenizationContext);
		
		return analysisVisitor.getIndex();
	}
}
