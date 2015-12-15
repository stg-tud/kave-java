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
		allMethods.add(createEpisode("a", "b"));
		allMethods.add(createEpisode("a"));
		allMethods.add(createEpisode("a", "b", "c", "d"));
		
		sut = new QueryGenerator();
	}

	@Test
	public void configuration1Test() throws Exception {
		Map<Episode, Map<Integer, List<Episode>>> expected = new HashMap<Episode, Map<Integer, List<Episode>>>();
		
		Map<Integer, List<Episode>> episodeQueries = new HashMap<Integer, List<Episode>>();
		List<Episode> queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("a"));
		episodeQueries.put(1, queryLevel);
		expected.put(createEpisode("a", "b"), episodeQueries);
		
		episodeQueries = new HashMap<Integer, List<Episode>>();
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("a", "b", "c"));
		queryLevel.add(createEpisode("a", "b", "d"));
		queryLevel.add(createEpisode("a", "c", "d"));
		episodeQueries.put(1, queryLevel);
		
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("a", "b"));
		queryLevel.add(createEpisode("a", "c"));
		queryLevel.add(createEpisode("a", "d"));
		episodeQueries.put(2, queryLevel);
		
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("a"));
		episodeQueries.put(3, queryLevel);
		
		expected.put(createEpisode("a", "b", "c", "d"), episodeQueries);
		
		Map<Episode, Map<Integer, List<Episode>>> actuals = sut.createQuery(allMethods, QueryConfigurations.INCLUDEMD_REMOVEONEBYONE);
		
		Assert.assertEquals(expected, actuals);
	}
	
	@Test
	public void configuration3Test() throws Exception {
		Map<Episode, Map<Integer, List<Episode>>> expected = new HashMap<Episode, Map<Integer, List<Episode>>>();
		
		Map<Integer, List<Episode>> episodeQueries = new HashMap<Integer, List<Episode>>();
		List<Episode> queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("b"));
		episodeQueries.put(0, queryLevel);
		expected.put(createEpisode("a", "b"), episodeQueries);
		
		episodeQueries = new HashMap<Integer, List<Episode>>();
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("b", "c", "d"));
		episodeQueries.put(0, queryLevel);
		
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("b", "c"));
		queryLevel.add(createEpisode("b", "d"));
		queryLevel.add(createEpisode("c", "d"));
		episodeQueries.put(1, queryLevel);
		
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("b"));
		queryLevel.add(createEpisode("c"));
		queryLevel.add(createEpisode("d"));
		episodeQueries.put(2, queryLevel);
		
		expected.put(createEpisode("a", "b", "c", "d"), episodeQueries);
		
		Map<Episode, Map<Integer, List<Episode>>> actuals = sut.createQuery(allMethods, QueryConfigurations.REMOVEMD_REMOVEONEBYONE);
		
		Assert.assertEquals(expected, actuals);
	}
	
	@Test
	public void configuration2Test() throws Exception {
		allMethods.add(createEpisode("a", "b", "c", "d", "e", "f"));
		
		Map<Episode, Map<Integer, List<Episode>>> expected = new HashMap<Episode, Map<Integer, List<Episode>>>();
		
		Map<Integer, List<Episode>> episodeQueries = new HashMap<Integer, List<Episode>>();
		List<Episode> queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("a"));
		episodeQueries.put(1, queryLevel);
		expected.put(createEpisode("a", "b"), episodeQueries);
		
		episodeQueries = new HashMap<Integer, List<Episode>>();
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("a", "b", "c"));
		queryLevel.add(createEpisode("a", "b", "d"));
		queryLevel.add(createEpisode("a", "c", "d"));
		episodeQueries.put(1, queryLevel);
		
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("a", "b"));
		queryLevel.add(createEpisode("a", "c"));
		queryLevel.add(createEpisode("a", "d"));
		episodeQueries.put(2, queryLevel);
		
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("a"));
		episodeQueries.put(3, queryLevel);
		
		expected.put(createEpisode("a", "b", "c", "d"), episodeQueries);
		
		episodeQueries = new HashMap<Integer, List<Episode>>();
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("a", "b", "c", "d"));
		queryLevel.add(createEpisode("a", "b", "c", "e"));
		queryLevel.add(createEpisode("a", "b", "d", "e"));
		queryLevel.add(createEpisode("a", "c", "d", "e"));
		queryLevel.add(createEpisode("a", "b", "c", "f"));
		queryLevel.add(createEpisode("a", "b", "d", "f"));
		queryLevel.add(createEpisode("a", "c", "d", "f"));
		queryLevel.add(createEpisode("a", "b", "e", "f"));
		queryLevel.add(createEpisode("a", "c", "e", "f"));
		queryLevel.add(createEpisode("a", "d", "e", "f"));
		episodeQueries.put(2, queryLevel);
		
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("a", "b", "c"));
		queryLevel.add(createEpisode("a", "b", "d"));
		queryLevel.add(createEpisode("a", "c", "d"));
		queryLevel.add(createEpisode("a", "b", "e"));
		queryLevel.add(createEpisode("a", "c", "e"));
		queryLevel.add(createEpisode("a", "d", "e"));
		queryLevel.add(createEpisode("a", "b", "f"));
		queryLevel.add(createEpisode("a", "c", "f"));
		queryLevel.add(createEpisode("a", "d", "f"));
		queryLevel.add(createEpisode("a", "e", "f"));
		episodeQueries.put(3, queryLevel);
		
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("a", "b"));
		queryLevel.add(createEpisode("a", "c"));
		queryLevel.add(createEpisode("a", "d"));
		queryLevel.add(createEpisode("a", "e"));
		queryLevel.add(createEpisode("a", "f"));
		episodeQueries.put(4, queryLevel);
		
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("a"));
		episodeQueries.put(5, queryLevel);
		
		expected.put(createEpisode("a", "b", "c", "d", "e", "f"), episodeQueries);
		
		Map<Episode, Map<Integer, List<Episode>>> actuals = sut.createQuery(allMethods, QueryConfigurations.INCLUDEMD_REMOVEBYPERCENTAGE);
		
		Assert.assertEquals(expected, actuals);
	}
	
	@Test
	public void configuration4Test() throws Exception {
		allMethods.add(createEpisode("a", "b", "c", "d", "e", "f"));
		
		Map<Episode, Map<Integer, List<Episode>>> expected = new HashMap<Episode, Map<Integer, List<Episode>>>();
		
		Map<Integer, List<Episode>> episodeQueries = new HashMap<Integer, List<Episode>>();
		List<Episode> queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("b"));
		episodeQueries.put(0, queryLevel);
		expected.put(createEpisode("a", "b"), episodeQueries);
		
		episodeQueries = new HashMap<Integer, List<Episode>>();
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("b", "c", "d"));
		episodeQueries.put(0, queryLevel);
		
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("b", "c"));
		queryLevel.add(createEpisode("b", "d"));
		queryLevel.add(createEpisode("c", "d"));
		episodeQueries.put(1, queryLevel);
		
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("b"));
		queryLevel.add(createEpisode("c"));
		queryLevel.add(createEpisode("d"));
		episodeQueries.put(2, queryLevel);
		
		expected.put(createEpisode("a", "b", "c", "d"), episodeQueries);
		
		episodeQueries = new HashMap<Integer, List<Episode>>();
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("b", "c", "d", "e", "f"));
		episodeQueries.put(0, queryLevel);
		
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("b", "c", "d"));
		queryLevel.add(createEpisode("b", "c", "e"));
		queryLevel.add(createEpisode("b", "d", "e"));
		queryLevel.add(createEpisode("c", "d", "e"));
		queryLevel.add(createEpisode("b", "c", "f"));
		queryLevel.add(createEpisode("b", "d", "f"));
		queryLevel.add(createEpisode("c", "d", "f"));
		queryLevel.add(createEpisode("b", "e", "f"));
		queryLevel.add(createEpisode("c", "e", "f"));
		queryLevel.add(createEpisode("d", "e", "f"));
		episodeQueries.put(2, queryLevel);
		
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("b", "c"));
		queryLevel.add(createEpisode("b", "d"));
		queryLevel.add(createEpisode("c", "d"));
		queryLevel.add(createEpisode("b", "e"));
		queryLevel.add(createEpisode("c", "e"));
		queryLevel.add(createEpisode("d", "e"));
		queryLevel.add(createEpisode("b", "f"));
		queryLevel.add(createEpisode("c", "f"));
		queryLevel.add(createEpisode("d", "f"));
		queryLevel.add(createEpisode("e", "f"));
		episodeQueries.put(3, queryLevel);
		
		queryLevel = new LinkedList<Episode>();
		queryLevel.add(createEpisode("b"));
		queryLevel.add(createEpisode("c"));
		queryLevel.add(createEpisode("d"));
		queryLevel.add(createEpisode("e"));
		queryLevel.add(createEpisode("f"));
		episodeQueries.put(4, queryLevel);
		
		expected.put(createEpisode("a", "b", "c", "d", "e", "f"), episodeQueries);
		
		Map<Episode, Map<Integer, List<Episode>>> actuals = sut.createQuery(allMethods, QueryConfigurations.REMOVEMD_REMOVEBYPERCENTAGE);
		
		Assert.assertEquals(expected, actuals);
	}
	
	private Episode createEpisode(String ... strings) {
		Episode episode = new Episode();
		episode.setFrequency(1);
		episode.setNumEvents(strings.length);
		episode.addStringsOfFacts(strings);
		
		String previousEvent = "";
		
		for (String event : strings) {
			if (previousEvent.isEmpty()) {
				previousEvent = event;
			} else {
				episode.addFact(previousEvent + ">" + event);
				previousEvent = event;
			}
		}
		return episode;
	}
}
