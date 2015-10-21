package cc.kave.commons.mining.reader;

import java.io.File;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

import com.google.common.reflect.TypeToken;

import cc.kave.commons.model.episodes.Event;
import cc.kave.commons.utils.json.JsonUtils;

public class EventMappingParser {

	public List<Event> parse(File file) {

		@SuppressWarnings("serial")
		Type listType = new TypeToken<LinkedList<Event>>() {
		}.getType();
		List<Event> events = JsonUtils.fromJson(file, listType);
		return events;
	}
}
