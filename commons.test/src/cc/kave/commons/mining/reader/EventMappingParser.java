package cc.kave.commons.mining.reader;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class EventMappingParser {

	private File rootFolder;
	private FileReader reader;

	@Inject
	public EventMappingParser(@Named("eventMapping") File directory, FileReader reader) {
		assertTrue(directory.exists(), "Event mapping folder does not exist!");
		assertTrue(directory.isDirectory(), "Event mapping folder is not a folder, but a file!");
		this.rootFolder = directory;
		this.reader = reader;
	}
	
}
