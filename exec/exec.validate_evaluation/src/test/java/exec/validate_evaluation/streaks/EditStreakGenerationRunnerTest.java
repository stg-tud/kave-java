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

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.CompletionEvent;
import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.kave.commons.model.events.completionevents.Proposal;
import cc.kave.commons.model.events.completionevents.ProposalSelection;
import cc.kave.commons.model.events.completionevents.TerminationState;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.Name;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.impl.SST;
import cc.recommenders.io.Logger;

public class EditStreakGenerationRunnerTest {

	private static final IName anyName = Name.newName("abc");

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
	public void happyPath() {
		Context ctx1 = context(TypeName.newTypeName("T,P"));
		Context ctx2 = context(TypeName.newTypeName("T,P"));
		addInput("a.zip", apply(anyMethod(1), ctx1), apply(anyMethod(2), ctx2));

		sut.run();

	}

	@Test
	public void noMethodSelected() {
		addInput("a.zip", apply(anyName, new Context()));

		sut.run();

	}

	private static IMethodName anyMethod(int i) {
		return MethodName.newMethodName("[T,P] [T,P].m" + i + "()");
	}

	private Context context(ITypeName encType) {
		SST sst = new SST();
		sst.setEnclosingType(encType);
		Context ctx = new Context();
		ctx.setSST(sst);
		return ctx;
	}

	private void addInput(String string, ICompletionEvent... eventArr) {
		Set<ICompletionEvent> events = Sets.newLinkedHashSet();
		for (ICompletionEvent event : eventArr) {
			events.add(event);
		}
		input.put(string, events);
	}

	private static ICompletionEvent apply(IName name, Context context) {
		return completionEventWithSelection(TerminationState.Applied, name, context);
	}

	private static ICompletionEvent abort(IName name, Context context) {
		return completionEventWithSelection(TerminationState.Cancelled, name, context);
	}

	private static ICompletionEvent completionEventWithSelection(TerminationState state, IName name, Context context) {
		CompletionEvent e = completionEvent(context);

		e.terminatedState = state;

		Proposal p = new Proposal();
		p.Name = name;
		e.proposalCollection.add(p);

		ProposalSelection ps = new ProposalSelection();
		ps.Proposal = p;
		e.selections.add(ps);

		return e;
	}

	private static CompletionEvent completionEvent(Context context) {
		CompletionEvent e = new CompletionEvent();
		e.TriggeredAt = LocalDateTime.now();

		e.terminatedState = TerminationState.Unknown;
		e.context = context;

		return e;
	}
}