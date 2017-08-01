package cc.kave.episodes.eventstream;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.EventStream;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.Fact;
import cc.recommenders.io.Logger;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class PartitionStream {

	private EventStreamIo streamIo;

	@Inject
	public PartitionStream(EventStreamIo streamIo) {
		this.streamIo = streamIo;
	}

	public void partition(int frequency) throws IOException {
		String streamText = streamIo.readStreamText(frequency);
		List<List<Fact>> streamAll = parseStream(streamText);
		
		List<List<Fact>> stream1 = Lists.newLinkedList();
		List<List<Fact>> stream2 = Lists.newLinkedList();
		
		Set<Fact> eventsStream1 = Sets.newHashSet();
		
		for (List<Fact> method : streamAll) {
			boolean isInFirst = isInStream1(method, eventsStream1);
			
			if (stream1.isEmpty() || isInFirst) {
				stream1.add(method);
				eventsStream1.addAll(method);
			} else {
				stream2.add(method);
			}
		}
		Logger.log("Number of methods in stream1: %d", stream1.size());
		Logger.log("Number of methods in stream2: %d\n", stream2.size());
		Logger.log("Number of events in partition 1: %d\n", eventsStream1.size());
		
		Logger.log("Generating event stream partitions for freq = %d ...", frequency);
		genPartition(stream1, frequency, -21);
		genPartition(stream2, frequency, -22);
	}

	private void genPartition(List<List<Fact>> stream, int frequency, int streamID) {
		EventStream es = new EventStream();
		List<Event> events = streamIo.readMapping(frequency);
		
		for (List<Fact> method : stream) {
			for (Fact fact : method) {
				es.addEvent(events.get(fact.getFactID()));
			}
			es.increaseTimeout();
		}
		streamIo.write(es, streamID);
	}

	private boolean isInStream1(List<Fact> method, Set<Fact> stream) {
		for (Fact fact : method) {
			if (stream.contains(fact)) {
				return true;
			}
		}
		return false;
	}

	private List<List<Fact>> parseStream(String streamText) {
		List<List<Fact>> stream = Lists.newLinkedList();
		List<Fact> method = Lists.newLinkedList();

		double timer = -1;
		
		String[] lines = streamText.split("\n");

		for (String line : lines) {
			String[] eventTime = line.split(",");
			int eventID = Integer.parseInt(eventTime[0]);
			double timestamp = Double.parseDouble(eventTime[1]);
			if ((timer != -1) && ((timestamp - timer) >= 0.5)) {
				stream.add(method);
				method = new LinkedList<Fact>();
			}
			timer = timestamp;
			method.add(new Fact(eventID));
		}
		stream.add(method);
		return stream;
	}
}
