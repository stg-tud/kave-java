package exec.recommender_reimplementation.raychev_analysis;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.pointsto.analysis.AbstractLocation;

import com.google.common.collect.Sets;

public class HistoryMapTest {

	private HistoryMap historyMap;

	@Before
	public void setup() {
		historyMap = new HistoryMap();
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
}
