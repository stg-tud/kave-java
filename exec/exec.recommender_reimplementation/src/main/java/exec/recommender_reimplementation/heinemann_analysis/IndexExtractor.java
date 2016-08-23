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

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.types.ITypeName;
import exec.recommender_reimplementation.tokenization.TokenizationContext;
import exec.recommender_reimplementation.tokenization.TokenizationSettings;

public class IndexExtractor {
	
	public static Map<ITypeName, Set<Entry>> buildModel(List<Context> contexts, int lookback, boolean removeStopwords, boolean removeKeywords) {
		Set<Entry> entrySet = new HashSet<>();
		
		Queue<Context> contextQueue = new LinkedList<>(contexts);
		
		while (!contextQueue.isEmpty()) {
			Context context = contextQueue.poll();
			try {
				entrySet.addAll(extractIndex(context, lookback, removeStopwords, removeKeywords));
			} catch (Exception e) {
				// eat exception because context has errors
			}
		}
		
		return buildMap(entrySet);
	}

	public static Map<ITypeName, Set<Entry>> buildMap(Set<Entry> entrySet) {
		Map<ITypeName, Set<Entry>> entryMap = new HashMap<>();
		
		for (Entry entry : entrySet) {
			ITypeName declaringType = entry.getMethodName().getDeclaringType();
			if(entryMap.containsKey(declaringType)) {
				entryMap.get(declaringType).add(entry);
			}
			else {
				entryMap.put(declaringType, Sets.newHashSet(entry));
			}
		}
		return entryMap;
	}
	
	public static Set<Entry> extractIndex(Context context, int lookback, boolean removeStopwords, boolean removeKeywords) {
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
