package cc.kave.episodes.evaluation.queries;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import cc.recommenders.exceptions.AssertionException;

public class QueryGenerationTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	
	private static final QStrategies STRATEGY = QStrategies.TOPBOTTOM;
	
	private QueryGeneration sut;
	
	@Before
	public void setup() {
		sut = new QueryGeneration(rootFolder.getRoot());
	}
	
	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Events folder does not exist!");
		sut = new QueryGeneration(new File("does not exist"));
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Events is not a folder, but a file!");
		sut = new QueryGeneration(file);
	}
}
