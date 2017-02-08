package cc.kave.episodes.mining.evaluation;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class MethodSize {

	private EventStreamIo eventStreamIo;

	public void statistics(int frequency, int methodLength) {
		List<Tuple<Event, List<Fact>>> stream = eventStreamIo.parseStream(frequency);
		int streamLength = calcStreamLength(stream);
		
		List<Event> events = eventStreamIo.readMapping(frequency);

		Set<Event> uniqMethods = numCtxs(stream);
		assertTrue(uniqMethods.size() <= stream.size(), "Error in converting List to Set!");

		Logger.log("Number of methods in stream data is %d", stream.size());
		Logger.log("Number of events in the event stream is %d", streamLength);
		Logger.log("Number of unique events is %d", events.size());
		Logger.log("Number of enclosing methods is %d", uniqMethods.size());

//		checkMethodSize(stream, events, methodLength);
	}

	private int calcStreamLength(List<Tuple<Event, List<Fact>>> stream) {
		int length = 0;
		
		for (Tuple<Event, List<Fact>> method : stream) {
			length += method.getSecond().size();
		}
		return length;
	}

	private Set<Event> numCtxs(List<Tuple<Event, List<Fact>>> methods) {
		Set<Event> result = Sets.newLinkedHashSet();
		
		for (Tuple<Event, List<Fact>> m : methods) {
			result.add(m.getFirst());
		}
		return result;
	}

	private void checkMethodSize(List<List<Fact>> stream, List<Event> events,
			int methodLength) {
		Map<List<Fact>, Integer> methods = Maps.newLinkedHashMap();
		int longMethodSize = 0;
		List<Fact> longestMethod = new LinkedList<Fact>();
		for (List<Fact> method : stream) {
			if (method.size() >= methodLength) {
				methods.put(method, method.size());
			}
			if (method.size() > longMethodSize) {
				longestMethod.clear();
				longestMethod.addAll(method);
				longMethodSize = method.size();
			}
		}
		printLongestMethod(longestMethod, events, longMethodSize);
		printMethodsAboveThreshold(methods, events, methodLength);
	}

	private void printMethodsAboveThreshold(Map<List<Fact>, Integer> methods,
			List<Event> events, int methodLength) {
		if (!methods.isEmpty()) {
			Logger.log("Methods with more than %d events are: %d",
					methodLength, methods.size());
			for (Map.Entry<List<Fact>, Integer> m : methods.entrySet()) {
				String methodName = getMethodName(m.getKey(), events);
				Logger.log("Method %s\thas %d events", methodName, m.getValue());
			}
		}
	}

	private void printLongestMethod(List<Fact> longestMethod,
			List<Event> events, int longMethodSize) {
		String methodName = getMethodName(longestMethod, events);
		Logger.log("Size of the largest method is: %d", longMethodSize);
		Logger.log("The longest method is: %s", methodName);

	}

	private String getMethodName(List<Fact> method, List<Event> events) {
		String fileName = "";
		for (Fact fact : method) {
			Event event = events.get(fact.getFactID());
			if (event.getKind() == EventKind.METHOD_DECLARATION) {
				fileName = event.getMethod().getDeclaringType().getFullName()
						+ "." + event.getMethod().getName();
				// fileName = event.getMethod().getIdentifier();
				break;
			}
		}
		return fileName;
	}
}
