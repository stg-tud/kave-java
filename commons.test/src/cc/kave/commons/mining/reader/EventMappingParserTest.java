package cc.kave.commons.mining.reader;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.utils.json.JsonUtils;

public class EventMappingParserTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	
	private EventMappingParser sut;
	
	@Before
	public void setup() {
		sut = new EventMappingParser(rootFolder.getRoot());
	}
	
	@Test
	public void readMapping() throws IOException {
		Event e1 = new Event();
		Event e2 = new Event();
		e2.setKind(EventKind.INVOCATION);

		List<Event> expected = new LinkedList<Event>();
		expected.add(e1);
		expected.add(e2);
		
		String jsonString = JsonUtils.toJson(expected);
		File file = getFilePath();

		FileUtils.writeStringToFile(file, jsonString);
		
		List<Event> actuals = sut.parse();
		
		assertEquals(expected, actuals);
	}
	
	private File getFilePath() {
		File fileName = new File(rootFolder.getRoot().getAbsolutePath() + "/eventMapping.txt");
		return fileName;
	}
}
