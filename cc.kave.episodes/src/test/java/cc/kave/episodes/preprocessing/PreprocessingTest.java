package cc.kave.episodes.preprocessing;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.data.ContextsParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Events;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

@RunWith(MockitoJUnitRunner.class)
public class PreprocessingTest {

	@Mock
	private ContextsParser ctxParser;
	@Mock
	private EventStreamIo trainingIo;
	
	private List<Tuple<Event, List<Event>>> stream;

	private static final int FREQUENCY = 5;

	private Preprocessing sut;

	@Before
	public void setup() throws Exception {
		initMocks(this);
		stream = Lists.newLinkedList();
		
		sut = new Preprocessing(ctxParser, trainingIo);
		
		stream.add(Tuple.newTuple(enclCtx(20),
				Lists.newArrayList(firstCtx(1), inv(2), inv(3))));
		stream.add(Tuple.newTuple(enclCtx(7), Lists.newArrayList(firstCtx(2),
				superCtx(2), inv(5), inv(20), inv(2))));
		stream.add(Tuple.newTuple(enclCtx(6),
				Lists.newArrayList(firstCtx(1), inv(2), inv(3))));
		stream.add(Tuple.newTuple(enclCtx(20),
				Lists.newArrayList(firstCtx(1), inv(2), inv(3))));
		stream.add(Tuple.newTuple(enclCtx(8),
				Lists.newArrayList(firstCtx(2), inv(2))));
		stream.add(Tuple.newTuple(enclCtx(6),
				Lists.newArrayList(firstCtx(1), inv(2), inv(3))));
		stream.add(Tuple.newTuple(enclCtx(20),
				Lists.newArrayList(firstCtx(3), superCtx(4), inv(3))));
		
		when(ctxParser.getRepoCtxMapper()).thenReturn(generateMapper());

		doNothing().when(trainingIo).write(any(EventStream.class), anyInt());
	}

	@Test
	public void checkAllRepos() throws Exception {
		sut.run(FREQUENCY);
		
		verify(ctxParser).parse(FREQUENCY);
		verify(trainingIo).write(any(EventStream.class), anyInt());
	}

	private Map<String, Set<IMethodName>> generateMapper() {

		Map<String, Set<IMethodName>> mapper = Maps.newLinkedHashMap();
		mapper.put("Github/usr1/repo1", Sets.newHashSet(enclCtx(1).getMethod()));
		mapper.put("Github/usr1/repo2", Sets.newHashSet(enclCtx(2).getMethod()));
		mapper.put("Github/usr1/repo3", Sets.newHashSet(enclCtx(3).getMethod()));
		mapper.put("Github/usr1/repo4", Sets.newHashSet(enclCtx(4).getMethod()));
		mapper.put("Github/usr1/repo5", Sets.newHashSet(enclCtx(5).getMethod()));
		mapper.put("Github/usr1/repo6", Sets.newHashSet(enclCtx(6).getMethod()));
		mapper.put("Github/usr1/repo7", Sets.newHashSet(enclCtx(7).getMethod()));
		mapper.put("Github/usr1/repo8", Sets.newHashSet(enclCtx(8).getMethod()));
		mapper.put("Github/usr1/repo9", Sets.newHashSet(enclCtx(9).getMethod()));
		mapper.put("Github/usr1/repo10", Sets.newHashSet(enclCtx(10).getMethod()));

		return mapper;
	}

	private static Event inv(int i) {
		return Events.newInvocation(m(i));
	}

	private static Event firstCtx(int i) {
		return Events.newFirstContext(m(i));
	}

	private static Event superCtx(int i) {
		return Events.newSuperContext(m(i));
	}

	private static Event enclCtx(int i) {
		return Events.newContext(m(i));
	}

	private static IMethodName m(int i) {
		if (i == 20) {
			return Names
					.newMethod("[mscorlib,P, 4.0.0.0] [mscorlib,P, 4.0.0.0].m()");
		} else if (i == 30) {
			return Names.newMethod("[mscorlib,P] [mscorlib,P].m()");
		} else {
			return Names
					.newMethod("[T,P, 1.2.3.4] [T,P, 1.2.3.4].m" + i + "()");
		}
	}
}
