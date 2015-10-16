package cc.kave.commons.mining.reader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import cc.kave.commons.model.episodes.Episode;
import cc.recommenders.exceptions.AssertionException;

public class QueryParserTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private FileReader reader;
	private QueryParser sut;

	@Before
	public void setup() {
		reader = mock(FileReader.class);
		sut = new QueryParser(rootFolder.getRoot(), reader);
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Event stream folder does not exist!");
		sut = new QueryParser(new File("does not exist"), reader);
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Event stream folder is not a folder, but a file!");
		sut = new QueryParser(file, reader);
	}
	
	@Test
	public void oneQueryWithNonStopEventAt() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("1,0.000\n");
		sb.append("2,0.501\n");
		sb.append("3,0.502\n");

		String content = sb.toString();

		File file = getFilePath();

		try {
			FileUtils.writeStringToFile(file, content, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		List<Episode> expected = new LinkedList<Episode>();		
		Episode episode = new Episode();
		episode.addStringsOfFacts("2", "3", "1", "2>3", "2>1", "3>1");
		episode.setFrequency(1);
		episode.setNumEvents(3);
		
		expected.add(episode);
		
		doCallRealMethod().when(reader).readFile(eq(file)); 
		
		List<Episode> actual = sut.parse();
		
		verify(reader).readFile(file);

		assertEquals(expected, actual);
	}
	
	@Test
	public void oneQueryWithStopEventAtEnd() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("2,0.501\n");
		sb.append("3,0.502\n");
		sb.append("1,0.503\n");

		String content = sb.toString();

		File file = getFilePath();

		try {
			FileUtils.writeStringToFile(file, content, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		List<Episode> expected = new LinkedList<Episode>();		
		Episode episode = new Episode();
		episode.addStringsOfFacts("2", "3", "1", "2>3", "2>1", "3>1");
		episode.setFrequency(1);
		episode.setNumEvents(3);
		
		expected.add(episode);
		
		doCallRealMethod().when(reader).readFile(eq(file)); 
		
		List<Episode> actual = sut.parse();
		
		verify(reader).readFile(file);

		assertEquals(expected, actual);
	}
	
	@Test
	public void twoQuery() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("1,0.000\n");
		sb.append("2,0.501\n");
		sb.append("3,0.502\n");
		sb.append("4,0.503\n");
		sb.append("1,0.504\n");
		sb.append("11,1.005\n");
		sb.append("12,1.006\n");
		sb.append("10,1.007\n");

		String content = sb.toString();

		File file = getFilePath();

		try {
			FileUtils.writeStringToFile(file, content, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		List<Episode> expected = new LinkedList<Episode>();		
		Episode episode = new Episode();
		episode.addStringsOfFacts("2", "3", "4", "1", "2>3", "2>4", "2>1", "3>4", "3>1", "4>1");
		episode.setFrequency(1);
		episode.setNumEvents(4);
		
		expected.add(episode);
		
		episode = new Episode();
		episode.addStringsOfFacts("11", "12", "10", "1", "11>12", "11>10", "11>1", "12>10", "12>1", "10>1");
		episode.setFrequency(1);
		episode.setNumEvents(4);
		
		expected.add(episode);
		
		doCallRealMethod().when(reader).readFile(eq(file)); 
		
		List<Episode> actual = sut.parse();
		
		verify(reader).readFile(file);

		assertEquals(expected, actual);
	}
	
	private File getFilePath() {
		File fileName = new File(rootFolder.getRoot().getAbsolutePath() + "/eventstream.txt");
		return fileName;
	}
}
