package cc.kave.episodes.eventstream;

import java.util.List;
import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class Filters {
	
	private int numRemovals = 0;
	private int numOverlaps = 0;
	private int numUnknowns = 0;
	private int numLocals = 0;

	public Map<Event, List<Event>> apply(Set<IMethodName> repoMethods,
			List<Event> stream) {
		Map<Event, List<Event>> streamStruct = getStructStream(stream);
		Map<Event, List<Event>> streamRemOverlaps = overlaps(repoMethods,
				streamStruct);
		Map<Event, List<Event>> streamRemUnknownLocals = unknownAndLocals(
				repoMethods, streamRemOverlaps);
		return streamRemUnknownLocals;
	}
	
	public int getNumRemovals() {
		return numRemovals;
	}
	
	public int getNumOverlaps() {
		return numOverlaps;
	}
	
	public int getNumUnknowns() {
		return numUnknowns;
	}
	
	public int getNumLocals() {
		return numLocals;
	}

	private Map<Event, List<Event>> getStructStream(
			List<Event> stream) {
		Map<Event, List<Event>> result = Maps.newLinkedHashMap();
		List<Event> method = Lists.newLinkedList();
		Event eventElement = null;

		for (Event event : stream) {
			if (event.getKind() == EventKind.METHOD_DECLARATION) {
				if (eventElement != null) {
					if ((!method.isEmpty()) && validMethod(method)) {
						result.put(eventElement, method);
					} else {
						numRemovals += method.size() + 1;
					}
				}
				eventElement = event;
				method = Lists.newLinkedList();
				continue;
			}
			method.add(event);
		}
		return result;
	}

	private Map<Event, List<Event>> overlaps(
			Set<IMethodName> repoMethods, Map<Event, List<Event>> stream) {
		Map<Event, List<Event>> result = Maps.newLinkedHashMap();
		Set<Event> seenMethods = Sets.newHashSet();

		for (Map.Entry<Event, List<Event>> entry : stream.entrySet()) {
			Event method = entry.getKey();
			if (repoMethods.contains(method.getMethod()) && seenMethods.add(method)) {
				result.put(method, entry.getValue());
			} else {
				numOverlaps += entry.getValue().size() + 1;
			}
		}
		return result;
	}

	private Map<Event, List<Event>> unknownAndLocals(
			Set<IMethodName> repoMethods, Map<Event, List<Event>> stream) {
		Map<Event, List<Event>> result = Maps.newLinkedHashMap();
		List<Event> method = Lists.newLinkedList();

		for (Map.Entry<Event, List<Event>> entry : stream.entrySet()) {
			for (Event event : entry.getValue()) {
				if (event.getMethod().isUnknown()) {
					numUnknowns++;
					continue;
				}
				if (isLocal(event)) {
					numLocals++;
					continue;
				}
				method.add(event);
			}
			if (validMethod(method)) {
				result.put(entry.getKey(), method);
			} else {
				numRemovals += method.size() + 1;
			}
			method = Lists.newLinkedList();
		}
		return result;
	}

	private boolean validMethod(List<Event> method) {
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
			System.out.printf("different localness for: %s\n", type);
		}
		return newVal;
	}
}
