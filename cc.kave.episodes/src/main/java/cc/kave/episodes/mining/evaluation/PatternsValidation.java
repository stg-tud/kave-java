package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;

import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.IndivReposParser;
import cc.kave.episodes.io.StreamParser;
import cc.kave.episodes.mining.graphs.EpisodeAsGraphWriter;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.mining.graphs.TransitivelyClosedEpisodes;
import cc.kave.episodes.mining.patterns.MaximalEpisodes;
import cc.kave.episodes.model.events.Fact;
import cc.kave.episodes.postprocessor.EpisodesPostprocessor;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class PatternsValidation {

	private File reposFolder;
	private StreamParser streamParser;
	private EventStreamIo mapping;
	private EpisodesPostprocessor episodeProcessor;
	private MaximalEpisodes maxEpisodes;
	private TransitivelyClosedEpisodes closedEpisodes;
	private EpisodeToGraphConverter episodeToGraph;
	private EpisodeAsGraphWriter graphWriter;

	private IndivReposParser reposParser;
	
	private static final int NUMREPOS = 353;

	@Inject
	public PatternsValidation(@Named("repositories") File folder,
			StreamParser stream, EventStreamIo mapping,
			EpisodesPostprocessor processor, MaximalEpisodes maxEp,
			TransitivelyClosedEpisodes closedEp,
			EpisodeToGraphConverter episodeToGraph,
			EpisodeAsGraphWriter grpahWriter, IndivReposParser repoParser) {
		assertTrue(folder.exists(), "Repositories folder does not exist");
		assertTrue(folder.isDirectory(),
				"Repositories folder is not a folder, but a file");
		this.reposFolder = folder;
		this.streamParser = stream;
		this.mapping = mapping;
		this.episodeProcessor = processor;
		this.maxEpisodes = maxEp;
		this.closedEpisodes = closedEp;
		this.episodeToGraph = episodeToGraph;
		this.graphWriter = grpahWriter;
		this.reposParser = repoParser;
	}
	
	private void trainingCode(int freq, double entropy) {
		for (int repoID = 0; repoID < NUMREPOS; repoID++) {
			List<List<Fact>> stream = streamParser.parse(repoID);
		}
	}
}
