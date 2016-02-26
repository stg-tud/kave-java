package cc.kave.episodes.evaluation.queries;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cc.kave.episodes.model.Query;
import cc.kave.episodes.model.QueryTarget;

public class QueryGeneratorByNumberTest {

	private QueryTarget queryTarget = new QueryTarget();
	private QueryGeneratorByNumber sut;

	@Before
	public void setup() {
		sut = new QueryGeneratorByNumber();
	}

	@Ignore
	@Test
	public void emptyEpisode() {
		Map<Integer, List<Query>> expected = new HashMap<Integer, List<Query>>();
		Map<Integer, List<Query>> actuals = sut.generateQueries(queryTarget, 5);

		Assert.assertEquals(expected, actuals);
	}
}
