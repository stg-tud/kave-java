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
package cc.kave.episodes.patterns;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Events;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.episodes.model.Episode;

public class PatternExtractorTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private List<List<Fact>> stream = new LinkedList<>();
	private List<Event> events = new LinkedList<>();

	Map<Episode, List<IMethodName>> expected = Maps.newLinkedHashMap();
	Map<Integer, Set<Episode>> allEpisodes;
	Set<Episode> episodes;
	Episode episode0;
	Episode episode1;
	Episode episode2;
	Episode episode3;
	Episode episode4;
	Episode episode5;

	private PatternExtractor sut;

	@Before
	public void setup() {

		List<Fact> method = new LinkedList<Fact>();
		method.add(new Fact(1)); // FM
		method.add(new Fact(2)); // SM
		method.add(new Fact(3)); // EM1
		method.add(new Fact(4)); // I1
		method.add(new Fact(5)); // I2
		method.add(new Fact(6)); // I3
		stream.add(method);

		method = new LinkedList<Fact>();
		method.add(new Fact(1));
		method.add(new Fact(7)); // EM2
		method.add(new Fact(6));
		method.add(new Fact(4));
		stream.add(method);

		method = new LinkedList<Fact>();
		method.add(new Fact(2));
		method.add(new Fact(3));
		method.add(new Fact(6));
		stream.add(method);

		method = new LinkedList<Fact>();
		method.add(new Fact(3));
		method.add(new Fact(5));
		method.add(new Fact(4));
		method.add(new Fact(6));
		stream.add(method);

		method = new LinkedList<Fact>();
		method.add(new Fact(1));
		method.add(new Fact(8)); // EM4
		method.add(new Fact(5));
		method.add(new Fact(9)); // I4
		method.add(new Fact(4));
		method.add(new Fact(6));
		stream.add(method);
		
		method = new LinkedList<Fact>();
		method.add(new Fact(1));
		method.add(new Fact(10));
		method.add(new Fact(9));
		stream.add(method);

		events.add(Events.newDummyEvent());
		events.add(Events.newFirstContext(m(1, 11)));
		events.add(Events.newSuperContext(m(1, 21)));
		events.add(Events.newContext(m(1, 31)));
		events.add(Events.newInvocation(m(1, 1)));
		events.add(Events.newInvocation(m(1, 2)));
		events.add(Events.newInvocation(m(1, 3)));
		events.add(Events.newContext(m(2, 32)));
		events.add(Events.newContext(m(5, 34)));
		events.add(Events.newInvocation(m(5, 4)));
		events.add(Events.newInvocation(m(6, 5)));

		allEpisodes = Maps.newLinkedHashMap();
		episodes = Sets.newLinkedHashSet();
	
		episode0 = new Episode();
		episode0.addFact(new Fact(3));
		
		episode1 = new Episode();
		episode1.addStringsOfFacts("4", "5", "6");
		
		episode2 = new Episode();
		episode2.addStringsOfFacts("4", "5", "6", "4>5", "4>6", "5>6");
		
		episode3 = new Episode();
		episode3.addStringsOfFacts("1", "4", "6");
		
		episode4 = new Episode();
		episode4.addStringsOfFacts("4", "5", "6", "11");
		
		episode5 = new Episode();
		episode5.addStringsOfFacts("10", "9");

		sut = new PatternExtractor();
	}

	@Test
	public void invocations() throws Exception {
		allEpisodes.put(3, Sets.newHashSet(episode1));
		
		expected.put(episode1, Lists.newArrayList(m(1, 31), m(1, 31), m(5, 34)));

		Map<Episode, List<IMethodName>> actuals = sut.getMethodsFromCode(allEpisodes, stream, events, false);

		assertEquals(expected, actuals);
	}

	@Test
	public void invocationsWithOrderRelations() throws Exception {
		allEpisodes.put(3, Sets.newHashSet(episode1, episode2));
		
		expected.put(episode1, Lists.newArrayList(m(1, 31), m(1, 31), m(5, 34)));
		expected.put(episode2, Lists.newArrayList(m(1, 31)));

		Map<Episode, List<IMethodName>> actuals = sut.getMethodsFromCode(allEpisodes, stream, events, true);

		assertEquals(expected, actuals);
	}

	@Test
	public void firstDeclaration() throws Exception {
		allEpisodes.put(3, Sets.newHashSet(episode1, episode2, episode3));
		
		expected.put(episode1, Lists.newArrayList(m(1, 31), m(1, 31), m(5, 34)));
		expected.put(episode2, Lists.newArrayList(m(1, 31), m(1, 31), m(5, 34)));
		expected.put(episode3, Lists.newArrayList(m(1, 31), m(2, 32), m(5, 34)));

		Map<Episode, List<IMethodName>> actuals = sut.getMethodsFromCode(allEpisodes, stream, events, false);

		assertEquals(expected, actuals);
	}

	@Test
	public void noOccurrences() throws Exception {
		allEpisodes.put(1, Sets.newHashSet(episode0));
		allEpisodes.put(3, Sets.newHashSet(episode1, episode2, episode3));
		allEpisodes.put(4, Sets.newHashSet(episode4));
		
		expected.put(episode1, Lists.newArrayList(m(1, 31), m(1, 31), m(5, 34)));
		expected.put(episode2, Lists.newArrayList(m(1, 31), m(1, 31), m(5, 34)));
		expected.put(episode3, Lists.newArrayList(m(1, 31), m(2, 32), m(5, 34)));

		Map<Episode, List<IMethodName>> actuals = sut.getMethodsFromCode(allEpisodes, stream, events, false);

		assertEquals(expected, actuals);
	}
	
	@Test
	public void noEnclosingMethos() throws Exception {
		thrown.expect(Exception.class);
		thrown.expectMessage("Method does not have enclosing method!");
		
		allEpisodes.put(1, Sets.newHashSet(episode0));
		allEpisodes.put(2, Sets.newHashSet(episode5));
		
		Map<Episode, List<IMethodName>> actuals = sut.getMethodsFromCode(allEpisodes, stream, events, false);

		assertTrue(actuals.isEmpty());
		assertEquals(expected, actuals);
	}

	private IMethodName m(int typeNum, int methodNum) {
		return MethodName.newMethodName(String.format("[R,P] [%s].m%d()", t(typeNum), methodNum));
	}

	private ITypeName t(int typeNum) {
		return TypeName.newTypeName(String.format("T%d,P", typeNum));
	}
}
