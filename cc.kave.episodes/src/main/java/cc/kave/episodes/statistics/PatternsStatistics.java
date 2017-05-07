package cc.kave.episodes.statistics;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;

public class PatternsStatistics {

	private EventStreamIo eventStreamIo;
	
	private Map<IMethodName, Integer> ctxFirst = Maps.newLinkedHashMap();
	private Map<IMethodName, Integer> ctxSuper = Maps.newLinkedHashMap();
	private Map<IMethodName, Integer> ctxElement = Maps.newLinkedHashMap();
	private Map<IMethodName, Integer> invExpr = Maps.newLinkedHashMap();

	@Inject
	public PatternsStatistics(EventStreamIo streamIo) {
		this.eventStreamIo = streamIo;
	}

	public void generate(int frequency, int foldNum) {
		List<Tuple<Event, List<Fact>>> stream = eventStreamIo.parseStream(
				frequency, foldNum);
		List<Event> events = eventStreamIo.readMapping(frequency, foldNum);
		
		for (Tuple<Event, List<Fact>> method : stream) {
			Event elementCtx = method.getFirst();
			if (elementCtx.getKind() != EventKind.METHOD_DECLARATION) {
				Logger.log("Invalid method declaration element!");
			} else {
				addEvent(elementCtx, ctxElement);
			}
			for (Fact fact : method.getSecond()) {
				Event event = events.get(fact.getFactID());
				if (event.getKind() == EventKind.FIRST_DECLARATION) {
					addEvent(event, ctxFirst);
					continue;
				}
				if (event.getKind() == EventKind.SUPER_DECLARATION) {
					addEvent(event, ctxSuper);
					continue;
				}
				if (event.getKind() == EventKind.INVOCATION) {
					addEvent(event, invExpr);
					continue;
				}
				Logger.log("Invalid event in event stream!");
			}
		}
		printStatistics();
		Logger.log("Number of full methods in event stream: %d", stream.size());
		Logger.log("Number of unique events in event stream: %d", events.size());
	}

	private void printStatistics() {
		Logger.log("ctxFirst: %d (%d unique)", getOccs(ctxFirst), ctxFirst.size());
		Logger.log("ctxSuper: %d (%d unique)", getOccs(ctxSuper), ctxSuper.size());
		Logger.log("ctxElem: %d (%d unique)", getOccs(ctxElement), ctxElement.size());
		Logger.log("invExpr: %d (%d unique)\n", getOccs(invExpr), invExpr.size());
	}

	private int getOccs(Map<IMethodName, Integer> cumulator) {
		int occurrences = 0;
		for (Map.Entry<IMethodName, Integer> entry : cumulator.entrySet()) {
			occurrences += entry.getValue();
		}
		return occurrences;
	}

	private void addEvent(Event event, Map<IMethodName, Integer> cumulator) {
		IMethodName methodName = event.getMethod();
		if (cumulator.containsKey(methodName)) {
			int counter = cumulator.get(methodName);
			cumulator.put(methodName, counter + 1);
		} else {
			cumulator.put(methodName, 1);
		}
	}
}
