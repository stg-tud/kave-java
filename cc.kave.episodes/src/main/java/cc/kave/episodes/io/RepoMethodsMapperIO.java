package cc.kave.episodes.io;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipException;

import javax.inject.Inject;
import javax.inject.Named;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.utils.json.JsonUtils;
import cc.kave.episodes.eventstream.EventStreamGenerator;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class RepoMethodsMapperIO {

	private File eventsFile;
	
	@Inject
	public RepoMethodsMapperIO(@Named("events") File folder) {
		assertTrue(folder.exists(), "Events folder does not exist");
		assertTrue(folder.isDirectory(),
				"Events is not a folder, but a file");
		this.eventsFile = folder;
	}
	
	public void writer(Map<String, EventStreamGenerator> repos) throws ZipException, IOException {
		Map<String, Set<IMethodName>> repoCtx = Maps.newLinkedHashMap();

		for (Map.Entry<String, EventStreamGenerator> entry : repos.entrySet()) {
			List<Event> events = entry.getValue().getEventStream();
			Set<IMethodName> ctx = Sets.newLinkedHashSet();

			for (Event e : events) {
				if (e.getKind() == EventKind.METHOD_DECLARATION) {
					ctx.add(e.getMethod());
				}
			}
			repoCtx.put(entry.getKey(), ctx);
			JsonUtils.toJson(repoCtx, new File(repoMethodsPath()));
		}
	}
	
	public Map<String, Set<IMethodName>> reader() throws JsonParseException, JsonMappingException, IOException {
		HashMap<String, Set<IMethodName>> result =
		        new ObjectMapper().readValue(new File(repoMethodsPath()), HashMap.class);
		return result;
//		@SuppressWarnings("serial")
//		Type type = new TypeToken<Map<String, Set<IMethodName>>>() {
//		}.getType();
//		return JsonUtils.fromJson(new File(repoMethodsPath()), type);
	}
	
	private String repoMethodsPath() {
		String fileName = eventsFile.getAbsolutePath() + "/repoMethodsMapper.json";
		return fileName;
	}
}
