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

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Fact;

public class SubsetsGenerator {
	
	public Set<Set<Fact>> generateSubsets(Set<Fact> originalSet, int length) {
		assertTrue((originalSet.size() > 1), "Cannot subselect from less then one method invocation!");
		assertTrue(length < originalSet.size(), "Please subselect less than the total number of Facts!");
		
		Set<Set<Fact>> allSubsets = powerSet(originalSet);
		return selectLength(allSubsets, length);
	}
 
	private Set<Set<Fact>> powerSet(Set<Fact> originalSet) {
		Set<Set<Fact>> sets = new HashSet<Set<Fact>>(); 
		if (originalSet.isEmpty()) { 
			sets.add(new HashSet<Fact>()); 
			return sets; 
		} 
		List<Fact> list = new ArrayList<Fact>(originalSet); 
		Fact head = list.get(0); 
		Set<Fact> rest = new HashSet<Fact>(list.subList(1, list.size())); 
		for (Set<Fact> set : powerSet(rest)) { 
			Set<Fact> newSet = new HashSet<Fact>(); 
			newSet.add(head); 
			newSet.addAll(set); 
			sets.add(newSet); 
			sets.add(set); 
		} 
		return sets; 
	}
	
	private Set<Set<Fact>> selectLength(Set<Set<Fact>> allSets, int length) {
		Set<Set<Fact>> result = Sets.newHashSet(Sets.newHashSet());
		
		for (Set<Fact> set : allSets) {
			if (set.size() == length) {
				result.add(set);
			}
		}
		return result;
	}
}
