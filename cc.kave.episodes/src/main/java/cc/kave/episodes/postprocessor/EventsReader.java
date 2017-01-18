package cc.kave.episodes.postprocessor;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.io.Logger;

public class EventsReader {

	private File eventsFolder;
	private EventStreamIo eventsIo;
	
	@Inject
	public EventsReader(@Named("events") File directory, EventStreamIo events) {
		assertTrue(directory.exists(), "Events folder does not exist");
		assertTrue(directory.isDirectory(), "Patterns folder is not a folder, but a file");
		this.eventsFolder = directory;
		this.eventsIo = events;
	}
	
	public void read(int frequency) {
		List<Event> events = eventsIo.readMapping(getMappingFile(frequency));
		Logger.log("Number of unique events is %d", events.size());
	}

	private String getMappingFile(int frequency) {
		String fileName = eventsFolder.getAbsolutePath() + "/freq" + frequency + "/TrainingData/fold0/mapping.txt";
		return fileName;
	}
}
