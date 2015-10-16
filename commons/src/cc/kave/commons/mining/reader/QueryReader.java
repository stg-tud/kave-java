package cc.kave.commons.mining.reader;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Episode;

public class QueryReader {

	private File rootFolder;

	@Inject
	public QueryReader(@Named("eventStream") File directory) {
		assertTrue(directory.exists(), "Event stream folder does not exist");
		assertTrue(directory.isDirectory(), "Event stream folder is not a folder, but a file");
		this.rootFolder = directory;
	}

	public List<Episode> build() {
		return null;
	}

	private File getFilePath() {
		String fileName = rootFolder.getAbsolutePath() + "/eventstream.txt";
		File file = new File(fileName);
		return file;
	}
}
