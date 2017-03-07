package cc.kave.episodes.postprocessor;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.mockito.Mock;

import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.mining.evaluation.PatternsValidation;
import cc.kave.episodes.mining.graphs.EpisodeToGraphConverter;
import cc.kave.episodes.model.Episode;
import cc.recommenders.datastructures.Tuple;

public class SpecificPatternsTest {

	@Mock
	private PatternsValidation validation;
	@Mock
	private EventStreamIo eventsStream;
	@Mock
	private EpisodeToGraphConverter graphConverter;
	
	private Map<Integer, Set<Tuple<Episode, Boolean>>> patterns;
	
	@Before
	public void setup() {
		
	}
}
