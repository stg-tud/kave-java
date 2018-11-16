package cc.kave.episodes.mining.patterns;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import cc.kave.episodes.model.Episode;
import cc.recommenders.exceptions.AssertionException;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class PatternFilterTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	private static final int FREQUENCY = 2;
	private static final double ENTROPY = 0.5;
	private static final double THRESHSUB = 1.0;

	private Map<Integer, Set<Episode>> episodes;

	private PatternFilter sut;

	@Before
	public void setup() {
		episodes = Maps.newLinkedHashMap();

		sut = new PatternFilter();
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
	public void oneElement() {
		Set<Episode> nodes = Sets.newLinkedHashSet();
		nodes.add(createEpisode(8, 0.7, "1", "2", "1>2"));
		nodes.add(createEpisode(5, 0.6, "1", "2", "2>1"));
		episodes.put(2, nodes);
		
		Map<Integer, Set<Episode>> actuals = sut.representatives(episodes);
		
		assertEquals(episodes, actuals);
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
		set.add(createEpisode(8, 1.0, "1", "2", "1>2"));
		set.add(createEpisode(9, 1.0, "1", "2", "2>1"));
		expected.put(2, set);

		Map<Integer, Set<Episode>> actuals = sut.representatives(episodes);

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
		set.add(createEpisode(5, 0.5, "7", "8", "34", "34>7", "34>8"));
		set.add(createEpisode(5, 0.5, "7", "8", "34", "7>8", "34>8"));
		set.add(createEpisode(5, 0.5, "7", "8", "34", "7>8", "7>34"));
		set.add(createEpisode(5, 0.5, "7", "8", "34", "8>7", "34>7"));
		expected.put(3, set);

		Map<Integer, Set<Episode>> actuals = sut.representatives(episodes);

		assertEquals(expected, actuals);
	}
	
	@Test
	public void bidirect() throws Exception {

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
	
	@Test
	public void selfRepr() throws Exception {

		Set<Episode> twoNodes = Sets.newLinkedHashSet();
		twoNodes.add(createEpisode(8, 0.7, "1", "2"));
		twoNodes.add(createEpisode(3, 0.4, "1", "3"));
		episodes.put(2, twoNodes);

		Map<Integer, Set<Episode>> expected = Maps.newLinkedHashMap();
		Set<Episode> set = Sets.newLinkedHashSet();
		set.add(createEpisode(8, 0.7, "1", "2"));
		set.add(createEpisode(3, 0.4, "1", "3"));
		expected.put(2, set);

		Map<Integer, Set<Episode>> actuals = sut.representatives(episodes);

		assertEquals(expected, actuals);
	}
	
	@Test
	public void allDisconnected() {
		Set<Episode> set = Sets.newLinkedHashSet();
		set.add(createEpisode(10, 0.5, "1", "2", "3"));
		set.add(createEpisode(10, 0.5, "1", "2", "3", "1>2"));
		set.add(createEpisode(10, 0.5, "1", "2", "3", "1>3"));
		set.add(createEpisode(10, 0.5, "1", "2", "3", "2>3"));
		episodes.put(3, set);
		
		Map<Integer, Set<Episode>> expected = Maps.newLinkedHashMap();
		set = Sets.newLinkedHashSet();
		set.add(createEpisode(10, 0.5, "1", "2", "3", "1>2"));
		set.add(createEpisode(10, 0.5, "1", "2", "3", "1>3"));
		set.add(createEpisode(10, 0.5, "1", "2", "3", "2>3"));
		expected.put(3, set);
		
		Map<Integer, Set<Episode>> actuals = sut.representatives(episodes);
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void notCoverage() {
		Set<Episode> set = Sets.newLinkedHashSet();
		set.add(createEpisode(10, 0.5, "1", "2", "3", "1>2", "1>3"));
		set.add(createEpisode(10, 0.5, "1", "2", "3", "3>1", "3>2", "2>1"));
		episodes.put(3, set);
		
		Map<Integer, Set<Episode>> expected = Maps.newLinkedHashMap();
		set = Sets.newLinkedHashSet();
		set.add(createEpisode(10, 0.5, "1", "2", "3", "1>2", "1>3"));
		set.add(createEpisode(10, 0.5, "1", "2", "3", "3>1", "3>2", "2>1"));
		expected.put(3, set);
		
		Map<Integer, Set<Episode>> actuals = sut.representatives(episodes);
		
		assertEquals(expected, actuals);
	}
	
	@Test
	public void subLevelRepr() {
		Set<Episode> set = Sets.newLinkedHashSet();
		set.add(createEpisode(10, 0.5, "2", "4", "8", "8>2", "8>4"));
		set.add(createEpisode(10, 0.5, "2", "4", "8", "2>8", "4>8"));
		set.add(createEpisode(10, 0.5, "2", "4", "8", "8>2", "8>4", "4>2"));
		set.add(createEpisode(10, 0.5, "2", "4", "8", "8>2", "8>4", "2>4"));
		set.add(createEpisode(10, 0.5, "2", "4", "8", "2>4", "2>8", "4>8"));
		set.add(createEpisode(10, 0.5, "2", "4", "8", "2>8", "4>2", "4>8"));
		set.add(createEpisode(10, 0.5, "2", "4", "8", "2>4", "2>8", "8>4"));
		set.add(createEpisode(10, 0.5, "2", "4", "8", "4>2", "4>8", "8>2"));
		episodes.put(3, set);
		
		Map<Integer, Set<Episode>> expected = Maps.newLinkedHashMap();
		set = Sets.newLinkedHashSet();
		set.add(createEpisode(10, 0.5, "2", "4", "8", "8>2", "8>4"));
		set.add(createEpisode(10, 0.5, "2", "4", "8", "2>8", "4>8"));
		set.add(createEpisode(10, 0.5, "2", "4", "8", "2>4", "2>8", "8>4"));
		set.add(createEpisode(10, 0.5, "2", "4", "8", "4>2", "4>8", "8>2"));
		expected.put(3, set);
		
		Map<Integer, Set<Episode>> actuals = sut.representatives(episodes);
		
		assertEquals(expected, actuals);
	}

	@Test
	public void emptyEpisodes() throws Exception {
		thrown.expect(AssertionException.class);
		thrown.expectMessage("The list of episodes is empty!");
		
		sut.subEpisodes(episodes, THRESHSUB);
	}
	
	@Test
	public void subepisodes() throws Exception {
		episodes.put(1, Sets.newHashSet(createEpisode(2, 1.0, "a")));
		
		Set<Episode> epSet = Sets.newLinkedHashSet();
		epSet.add(createEpisode(5, 1.0, "a", "b", "a>b"));
		epSet.add(createEpisode(6,  0.5, "c", "d"));
		epSet.add(createEpisode(5, 1.0, "e", "f", "e>f"));
		episodes.put(2, epSet);
		
		epSet = Sets.newLinkedHashSet();
		epSet.add(createEpisode(4, 0.5, "a", "b", "c", "a>b", "a>c"));
		epSet.add(createEpisode(6, 0.5, "c", "d", "e", "c>d", "c>e"));
		epSet.add(createEpisode(2, 0.8, "b", "d", "f"));
		episodes.put(3, epSet);
		
		Map<Integer, Set<Episode>> expected = Maps.newLinkedHashMap();
		epSet = Sets.newLinkedHashSet();
		epSet.add(createEpisode(6,  0.5, "c", "d"));
		epSet.add(createEpisode(5, 1.0, "e", "f", "e>f"));
		expected.put(2, epSet);
		
		epSet = Sets.newLinkedHashSet();
		epSet.add(createEpisode(4, 0.5, "a", "b", "c", "a>b", "a>c"));
		epSet.add(createEpisode(6, 0.5, "c", "d", "e", "c>d", "c>e"));
		epSet.add(createEpisode(2, 0.8, "b", "d", "f"));
		expected.put(3, epSet);
		
		Map<Integer, Set<Episode>> actuals = sut.subEpisodes(episodes, THRESHSUB);
		
		assertEquals(expected, actuals);
	}

	private Episode createEpisode(int frequency, double entropy, String...strings) {
		Episode episode = new Episode();
		episode.setFrequency(frequency);
		episode.setEntropy(entropy);
		
		for (String string :  strings) {
			episode.addFact(string);
		}
		return episode;
	}
}
