package exec.episodes;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class EventStreamChecker {

	private EventStreamIo streamIo;

	@Inject
	public EventStreamChecker(EventStreamIo streamIo) {
		this.streamIo = streamIo;
	}

	private Map<List<Fact>, Set<IMethodName>> methodOccs = Maps.newHashMap();

	public void duplicates(int frequency) throws IOException {
		List<Tuple<IMethodName, List<Fact>>> stream = streamIo
				.readStreamObject(frequency);

		for (Tuple<IMethodName, List<Fact>> tuple : stream) {
			boolean isContained = false;

			for (Map.Entry<List<Fact>, Set<IMethodName>> entry : methodOccs
					.entrySet()) {
				isContained = assertLists(tuple.getSecond(), entry.getKey());

				if (isContained) {
					methodOccs.get(entry.getKey()).add(tuple.getFirst());

					Logger.log("Cur: %s", entry.getKey().toString());
					Logger.log("New: %s", tuple.getSecond().toString());
					Logger.log("");
					break;
				}
			}
			if (!isContained) {
				methodOccs.put(tuple.getSecond(),
						Sets.newHashSet(tuple.getFirst()));
			}
		}
		Logger.log("Ouptutting duplicate code occurrences ...");
		for (Map.Entry<List<Fact>, Set<IMethodName>> entry : methodOccs
				.entrySet()) {
			Logger.log("Method: %s", entry.getKey().toString());
			
			for (IMethodName methodNames : entry.getValue()) {
				Logger.log("%s", methodNames.getIdentifier());
			}
			Logger.log("");
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
