package cc.kave.episodes.mining.patterns;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cc.kave.episodes.model.Episode;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PartialPatternsTest {

	private static final int FREQUENCY = 2;
	private static final double ENTROPY = 0.5;

	private Map<Integer, Set<Episode>> episodes;

	private PartialPatterns sut;

	@Before
	public void setup() {
		episodes = Maps.newLinkedHashMap();
		sut = new PartialPatterns();
	}

	@Test
	public void removeOneNodes() throws Exception {
		Set<Episode> oneNodes = Sets.newLinkedHashSet();

		oneNodes.add(createEpisode(3, 0.7, "1"));
		oneNodes.add(createEpisode(8, 0.5, "2"));
		episodes.put(1, oneNodes);

		Map<Integer, Set<Episode>> actuals = sut.filter(episodes, FREQUENCY,
				ENTROPY);

		assertEquals(Maps.newLinkedHashMap(), actuals);
	}

	@Test
	public void seqEpisodes() throws Exception {

		Set<Episode> twoNodes = Sets.newLinkedHashSet();
		twoNodes.add(createEpisode(8, 1.0, "1", "2", "1>2"));
		twoNodes.add(createEpisode(5, 1.0, "1", "2", "2>1"));
		episodes.put(2, twoNodes);

		Map<Integer, Set<Episode>> actuals = sut.filter(episodes, FREQUENCY,
				ENTROPY);

		assertEquals(episodes, actuals);
	}

	@Test
	public void freqRepr() throws Exception {

		Set<Episode> twoNodes = Sets.newLinkedHashSet();
		twoNodes.add(createEpisode(8, 1.0, "1", "2", "1>2"));
		twoNodes.add(createEpisode(9, 1.0, "1", "2", "2>1"));
		twoNodes.add(createEpisode(9, 0.5, "1", "2"));
		episodes.put(2, twoNodes);

		Map<Integer, Set<Episode>> expected = Maps.newLinkedHashMap();
		Set<Episode> set = Sets.newLinkedHashSet();
		set.add(createEpisode(9, 0.5, "1", "2"));
		expected.put(2, set);

		Map<Integer, Set<Episode>> actuals = sut.filter(episodes, FREQUENCY,
				ENTROPY);

		assertEquals(expected, actuals);
	}
	
	@Test
	public void multiRepr() {
		Set<Episode> threNodes = Sets.newLinkedHashSet();
		threNodes.add(createEpisode(5, 0.5, "7", "8", "34", "34>7", "34>8"));
		threNodes.add(createEpisode(5, 0.5, "7", "8", "34", "7>8"));
		threNodes.add(createEpisode(5, 0.5, "7", "8", "34", "7>8", "34>8"));
		threNodes.add(createEpisode(5, 0.5, "7", "8", "34", "7>8", "7>34"));
		threNodes.add(createEpisode(5, 0.5, "7", "8", "34", "7>8", "7>34",
				"8>34"));
		threNodes.add(createEpisode(5, 0.5, "7", "8", "34", "7>34", "7>8",
				"34>8"));
		threNodes.add(createEpisode(5, 0.5, "7", "8", "34", "34>7", "34>8",
				"7>8"));
		threNodes.add(createEpisode(5, 0.5, "7", "8", "34", "8>7", "34>7"));
		threNodes.add(createEpisode(5, 0.5, "7", "8", "34", "34>8", "34>7",
				"8>7"));
		episodes.put(3, threNodes);

		Map<Integer, Set<Episode>> expected = Maps.newLinkedHashMap();
		Set<Episode> set = Sets.newLinkedHashSet();
		set.add(createEpisode(5, 0.5, "7", "8", "34", "7>8"));
		set.add(createEpisode(5, 0.5, "7", "8", "34", "8>7", "34>7"));
		expected.put(3, set);

		Map<Integer, Set<Episode>> actuals = sut.filter(episodes, FREQUENCY, ENTROPY);

		assertEquals(expected, actuals);
	} 
	
	@Test
	public void bidirectRepr() throws Exception {

		Set<Episode> twoNodes = Sets.newLinkedHashSet();
		twoNodes.add(createEpisode(8, 0.7, "1", "2"));
		twoNodes.add(createEpisode(3, 0.4, "1", "3"));
		episodes.put(2, twoNodes);

		Set<Episode> threeNodes = Sets.newLinkedHashSet();
		threeNodes.add(createEpisode(8, 0.5, "1", "2", "3"));
		episodes.put(3, threeNodes);

		Map<Integer, Set<Episode>> expected = Maps.newLinkedHashMap();
		Set<Episode> set2 = Sets.newLinkedHashSet();
		set2.add(createEpisode(8, 0.7, "1", "2"));
		expected.put(2, set2);

		Set<Episode> set3 = Sets.newLinkedHashSet();
		set3.add(createEpisode(8, 0.5, "1", "2", "3"));
		expected.put(3, set3);

		Map<Integer, Set<Episode>> actuals = sut.filter(episodes, FREQUENCY, ENTROPY);

		assertEquals(expected, actuals);
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
