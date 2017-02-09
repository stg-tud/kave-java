package cc.kave.episodes.postprocessor;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.io.ValidationDataIO;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Logger;

import com.google.common.collect.Sets;

public class OverlapingTypes {

	private EventStreamIo trainStreamIo;
	private ValidationDataIO valStream;

	@Inject
	public OverlapingTypes(@Named("events") File folder,
			EventStreamIo streamIo, ValidationDataIO validation) {
		this.trainStreamIo = streamIo;
		this.valStream = validation;
	}

	private static final int FOLDNUM = 0;

	public Set<ITypeName> getOverlaps(int frequency) {
		List<Tuple<Event, List<Fact>>> trainStream = trainStreamIo.parseStream(
				frequency, FOLDNUM);
		Set<ITypeName> trainTyes = Sets.newLinkedHashSet();
		Set<ITypeName> valTypes = Sets.newLinkedHashSet();

		for (Tuple<Event, List<Fact>> tuple : trainStream) {
			try {
				trainTyes.add(tuple.getFirst().getMethod().getDeclaringType());
			} catch (Exception e) {
				continue;
			}
		}
		List<Event> valData = valStream.read(frequency, FOLDNUM);

		for (Event event : valData) {
			if (event.getKind() == EventKind.METHOD_DECLARATION) {
				try {
					ITypeName typeName = event.getMethod().getDeclaringType();
					valTypes.add(typeName);
				} catch (Exception e) {
					continue;
				}
			}
		}

		Set<ITypeName> overlaps = getSetOverlaps(trainTyes, valTypes);
		Logger.log(
				"Number of overlaping types for training and validation data: %d",
				overlaps.size());
		Logger.log("Number of types in training stream: %d", trainTyes.size());
		Logger.log("Number of types in validation stream: %d", valTypes.size());

		return overlaps;
	}

	private Set<ITypeName> getSetOverlaps(Set<ITypeName> set1,
			Set<ITypeName> set2) {
		Set<ITypeName> results = Sets.newLinkedHashSet();

		for (ITypeName type : set1) {
			if (set2.contains(type)) {
				results.add(type);
			}
		}
		return results;
	}
}
