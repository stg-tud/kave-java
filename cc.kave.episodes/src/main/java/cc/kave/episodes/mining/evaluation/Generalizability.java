package cc.kave.episodes.mining.evaluation;

import java.io.File;

import javax.inject.Inject;

import cc.kave.episodes.data.ContextsParser;
import cc.kave.episodes.io.EpisodeParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.patterns.PatternFilter;
import cc.kave.episodes.model.EpisodeType;
import static cc.recommenders.assertions.Asserts.assertTrue;

import com.google.inject.name.Named;

public class Generalizability {

	private File patternFolder;

	private ContextsParser contexts;
	private EpisodeParser episodes;
	private PatternFilter patterns;

	private EventStreamIo streamIo;

	@Inject
	public Generalizability(@Named("patterns") File folder,
			ContextsParser ctxParser, EpisodeParser epParser,
			PatternFilter pattFilter, EventStreamIo eventsIo) {
		assertTrue(folder.exists(), "Patterns folder does not exist!");
		assertTrue(folder.isDirectory(),
				"Patterns is not a folder, but a file!");
		this.contexts = ctxParser;
		this.episodes = epParser;
		this.patterns = pattFilter;
		this.streamIo = eventsIo;
	}

	public void validate(EpisodeType type, int frequency, int threshFreq,
			double threshEntropy) {

	}
}
