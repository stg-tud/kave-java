package cc.kave.commons.mining.reader;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.utils.json.JsonUtils;

public class EventMappingParser {

	private File rootFolder;

	@Inject
	public EventMappingParser(@Named("mapping") File directory) {
		assertTrue(directory.exists(), "Event mapping folder does not exist");
		assertTrue(directory.isDirectory(), "Event mapping folder is not a folder, but a file");
		this.rootFolder = directory;
	}

	public List<Event> parse() {

		@SuppressWarnings("serial")
		Type listType = new TypeToken<LinkedList<Event>>() {
		}.getType();
		List<Event> events = JsonUtils.fromJson(getFilePath(), listType);
		return events;
	}

	private File getFilePath() {
		String fileName = rootFolder.getAbsolutePath() + "/eventMapping.txt";
		File file = new File(fileName);
		return file;
	}
}
