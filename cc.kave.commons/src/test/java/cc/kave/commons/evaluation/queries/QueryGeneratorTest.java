package cc.kave.commons.evaluation.queries;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import c.kave.commons.evaluation.queries.QueryGenerator;
import cc.kave.commons.model.episodes.Episode;
import cc.kave.commons.model.episodes.QueryConfigurations;

public class QueryGeneratorTest {

	private List<Episode> allMethods = new LinkedList<Episode>();
	private QueryGenerator sut;
	
	@Before
	public void setup() {
		allMethods.add(createEpisode("a", "b", "a>b"));
		allMethods.add(createEpisode("a"));
		allMethods.add(createEpisode("a", "b", "c", "d", "a>b", "b>c", "c>d"));
		sut = new QueryGenerator();
	}

	@Test
	public void configuration1Test() throws Exception {
		Map<Episode, Map<Integer, List<Episode>>> expected = new HashMap<Episode, Map<Integer, List<Episode>>>();
		
		Map<Integer, List<Episode>> episodeQueries = new HashMap<Integer, List<Episode>>();
		List<Episode> queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("a"));
		episodeQueries.put(1, queryLevel);
		expected.put(createEpisode("a", "b", "a>b"), episodeQueries);
		
		episodeQueries = new HashMap<Integer, List<Episode>>();
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("a", "c", "d", "a>c", "c>d"));
		queryLevel.add(createEpisode("a", "b", "d", "a>b", "b>d"));
		queryLevel.add(createEpisode("a", "b", "c", "a>b", "b>c"));
		episodeQueries.put(1, queryLevel);
		
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("a", "d", "a>d"));
		queryLevel.add(createEpisode("a", "b", "a>b"));
		queryLevel.add(createEpisode("a", "c", "a>c"));
		episodeQueries.put(2, queryLevel);
		
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("a"));
		episodeQueries.put(3, queryLevel);
		
		expected.put(createEpisode("a", "b", "c", "d", "a>b", "b>c", "c>d"), episodeQueries);
		
		Map<Episode, Map<Integer, List<Episode>>> actuals = sut.createQuery(allMethods, QueryConfigurations.INCLUDEMD_REMOVEONEBYONE);
		
		Assert.assertEquals(expected, actuals);
	}
	
	private Episode createEpisode(String ... strings) {
		Episode episode = new Episode();
		episode.setFrequency(1);
		int numberOfEvents = 0;
		
		for (String fact : strings) {
			if (!fact.contains(">")) {
				numberOfEvents++;
			} else {
				break;
			}
		}
		episode.setNumEvents(numberOfEvents);
		episode.addStringsOfFacts(strings);
		
		return episode;
	}
}
