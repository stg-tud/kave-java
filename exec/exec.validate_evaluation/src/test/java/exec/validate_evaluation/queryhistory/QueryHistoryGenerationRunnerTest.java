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
package exec.validate_evaluation.queryhistory;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.CoReTypeName;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import exec.validate_evaluation.streaks.EditStreak;
import exec.validate_evaluation.streaks.EditStreakGenerationIo;
import exec.validate_evaluation.streaks.Snapshot;

public class QueryHistoryGenerationRunnerTest {

	@Captor
	private ArgumentCaptor<Collection<List<Usage>>> usageCaptor;

	private Map<String, Set<EditStreak>> in;
	private Map<String, Collection<List<Usage>>> out;

	private QueryHistoryGenerationRunner sut;

	private QueryHistoryCollector histCollector;
	private Collection<List<Usage>> collectedUsages;

	private IUsageExtractor usageExtractor;

	private List<Usage> curUsages;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		in = Maps.newLinkedHashMap();
		out = Maps.newLinkedHashMap();

		EditStreakGenerationIo esIo = mockEditStreakIo();
		QueryHistoryIo io = mockQueryHistoryIo();

		QueryHistoryGenerationLogger log = mock(QueryHistoryGenerationLogger.class);
		mockQueryHistoryCollector();
		mockUsageExtractor();
		sut = new QueryHistoryGenerationRunner(esIo, io, log, histCollector, usageExtractor);
	}

	private void mockUsageExtractor() {
		usageExtractor = mock(IUsageExtractor.class);
		for (int num = 1; num <= 10; num++) {
			for (int i = 0; i < num; i++) {
				mockUsageExtraction(num, i);
			}
		}
	}

	private void mockUsageExtraction(int num, int i) {
		when(usageExtractor.analyse(context(num, i))).thenAnswer(new Answer<IAnalysisResult>() {
			@Override
			public IAnalysisResult answer(InvocationOnMock invocation) throws Throwable {
				return analysisResult(num, i);
			}
		});
	}

	private IAnalysisResult analysisResult(int num, int i) {

		Query query = new Query();
		query.setMethodContext(methodContext(num, i));
		query.addCallSite(CallSites.createReceiverCallSite("LT.query()V"));

		IAnalysisResult result = mock(IAnalysisResult.class);
		when(result.getUsages()).thenReturn(usages(num, i));
		when(result.getFirstQuery()).thenReturn(query);

		return result;
	}

	private List<Usage> usages(int num, int i) {
		List<Usage> usages = Lists.newLinkedList();

		Query u = new Query();
		u.setType(CoReTypeName.get("LT-" + num + "-" + i));
		u.setMethodContext(methodContext(num, i));
		u.addCallSite(CallSites.createReceiverCallSite("LT.m()V"));
		usages.add(u);
		return usages;
	}

	private CoReMethodName methodContext(int num, int i) {
		return CoReMethodName.get(String.format("LT.num%di%d()V", num, i));
	}

	private void mockQueryHistoryCollector() {
		histCollector = mock(QueryHistoryCollector.class);

		// be aware that this mocking does not reflect the real implementation,
		// but that it simply uses values that are easy to test for!

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				startUser();
				return null;
			}
		}).when(histCollector).startUser();

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				startSnapshot();
				Usage u = (Usage) invocation.getArguments()[0];
				curUsages.add(u);
				return null;
			}
		}).when(histCollector).register(any(Usage.class));

		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				Usage u = (Usage) invocation.getArguments()[0];
				curUsages.add(u);
				return null;
			}
		}).when(histCollector).registerSelectionResult(any(Usage.class));

		when(histCollector.getHistories()).then(new Answer<Collection<List<Usage>>>() {
			@Override
			public Collection<List<Usage>> answer(InvocationOnMock invocation) throws Throwable {
				return collectedUsages;
			}
		});
	}

	private void startUser() {
		collectedUsages = Lists.newLinkedList();
	}

	private void startSnapshot() {
		curUsages = Lists.newLinkedList();
		collectedUsages.add(curUsages);
	}

	private EditStreakGenerationIo mockEditStreakIo() {
		EditStreakGenerationIo esIo = mock(EditStreakGenerationIo.class);
		when(esIo.findEditStreakZips()).thenAnswer(new Answer<Set<String>>() {
			@Override
			public Set<String> answer(InvocationOnMock invocation) throws Throwable {
				return in.keySet();
			}
		});
		when(esIo.readEditStreaks(anyString())).thenAnswer(new Answer<Set<EditStreak>>() {
			@Override
			public Set<EditStreak> answer(InvocationOnMock invocation) throws Throwable {
				String zip = (String) invocation.getArguments()[0];
				return in.get(zip);
			}
		});
		return esIo;
	}

	private QueryHistoryIo mockQueryHistoryIo() {
		QueryHistoryIo io = mock(QueryHistoryIo.class);
		doAnswer(new Answer<Void>() {
			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				String zip = (String) invocation.getArguments()[1];
				out.put(zip, usageCaptor.getValue());
				return null;
			}
		}).when(io).storeQueryHistories(usageCaptor.capture(), anyString());
		return io;
	}

	@Test
	public void historyCollectorOutputIsStored() {
		in.put("a.zip", streaks());
		in.put("b.zip", streaks());

		sut.run();

		assertEquals(collectedUsages, out.get("a.zip"));
		assertEquals(collectedUsages, out.get("b.zip"));
	}

	@Test
	public void noProblemWithMultipleZipsAndStreaks() {
		in.put("a.zip", streaks(streak(1), streak(2)));
		in.put("b.zip", streaks(streak(3)));

		sut.run();

		Collection<List<Usage>> expectedA = Lists.newLinkedList();
		expectedA.add(usages(1, 0));
		expectedA.add(usages(2, 0));
		expectedA.add(usages(2, 1));

		Collection<List<Usage>> expectedB = Lists.newLinkedList();
		expectedB.add(usages(3, 0));
		expectedB.add(usages(3, 1));
		expectedB.add(usages(3, 2));

		Collection<List<Usage>> actualA = out.get("a.zip");
		assertEquals(expectedA, actualA);
		Collection<List<Usage>> actualB = out.get("b.zip");
		assertEquals(expectedB, actualB);
	}

	@Test
	public void selectionsAreMerged() {
		Context ctx = mock(Context.class);

		EditStreak e = new EditStreak();
		e.add(Snapshot.create(LocalDateTime.now(), ctx, MethodName.newMethodName("[T,P] [T,P].sel()")));
		in.put("a.zip", streaks(e));

		Usage usage = new Query();
		Query usageMerged = new Query();
		usageMerged.addCallSite(CallSites.createReceiverCallSite("LT.sel()LT;"));
		IAnalysisResult res = new IAnalysisResult() {
			@Override
			public List<Usage> getUsages() {
				return Lists.newArrayList(usage);
			}

			@Override
			public Usage getFirstQuery() {
				return usage;
			}
		};
		when(usageExtractor.analyse(ctx)).thenReturn(res);

		sut.run();

		Collection<List<Usage>> actuals = out.get("a.zip");

		List<Usage> usages = Lists.newArrayList(usage, usageMerged);
		Collection<List<Usage>> expecteds = Lists.newArrayList();
		expecteds.add(usages);

		assertEquals(expecteds, actuals);
	}

	@Test
	public void keysAreCorrectlyGeneratedAndPassed() {
		in.put("a.zip", streaks(streak(1), streak(2)));

		sut.run();

		verify(histCollector).startEditStreak(1, Sets.newHashSet(key("LT-1-0", "LT.num1i0()V")));
		verify(histCollector).startEditStreak(2,
				Sets.newHashSet(key("LT-2-0", "LT.num2i0()V"), key("LT-2-1", "LT.num2i1()V")));
	}

	@Test
	@Ignore
	public void integrationTest() {
		// drei EditStreaks für zwei User erzeugen, in dem alle Fälle abgedeckt
		// sind (inkl. Selection).
	}

	private Tuple<ICoReTypeName, ICoReMethodName> key(String t, String m) {
		return Tuple.newTuple(CoReTypeName.get(t), CoReMethodName.get(m));
	}

	private static Set<EditStreak> streaks(EditStreak... streakArr) {
		Set<EditStreak> streaks = Sets.newLinkedHashSet();
		for (EditStreak es : streakArr) {
			streaks.add(es);
		}
		return streaks;
	}

	private static EditStreak streak(int num) {
		EditStreak e = new EditStreak();
		for (int i = 0; i < num; i++) {
			LocalDateTime date = LocalDateTime.now().plusSeconds(num);
			Context context = context(num, i);
			IMethodName sel = MethodName.newMethodName("[T,P] [T,P] m" + num + "()");
			Snapshot snapshot = Snapshot.create(date, context, sel);
			e.add(snapshot);
		}
		return e;
	}

	private static Context context(int num, int i) {
		FieldDeclaration fd1 = new FieldDeclaration();
		fd1.setName(FieldName.newFieldName("[T,P] [T,P].num" + num));

		FieldDeclaration fd2 = new FieldDeclaration();
		fd2.setName(FieldName.newFieldName("[T,P] [T,P].i" + i));

		Context ctx = new Context();
		ctx.getSST().getFields().add(fd1);
		ctx.getSST().getFields().add(fd2);

		return ctx;
	}
}