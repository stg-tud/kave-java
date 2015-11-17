package cc.kave.commons.mining.episodes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import cc.kave.commons.model.episodes.Episode;

public class NoTransitivelyClosedEpisodesTest {

	private Map<Integer, List<Episode>> maximalEpisodes;
	private NoTransitivelyClosedEpisodes sut;
	
	@Before
	public void setup() {
		sut = new NoTransitivelyClosedEpisodes();
		maximalEpisodes = new HashMap<Integer, List<Episode>>();
		maximalEpisodes.put(1, createListOfEpisodes(1, true));
		maximalEpisodes.put(2, createListOfEpisodes(2, true));
		maximalEpisodes.put(3, createListOfEpisodes(3, true));
		maximalEpisodes.put(4, createListOfEpisodes(4, true));
		maximalEpisodes.put(5, createListOfEpisodes(5, true));
	}

	@Test(expected=Exception.class)
	public void emptyEpisode() throws Exception {
		sut.removeTransitivelyClosure(new HashMap<Integer, List<Episode>>());
	}
	
	@Test
	public void removeClosures() throws Exception {
		Map<Integer, List<Episode>> expected = new HashMap<Integer, List<Episode>>();
		expected.put(1, createListOfEpisodes(1, false));
		expected.put(2, createListOfEpisodes(2, false));
		expected.put(3, createListOfEpisodes(3, false));
		expected.put(4, createListOfEpisodes(4, false));
		expected.put(5, createListOfEpisodes(5, false));
		
		Map<Integer, List<Episode>> actuals = sut.removeTransitivelyClosure(maximalEpisodes);
		
//		assertEquals(expected, actuals);
				
		for (Map.Entry<Integer, List<Episode>> entry : expected.entrySet()) {
			List<Episode> expectedList = expected.get(entry.getKey());
			List<Episode> actualsList = actuals.get(entry.getKey());
			assertTrue(expectedList.size() == actualsList.size());
			for (int idx = 0; idx < expectedList.size(); idx++) {
				assertTrue(expectedList.get(idx).equals(actualsList.get(idx)));
			}
		}
	}
	
	private List<Episode> createListOfEpisodes(int i, boolean closure) {
		List<Episode> episodes = new LinkedList<Episode>();
		if (i == 1) {
			episodes.add(createEpisode("a"));
		} else if (i == 2) {
			episodes.add(createEpisode("a", "b"));
			episodes.add(createEpisode("c", "d", "c>d"));
		} else if (i == 3) {
			if (closure) {
				episodes.add(createEpisode("a", "b", "c", "a>b", "a>c", "b>c"));
				episodes.add(createEpisode("c", "d", "e", "c>d", "c>e"));
			} else {
				episodes.add(createEpisode("a", "b", "c", "a>b", "b>c"));
				episodes.add(createEpisode("c", "d", "e", "c>d", "c>e"));
			}
		} else if (i == 4) {
			if (closure) {
				episodes.add(createEpisode("a", "b", "c", "d", "a>b", "a>c", "a>d", "b>d", "c>d"));
			} else {
				episodes.add(createEpisode("a", "b", "c", "d", "a>b", "a>c", "b>d", "c>d"));
			}
		} else if (i == 5) {
			if (closure) {
				episodes.add(createEpisode("a", "b", "c", "d", "e", "a>d", "a>e", "b>a", "b>c", "b>d", "b>e"));
				episodes.add(createEpisode("f", "g", "h", "i", "j", "f>g", "f>h", "f>i", "f>j", "g>h", "g>i", "g>j", "h>i", "h>j", "i>j"));
			} else {
				episodes.add(createEpisode("a", "b", "c", "d", "e", "a>d", "a>e", "b>a", "b>c"));
				episodes.add(createEpisode("f", "g", "h", "i", "j", "f>g", "g>h", "h>i", "i>j"));
			}
		}
		return episodes;
	}

	private Episode createEpisode(String... string) {
		Episode episode = new Episode();
		episode.setFrequency(3);
		int numEvents = 0;
		for (String s : string) {
			episode.addFact(s);
			if (s.length() == 1) {
				numEvents++;
			}
		}
		episode.setNumEvents(numEvents);
		return episode;
	}
}
