package cc.kave.episodes.data;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.statistics.EventStreamGenerator2;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class CompareStreams {

	private ContextsParser parser;

	@Inject
	public CompareStreams(ContextsParser parser) {
		this.parser = parser;
	}

	private String name0 = "[p:string] [p:object].ToString()";
	private String name1 = "Arp.Generator.Preprocessing.Impl.AttributesGroupGenerationInfo, Arp.Generator]..init()";
	private String name2 = "Arp.Generator.Preprocessing.Impl.PreprocesingVisitor, Arp.Generator]..init()";

	private IMethodName ctxSuper = Names.newMethod(name0);
	private IMethodName ctxElem1 = Names.newMethod(name1);
	private IMethodName ctxElem2 = Names.newMethod(name2);

	public void compare() throws Exception {
		String path = "/Users/ervinacergani/Documents/EpisodeMining/dataSet/SSTs/";
		EventStreamGenerator2 gen = new EventStreamGenerator2(path);
		List<Event> stream = gen.run();
		List<Tuple<Event, List<Event>>> streamSeb = classStruct(stream);
		Map<Event, List<Event>> stream1 = methodStruct(stream);

		System.out.println();

		Set<ITypeName> types1 = typesCounter(streamSeb);
		localCtxCounter(stream);

		List<Tuple<Event, List<Event>>> streamEr = parser.parse();
		Map<Event, List<Event>> stream2 = mapConverter(streamEr);

		// compareTypeDecls(types1, streamEr);
		compareSupers(stream1, stream2);
		// compareCtxElems(stream1, stream2);
		// compareStreams(stream1, stream2);
	}

	private void compareSupers(Map<Event, List<Event>> stream1,
			Map<Event, List<Event>> stream2) {
		Logger.log("Printing method declarations from stream1 ...");
		extractDecls(stream1);
		Logger.log("Printing method declarations from stream2 ...");
		extractDecls(stream2);
	}

	private void extractDecls(Map<Event, List<Event>> stream) {
		for (Map.Entry<Event, List<Event>> entry : stream.entrySet()) {
			if (entry.getKey().getMethod().toString().equalsIgnoreCase(name1)) {
				Logger.log("%s", entry.getValue().toString());
			}
		}
	}

	private void compareTypeDecls(Set<ITypeName> types,
			List<Tuple<Event, List<Event>>> stream) {

		Set<ITypeName> newTypes = Sets.newHashSet();
		for (Tuple<Event, List<Event>> tuple : stream) {
			ITypeName typeName = tuple.getFirst().getMethod()
					.getDeclaringType();
			if (!types.contains(typeName)) {
				newTypes.add(typeName);
			}
		}
		System.out.println("Non-overlapping types: " + newTypes.size());
		for (ITypeName type : newTypes) {
			System.out.println(type.getFullName());
		}
	}

	private void compareCtxElems(Map<Event, List<Event>> stream1,
			Map<Event, List<Event>> stream2) {
		System.out.println();
		System.out.println("Number of methods in stream1: " + stream1.size());
		System.out.println("Number of methods in stream2: " + stream2.size());

		System.out.println("Compare stream2 to stream1 ...");
		int numOverlaps = 0;
		int numDiff = 0;
		for (Map.Entry<Event, List<Event>> entry : stream2.entrySet()) {
			if (stream1.containsKey(entry.getKey())) {
				numOverlaps++;
			} else {
				numDiff++;
				System.out.println("typeDecl: "
						+ entry.getKey().getMethod().getDeclaringType()
								.getFullName());
				System.out.println("Event: " + entry.getKey().toString());
				System.out.println("Method: " + entry.getValue().toString());
			}
		}
		System.out.println("Number of decls overlaps: " + numOverlaps);
		System.out.println("Number of decls different: " + numDiff);
	}

	private Map<Event, List<Event>> mapConverter(
			List<Tuple<Event, List<Event>>> stream) {
		Map<Event, List<Event>> result = Maps.newLinkedHashMap();
		for (Tuple<Event, List<Event>> tuple : stream) {
			result.put(tuple.getFirst(), tuple.getSecond());
		}
		return result;
	}

	private void compareStreams(Map<Event, List<Event>> stream1,
			Map<Event, List<Event>> stream2) {

		System.out.println("Stream2 contains stream1? "
				+ stream2.keySet().containsAll(stream1.keySet()));
		for (Map.Entry<Event, List<Event>> entry : stream2.entrySet()) {
			if (!stream1.containsKey(entry.getKey())) {
				System.out.println(entry.getKey().toString());
				System.out.println();
				System.out.println(entry.getValue().toString());
				System.out.println();
			}
		}

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
		System.out.printf("ctxFirst locals: %d (%d unique)\n", numLocalFirst,
				ctxFirst.size());
		System.out.printf("ctxSuper locals: %d (%d unique)\n", numLocalSuper,
				ctxSuper.size());
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

	private Set<ITypeName> typesCounter(List<Tuple<Event, List<Event>>> stream1) {

		Set<ITypeName> classes = Sets.newHashSet();
		int numClasses = 0;
		for (Tuple<Event, List<Event>> tuple : stream1) {
			ITypeName type = tuple.getFirst().getType();

			if (tuple.getSecond().size() == 0) {
				continue;
			}
			classes.add(type);
			numClasses++;
		}
		System.out.printf("non-empty types: %d (%d unique)\n", numClasses,
				classes.size());
		return classes;
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

	private Map<Event, List<Event>> methodStruct(List<Event> stream) {
		Map<Event, List<Event>> methodStruct = Maps.newLinkedHashMap();
		Tuple<Event, List<Event>> method = null;

		for (Event event : stream) {
			if (event.getKind() == EventKind.TYPE) {
				continue;
			}
			if (event.getKind() == EventKind.METHOD_DECLARATION) {
				if (method != null) {
					methodStruct.put(method.getFirst(), method.getSecond());
				}
				method = Tuple.newTuple(event, Lists.newLinkedList());
			} else {
				method.getSecond().add(event);
			}
		}
		methodStruct.put(method.getFirst(), method.getSecond());
		return methodStruct;
	}
}
