package cc.kave.commons.mining.episodes;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.mining.reader.FileReader;
import cc.kave.commons.model.episodes.Episode;

public class QueryGenerator {

	private File rootFolder;
	private FileReader reader;

	@Inject
	public QueryGenerator(@Named("events") File directory, FileReader reader) {
		assertTrue(directory.exists(), "Event stream folder does not exist!");
		assertTrue(directory.isDirectory(), "Event stream folder is not a folder, but a file!");
		this.rootFolder = directory;
		this.reader = reader;
	}

	public List<Episode> parse() {
		List<String> eventStream = reader.readFile(getFilePath());
		List<Episode> queryStream = new LinkedList<Episode>();
		List<String> facts = new LinkedList<String>();
		double previousEventTimestamp = 0.000;

		for (String line : eventStream) {
			String[] rowValues = line.split(",");
			double currentEventTimestamp = Double.parseDouble(rowValues[1]);
			if ((currentEventTimestamp - previousEventTimestamp) >= 0.5) {
				Episode query = createQuery(facts);
				queryStream.add(query);
				facts = new LinkedList<String>();
			}
			if (!facts.contains(rowValues[0])) {
				facts.add(rowValues[0]);
			}
			previousEventTimestamp = currentEventTimestamp;
		}
		Episode query = createQuery(facts);
		queryStream.add(query);
		return queryStream;
	}

	private Episode createQuery(List<String> events) {
		Episode episode = new Episode();
		episode.addListOfFacts(events);
		for (int idx = 0; idx < (events.size() - 1); idx++) {
			episode.addFact(events.get(idx) + ">" + events.get(idx + 1));
		}
		episode.setFrequency(1);
		episode.setNumEvents(events.size());
		return episode;
	}

	private File getFilePath() {
		String fileName = rootFolder.getAbsolutePath() + "/eventstreamModified.txt";
		File file = new File(fileName);
		return file;
	}
}
