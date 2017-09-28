package cc.kave.episodes.eventstream;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.utils.naming.ProjectNormalizationNameRewriter;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Events;
import cc.kave.episodes.statistics.EventsStatistics;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class Filters {

	private Set<IMethodName> seenMethods = Sets.newHashSet();

	public List<Tuple<Event, List<Event>>> getStructStream(List<Event> stream) {
		List<Tuple<Event, List<Event>>> result = Lists.newLinkedList();
		Tuple<Event, List<Event>> method = null;

		for (Event event : stream) {
			if (event.getKind() == EventKind.METHOD_DECLARATION) {
				if ((method != null) && validMethod(method.getSecond())) {
					result.add(method);
				}
				method = Tuple.newTuple(event, Lists.newLinkedList());
			} else {
				method.getSecond().add(event);
			}
		}
		if ((method != null) && validMethod(method.getSecond())) {
			result.add(method);
		}
		return result;
	}

	public List<Tuple<Event, List<Event>>> locals(
			List<Tuple<Event, List<Event>>> stream) {
		List<Tuple<Event, List<Event>>> result = Lists.newLinkedList();
		List<Event> method = Lists.newLinkedList();

		for (Tuple<Event, List<Event>> tuple : stream) {
			for (Event event : tuple.getSecond()) {
				if (!isLocal(event)) {
					method.add(event);
				}
			}
			if (validMethod(method)) {
				result.add(Tuple.newTuple(tuple.getFirst(), method));
			}
			method = Lists.newLinkedList();
		}
		return result;
	}

	public List<Tuple<Event, List<Event>>> unknowns(
			List<Tuple<Event, List<Event>>> stream) {
		List<Tuple<Event, List<Event>>> result = Lists.newLinkedList();

		for (Tuple<Event, List<Event>> tuple : stream) {
			List<Event> method = Lists.newLinkedList();
			for (Event event : tuple.getSecond()) {
				if (!event.getMethod().isUnknown()) {
					method.add(event);
				}
			}
			if (validMethod(method)) {
				result.add(Tuple.newTuple(tuple.getFirst(), method));
			}
		}
		return result;
	}

	public List<Tuple<Event, List<Event>>> overlaps(
			List<Tuple<Event, List<Event>>> stream) {
		List<Tuple<Event, List<Event>>> result = Lists.newLinkedList();

		for (Tuple<Event, List<Event>> tuple : stream) {
			Event normEvent = normalize(tuple.getFirst());
			IMethodName methodName = normEvent.getMethod();

			if (seenMethods.add(methodName)) {
				result.add(Tuple.newTuple(normEvent, tuple.getSecond()));
			}
		}
		return result;
	}

	private Event normalize(Event event) {
		ProjectNormalizationNameRewriter rw = new ProjectNormalizationNameRewriter();

		IMethodName normMethod = rw.rewrite(event.getMethod());
		Event normEvent = Events.newElementContext(normMethod);

		return normEvent;
	}

	public List<Tuple<Event, List<Event>>> freqEvents(
			List<Tuple<Event, List<Event>>> stream, int frequency) {
		List<Tuple<Event, List<Event>>> results = Lists.newLinkedList();
		Map<Event, Integer> occurrences = EventsStatistics
				.getFrequencies(stream);

		for (Tuple<Event, List<Event>> tuple : stream) {
			List<Event> method = Lists.newLinkedList();
			for (Event event : tuple.getSecond()) {
				if (occurrences.get(event) >= frequency) {
					method.add(event);
				}
			}
			if (validMethod(method) && (method.size() > 1)) {
				results.add(Tuple.newTuple(tuple.getFirst(), method));
			}
		}
		return results;
	}

	private boolean validMethod(List<Event> method) {
		if (method.size() == 0) {
			return false;
		}
		for (Event event : method) {
			if (event.getKind() == EventKind.INVOCATION) {
				return true;
			}
		}
		return false;
	}

	private boolean isLocal(Event e) {
		// predefined types have always an unknown version, but come
		// from mscorlib, so they should be included
		boolean oldVal = false;
		boolean newVal = false;
		ITypeName type = e.getMethod().getDeclaringType();
		IAssemblyName asm = type.getAssembly();
		if (!asm.getName().equals("mscorlib") && asm.getVersion().isUnknown()) {
			oldVal = true;
		}
		if (asm.isLocalProject()) {
			newVal = true;
		}
		if (oldVal != newVal) {
			System.err.printf("different localness for: %s\n", type);
		}
		return newVal;
	}
}
