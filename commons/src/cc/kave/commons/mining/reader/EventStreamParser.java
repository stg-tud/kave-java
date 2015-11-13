package cc.kave.commons.mining.reader;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;

public class EventStreamParser {

	private File rootFolder;
	private FileReader reader;
	private EventMappingParser eventMapper;

	@Inject
	public EventStreamParser(@Named("eventStream") File directory, FileReader reader, EventMappingParser eventMapping) {
		assertTrue(directory.exists(), "Event Stream folder does not exist");
		assertTrue(directory.isDirectory(), "Event Stream folder is not a folder, but a file");
		this.rootFolder = directory;
		this.reader = reader;
		this.eventMapper = eventMapping;
	}

	public void eventStreamming() {
		File filePath = getFilePath();
		List<Event> allEvents = eventMapper.parse();
		List<String> lines = reader.readFile(filePath);

		int counter = 0;

		for (String line : lines) {
			String[] lineValues = line.split(",");
			counter++;
			System.out.println(counter + ".");
			System.out.println(allEvents.get(Integer.parseInt(lineValues[0])));
		}
	}

	private File getFilePath() {
		String fileName = rootFolder.getAbsolutePath() + "/eventstream.txt";
		File file = new File(fileName);
		return file;
	}
}
