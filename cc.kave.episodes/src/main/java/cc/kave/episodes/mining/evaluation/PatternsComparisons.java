package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.model.Episode;
import cc.kave.episodes.model.EpisodeType;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EpisodesPostprocessor;
import cc.recommenders.io.Logger;

import com.google.common.collect.Sets;
import com.google.inject.name.Named;

public class PatternsComparisons {

	private File eventsFolder;
	private EpisodesParser parser;
	private EpisodesPostprocessor processor;

	@Inject
	public PatternsComparisons(@Named("events") File folder,
			EpisodesParser parser, EpisodesPostprocessor processor) {
		assertTrue(folder.exists(), "Events folder does not exist");
		assertTrue(folder.isDirectory(), "Events is not a folder, but a file");
		this.eventsFolder = folder;
		this.parser = parser;
		this.processor = processor;
	}

	public void compare(int foldNum, EpisodeType kind1, EpisodeType kind2,
			int frequency, double entropy) {
		Map<Integer, Set<Episode>> set1Episodes = generatePatterns(foldNum,
				kind1, frequency, entropy);
		Map<Integer, Set<Episode>> set2Episodes = generatePatterns(foldNum,
				kind2, frequency, entropy);

		Logger.log("Comparing %s with %s", kind1.toString(), kind2.toString());
		compare(set1Episodes, set2Episodes);
	}

	private void compare(Map<Integer, Set<Episode>> set1Episodes,
			Map<Integer, Set<Episode>> set2Episodes) {

		Logger.log("Number of Nodes\tcoverage");
		for (Map.Entry<Integer, Set<Episode>> entry : set1Episodes.entrySet()) {
			if (entry.getKey() == 1) {
				continue;
			}
			Set<Episode> coveredEpisodes = Sets.newHashSet();
			Set<Episode> episodes1 = entry.getValue();
			
			if (!set2Episodes.containsKey(entry.getKey())) {
				break;
			}
			
			Set<Episode> episodes2 = set2Episodes.get(entry.getKey());
			
			for (Episode ep1 : episodes1) {
				Set<Episode> currentCoverage = Sets.newHashSet();

				for (Episode ep2 : episodes2) {
					if (coveredEpisodes.contains(ep2)) {
						continue;
					}
					boolean cover = compare(ep1, ep2);
					if (cover) {
						coveredEpisodes.add(ep2);
						currentCoverage.add(ep2);
					}
				}
				// System.out.println("partial episode is " + ep1.toString());
				// System.out.println("matching sequential episodes are: " +
				// currentCoverage.toString());
			}
			Logger.log("%d\t%d from %d", entry.getKey(),
					coveredEpisodes.size(), set2Episodes.get(entry.getKey())
							.size());
		}
		Logger.log("");
	}

	private boolean compare(Episode ep1, Episode ep2) {
		Set<Fact> events1 = ep1.getEvents();
		Set<Fact> events2 = ep2.getEvents();

		if (!events1.containsAll(events2)) {
			return false;
		} else {
			Set<Fact> relations1 = ep1.getRelations();
			Set<Fact> relations2 = ep2.getRelations();

			for (Fact rel2 : relations2) {
				if (!relations1.contains(rel2)) {
					return false;
				}
			}
		}
		return true;
	}

	private Map<Integer, Set<Episode>> generatePatterns(int foldNum,
			EpisodeType kind, int frequency, double entropy) {
		Map<Integer, Set<Episode>> episodes = parser.parse(frequency, kind);
		Map<Integer, Set<Episode>> patterns = getProcessedPatterns(episodes,
				kind, frequency, entropy);

		Logger.log("Information for episode types %s:", kind.toString());
		Logger.log("Number of nodes\tNumber of Patterns");
		for (Map.Entry<Integer, Set<Episode>> entry : patterns.entrySet()) {
			Logger.log("%d\t%d", entry.getKey(), entry.getValue().size());
		}
		Logger.log("");
		return patterns;
	}

	private Map<Integer, Set<Episode>> getProcessedPatterns(
			Map<Integer, Set<Episode>> episodes, EpisodeType kind,
			int frequency, double entropy) {
//		if (kind == EpisodeKind.GENERAL) {
//			return processor.postprocess(episodes, frequency, entropy);
//		} else {
			return episodes;
//		}
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
