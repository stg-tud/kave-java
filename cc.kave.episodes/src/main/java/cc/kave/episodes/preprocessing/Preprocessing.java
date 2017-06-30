package cc.kave.episodes.preprocessing;

import java.util.List;

import cc.kave.episodes.data.ContextsParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

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
		EventStream es = new EventStream();

		Logger.log("Generating event stream data for freq = %d ...", frequency);
		for (Tuple<Event, List<Event>> tuple : stream) {
			for (Event event : tuple.getSecond()) {
				es.addEvent(event);
			}
			es.increaseTimeout();
		}
		eventStreamIo.write(es, frequency);
		return es;
	}
}
