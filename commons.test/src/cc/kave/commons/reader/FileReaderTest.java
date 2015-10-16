package cc.kave.commons.reader;

import static org.junit.Assert.*;

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

import cc.recommenders.exceptions.AssertionException;
import junit.framework.Assert;

public class FileReaderTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private FileReader sut;
	
	@Before
	public void setup() {
		sut = new FileReader();
	}
	
	@Test
	public void nonExistingIntFilesThrowException() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("File does not exist");
		sut.readFile(new File("does not exist"));
	}
	
	@Test
	public void nonExistingParentIntFilesThrowException() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("File does not exist");
		sut.readFile(new File("folder/does not exist"));
	}
	
	@Test
	public void youCannotPassFoldersToReadInt() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("File is not a file, but a directory");
		sut.readFile(rootFolder.getRoot());
	}
	
	@Test
	public void oneNodeEpisodes() throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("1,0.001\n");
		sb.append("2,0.002\n");
		sb.append("3,0.003\n");
		sb.append("4,0.004\n");
		sb.append("5,0.005\n");
		sb.append("6,0.006\n");
		sb.append("7,0.007\n");
		sb.append("8,0.008\n");
		sb.append("9,0.009\n");
		sb.append("10,0.01\n");

		String content = sb.toString();

		File file = getFilePath();

		try {
			FileUtils.writeStringToFile(file, content, true);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		List<String> expected = new LinkedList<String>();
		expected.add("1,0.001");
		expected.add("2,0.002");
		expected.add("3,0.003");
		expected.add("4,0.004");
		expected.add("5,0.005");
		expected.add("6,0.006");
		expected.add("7,0.007");
		expected.add("8,0.008");
		expected.add("9,0.009");
		expected.add("10,0.01");
		
		List<String> actuals = sut.readFile(file);
		
		assertEquals(expected, actuals);
	}
	
	private File getFilePath() {
		File fileName = new File(rootFolder.getRoot().getAbsolutePath() + "/eventstream.txt");
		return fileName;
	}
}
