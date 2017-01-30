package cc.kave.episodes.postprocessor;

import java.util.List;

import javax.inject.Inject;

import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.io.Logger;

public class EventsReader {

	private EventStreamIo eventsIo;
	
	@Inject
	public EventsReader(EventStreamIo events) {
		this.eventsIo = events;
	}
	
	public void read(int frequency) {
		List<Event> events = eventsIo.readMapping(frequency);
		Logger.log("Number of unique events is %d", events.size());
	}
}
