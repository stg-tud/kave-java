/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.episodes.mining.reader;

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
