package cc.kave.commons.mining.episodes;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;

import cc.kave.commons.mining.reader.EpisodeParser;
import cc.kave.commons.mining.reader.EventMappingParser;
import cc.kave.commons.model.persistence.EpisodeAsGraphWriter;
import cc.recommenders.exceptions.AssertionException;

public class EpisodeGraphGeneratorTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Mock
	private EpisodeParser episodeParser;
	@Mock
	private MaximalFrequentEpisodes episodeLearned;
	@Mock
	private EventMappingParser mappingParser;
	@Mock
	private EpisodeToGraphConverter graphConverter;
	@Mock
	private NoTransitivelyClosedEpisodes transitivityClosure;
	@Mock
	private EpisodeAsGraphWriter writer;
	
	private EpisodeGraphGenerator sut;
	
	@Before
	public void setup() {
		sut = new EpisodeGraphGenerator(rootFolder.getRoot(), episodeParser, episodeLearned, mappingParser, transitivityClosure, writer, graphConverter);
	}
	
	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Episode-miner folder does not exist");
		sut = new EpisodeGraphGenerator(new File("does not exist"), episodeParser, episodeLearned, mappingParser, transitivityClosure, writer, graphConverter);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Episode-miner folder is not a folder, but a file");
		sut = new EpisodeGraphGenerator(file, episodeParser, episodeLearned, mappingParser, transitivityClosure, writer, graphConverter);
	}
}
