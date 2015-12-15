package cc.kave.commons.mining.episodes;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.OrderedEventsInEpisode;

public class EpisodeEventsSorterTest {

	private Episode episode1;
	private Episode episode2;
	private EpisodeEventsSorter sut;
	
	@Before
	public void setup() {
		sut = new EpisodeEventsSorter();
		
		episode1 = new Episode();
		episode1.setFrequency(3);
		episode1.setNumEvents(4);
		episode1.addStringsOfFacts("a", "b", "c", "d", "a>b", "a>c", "a>d", "b>d", "c>d");
		
		episode2 = new Episode();
		episode2.setFrequency(3);
		episode2.setNumEvents(2);
		episode2.addStringsOfFacts("a", "b");
	}
	
	@Test(expected=Exception.class)
	public void emptyEpisode() throws Exception {
		sut.sort(new Episode());
	}
	
	@Test
	public void sorterTest() throws Exception {
		OrderedEventsInEpisode expected = new OrderedEventsInEpisode();
		expected.addEventIDInSequentialOrderList("a");
		expected.addPartialEventsIDInSequentialOrderList("b", "c");
		expected.addEventIDInSequentialOrderList("d");
		
		OrderedEventsInEpisode actuals = sut.sort(episode1);
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void partialOrderSorterTest() throws Exception {
		OrderedEventsInEpisode expected = new OrderedEventsInEpisode();
		expected.addEventIDInPartialOrderList("a");
		expected.addEventIDInPartialOrderList("b");
		
		OrderedEventsInEpisode actuals = sut.sort(episode2);
		
		assertEquals(expected, actuals);
	}
}
