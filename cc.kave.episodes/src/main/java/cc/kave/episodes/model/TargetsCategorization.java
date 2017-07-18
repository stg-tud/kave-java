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
package cc.kave.episodes.model;

import static cc.kave.commons.assertions.Asserts.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

public class TargetsCategorization {

	public Map<String, Set<Episode>> categorize(Set<Episode> targets) {
		assertTrue(!targets.isEmpty(), "The set of validation data is empty!");
		
		Map<String, Set<Episode>> categories = new LinkedHashMap<String, Set<Episode>>();
		categories.put("0-1", Sets.newHashSet());
		categories.put("2", Sets.newHashSet());
		categories.put("3", Sets.newHashSet());
		categories.put("4", Sets.newHashSet());
		categories.put("5", Sets.newHashSet());
		categories.put("6-9", Sets.newHashSet());
		categories.put("10-19", Sets.newHashSet());
		categories.put("20-29", Sets.newHashSet());
		categories.put("30-66", Sets.newHashSet());
		
		for (Episode target : targets) {
			int numInvoc = target.getNumEvents() - 1;
			assertTrue(numInvoc < 67, "Review the pre-defined categories!");
			
			if (numInvoc == 0 || numInvoc == 1) {
				categories.get("0-1").add(target);
				continue;
			}
			if (numInvoc == 2) {
				categories.get("2").add(target);
				continue;
			}
			if (numInvoc == 3) {
				categories.get("3").add(target);
				continue;
			}
			if (numInvoc == 4) {
				categories.get("4").add(target);
				continue;
			}
			if (numInvoc == 5) {
				categories.get("5").add(target);
				continue;
			}
			if (numInvoc > 5 && numInvoc < 10) {
				categories.get("6-9").add(target);
				continue;
			}
			if (numInvoc > 9 && numInvoc < 20) {
				categories.get("10-19").add(target);
				continue;
			}
			if (numInvoc > 19 && numInvoc < 30) {
				categories.get("20-29").add(target);
				continue;
			}
			if (numInvoc > 29 && numInvoc < 67) {
				categories.get("30-66").add(target);
				continue;
			} 
		}
		return categories;
	}
}
