package cc.kave.episodes.preprocessing;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.data.ContextsParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class Preprocessing {

	private ContextsParser ctxParser;
	private EventStreamIo eventStreamIo;

	@Inject
	public Preprocessing(ContextsParser repositories, EventStreamIo streamData) {
		this.ctxParser = repositories;
		this.eventStreamIo = streamData;
	}

	public EventStream run(int frequency) throws Exception {

		List<Tuple<Event, List<Event>>> stream = ctxParser.parse(frequency);
		Map<String, Set<IMethodName>> repoCtsx = ctxParser.getRepoCtxMapper();

		EventStream es = new EventStream();

		Logger.log("Generating event stream data for freq = %d ...", frequency);
		for (Tuple<Event, List<Event>> tuple : stream) {
			for (Event event : tuple.getSecond()) {
				es.addEvent(event);
			}
			es.increaseTimeout();
		}
		eventStreamIo.write(es, frequency);
		List<Tuple<IMethodName, List<Fact>>> streamObject = streamOfFacts(
				stream, frequency);
		eventStreamIo.writeObjects(streamObject, repoCtsx, frequency);

		return es;
	}

	private List<Tuple<IMethodName, List<Fact>>> streamOfFacts(
			List<Tuple<Event, List<Event>>> stream, int frequency) {
		
		List<Event> events = eventStreamIo.readMapping(frequency);
		List<Tuple<IMethodName, List<Fact>>> results = Lists.newLinkedList();
		
		for (Tuple<Event, List<Event>> tuple : stream) {
			List<Fact> method = Lists.newLinkedList();
			
			for (Event e : tuple.getSecond()) {
				method.add(new Fact(events.indexOf(e)));
			}
			results.add(Tuple.newTuple(tuple.getFirst().getMethod(), method));
		}
		
		return results;
	}
}
