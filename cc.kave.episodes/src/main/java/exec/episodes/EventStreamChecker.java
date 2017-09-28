package exec.episodes;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.events.Fact;

import com.google.common.collect.Maps;

public class EventStreamChecker {

	private EventStreamIo streamIo;

	@Inject
	public EventStreamChecker(EventStreamIo streamIo) {
		this.streamIo = streamIo;
	}
	
	public void duplicates(int frequency) throws IOException {
		List<List<Fact>> stream = streamIo.parseStream(frequency);
		
		Map<List<Fact>, Integer> methodOccs = Maps.newHashMap();
		
		for (List<Fact> method : stream) {
			
		}
	}
}
