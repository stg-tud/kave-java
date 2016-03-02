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
package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.testutils.LoggerUtils.assertLogContains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.LinkedList;
import java.util.zip.ZipException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Sets;

import cc.kave.commons.model.episodes.Event;
import cc.kave.episodes.mining.reader.EventMappingParser;
import cc.kave.episodes.mining.reader.ValidationContextsParser;
import cc.kave.episodes.model.Episode;
import cc.recommenders.io.Logger;

public class TargetsCategorizationTest {

	@Mock
	private EventMappingParser mappingParser;
	@Mock
	private ValidationContextsParser validationParser;
	
	private LinkedList<Event> events;
	
	private TargetsCategorization sut;
	
	@Before
	public void setup() throws ZipException, IOException {
		Logger.reset();
		Logger.setCapturing(true);
		
		MockitoAnnotations.initMocks(this);
		
		events = new LinkedList<Event>();
		
		sut = new TargetsCategorization(mappingParser, validationParser);
		
		when(mappingParser.parse()).thenReturn(events);
		when(validationParser.parse(events)).thenReturn(Sets.newHashSet(createTarget("11"),
				createTarget("11", "12", "11>12"), createTarget("11", "13", "11>13"),
				createTarget("11", "12", "13", "11>12", "11>13", "12>13")));
	}
	
	@After
	public void teardown() {
		Logger.reset();
	}
	
	@Test
	public void logTest() throws ZipException, IOException {
		Logger.clearLog();
		
		sut.categorize();
		
		verify(mappingParser).parse();
		verify(validationParser).parse(events);
		
		assertLogContains(0, "#Invocations\t#Targets\n");
		assertLogContains(1, "0\t1\n");
		assertLogContains(2, "1\t2\n");
		assertLogContains(3, "2\t1\n");
	}
	
	private Episode createTarget(String...strings) {
		Episode target = new Episode();
		target.addStringsOfFacts(strings);
		return target;
	}
}
