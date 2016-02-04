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
package cc.kave.commons.model.groum;

import java.util.HashSet;
import java.util.Set;

import cc.kave.commons.model.groum.comparator.ExasGroumComparator;
import cc.kave.commons.model.groum.comparator.HashCodeComparator;

import com.google.common.collect.TreeMultimap;

public class SubGroumMultiSet {
	private TreeMultimap<IGroum, SubGroum> data = TreeMultimap.create(new ExasGroumComparator(),
			new HashCodeComparator());

	public SubGroumMultiSet() {
	}

	public SubGroumMultiSet(SubGroumMultiSet other) {
		data.putAll(other.data);
	}

	public void add(SubGroum instance) {
		data.put(instance, instance);
	}

	public void addAll(Iterable<SubGroum> instances) {
		instances.forEach(instance -> add(instance));
	}

	public void addAll(SubGroumMultiSet other) {
		data.putAll(other.data);
	}

	public Set<IGroum> getPatterns() {
		return new HashSet<>(data.keySet());
	}

	public SubGroumMultiSet getFrequentSubSet(int threshold) {
		SubGroumMultiSet subset = new SubGroumMultiSet();
		for (IGroum pattern : getPatterns()) {
			if (getPatternFrequency(pattern) >= threshold) {
				subset.addAll(getPatternInstances(pattern));
			}
		}
		return subset;
	}

	private int getPatternFrequency(IGroum pattern) {
		return data.get(pattern).size();
	}

	public Set<SubGroum> getAllInstances() {
		return new HashSet<>(data.values());
	}

	public Set<SubGroum> getPatternInstances(IGroum pattern) {
		return new HashSet<>(data.get(pattern));
	}
}