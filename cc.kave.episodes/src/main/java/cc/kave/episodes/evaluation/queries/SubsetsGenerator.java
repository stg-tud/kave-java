/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.episodes.evaluation.queries;

import static cc.kave.assertions.Asserts.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.episodes.model.events.Fact;

public class SubsetsGenerator {
	
	public Set<Set<Fact>> generateSubsets(Set<Fact> originalSet, int length, int limit) {
		assertTrue((originalSet.size() > 1), "Cannot subselect from less then one method invocation!");
		assertTrue(length < originalSet.size(), "Please subselect less than the total number of Facts!");
		
		List<Fact> superSet = setToListConverter(originalSet);
		List<Set<Fact>> queryList = getSubsets(superSet, length);
		Set<Set<Fact>> querySet = limitQueries(queryList, limit);
		return querySet;
	}
	
	private Set<Set<Fact>> limitQueries(List<Set<Fact>> queryList, int limit) {
		if (queryList.size() <= limit) {
			return listofsetsToSetofsets(queryList);
		}
		Set<Set<Fact>> result = Sets.newHashSet();
		Random random = new Random();
		while(result.size() < limit) {
			int number = random.nextInt(queryList.size());
			result.add(queryList.get(number));
		}
 		return result;
	}

	private Set<Set<Fact>> listofsetsToSetofsets(List<Set<Fact>> queryList) {
		Set<Set<Fact>> result = Sets.newHashSet();
		for (Set<Fact> set : queryList) {
			result.add(set);
		}
		return result;
	}

	private List<Fact> setToListConverter(Set<Fact> originalSet) {
		List<Fact> list = new LinkedList<Fact>();
		for (Fact fact : originalSet) {
			list.add(fact);
		}
		return list;
	}

	private static List<Set<Fact>> getSubsets(List<Fact> superSet, int k) {
	    List<Set<Fact>> res = new ArrayList<>();
	    getSubsets(superSet, k, 0, new HashSet<Fact>(), res);
	    return res;
	}
	
	private static void getSubsets(List<Fact> superSet, int k, int idx, Set<Fact> current,List<Set<Fact>> solution) {
	    //successful stop clause
	    if (current.size() == k) {
	        solution.add(new HashSet<>(current));
	        return;
	    }
	    //unseccessful stop clause
	    if (idx == superSet.size()) return;
	    Fact x = superSet.get(idx);
	    current.add(x);
	    //"guess" x is in the subset
	    getSubsets(superSet, k, idx+1, current, solution);
	    current.remove(x);
	    //"guess" x is not in the subset
	    getSubsets(superSet, k, idx+1, current, solution);
	}
}
