package cc.kave.commons.mining.reader;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;

public class EventStreamReader {

	private File rootFolder;
	private FileReader reader;
	private EventMappingParser eventMapper;

	@Inject
	public EventStreamReader(@Named("events") File directory, FileReader reader, EventMappingParser eventMapping) {
		assertTrue(directory.exists(), "Event Stream folder does not exist");
		assertTrue(directory.isDirectory(), "Event Stream folder is not a folder, but a file");
		this.rootFolder = directory;
		this.reader = reader;
		this.eventMapper = eventMapping;
	}

	public void read() {
		File filePath = getFilePath();
		List<Event> allEvents = eventMapper.parse();
		List<String> lines = reader.readFile(filePath);

		int counter = 0;

		for (String line : lines) {
			String[] lineValues = line.split(",");
			int eventIndex = Integer.parseInt(lineValues[0]);
			if (eventIndex == 0 || eventIndex == 2293 || eventIndex == 6457 || eventIndex == 6461 || eventIndex == 6465
					|| eventIndex == 7400) {
				counter++;
				continue;
			}
			System.out.println("--- " + (counter++) + " ---------------------");
			System.out.println(allEvents.get(eventIndex));
			System.out.println(allEvents.get(eventIndex).getMethod().getDeclaringType().toString());
		}
		System.out.printf("found %s events:\n", allEvents.size());
	}

	private File getFilePath() {
		String fileName = rootFolder.getAbsolutePath() + "/eventstream.txt";
		File file = new File(fileName);
		return file;
	}
}
