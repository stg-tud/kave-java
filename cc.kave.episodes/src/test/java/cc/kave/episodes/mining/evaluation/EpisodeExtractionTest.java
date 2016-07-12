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
package cc.kave.episodes.mining.evaluation;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Events;
import cc.kave.commons.model.episodes.Fact;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.episodes.model.Episode;

public class EpisodeExtractionTest {

	private Set<Set<Fact>> stream = Sets.newLinkedHashSet();
	private List<Event> events = new LinkedList<>();
	
	private EpisodeExtraction sut;
	
	@Before 
	public void setup() {
		Set<Fact> facts = Sets.newLinkedHashSet();
		facts.add(new Fact(1));
		facts.add(new Fact(2));
		facts.add(new Fact(3));
		facts.add(new Fact(4));
		facts.add(new Fact(5));
		stream.add(facts);
		
		facts = Sets.newLinkedHashSet();
		facts.add(new Fact(4));
		stream.add(facts);
		
		facts = Sets.newLinkedHashSet();
		facts.add(new Fact(6));
		facts.add(new Fact(3));
		facts.add(new Fact(5));
		facts.add(new Fact(7));
		stream.add(facts);
		
		events.add(Events.newDummyEvent());
		events.add(Events.newFirstContext(m(1, 1)));
		events.add(Events.newSuperContext(m(1, 2)));
		events.add(Events.newInvocation(m(1, 3)));
		events.add(Events.newInvocation(m(1, 4)));
		events.add(Events.newInvocation(m(1, 5)));
		events.add(Events.newFirstContext(m(2, 6)));
		events.add(Events.newInvocation(m(2, 7)));
		
		sut = new EpisodeExtraction();
	}
	
	@Test
	public void invocations() {
		Episode episode = new Episode();
		episode.addFact(new Fact(3));
		episode.addFact(new Fact(5));
		
		StringBuilder expected = new StringBuilder();
		expected.append("T1.m2\n");
		
		StringBuilder actuals = sut.getMethods(episode, stream, events);
		
		assertEquals(expected.toString(), actuals.toString());
	}
	
	private IMethodName m(int typeNum, int methodNum) {
		return MethodName.newMethodName(String.format("[R,P] [%s].m%d()", t(typeNum), methodNum));
	}

	private ITypeName t(int typeNum) {
		return TypeName.newTypeName(String.format("T%d,P", typeNum));
	}
}
