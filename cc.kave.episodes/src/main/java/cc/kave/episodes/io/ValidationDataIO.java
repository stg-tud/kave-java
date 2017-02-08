package cc.kave.episodes.io;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import cc.kave.commons.utils.json.JsonUtils;
import cc.kave.episodes.model.events.Event;

import com.google.common.reflect.TypeToken;

public class ValidationDataIO {

	private File repoDir;
	
	@Inject
	public ValidationDataIO(@Named("events") File folder) {
		assertTrue(folder.exists(), "Events folder does not exist");
		assertTrue(folder.isDirectory(),
				"Events is not a folder, but a file");
		this.repoDir = folder;
	}
	
	public void write(List<Event> stream, int frequency) {
		JsonUtils.toJson(stream, getValidationPath(frequency));
	}

	public List<Event> read(int frequency) {
		@SuppressWarnings("serial")
		Type type = new TypeToken<List<Event>>() {
		}.getType();
		List<Event> stream = JsonUtils.fromJson(getValidationPath(frequency), type);
		return stream;
	}
	
	private File getValidationPath(int frequency) {
		File path = new File(repoDir.getAbsolutePath() + "/freq" + frequency + "/ValidationData");
		if (!path.exists()) {
			path.mkdir();
		}
		File fileName = new File(path.getAbsoluteFile() + "/stream0.txt");
		return fileName;
	}
}
