package exec.recommender_reimplementation.raychev_analysis;

import static org.junit.Assert.*;

import org.junit.Test;

import cc.kave.commons.pointsto.analysis.AbstractLocation;
import com.google.common.collect.Sets;

import java.util.Set;

import com.google.common.collect.Lists;

public class HistoryMapTest extends RaychevAnalysisVisitorTest {

	@Test
	public void mergesTwoHistoryMaps() {
		HistoryMap a = new HistoryMap();
		HistoryMap b = new HistoryMap();

		AbstractLocation abstractLocation = new AbstractLocation();
		AbstractLocation abstractLocation2 = new AbstractLocation();

		a.put(Sets.newHashSet(abstractLocation),
				createAbstractHistory(constructorCall(), callInDefaultContextAsReceiver("m1")));
		a.put(Sets.newHashSet(abstractLocation2), createAbstractHistory(callInDefaultContextAsReceiver("m1")));

		b.put(Sets.newHashSet(abstractLocation),
				createAbstractHistory(constructorCall(), callInDefaultContextAsReceiver("m1"),
						callInDefaultContextAsReceiver("m2")));
		b.put(Sets.newHashSet(abstractLocation2),
				createAbstractHistory(callInDefaultContextAsReceiver("m1"), callInDefaultContextAsReceiver("m2"),
						callInDefaultContextAsReceiver("m3")));

		HistoryMap expected = new HistoryMap();
		expected.put(Sets.newHashSet(abstractLocation),
				createAbstractHistory(Lists.newArrayList(constructorCall(), callInDefaultContextAsReceiver("m1"),
						callInDefaultContextAsReceiver("m2")),//
						Sets.newHashSet(
						createConcreteHistory(//
								constructorCall(), callInDefaultContextAsReceiver("m1")),
						createConcreteHistory(constructorCall(), callInDefaultContextAsReceiver("m1"),
								callInDefaultContextAsReceiver("m2")))));

		expected.put(Sets.newHashSet(abstractLocation2),
				createAbstractHistory(Lists.newArrayList(
						//
						callInDefaultContextAsReceiver("m1"), callInDefaultContextAsReceiver("m2"),
						callInDefaultContextAsReceiver("m3")), //
						Sets.newHashSet(
								createConcreteHistory(//
								callInDefaultContextAsReceiver("m1")),
								createConcreteHistory(callInDefaultContextAsReceiver("m1"),
										callInDefaultContextAsReceiver("m2"), callInDefaultContextAsReceiver("m3")))));
		a.mergeInto(b);
		
		assertEquals(Sets.newHashSet(expected.values()), Sets.newHashSet(a.values()));		
	}
	
	@Test
	public void getsAbstractHistoryIfExistsForAbstractLocation() {
		Set<AbstractLocation> abstractLocations = Sets.newHashSet(new AbstractLocation());
		AbstractHistory abstractHistory = createAbstractHistory(constructorCall(), callInDefaultContextAsReceiver("m1"));
		historyMap.put(abstractLocations,
				abstractHistory);
		
		assertEquals(abstractHistory, historyMap.getOrCreateAbstractHistory(abstractLocations));
	}
	
	@Test
	public void createsNewAbstractHistoryWhenItDoesNotExist() {
		assertEquals(new AbstractHistory(), historyMap.getOrCreateAbstractHistory(Sets.newHashSet(new AbstractLocation())));
	}

	@Test
	public void evictsRandomEntryFromHashmap() {
		historyMap.put(Sets.newHashSet(//
				new AbstractLocation()), new AbstractHistory());
		historyMap.put(Sets.newHashSet(//
				new AbstractLocation()), new AbstractHistory());
		historyMap.put(Sets.newHashSet(//
				new AbstractLocation()), new AbstractHistory());

		historyMap.evictRandomAbstractHistory();

		assertEquals(2, historyMap.size());
	}
	
	@Test
	public void cloneTest() {
		AbstractLocation abstractLocation = new AbstractLocation();
		AbstractLocation abstractLocation2 = new AbstractLocation();

		historyMap.put(Sets.newHashSet(abstractLocation),
				createAbstractHistory(constructorCall(), callInDefaultContextAsReceiver("m1")));
		historyMap.put(Sets.newHashSet(abstractLocation2), createAbstractHistory(callInDefaultContextAsReceiver("m1")));
		
		HistoryMap clone = historyMap.clone();
		
		assertEquals(Sets.newHashSet(historyMap.values()), Sets.newHashSet(clone.values()));		
		assertFalse(historyMap == clone);
	}
}
