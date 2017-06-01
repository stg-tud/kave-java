package cc.kave.episodes.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.statistics.EventStreamGenerator2;
import cc.recommenders.datastructures.Tuple;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class CompareStreams {

	private ContextsParser parser;

	@Inject
	public CompareStreams(ContextsParser parser) {
		this.parser = parser;
	}

	public void compare() throws Exception {
		String path = "/Users/ervinacergani/Documents/EpisodeMining/dataSet/SSTs/";
		EventStreamGenerator2 gen = new EventStreamGenerator2(path);
		List<Event> stream = gen.run();

		parser.parse();

		System.out.println();
		
		structStream(stream);
		
		for (Event event : stream) {
//			System.out.println(event.getKind());
		}
	}
	
	private void structStream(List<Event> stream) {
		Map<Event, List<Event>> classStruct = Maps.newLinkedHashMap();
		Tuple<Event, List<Event>> className = null;
		
		for (Event event : stream) {
			if (event.getKind() == EventKind.TYPE) {
				if (className != null) {
					classStruct.put(className.getFirst(), className.getSecond());
				}
				className = Tuple.newTuple(event, Lists.newLinkedList());
			} else {
				className.getSecond().add(event);
			}
		}
		classStruct.put(className.getFirst(), className.getSecond());
		
		Set<ITypeName> types = Sets.newLinkedHashSet();
		
		for (Map.Entry<Event, List<Event>> entry : classStruct.entrySet()) {
			if (entry.getValue().isEmpty()) {
				types.add(entry.getKey().getType());
			}
		}
		System.out.printf("Number of empty classes: %d\n", types.size());
	}

	// public void compare() throws Exception {
	// String path =
	// "/Users/ervinacergani/Documents/EpisodeMining/dataSet/SSTs/";
	// EventStreamGenerator2 gen = new EventStreamGenerator2(path);
	// Map<Event, List<Event>> res1 = gen.run();
	//
	// Map<Event, List<Event>> res2 = parser.parse();
	//
	// System.out.println();
	// for (Map.Entry<Event, List<Event>> entry : res1.entrySet()) {
	// Event decl = entry.getKey();
	// if (!res2.containsKey(decl)) {
	// System.out.printf("%s: %s\n", decl.getKind().toString(),decl
	// .getMethod().toString());
	// print(entry.getValue());
	// System.out.println();
	// }
	// }
	// }

	private void print(List<Event> method) {
		for (Event event : method) {
			System.out.printf("%s: %s\n", event.getKind().toString(), event
					.getMethod().toString());
		}
	}
}
