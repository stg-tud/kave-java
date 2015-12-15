package cc.kave.commons.mining.episodes;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.mining.episodes.ProposalHelper;
import cc.kave.commons.model.episodes.Episode;
import cc.recommenders.datastructures.Tuple;

public class ProposalHelperTest {
	
	private Episode episode1;
	private Episode episode2;
	private Episode episode3;
	private Episode episode4;
	private Episode episode5;
	private Episode episode6;
	private Episode episode7;

	private Set<Tuple<Episode, Double>> actuals;
	private Set<Tuple<Episode, Double>> expecteds;

	@Before
	public void setup() {
		episode1 = new Episode();
		episode1.addFact("1");
		episode1.setNumEvents(1);
		episode1.setFrequency(3);

		episode2 = new Episode();
		episode2.addStringsOfFacts("1", "2", "1>2");
		episode2.setNumEvents(2);
		episode2.setFrequency(3);

		episode3 = new Episode();
		episode3.addStringsOfFacts("1", "3", "1>3");
		episode3.setNumEvents(2);
		episode3.setFrequency(4);
		
		episode4 = new Episode();
		episode4.addStringsOfFacts("2", "3", "2>3");
		episode4.setNumEvents(2);
		episode4.setFrequency(2);
		
		episode5 = new Episode();
		episode5.addStringsOfFacts("1", "2", "3", "1>2");
		episode5.setNumEvents(3);
		episode5.setFrequency(3);
		
		episode6 = new Episode();
		episode6.addStringsOfFacts("1", "2", "4", "2>4");
		episode6.setNumEvents(3);
		episode6.setFrequency(3);
		
		episode7 = new Episode();
		episode7.addStringsOfFacts("1", "3", "4", "1>3");
		episode7.setNumEvents(3);
		episode7.setFrequency(3);
		
		actuals = ProposalHelper.createEpisodesSortedSet();
		expecteds = Sets.newLinkedHashSet();
	}

	@Test
	public void firstElemIsNUllAndSecondIsEqual() {
		actuals.add(Tuple.newTuple(episode1, 1.0));
		actuals.add(Tuple.newTuple(new Episode(), 1.0));
	}

	@Test
	public void firstElemIsNUllAndSecondIsEqual_2() {
		actuals.add(Tuple.newTuple(new Episode(), 1.0));
		actuals.add(Tuple.newTuple(episode1, 1.0));
	}

	@Test
	public void isInDescendingOrder() {
		actuals.add(Tuple.newTuple(episode4, 0.5));
		actuals.add(Tuple.newTuple(episode1, 1.0));
		actuals.add(Tuple.newTuple(episode2, 0.7));

		expecteds.add(Tuple.newTuple(episode1, 1.0));
		expecteds.add(Tuple.newTuple(episode2, 0.7));
		expecteds.add(Tuple.newTuple(episode4, 0.5));

		assertSets();
	}

	@Test
	public void frequencySorting() {
		actuals.add(Tuple.newTuple(episode2, 0.9));
		actuals.add(Tuple.newTuple(episode3, 0.9));
		actuals.add(Tuple.newTuple(episode4, 0.9));

		expecteds.add(Tuple.newTuple(episode3, 0.9));
		expecteds.add(Tuple.newTuple(episode2, 0.9));
		expecteds.add(Tuple.newTuple(episode4, 0.9));

		assertSets();
	}

	@Test
	public void numberOfEventsSorting() {
		actuals.add(Tuple.newTuple(episode2, 0.9));
		actuals.add(Tuple.newTuple(episode5, 0.9));
		actuals.add(Tuple.newTuple(episode1, 0.9));

		expecteds.add(Tuple.newTuple(episode5, 0.9));
		expecteds.add(Tuple.newTuple(episode2, 0.9));
		expecteds.add(Tuple.newTuple(episode1, 0.9));

		assertSets();
	}
	
	@Test 
	public void eventSorting() {
		actuals.add(Tuple.newTuple(episode7, 0.9));
		actuals.add(Tuple.newTuple(episode5, 0.9));
		actuals.add(Tuple.newTuple(episode6, 0.9));
		
		expecteds.add(Tuple.newTuple(episode5, 0.9));
		expecteds.add(Tuple.newTuple(episode6, 0.9));
		expecteds.add(Tuple.newTuple(episode7, 0.9));
		
		assertSets();
	}

	private void assertSets() {
		assertEquals(expecteds.size(), actuals.size());
		Iterator<Tuple<Episode, Double>> itA = expecteds.iterator();
		Iterator<Tuple<Episode, Double>> itB = actuals.iterator();
		while (itA.hasNext()) {
			assertEquals(itA.next(), itB.next());
		}
	}
}