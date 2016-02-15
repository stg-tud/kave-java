package cc.kave.episodes.evaluation.queries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cc.kave.episodes.model.Episode;

public class QueryGeneratorByNumberTest {

	private Episode episode = new Episode();
	private QueryGeneratorByNumber sut;
	
	@Before
	public void setup() {
		sut = new QueryGeneratorByNumber();
	}
	
	@Ignore
	@Test
	public void emptyEpisode() {
		Map<Integer, List<Episode>> expected = new HashMap<Integer, List<Episode>>();
		Map<Integer, List<Episode>> actuals = sut.generateQueries(episode, 5);
		
		Assert.assertEquals(expected, actuals);
	}
}
