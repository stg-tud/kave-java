package cc.kave.episodes.postprocessor;

import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.episodes.io.EpisodesParser;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.FileReader;
import cc.kave.episodes.io.ValidationDataIO;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class PostChecking {

	private File eventsFolder;

	private EventStreamIo trainStreamIo;
	private ValidationDataIO valStreamIo;
	private FileReader fileReader;

	private EpisodesParser episodeParser;

	@Inject
	public PostChecking(@Named("events") File folder, EventStreamIo streamIo,
			ValidationDataIO validation, FileReader fileIo,
			EpisodesParser parser) {
		assertTrue(folder.exists(), "Events folder does not exist");
		assertTrue(folder.isDirectory(), "Events is not a folder, but a file");
		this.eventsFolder = folder;
		this.trainStreamIo = streamIo;
		this.valStreamIo = validation;
		this.fileReader = fileIo;
		this.episodeParser = parser;
	}

	private static final int FOLDNUM = -1;
	private static final int METHODSIZE = 5000;
	private static final double TIMEOUT = 5.0;

	// public void updatedEvent(int frequency) {
	// Map<Integer, Set<Episode>> episodes = episodeParser.parse(
	// EpisodeType.PARALLEL, frequency, -2);
	// List<Event> events = trainStreamIo.readMapping(frequency, -2);
	// Set<ITypeName> episodeTypes = Sets.newLinkedHashSet();
	//
	// Logger.log("Extracting episode types ...");
	// for (Episode episode : episodes.get(2)) {
	// Set<Fact> facts = episode.getEvents();
	//
	// for (Fact fact : facts) {
	// Event event = events.get(fact.getFactID());
	// episodeTypes.add(event.getMethod().getDeclaringType());
	// }
	// }
	//
	// List<Event> updatedEvents = trainStreamIo.readMapping(frequency,
	// FOLDNUM);
	// Set<ITypeName> updatedTypes = Sets.newLinkedHashSet();
	// Logger.log("Extracting updated types ... ");
	// for (Event event : updatedEvents) {
	// updatedTypes.add(event.getMethod().getDeclaringType());
	// }
	// Logger.log("Comparing types ...");
	// for (ITypeName type : episodeTypes) {
	// if (!updatedTypes.contains(type)) {
	// Logger.log("Missed type: %s", type.getIdentifier());
	// }
	// }
	// }

	public void eventsVersions(int frequency) {
		List<Event> eventsNew = trainStreamIo.readMapping(frequency, -1);
		List<Event> eventsCurrent = trainStreamIo.readMapping(frequency, -11);
		Logger.log("Number of events in the new version: %d", eventsNew.size());
		Logger.log("Number of events in the current version: %d", eventsCurrent.size());

//		if (eventsNew.containsAll(eventsCurrent)) {
//			Logger.log("All events are icluded in the new contexts version!");
//		} else {
//			Logger.log("The new contexts version misses some of the events from the previous version!");
//		}
		
		for (Event event : eventsCurrent) {
			if (!eventsNew.contains(event)) {
				Logger.log("Kind: %s", event.getKind());
				Logger.log("Name: %s\n", event.getMethod().getIdentifier());
			}
		}
	}

	public void streamData(int frequency) {
		List<Tuple<Event, List<Fact>>> stream = trainStreamIo.parseStream(
				frequency, FOLDNUM);
		List<Event> events = trainStreamIo.readMapping(frequency, FOLDNUM);
		int numbEvents = 0;
		int numbDecls = 0;
		int numbInvs = 0;
		Set<Event> declarations = Sets.newLinkedHashSet();
		Set<Event> invocations = Sets.newLinkedHashSet();
		Set<ITypeName> declTypes = Sets.newLinkedHashSet();
		Set<ITypeName> invTypes = Sets.newLinkedHashSet();

		for (Tuple<Event, List<Fact>> tuple : stream) {
			if (tuple.getSecond().isEmpty()) {
				Logger.log("empty method");
			}
			for (Fact fact : tuple.getSecond()) {
				numbEvents++;
				int factId = fact.getFactID();
				Event e = events.get(factId);

				ITypeName type = e.getMethod().getDeclaringType();
				if (e.getKind() == EventKind.INVOCATION) {
					numbInvs++;
					invocations.add(e);
					if (!type.isUnknown()) {
						invTypes.add(type);
					}
				} else {
					numbDecls++;
					declarations.add(e);
					if (!type.isUnknown()) {
						declTypes.add(type);
					}
				}
			}
		}
		Logger.log("Event stream information:");
		Logger.log("Number of events: %d", events.size());
		Logger.log("Number of methods: %d", stream.size());

		Logger.log("Number of events in event stream: %d", numbEvents);
		Logger.log("Number of method declarations: total = %d, unique = %d",
				numbDecls, declarations.size());
		Logger.log("Number of method invocations: total = %d, unique = %d",
				numbInvs, invocations.size());
		Logger.log("Number of unique declaration types: %d", declTypes.size());
		Logger.log("Number of unique invocation types: %d", invTypes.size());
	}

	public void trainValOverlaps(int frequency) {
		List<Tuple<Event, List<Fact>>> trainStream = trainStreamIo.parseStream(
				frequency, FOLDNUM);
		List<Event> valStream = valStreamIo.read(frequency, FOLDNUM);

		Set<ITypeName> trainTypes = getTrainCtxs(trainStream);
		Set<ITypeName> valTypes = getValCtxs(valStream);

		Set<ITypeName> overlaps = getSetOverlaps(trainTypes, valTypes);
		Logger.log(
				"Number of overlaping types for training and validation stream: %d",
				overlaps.size());
		Logger.log("Number of types in training stream: %d", trainTypes.size());
		Logger.log("Number of types in validation stream: %d", valTypes.size());

		if (overlaps.size() > 0) {
			for (ITypeName type : overlaps) {
				Logger.log("Type name: %s", type.getFullName());
				Logger.log("Is it unknown type? %s",
						type.equals(Names.getUnknownType()));
			}
		}
	}

	public void methodSize(int frequency) {
		List<Tuple<Event, List<Fact>>> trainStream = trainStreamIo.parseStream(
				frequency, FOLDNUM);
		List<Event> longMethodsCtxs = Lists.newLinkedList();

		for (Tuple<Event, List<Fact>> tuple : trainStream) {

			if ((tuple.getSecond().size() > METHODSIZE)
					&& !(tuple.getFirst().getMethod().equals(Names
							.getUnknownMethod()))) {
				longMethodsCtxs.add(tuple.getFirst());
			}
		}
		Logger.log("Number of methods with more than %d events: %d",
				METHODSIZE, longMethodsCtxs.size());
		for (Tuple<Event, List<Fact>> tuple : trainStream) {

			if (longMethodsCtxs.contains(tuple.getFirst())) {
				IMethodName method = tuple.getFirst().getMethod();
				String typeName = method.getDeclaringType().getFullName();
				String methodName = method.getName();
				String fullName = typeName + "." + methodName;
				Logger.log("%s: %d events", fullName, tuple.getSecond().size());
			}
		}
	}

	public void methodContent(int frequency) {
		List<Tuple<Event, List<Fact>>> trainStream = trainStreamIo.parseStream(
				frequency, FOLDNUM);
		List<Event> events = trainStreamIo.readMapping(frequency, FOLDNUM);

		for (Tuple<Event, List<Fact>> method : trainStream) {
			assertTrue(
					method.getFirst().getKind() == EventKind.METHOD_DECLARATION,
					"Method contexts is not the element!");
			List<Event> invocations = getInvocations(method.getSecond(), events);

			for (Event event : invocations) {
				assertTrue(event.getKind() != EventKind.METHOD_DECLARATION,
						"There is method element in method body!");
			}
		}
		Logger.log("Method content is correct in training stream!");
	}

	public void streamSizes(int frequency) {
		List<String> streamText = fileReader.readFile(new File(
				getStreamTextPath(frequency)));
		List<List<Fact>> streamFacts = parseStreamText(streamText);
		List<Tuple<Event, List<Fact>>> streamData = trainStreamIo.parseStream(
				frequency, FOLDNUM);

		Logger.log("%d - %d", streamFacts.size(), streamData.size());
		assertTrue(streamFacts.size() == streamData.size(),
				"Missmatch in stream sizes!");
	}

	private List<List<Fact>> parseStreamText(List<String> stream) {
		List<List<Fact>> streamResult = Lists.newLinkedList();
		List<Fact> method = Lists.newLinkedList();
		double prevTime = 0.0;

		for (String entry : stream) {
			String[] line = entry.split(",");
			int eventId = Integer.parseInt(line[0]);
			double time = Double.parseDouble(line[1]);
			if ((time - prevTime) > TIMEOUT) {
				if (!method.isEmpty()) {
					streamResult.add(method);
					method = Lists.newLinkedList();
				}
			}
			prevTime = time;
			method.add(new Fact(eventId));
		}
		if (!method.isEmpty()) {
			streamResult.add(method);
		}
		return streamResult;
	}

	private String getStreamTextPath(int frequency) {
		String fileName = eventsFolder.getAbsolutePath() + "/freq" + frequency
				+ "/TrainingData/fold" + FOLDNUM + "/streamText.txt";
		return fileName;
	}

	private List<Event> getInvocations(List<Fact> method, List<Event> events) {
		List<Event> invocations = Lists.newLinkedList();

		for (Fact fact : method) {
			invocations.add(events.get(fact.getFactID()));
		}
		assertTrue(method.size() == invocations.size(),
				"Method length changes when converting from facts to events!");
		return invocations;
	}

	private Set<ITypeName> getValCtxs(List<Event> valStream) {
		Set<ITypeName> types = Sets.newLinkedHashSet();

		for (Event event : valStream) {
			if (event.getKind() == EventKind.METHOD_DECLARATION) {
				try {
					ITypeName typeName = event.getMethod().getDeclaringType();
					types.add(typeName);
				} catch (Exception e) {
					continue;
				}
			}
		}
		return types;
	}

	private Set<ITypeName> getTrainCtxs(
			List<Tuple<Event, List<Fact>>> trainStream) {
		Set<ITypeName> types = Sets.newLinkedHashSet();

		for (Tuple<Event, List<Fact>> tuple : trainStream) {
			try {
				types.add(tuple.getFirst().getMethod().getDeclaringType());
			} catch (Exception e) {
				continue;
			}
		}
		return types;
	}

	private Set<ITypeName> getSetOverlaps(Set<ITypeName> set1,
			Set<ITypeName> set2) {
		Set<ITypeName> results = Sets.newLinkedHashSet();

		for (ITypeName type : set2) {
			if (set1.contains(type)) {
				results.add(type);
			}
		}
		return results;
	}
}
