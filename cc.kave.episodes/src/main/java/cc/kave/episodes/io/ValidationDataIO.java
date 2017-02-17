package cc.kave.episodes.io;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import cc.kave.commons.utils.json.JsonUtils;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Fact;

import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;

public class ValidationDataIO {

	private File repoDir;

	@Inject
	public ValidationDataIO(@Named("events") File folder) {
		assertTrue(folder.exists(), "Events folder does not exist");
		assertTrue(folder.isDirectory(), "Events is not a folder, but a file");
		this.repoDir = folder;
	}

	public void write(List<Event> stream, int frequency, int foldNum) {
		JsonUtils.toJson(stream, getValidationPath(frequency, foldNum));
	}

	public List<Event> read(int frequency, int foldNum) {
		@SuppressWarnings("serial")
		Type type = new TypeToken<List<Event>>() {
		}.getType();
		List<Event> stream = JsonUtils.fromJson(
				getValidationPath(frequency, foldNum), type);
		return stream;
	}

	public List<List<Fact>> streamOfFacts(List<Event> stream,
			Map<Event, Integer> events) {
		List<List<Fact>> results = Lists.newLinkedList();
		List<Fact> method = Lists.newLinkedList();

		for (Event event : stream) {
			if (event.getKind() == EventKind.FIRST_DECLARATION) {
				if (!method.isEmpty()) {
					results.add(method);
					method = Lists.newLinkedList();
				}
			}
			int index = events.get(event);
			method.add(new Fact(index));
		}
		if (!method.isEmpty()) {
			results.add(method);
		}
		return results;
	}

	private File getValidationPath(int frequency, int foldNum) {
		File path = new File(repoDir.getAbsolutePath() + "/freq" + frequency
				+ "/ValidationData");
		if (!path.exists()) {
			path.mkdir();
		}
		File fileName = new File(path.getAbsoluteFile() + "/stream" + foldNum
				+ ".txt");
		return fileName;
	}
}
