package cc.kave.episodes.preprocessing;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Map;

import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class EventStreamSize {

	private File reposDir;

	@Inject
	public EventStreamSize(@Named("repositories") File folder) {
		assertTrue(folder.exists(), "Repositories folder does not exist");
		assertTrue(folder.isDirectory(),
				"Repositories is not a folder, but a file");
		this.reposDir = folder;
	}

	public void printNumberOfEvents(int numFods) {
		int numEvents = 0;

		for (int fold = 0; fold < numFods; fold++) {
			String filePath = getPath(fold, "mapping.txt");
			List<Event> mapping = EventStreamIo.readMapping(filePath);

			if (mapping.size() > numEvents) {
				numEvents = mapping.size();
			}
		}
		Logger.log("Number of unique events is %d", numEvents);
	}

	public void printMethodSize(int numFolds, int sizeLimit) {

		for (int fold = 0; fold < numFolds; fold++) {
			List<List<Fact>> stream = EventStreamIo.parseStream(getPath(fold,
					"stream.txt"));
			List<Event> methods = EventStreamIo.readMethods(getPath(fold, "methods.txt"));
			
			Map<String, Integer> longMethods = Maps.newLinkedHashMap();
			
			assertTrue(stream.size() == methods.size(), "Inconsistency between number of methods!");
		}
	}

	private String getPath(int fold, String file) {
		String fileName = reposDir.getAbsolutePath() + "/TrainingData/fold"
				+ fold + "/" + file;
		return fileName;
	}
}
