package cc.kave.episodes.preprocessing;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;

import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.io.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class EventStreamSize {

	private File reposDir;
	private EventStreamIo eventStreamIo;

	@Inject
	public EventStreamSize(@Named("repositories") File folder) {
		assertTrue(folder.exists(), "Repositories folder does not exist");
		assertTrue(folder.isDirectory(),
				"Repositories is not a folder, but a file");
		this.reposDir = folder;
	}

	public void printNumberOfEvents(int frequency) {
		int numEvents = 0;

		for (int fold = 0; fold < frequency; fold++) {
			List<Event> mapping = eventStreamIo.readMapping(frequency);

			if (mapping.size() > numEvents) {
				numEvents = mapping.size();
			}
		}
		Logger.log("Number of unique events is %d", numEvents);
	}

	public void printMethodSize(int frequency, int sizeLimit) {

		for (int fold = 0; fold < frequency; fold++) {
			List<List<Fact>> stream = eventStreamIo.parseStream(frequency);
			List<Event> methods = eventStreamIo.readMethods(frequency);
			
			assertTrue(stream.size() == methods.size(), "Inconsistency between number of methods!");
			
			for (List<Fact> m : stream) {
				if (m.size() > sizeLimit) {
					int index = stream.indexOf(m);
					Event event = methods.get(index);
					String enclMethod = getMethodName(event);
					
					Logger.log("Method: %s\t has %d events", enclMethod, m.size());
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
