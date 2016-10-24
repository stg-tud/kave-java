package cc.kave.episodes.mining.evaluation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cc.kave.episodes.io.MappingParser;
import cc.kave.episodes.io.StreamParser;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.io.Logger;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

public class MethodSize {

	private StreamParser streamParser;
	private MappingParser mappingParser;

	@Inject
	public MethodSize(StreamParser streamParser, MappingParser mappingParser) {
		this.streamParser = streamParser;
		this.mappingParser = mappingParser;
	}

	public void identifier(int numberOfRepos, int methodLength) {
		List<List<Fact>> stream = streamParser.parse(numberOfRepos);
		List<Event> events = mappingParser.parse(numberOfRepos);
		
//		Logger.log("Event: %s", events.get(16529));
//		Logger.log("Event: %s", events.get(29904));
//		Logger.log("Number of methods is %d", stream.size());
		Logger.log("Number of unique events is: %d", events.size());
		
		int md = 0;
		for (Event event : events) {
			if (event.getKind() == EventKind.METHOD_DECLARATION) {
				md++;
			}
		}
		Logger.log("Number of method declarations is %d out of %d", md, events.size());
		
		checkMethodSize(stream, events, methodLength);
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
//				fileName = event.getMethod().getIdentifier();
				break;
			}
		}
		return fileName;
	}
}
