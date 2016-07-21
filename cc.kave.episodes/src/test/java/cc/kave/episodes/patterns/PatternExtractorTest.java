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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

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

	List<IMethodName> expected = new LinkedList<IMethodName>();
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
		expected.add(m(1, 31));
		expected.add(m(1, 31));
		expected.add(m(5, 34));

		List<IMethodName> actuals = sut.getMethodsFromCode(episode1, stream, events, false);

		assertEquals(expected, actuals);
	}

	@Test
	public void invocationsWithOrderRelations() throws Exception {
		expected.add(m(1, 31));

		List<IMethodName> actuals = sut.getMethodsFromCode(episode2, stream, events, true);

		assertEquals(expected, actuals);
	}

	@Test
	public void firstDeclaration() throws Exception {
		expected.add(m(1, 31));
		expected.add(m(2, 32));
		expected.add(m(5, 34));

		List<IMethodName> actuals = sut.getMethodsFromCode(episode3, stream, events, false);

		assertEquals(expected, actuals);
	}

	@Test
	public void noOccurrences() throws Exception {

		List<IMethodName> actuals = sut.getMethodsFromCode(episode4, stream, events, false);

		assertEquals(expected, actuals);
	}
	
	@Test
	public void noEnclosingMethos() throws Exception {
		thrown.expect(Exception.class);
		thrown.expectMessage("Method does not have enclosing method!");
		
		List<IMethodName> actuals = sut.getMethodsFromCode(episode5, stream, events, false);

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
