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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.NotImplementedException;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.pointsto.extraction.CoReNameConverter;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.mining.calls.ICallsRecommender;
import cc.recommenders.mining.calls.ProposalHelper;
import cc.recommenders.names.ICoReMethodName;

public class HeinemannRecommender implements ICallsRecommender<HeinemannQuery> {

	private Map<ITypeName, Set<Entry>> model;
	private Set<Entry> completeSet;
	private int numberOfRecommendations;

	public HeinemannRecommender(Map<ITypeName,Set<Entry>> model, int numberOfRecommendations) {
		this.model = model;
		this.numberOfRecommendations = numberOfRecommendations;
				
		completeSet = new HashSet<>();
		for (Set<Entry> entrySet : model.values()) {
			completeSet.addAll(entrySet);
		}
	}
	
	@Override
	public Set<Tuple<ICoReMethodName, Double>> query(HeinemannQuery query) {
		ITypeName declaringType = query.getDeclaringType();
		Set<Entry> entrySet = model.get(declaringType);
		if(entrySet == null) {
			// Fallback if no matching declaring type
			entrySet = completeSet;
		}
		double minimumProbability = query.getMinimumSimilarity();
		
		Set<Tuple<ICoReMethodName, Double>> result = ProposalHelper.createSortedSet();
		
		for (Entry entry : entrySet) {
			double similarity = calculateJaccardSimilarity(entry.getLookback(), query.getLookback());
			if(similarity >= minimumProbability) {
				result.add(Tuple.newTuple(CoReNameConverter.convert(entry.getMethodName()), similarity));
			}
		}
		
		return result.stream().limit(numberOfRecommendations).collect(Collectors.toSet());
	}

	public double calculateJaccardSimilarity(Set<String> indexLookback, Set<String> queryLookback) {
		Set<String> intersection = new HashSet<String>(indexLookback);
		intersection.retainAll(queryLookback);
		
		Set<String> union = new HashSet<String>(indexLookback);
		union.addAll(queryLookback);
		
		return intersection.size() / (double) union.size();
	}

	@Override
	public Set<Tuple<String, Double>> getPatternsWithProbability() {
		throw new NotImplementedException();
	}

	@Override
	public Set<Tuple<ICoReMethodName, Double>> queryPattern(String patternName) {
		throw new NotImplementedException();
	}

	@Override
	public int getSize() {
		int size = 0;
		for (Set<Entry> entrySet : model.values()) {
			for (Entry entry : entrySet) {
				for (String s : entry.getLookback()) {
					size += getSizeForString(s);
				}
				size += getSizeForString(entry.getMethodName().getIdentifier());
			}
		}
		return size;
	}

	public int getSizeForString(String s) {
		int charactersSize = s.length() * 2;
		int pointerSize = 8;
		int lengthSize = 4;
		int hashCodeSize = 4;
		return charactersSize + pointerSize + lengthSize + hashCodeSize;
	}
}
