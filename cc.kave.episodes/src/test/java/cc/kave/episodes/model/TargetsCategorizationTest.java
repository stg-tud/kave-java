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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Sets;

public class TargetsCategorizationTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private Set<Episode> targets;
	private TargetsCategorization sut;

	@Before
	public void setup() {
		targets = Sets.newHashSet(createTarget("11"), createTarget("11", "12", "11>12"),
				createTarget("11", "12", "13", "11>12", "11>13"),
				createTarget("11", "12", "13", "14", "11>12", "11>13", "11>14", "12>13"),
				createTarget("11", "12", "13", "14", "15", "11>12", "11>13", "11>14", "11>15"),
				createTarget("11", "12", "13", "14", "15", "16", "11>12", "11>13", "11>14", "11>15"),
				createTarget("11", "12", "13", "14", "15", "16", "17", "11>12", "11>13", "11>14"),
				createTarget("11", "12", "13", "14", "15", "16", "17", "18", "11>12", "11>13", "11>14"),
				createTarget("11", "12", "13", "14", "15", "16", "17", "18", "19", "11>12", "11>13"),
				createTarget("11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "11>12"),
				createTarget("11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "11>12"),
				createTarget("11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"),
				createTarget("11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"));
		
		sut = new TargetsCategorization();
	}

	@Test
	public void categoryTest() {
		Map<String, Set<Episode>> expected = new LinkedHashMap<String, Set<Episode>>();
		expected.put("0-1", Sets.newHashSet(createTarget("11"), createTarget("11", "12", "11>12")));
		expected.put("2", Sets.newHashSet(createTarget("11", "12", "13", "11>12", "11>13")));
		expected.put("3", Sets.newHashSet(createTarget("11", "12", "13", "14", "11>12", "11>13", "11>14", "12>13")));
		expected.put("4",
				Sets.newHashSet(createTarget("11", "12", "13", "14", "15", "11>12", "11>13", "11>14", "11>15")));
		expected.put("5",
				Sets.newHashSet(createTarget("11", "12", "13", "14", "15", "16", "11>12", "11>13", "11>14", "11>15")));
		expected.put("6-9",
				Sets.newHashSet(createTarget("11", "12", "13", "14", "15", "16", "17", "11>12", "11>13", "11>14"),
						createTarget("11", "12", "13", "14", "15", "16", "17", "18", "11>12", "11>13", "11>14"),
						createTarget("11", "12", "13", "14", "15", "16", "17", "18", "19", "11>12", "11>13"),
						createTarget("11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "11>12")));
		expected.put("10-19",
				Sets.newHashSet(createTarget("11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "11>12"),
						createTarget("11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22"),
						createTarget("11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23")));
		expected.put("20-29", Sets.newHashSet());
		expected.put("30-66", Sets.newHashSet());
		
		Map<String, Set<Episode>> actuals = sut.categorize(targets);
		
		assertCategories(expected, actuals);
	}

	private void assertCategories(Map<String, Set<Episode>> expected, Map<String, Set<Episode>> actuals) {
		assertEquals(expected.size(), actuals.size());
		for (Map.Entry<String, Set<Episode>> entry : expected.entrySet()) {
			assertEquals(entry.getValue().size(), actuals.get(entry.getKey()).size());
			for (Episode target : entry.getValue()) {
				assertTrue(actuals.get(entry.getKey()).contains(target));
			}
		}
		
	}

	private Episode createTarget(String... strings) {
		Episode target = new Episode();
		target.addStringsOfFacts(strings);
		return target;
	}
}
