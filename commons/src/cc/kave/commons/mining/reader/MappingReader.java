package cc.kave.commons.mining.reader;

import java.util.List;

import com.google.inject.Inject;

import cc.kave.commons.model.episodes.Event;

public class MappingReader {

	private EventMappingParser eventMapper;

	@Inject
	public MappingReader(EventMappingParser eventMapping) {
		this.eventMapper = eventMapping;
	}

	public void read() {
		List<Event> events = eventMapper.parse();
		int counter = 0;

		for (Event e : events) {
			if (counter == 2293 || counter == 6457 || counter == 6461 || counter == 6465 || counter == 7400) {
				counter++;
				continue;
			}
			System.out.println("--- " + (counter++) + " ---------------------");
			System.out.println(e);
			System.out.println(e.getMethod().getDeclaringType().getFullName().toString());
		}
		System.out.println(events.size());
	}
}
