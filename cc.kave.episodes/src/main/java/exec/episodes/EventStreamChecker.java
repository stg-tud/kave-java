package exec.episodes;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;

public class EventStreamChecker {

	private EventStreamIo streamIo;

	@Inject
	public EventStreamChecker(EventStreamIo streamIo) {
		this.streamIo = streamIo;
	}

	private Map<List<Fact>, Integer> methodOccs = Maps.newHashMap();

	public void duplicates(int frequency) throws IOException {
		List<List<Fact>> stream = streamIo.parseStream(frequency);

		for (List<Fact> method : stream) {
			boolean isContained = false;

			for (Map.Entry<List<Fact>, Integer> entry : methodOccs.entrySet()) {
				isContained = assertLists(method, entry.getKey());

				if (isContained) {
					int counter = methodOccs.get(entry.getKey());
					methodOccs.put(entry.getKey(), counter + 1);
					
					Logger.log("Cur: %s", entry.getKey().toString());
					Logger.log("New: %s", method.toString());
					Logger.log("");
					break;
				}
			}
			if (!isContained) {
				methodOccs.put(method, 1);
			}
		}
	}

	private boolean assertLists(List<Fact> method, List<Fact> contained) {
		if (method.size() != contained.size()) {
			return false;
		}
		Iterator<Fact> itE = method.iterator();
		Iterator<Fact> itA = contained.iterator();
		while (itE.hasNext()) {
			if (itE.next().getFactID() != itA.next().getFactID()) {
				return false;
			}
		}
		return true;
	}
}
