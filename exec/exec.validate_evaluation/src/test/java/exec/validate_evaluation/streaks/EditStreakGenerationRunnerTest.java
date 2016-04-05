/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package exec.validate_evaluation.streaks;

import static org.mockito.Matchers.anySetOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.recommenders.io.Logger;

public class EditStreakGenerationRunnerTest {

	private EditStreakGenerationIo io;
	private EditStreakGenerationLogger log;
	private EditStreakGenerationRunner sut;

	private Map<String, Set<ICompletionEvent>> input;
	private Map<String, Set<EditStreak>> output;

	@Before
	public void setup() {
		Logger.setPrinting(true);

		input = Maps.newLinkedHashMap();
		output = Maps.newLinkedHashMap();

		io = mockIo();

		log = new EditStreakGenerationLogger();// (EditStreakGenerationLogger.class);

		sut = new EditStreakGenerationRunner(io, log);
	}

	private EditStreakGenerationIo mockIo() {
		EditStreakGenerationIo io = mock(EditStreakGenerationIo.class);

		when(io.findZips()).then(new Answer<Set<String>>() {
			@Override
			public Set<String> answer(InvocationOnMock invocation) throws Throwable {
				return Sets.newLinkedHashSet(input.keySet());
			}
		});

		when(io.read(anyString())).then(new Answer<Set<ICompletionEvent>>() {
			@Override
			public Set<ICompletionEvent> answer(InvocationOnMock invocation) throws Throwable {
				String zip = (String) invocation.getArguments()[0];
				return input.get(zip);
			}
		});

		doAnswer(new Answer<Void>() {
			@Override
			@SuppressWarnings("unchecked")
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Set<EditStreak> streaks = (Set<EditStreak>) invocation.getArguments()[0];
				String zip = (String) invocation.getArguments()[1];
				output.put(zip, streaks);
				return null;
			}
		}).when(io).storeStreaks(anySetOf(EditStreak.class), anyString());

		when(io.readStreaks(anyString())).then(new Answer<Set<EditStreak>>() {
			@Override
			public Set<EditStreak> answer(InvocationOnMock invocation) throws Throwable {
				String zip = (String) invocation.getArguments()[0];
				return Sets.newLinkedHashSet(output.get(zip));
			}
		});

		return io;
	}

	@Test
	public void asd() {
		addInput("a.zip", new CompletionEvent());
		addInput("b/c.zip", new CompletionEvent(), new CompletionEvent());

		sut.run();

	}

	private void addInput(String string, ICompletionEvent... eventArr) {
		Set<ICompletionEvent> events = Sets.newLinkedHashSet();
		for (ICompletionEvent event : eventArr) {
			events.add(event);
		}
		input.put(string, events);
	}
}