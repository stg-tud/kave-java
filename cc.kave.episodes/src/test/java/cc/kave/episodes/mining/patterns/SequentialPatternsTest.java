package cc.kave.episodes.mining.patterns;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cc.kave.episodes.model.Episode;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class SequentialPatternsTest {

	private static final int FREQUENCY = 2;
	
	private Map<Integer, Set<Episode>> episodes;
	
	private SequentialPatterns sut;
	
	@Before 
	public void setup() {
		episodes = Maps.newLinkedHashMap();
		sut = new SequentialPatterns();
	}
	
	@Test
	public void removeOneNodes() throws Exception {
		Set<Episode> oneNodes = Sets.newLinkedHashSet();

		oneNodes.add(createEpisode(3, 1.0, "1"));
		oneNodes.add(createEpisode(8, 1.0, "2"));
		episodes.put(1, oneNodes);

		Map<Integer, Set<Episode>> actuals = sut.filter(episodes, FREQUENCY);

		assertEquals(Maps.newLinkedHashMap(), actuals);
	}
	
	@Test
	public void multipleEqualEpisodes() throws Exception {

		Set<Episode> twoNodes = Sets.newLinkedHashSet();
		twoNodes.add(createEpisode(8, 1.0, "1", "2", "1>2"));
		twoNodes.add(createEpisode(8, 1.0, "1", "2", "2>1"));
		episodes.put(2, twoNodes);

		Map<Integer, Set<Episode>> actuals = sut.filter(episodes, FREQUENCY);

		assertEquals(episodes, actuals);
	}
	
	private Episode createEpisode(int freq, double bdmeas, String... strings) {
		Episode episode = new Episode();
		episode.setFrequency(freq);
		episode.setEntropy(bdmeas);
		for (String fact : strings) {
			episode.addFact(fact);
		}
		return episode;
	}
}
