package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.postprocessor.EpisodesFilter;
import cc.recommenders.io.Logger;

import com.google.inject.name.Named;

public class PartialOrderAnalyzer {

	private File eventsFolder;
	private EpisodesParser parser;
	private EpisodesFilter processor;

	@Inject
	public PartialOrderAnalyzer(@Named("events") File folder,
			EpisodesParser parser, EpisodesFilter processor) {
		assertTrue(folder.exists(), "Events folder does not exist");
		assertTrue(folder.isDirectory(), "Events is not a folder, but a file");
		this.eventsFolder = folder;
		this.parser = parser;
		this.processor = processor;
	}

	public void analyze(int foldNum, int frequency, double entropy) {
		Map<Integer, Set<Episode>> episodes = parser.parse(frequency, EpisodeType.GENERAL);
		Map<Integer, Set<Episode>> patterns = processor.postprocess(episodes, frequency, entropy);
		
		Logger.log("Number of Nodes\tPartial\tSequential\tParallel");
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			int partial = 0;
			int seq = 0;
			int parallel = 0;
			
			Set<Episode> pts = entry.getValue();
			
			for (Episode p : pts) {
				if (p.getEntropy() == 1.0) {
					seq++;
				} else if (p.getFacts().size() == p.getEvents().size()) {
					parallel++;
				} else {
					partial++;
				}
			}
			Logger.log("%d\t%d\t%d\t%d", entry.getKey(), partial, seq, parallel);
		}
	}


	private String getPath(int foldNum) {
		String filePath = eventsFolder.getAbsolutePath() + "/TrainingData/fold"
				+ foldNum + "/";
		return filePath;
	}

	private String getEpisodeType(EpisodeType kind) {
		String type = "";
		if (kind == EpisodeType.SEQUENTIAL) {
			type = "Seq";
		} else if (kind == EpisodeType.PARALLEL) {
			type = "Parallel";
		} else {
			type = "Mix";
		}
		return type;
	}

	private File getEpisodePath(int foldNum, EpisodeType kind) {
		String episodeType = getEpisodeType(kind);
		String fileName = getPath(foldNum) + "/episodes" + episodeType + ".txt";
		return new File(fileName);
	}
}
