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

		System.out.println();

		List<Tuple<Event, List<Event>>> stream1 = classStruct(stream);
		typesCounter(stream1);

		List<Tuple<Event, List<Event>>> stream2 = parser.parse();
	}

	private void typesCounter(List<Tuple<Event, List<Event>>> stream1) {

		Set<ITypeName> emptyClasses = Sets.newHashSet();
		Set<ITypeName> classes = Sets.newHashSet();

		int numEmpties = 0;
		int numClasses = 0;
		for (Tuple<Event, List<Event>> tuple : stream1) {
			ITypeName type = tuple.getFirst().getType();

			if (tuple.getSecond().size() == 0) {
				emptyClasses.add(type);
				numEmpties++;
				continue;
			}

			for (Event event : tuple.getSecond()) {
				if (event.getKind() == EventKind.METHOD_DECLARATION) {
					if (!event.getMethod().getDeclaringType().equals(type)) {
						System.out.println("Type: " + type.getFullName());
						System.out.println("Method: "
								+ event.getMethod().getDeclaringType()
										.getFullName());
					}
				}
			}
			classes.add(type);
			numClasses++;
		}
		System.out.printf("non-empty types: %d (%d unique)\n", numClasses,
				classes.size());
		System.out.printf("empty types: %d (%d unique)\n", numEmpties,
				emptyClasses.size());
	}

	private void convCompare(List<Event> stream, Map<Event, List<Event>> stream1) {
		int numTypes = 0;
		for (Event event : stream) {
			System.out.println(event.getKind());
			if (event.getKind() == EventKind.TYPE) {
				numTypes++;
			}
			if (numTypes == 10) {
				break;
			}
		}

		numTypes = 0;
		System.out.println();
		for (Map.Entry<Event, List<Event>> entry : stream1.entrySet()) {
			System.out.println(entry.toString());
			numTypes++;
			if (numTypes == 10) {
				break;
			}
		}
	}

	private List<Tuple<Event, List<Event>>> classStruct(List<Event> stream) {
		List<Tuple<Event, List<Event>>> classStruct = Lists.newLinkedList();
		Tuple<Event, List<Event>> className = null;

		for (Event event : stream) {
			if (event.getKind() == EventKind.TYPE) {
				if (className != null) {
					classStruct.add(className);
				}
				className = Tuple.newTuple(event, Lists.newLinkedList());
			} else {
				className.getSecond().add(event);
			}
		}
		classStruct.add(className);
		return classStruct;
	}

	private void compareTypes(Map<Event, List<Event>> stream1,
			List<Tuple<Event, List<Event>>> stream2) {

		Set<ITypeName> typesStream = Sets.newHashSet();
		System.out.println("Printing methods comming from empty classes: ...");
		for (Tuple<Event, List<Event>> tuple : stream2) {
			ITypeName type = tuple.getFirst().getMethod().getDeclaringType();
			typesStream.add(type);
		}

		Set<ITypeName> typesEmpty = Sets.newHashSet();
		Set<ITypeName> typesFull = Sets.newHashSet();

		System.out.println("Printing empty classes ...");
		for (Map.Entry<Event, List<Event>> entry : stream1.entrySet()) {
			if ((entry.getValue().isEmpty())
					&& (typesStream.contains(entry.getKey().getType()))) {
				typesEmpty.add(entry.getKey().getType());
				System.out.println(entry.toString());
			} else {
				typesFull.add(entry.getKey().getType());
			}
		}

		System.out.printf("Number of empty classes: %d\n", typesEmpty.size());
		System.out.println("Number of full classes: " + typesFull.size());
	}
}
