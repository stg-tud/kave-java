package cc.kave.episodes.preprocessing;

import java.util.List;

import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.inject.Inject;

public class EventStreamSize {

	private EventStreamIo eventStreamIo;

	@Inject
	public EventStreamSize(EventStreamIo eventStream) {
		this.eventStreamIo = eventStream;
	}

	public void printNumberOfEvents(int frequency) {
		int numEvents = 0;

		for (int fold = 0; fold < frequency; fold++) {
			List<Event> mapping = eventStreamIo.readMapping(frequency, 0);

			if (mapping.size() > numEvents) {
				numEvents = mapping.size();
			}
		}
		Logger.log("Number of unique events is %d", numEvents);
	}

	public void printMethodSize(int frequency, int sizeLimit) {

		for (int fold = 0; fold < frequency; fold++) {
			List<Tuple<Event, List<Fact>>> stream = eventStreamIo.parseStream(frequency, 0);
			
			for (Tuple<Event, List<Fact>> m : stream) {
				if (m.getSecond().size() > sizeLimit) {
					Event event = m.getFirst();
					String enclMethod = getMethodName(event);
					
					Logger.log("Method: %s\t has %d events", enclMethod, m.getSecond().size());
				}
			}
		}
	}

	private String getMethodName(Event event) {
		String methodName = event.getMethod().getDeclaringType().getFullName()
				+ "." + event.getMethod().getName();
		return methodName;
	}
}
