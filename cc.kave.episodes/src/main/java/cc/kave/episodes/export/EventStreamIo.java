/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package cc.kave.episodes.export;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.reflect.TypeToken;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.model.episodes.Events;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.utils.json.JsonUtils;
import cc.recommenders.io.Logger;

public class EventStreamIo {
	
	private static final String DUMMY_METHOD_NAME = "[You, Can] [Safely, Ignore].ThisDummyValue()";
	private static final IMethodName DUMMY_METHOD = MethodName.newMethodName(DUMMY_METHOD_NAME);
	public static final Event DUMMY_EVENT = Events.newContext(DUMMY_METHOD);

	public static final double DELTA = 0.001;
	public static final double TIMEOUT = 0.5;
	
	public static void write(List<Event> stream, String fileStream, String fileMapping) {
		try {
			StreamData sm = new StreamData();
			sm = EventsFilter.filter(stream);
			Logger.log("Writing stream data file!");
			FileUtils.writeStringToFile(new File(fileStream), sm.getStreamString());
			Logger.log("Writing mapping data file!");
			JsonUtils.toJson(sm.getMappingData().keySet(), new File(fileMapping));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static List<Event> readMapping(String path) {
		@SuppressWarnings("serial")
		Type type = new TypeToken<List<Event>>() {
		}.getType();
		return JsonUtils.fromJson(new File(path), type);
	}
}