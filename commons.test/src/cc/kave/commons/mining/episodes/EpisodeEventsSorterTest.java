package cc.kave.commons.mining.episodes;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.OrderedEventsInEpisode;

public class EpisodeEventsSorterTest {

	private Episode episode;
	private EpisodeEventsSorter sut;
	
	@Before
	public void setup() {
		sut = new EpisodeEventsSorter();
		episode = new Episode();
		episode.setFrequency(3);
		episode.setNumEvents(4);
		episode.addStringsOfFacts("a", "b", "c", "d", "a>b", "a>c", "a>d", "b>d", "c>d");
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
		
		OrderedEventsInEpisode actuals = sut.sort(episode);
		
		assertEquals(expected, actuals);
	}
}
