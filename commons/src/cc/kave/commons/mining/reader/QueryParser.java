package cc.kave.commons.mining.reader;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import cc.kave.commons.model.episodes.Episode;

public class QueryParser {

	private File rootFolder;
	private FileReader reader;

	@Inject
	public QueryParser(@Named("eventStream") File directory, FileReader reader) {
		assertTrue(directory.exists(), "Event stream folder does not exist!");
		assertTrue(directory.isDirectory(), "Event stream folder is not a folder, but a file!");
		this.rootFolder = directory;
		this.reader = reader;
	}

	public List<Episode> parse() {
		List<String> eventStream = reader.readFile(getFilePath());
		List<Episode> queryStream = new LinkedList<Episode>();
		List<String> facts = new LinkedList<String>();

		for (String line : eventStream) {
			String[] rowValues = line.split(",");
			if (rowValues[0].equalsIgnoreCase("1")) {
				if (facts.isEmpty()) {
					continue;
				}
				Episode episode = createEpisode(facts);
				queryStream.add(episode);
				facts = new LinkedList<String>();
			} else {
				facts.add(rowValues[0]);
			}
		}
		if (!facts.isEmpty()) {
			Episode episode = createEpisode(facts);
			queryStream.add(episode);
		}
		return queryStream;
	}

	private Episode createEpisode(List<String> events) {
		Episode episode = new Episode();
		events.add("1");
		episode.addListOfFacts(events);
		for (int firstIdx = 0; firstIdx < (events.size() - 1); firstIdx++) {
			for (int secondIdx = (firstIdx + 1); secondIdx < events.size(); secondIdx++) {
				episode.addFact(events.get(firstIdx) + ">" + events.get(secondIdx));
			}
		}
		episode.setFrequency(1);
		episode.setNumEvents(events.size());
		return episode;
	}

	private File getFilePath() {
		String fileName = rootFolder.getAbsolutePath() + "/eventstream.txt";
		File file = new File(fileName);
		return file;
	}
}
