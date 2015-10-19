package cc.kave.commons.mining.reader;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.lang.reflect.Type;
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

import com.google.common.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.EventKind;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.utils.json.JsonUtils;
import cc.recommenders.exceptions.AssertionException;

public class EventMappingParserTest {

	@Rule
	public TemporaryFolder rootFolder = new TemporaryFolder();
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	private EventMappingParser sut;
	
	@Before
	public void setup() {
		sut = new EventMappingParser(rootFolder.getRoot());
	}

	@Test
	public void cannotBeInitializedWithNonExistingFolder() {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Event mapping folder does not exist!");
		sut = new EventMappingParser(new File("does not exist"));
	}

	@Test
	public void cannotBeInitializedWithFile() throws IOException {
		File file = rootFolder.newFile("a");
		thrown.expect(AssertionException.class);
		thrown.expectMessage("Event mapping folder is not a folder, but a file!");
		sut = new EventMappingParser(file);
	}
	
	@Test
	public void readMapping() throws IOException {
		TypeName typeName = mock(TypeName.class);
		MethodName methodName = mock(MethodName.class);
		JsonUtils json = mock(JsonUtils.class);

		Event e1 = new Event();
		Event e2 = new Event();
		e2.setKind(EventKind.INVOCATION);
		e2.setType(typeName);
		e2.setMethod(methodName);

		List<Event> expected = new LinkedList<Event>();
		expected.add(e1);
		expected.add(e2);

//		StringBuilder sb = new StringBuilder();
//		sb.append(e1.toString());
//		sb.append(e2.toString());
//
//		String content = sb.toString();
		
		File file = getFilePath();
		Type listType = new TypeToken<LinkedList<Event>>() { }.getType();
//
//		Type writeType = JsonUtils.toJson(content, file);
//			FileUtils.writeStringToFile(file, content, true);
		
		doCallRealMethod().when(json).toJson(expected);
		
		String jsonWrite = json.toJson(expected);
		FileUtils.writeStringToFile(file, jsonWrite);
		
		doCallRealMethod().when(json).fromJson(file, listType);
		
		List<Event> actuals = sut.parse();
		
		verify(json).fromJson(file, listType);

		assertEquals(expected, actuals);
	}
	
	private File getFilePath() {
		File fileName = new File(rootFolder.getRoot().getAbsolutePath() + "/eventMapping.txt");
		return fileName;
	}
}
