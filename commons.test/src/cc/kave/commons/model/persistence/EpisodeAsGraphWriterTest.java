package cc.kave.commons.model.persistence;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import cc.kave.commons.mining.reader.EpisodeParser;
import cc.recommenders.exceptions.AssertionException;

public class EpisodeAsGraphWriterTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private EpisodeAsGraphWriter sut;

	@Before
	public void setup() {
		sut = new EpisodeAsGraphWriter(rootFolder.getRoot());
	}
	
	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Episode-miner folder does not exist");
		sut = new EpisodeAsGraphWriter(new File("does not exist"));
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Episode-miner folder is not a folder, but a file");
		sut = new EpisodeAsGraphWriter(file);
	}

	@Test
	public void writerTest() {
		
	}
	
	private File getFilePath() {
		String dirPath = rootFolder.getRoot().getAbsolutePath() + "/graphs/";
		new File(dirPath).mkdirs();
		File fileName = new File(dirPath + "/output.txt");
		return fileName;
	}
}
