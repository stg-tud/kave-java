package cc.kave.episodes.data;

import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
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
		List<Tuple<Event, List<Event>>> stream1 = classStruct(stream);

		System.out.println();

		typesCounter(stream1);
		localCtxCounter(stream);

//		parser.parse();
	}

	private void localCtxCounter(List<Event> stream) {
		Set<Event> ctxFirst = Sets.newHashSet();
		Set<Event> ctxSuper = Sets.newHashSet();

		int numLocalFirst = 0;
		int numLocalSuper = 0;

		for (Event event : stream) {
			if (event.getKind() == EventKind.TYPE) {
				continue;
			}
			if (isLocal(event)) {
				if (event.getKind() == EventKind.FIRST_DECLARATION) {
					ctxFirst.add(event);
					numLocalFirst++;
					continue;
				}
				if (event.getKind() == EventKind.SUPER_DECLARATION) {
					ctxSuper.add(event);
					numLocalSuper++;
				}
			}
		}
		System.out.printf("ctxFirst locals: %d (%d unique)\n", numLocalFirst, ctxFirst.size());
		System.out.printf("ctxSuper locals: %d (%d unique)\n", numLocalSuper, ctxSuper.size());
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
}
